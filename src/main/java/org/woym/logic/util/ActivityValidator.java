package org.woym.logic.util;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.woym.common.exceptions.DatasetException;
import org.woym.common.messages.GenericErrorMessage;
import org.woym.common.messages.GenericSuccessMessage;
import org.woym.common.messages.SpecificErrorMessage;
import org.woym.common.objects.Activity;
import org.woym.common.objects.Employee;
import org.woym.common.objects.EmployeeTimePeriods;
import org.woym.common.objects.Schoolclass;
import org.woym.common.objects.Room;
import org.woym.common.objects.TimePeriod;
import org.woym.logic.FailureStatus;
import org.woym.logic.SuccessStatus;
import org.woym.logic.spec.IStatus;
import org.woym.persistence.DataAccess;

/**
 * <h1>ActivityValidator</h1>
 * <p>
 * Der {@link ActivityValidator} ist dafür zuständig, übergebene Aktivitäten auf
 * ihre Gültigkeit zu prüfen.
 * <p>
 * Ist eine Aktivität ungültig, weil sie sich zum Beispiel mit dem Zeitraum
 * einer anderen Aktivität schneidet, der die gleichen Objekte zugeordnet sind
 * wie der zu vergleichenden, so wird ein entsprechender {@link FailureStatus}
 * zurückgegeben, der dies indiziert.
 * 
 * 
 * @author Tim Hansen (tihansen)
 * @version 0.0.9
 * @since 0.0.8
 * 
 * @see {@link IStatus}
 * @see {@link DataAccess}
 */
public class ActivityValidator {

	private final static ActivityValidator INSTANCE = new ActivityValidator();

	private static Logger LOGGER = LogManager
			.getLogger(ActivityValidator.class);

	private DataAccess dataAccess = DataAccess.getInstance();

	private ActivityValidator() {

	}

	/**
	 * Liefert eine Instanz des {@link ActivityValidator} zurück.
	 * 
	 * @return Eine Instanz des Validators
	 */
	public static ActivityValidator getInstance() {
		return INSTANCE;
	}

	/**
	 * Diese Methode validiert eine übergebene Aktivität.
	 * <p>
	 * Es wird überprüft, ob es Überschneidungen mit anderen in der Datenbank
	 * vorhandenen Objekten gibt und ein entsprechendes Status-Objekt
	 * zurückgegeben.
	 * <p>
	 * Bei einem Datenbankfehler wird ebenfalls ein {@link FailureStatus}
	 * zurückgeliefert um so das Eintragen in den Datenbank zu verhindern.
	 * 
	 * @param activity
	 *            Die Aktivität, die überprüft werden soll
	 * @return {@link FailureStatus} bei Überschneidung, {@link SuccessStatus}
	 *         sonst
	 */
	public IStatus validateActivity(Activity activity) {
		IStatus status = new SuccessStatus(
				GenericSuccessMessage.VALIDATE_SUCCESS);

		status = validateActivityEmployees(activity);
		if (status instanceof FailureStatus) {
			return status;
		}

		status = validateActivitySchoolclasses(activity);
		if (status instanceof FailureStatus) {
			return status;
		}

		status = validateActivityRooms(activity);
		if (status instanceof FailureStatus) {
			return status;
		}

		return status;
	}

	public IStatus validateActivityEmployees(Activity activity) {
		IStatus status = new SuccessStatus(
				GenericSuccessMessage.VALIDATE_SUCCESS);

		try {
			if ((activity.getEmployeeTimePeriods().size() > 0)
					&& !validateEmployees(activity)) {
				return new FailureStatus(
						SpecificErrorMessage.VALIDATE_ACTIVITY_EXCEPTION,
						Employee.class, FacesMessage.SEVERITY_ERROR);
			}
		} catch (DatasetException e) {
			LOGGER.error(e);
			status = new FailureStatus(
					GenericErrorMessage.DATABASE_COMMUNICATION_ERROR,
					FacesMessage.SEVERITY_ERROR);
		}

		return status;
	}

	public IStatus validateActivitySchoolclasses(Activity activity) {
		IStatus status = new SuccessStatus(
				GenericSuccessMessage.VALIDATE_SUCCESS);

		try {
			if ((activity.getSchoolclasses().size() > 0)
					&& !validateSchoolclasses(activity)) {
				return new FailureStatus(
						SpecificErrorMessage.VALIDATE_ACTIVITY_EXCEPTION,
						Schoolclass.class, FacesMessage.SEVERITY_ERROR);
			}
		} catch (DatasetException e) {
			LOGGER.error(e);
			status = new FailureStatus(
					GenericErrorMessage.DATABASE_COMMUNICATION_ERROR,
					FacesMessage.SEVERITY_ERROR);
		}

		return status;
	}

	public IStatus validateActivityRooms(Activity activity) {
		IStatus status = new SuccessStatus(
				GenericSuccessMessage.VALIDATE_SUCCESS);

		try {
			if ((activity.getRooms().size() > 0) && !validateRooms(activity)) {
				return new FailureStatus(
						SpecificErrorMessage.VALIDATE_ACTIVITY_EXCEPTION,
						Employee.class, FacesMessage.SEVERITY_ERROR);
			}
		} catch (DatasetException e) {
			LOGGER.error(e);
			status = new FailureStatus(
					GenericErrorMessage.DATABASE_COMMUNICATION_ERROR,
					FacesMessage.SEVERITY_ERROR);
		}

		return status;
	}

	/**
	 * Diese Methode validiert, dass es für den Zeitraum der übergebenen
	 * {@link Activity} für keinen der {@link Activity} zugeordneten
	 * {@link Employee} eine bereits existente Aktivität gibt.
	 * 
	 * @param activity
	 *            Die Aktivität, die überprüft werden soll
	 * @return Warheitswert, ob eine Überschneidung auftritt
	 * @throws DatasetException
	 *             Bei Datenbankzugriffsfehler
	 */
	private Boolean validateEmployees(Activity activity)
			throws DatasetException {
		for (EmployeeTimePeriods employeeTimePeriods : activity
				.getEmployeeTimePeriods()) {

			for (TimePeriod timePeriod : employeeTimePeriods.getTimePeriods()) {
				List<Activity> activities = new ArrayList<>();
				activities.addAll(dataAccess.getAllActivitiesBefore(
						employeeTimePeriods.getEmployee(), timePeriod));
				activities.addAll(dataAccess.getAllActivitiesAfter(
						employeeTimePeriods.getEmployee(), timePeriod));
				List<Activity> activitiesForWeekday = dataAccess
						.getAllActivities(timePeriod.getDay());

				if (activities.size() < activitiesForWeekday.size()) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Diese Methode validiert, dass es für den Zeitraum der übergebenen
	 * {@link Activity} für keinen der {@link Activity} zugeordnete
	 * {@link Schoolclass} eine bereits existente Aktivität gibt.
	 * 
	 * @param activity
	 *            Die Aktivität, die überprüft werden soll
	 * @return Warheitswert, ob eine Überschneidung auftritt
	 * @throws DatasetException
	 *             Bei Datenbankzugriffsfehler
	 */
	private Boolean validateSchoolclasses(Activity activity)
			throws DatasetException {

		for (Schoolclass schoolclass : activity.getSchoolclasses()) {

			List<Activity> activities = dataAccess.getAllActivities(
					schoolclass, activity.getTime());

			if (!validateActivities(activity, activities)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Diese Methode validiert, dass es für den Zeitraum der übergebenen
	 * {@link Activity} für keinen der {@link Activity} zugeordnete {@link Room}
	 * eine bereits existente Aktivität gibt.
	 * 
	 * @param activity
	 *            Die Aktivität, die überprüft werden soll
	 * @return Warheitswert, ob eine Überschneidung auftritt
	 * @throws DatasetException
	 *             Bei Datenbankzugriffsfehler
	 */
	private Boolean validateRooms(Activity activity) throws DatasetException {

		for (Room room : activity.getRooms()) {

			List<Activity> activities = dataAccess.getAllActivities(room,
					activity.getTime());

			if (!validateActivities(activity, activities)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Diese Methode validiert eine gegebene {@link Activity} anhand einer
	 * {@link List} von anderen {@link Activity}-Objekten. Sollte die Liste
	 * nicht leer sein und {@link Activity}-Objekte enthalten, die nicht der
	 * übergebenen {@link Activity} entsprechen, schlägt die Valdidierung fehl.
	 * 
	 * @param activity
	 *            Die zu validierende {@link Activity}
	 * @param activities
	 *            Die Liste mit zu prüfenden {@link Activity}-Objekten
	 * @return Einen Wahrheitswert, ob die {@link Activity} valide ist.
	 */
	private Boolean validateActivities(Activity activity,
			List<Activity> activities) {

		if (activities.size() > 0) {
			for (Activity localActivity : activities) {
				if (!activity.equals(localActivity)) {
					return false;
				}
			}
		}

		return true;
	}
}
