package org.woym.logic.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.woym.common.exceptions.DatasetException;
import org.woym.common.messages.GenericErrorMessage;
import org.woym.common.messages.GenericSuccessMessage;
import org.woym.common.objects.Activity;
import org.woym.common.objects.Employee;
import org.woym.common.objects.EmployeeTimePeriods;
import org.woym.common.objects.Location;
import org.woym.common.objects.Pause;
import org.woym.common.objects.Room;
import org.woym.common.objects.Schoolclass;
import org.woym.common.objects.TimePeriod;
import org.woym.common.objects.TravelTimeList;
import org.woym.common.objects.TravelTimeList.Edge;
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

	private TravelTimeList travelTimeList = TravelTimeList.getInstance();

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
	 * vorhandenen Objekten gibt (dabei werden Wegzeiten zwischen verschiedenen
	 * Standorten berücksichtigt) und ein entsprechendes Status-Objekt
	 * zurückgegeben.
	 * <p>
	 * Bei einem Datenbankfehler wird ebenfalls ein {@link FailureStatus}
	 * zurückgeliefert um so das Eintragen in den Datenbank zu verhindern.
	 * 
	 * @param activity
	 *            Die Aktivität, die überprüft werden soll
	 * @param timePeriod
	 *            Der Zeitslot, der überprüft werden soll
	 * @return {@link FailureStatus} bei Überschneidung, {@link SuccessStatus}
	 *         sonst
	 */
	public IStatus validateActivity(final Activity activity,
			final TimePeriod timePeriod) {
		IStatus status = new SuccessStatus(
				GenericSuccessMessage.VALIDATE_SUCCESS);

		try {
			status = validateActivityEmployees(activity, timePeriod);
			if (status instanceof FailureStatus) {
				return status;
			}

			status = validateActivitySchoolclasses(activity, timePeriod);
			if (status instanceof FailureStatus) {
				return status;
			}

			// Räume haben keine Wegzeiten
			status = validateActivityRooms(activity, timePeriod);
			if (status instanceof FailureStatus) {
				return status;
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
	 * Prüft, ob die an der übergebenen Aktivität teilnehmenden
	 * {@linkplain Employee}s in dem Zeitraum, der dem übergebenen
	 * {@linkplain TimePeriod}-Objekt entspricht, bereits eine Aktivität
	 * besitzen. Ist dies der Fall, wird ein {@linkplain FailureStatus}
	 * zurückgegeben, ansonsten ein {@linkplain SuccessStatus}.
	 * 
	 * @param activity
	 *            - Aktivität
	 * @param timePeriod
	 *            - (neuer) Zeitraum
	 * @return {@linkplain SuccessStatus}, wenn alle Teilnehmer des Personals
	 *         der Aktivität im übergebenen Zeitraum noch keine Aktivität haben,
	 *         ansonsten {@linkplain FailureStatus}
	 * @throws DatasetException
	 */
	public IStatus validateActivityEmployees(final Activity activity,
			final TimePeriod timePeriod) throws DatasetException {
		IStatus status = new SuccessStatus(
				GenericSuccessMessage.VALIDATE_SUCCESS);

		if ((activity.getEmployeeTimePeriods().size() > 0)
				&& !validateEmployees(activity, timePeriod)) {
			return new FailureStatus(
					"Validierung fehlgeschlagen.",
					"Es wurde eine Überschneidung dieser Aktivität mit einer vorhandenen"
							+ " von einem der gewählten Mitarbeiter festgestellt.",
					FacesMessage.SEVERITY_ERROR);
		}

		return status;
	}

	/**
	 * Prüft, ob die an der übergebenen Aktivität teilnehmenden
	 * {@linkplain Schoolclass}es in dem Zeitraum, der dem übergebenen
	 * {@linkplain TimePeriod}-Objekt entspricht, bereits eine Aktivität
	 * besitzen. Ist dies der Fall, wird ein {@linkplain FailureStatus}
	 * zurückgegeben, ansonsten ein {@linkplain SuccessStatus}.
	 * 
	 * @param activity
	 *            - Aktivität
	 * @param timePeriod
	 *            - (neuer) Zeitraum
	 * @return {@linkplain SuccessStatus}, wenn alle teilnehmenden Schulklassen
	 *         der Aktivität im übergebenen Zeitraum noch keine Aktivität haben,
	 *         ansonsten {@linkplain FailureStatus}
	 * @throws DatasetException
	 */
	public IStatus validateActivitySchoolclasses(final Activity activity,
			final TimePeriod timePeriod) throws DatasetException {
		IStatus status = new SuccessStatus(
				GenericSuccessMessage.VALIDATE_SUCCESS);

		if ((activity.getSchoolclasses().size() > 0)
				&& !validateSchoolclasses(activity, timePeriod)) {
			return new FailureStatus(
					"Validierung fehlgeschlagen.",
					"Es wurde ein Überschneidung dieser Aktivität mit einer vorhandenen"
							+ " von einer der gewählten Schulklassen festgestellt.",
					FacesMessage.SEVERITY_ERROR);
		}

		return status;
	}

	/**
	 * Prüft, ob in denen in der übergebenen Aktivität enthaltenen
	 * {@linkplain Room}s in dem Zeitraum, der dem übergebenen
	 * {@linkplain TimePeriod}-Objekt entspricht, bereits eine Aktivität
	 * stattfindet. Ist dies der Fall, wird ein {@linkplain FailureStatus}
	 * zurückgegeben, ansonsten ein {@linkplain SuccessStatus}.
	 * 
	 * @param activity
	 *            - Aktivität
	 * @param timePeriod
	 *            - (neuer) Zeitraum
	 * @return {@linkplain SuccessStatus}, wenn in allen in der Aktivität
	 *         enthaltenen Räumen zum übergebenen Zeitraum noch keine Aktivität
	 *         stattfindet, ansonsten {@linkplain FailureStatus}
	 * @throws DatasetException
	 */
	public IStatus validateActivityRooms(final Activity activity,
			final TimePeriod timePeriod) throws DatasetException {
		IStatus status = new SuccessStatus(
				GenericSuccessMessage.VALIDATE_SUCCESS);

		if ((activity.getRooms().size() > 0)
				&& !validateRooms(activity, timePeriod)) {
			return new FailureStatus("Validierung fehlgeschlagen.",
					"Es wurde einer Überschneidung mit einer vorhandenen Aktivität"
							+ " in einem der gewählten Räume festgestellt.",
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
	 * @param timePeriod
	 *            Der Zeitslot, der überprüft werden soll
	 * @return Warheitswert, ob eine Überschneidung auftritt
	 * @throws DatasetException
	 *             Bei Datenbankzugriffsfehler
	 */
	@SuppressWarnings("deprecation")
	private Boolean validateEmployees(final Activity activity,
			final TimePeriod timePeriod) throws DatasetException {
		for (EmployeeTimePeriods employeeTimePeriods : activity
				.getEmployeeTimePeriods()) {

			TimePeriod extendedTimePeriod = cloneTimePeriod(timePeriod);

			Location location = dataAccess.getOneLocation(activity.getRooms()
					.get(0));

			List<Activity> activitiesBefore = dataAccess
					.getAllActivitiesBefore(employeeTimePeriods.getEmployee(),
							extendedTimePeriod);
			if (!activitiesBefore.isEmpty()) {
				Activity lastActivity = activitiesBefore.get(activitiesBefore
						.size() - 1);
				Location otherLocation = dataAccess.getOneLocation(lastActivity
						.getRooms().get(0));

				Edge edge = travelTimeList.getEdge(location, otherLocation);
				if (edge != null) {
					extendedTimePeriod.getStartTime().setMinutes(
							extendedTimePeriod.getStartTime().getMinutes()
									- edge.getDistance());
				}
			}

			List<Activity> activitiesAfter = dataAccess.getAllActivitiesAfter(
					employeeTimePeriods.getEmployee(), extendedTimePeriod);
			if (!activitiesAfter.isEmpty()) {
				Activity firstActivity = activitiesAfter.get(activitiesAfter
						.size() - 1);
				Location otherLocation = dataAccess
						.getOneLocation(firstActivity.getRooms().get(0));
				Edge edge = travelTimeList.getEdge(location, otherLocation);
				if (edge != null) {
					extendedTimePeriod.getEndTime().setMinutes(
							extendedTimePeriod.getEndTime().getMinutes()
									+ edge.getDistance());
				}
			}

			List<Activity> activitiesForWeekday = new ArrayList<>(
					dataAccess.getAllActivities(
							employeeTimePeriods.getEmployee(),
							timePeriod.getDay()));

			activitiesForWeekday.removeAll(dataAccess.getAllActivitiesBefore(
					employeeTimePeriods.getEmployee(), extendedTimePeriod));
			activitiesForWeekday.removeAll(dataAccess.getAllActivitiesAfter(
					employeeTimePeriods.getEmployee(), extendedTimePeriod));

			if (!validateActivities(activity, activitiesForWeekday)) {
				return false;
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
	 * @param timePeriod
	 *            Der Zeitslot, der überprüft werden soll
	 * @return Warheitswert, ob eine Überschneidung auftritt
	 * @throws DatasetException
	 *             Bei Datenbankzugriffsfehler
	 */
	@SuppressWarnings("deprecation")
	private Boolean validateSchoolclasses(final Activity activity,
			final TimePeriod timePeriod) throws DatasetException {

		for (Schoolclass schoolclass : activity.getSchoolclasses()) {

			TimePeriod extendedTimePeriod = cloneTimePeriod(timePeriod);

			if (!(activity instanceof Pause)) {

				Location location = dataAccess.getOneLocation(activity
						.getRooms().get(0));

				Activity activityBefore = dataAccess.getFirstActivityBefore(
						schoolclass, extendedTimePeriod, location, true);
				if (activityBefore != null) {
					if (activityBefore instanceof Pause) {
						// Aktivität davor, die keine Pause ist
						Activity activityBeforePause = dataAccess
								.getFirstActivityBefore(schoolclass,
										extendedTimePeriod, location, false);
						Location otherLocation = dataAccess
								.getOneLocation(activityBeforePause.getRooms()
										.get(0));
						Edge edge = travelTimeList.getEdge(location,
								otherLocation);
						if (edge != null) {
							if (edge.getDistance() > activityBefore.getTime()
									.getDuration()
									&& (edge.getDistance() > activity.getTime()
											.getStartTime().getMinutes()
											- activityBeforePause.getTime()
													.getEndTime().getMinutes())) {
								return false;
							}
						}
					}
					Location otherLocation = dataAccess
							.getOneLocation(activityBefore.getRooms().get(0));
					Edge edge = travelTimeList.getEdge(location, otherLocation);
					if (edge != null) {
						extendedTimePeriod.getStartTime().setMinutes(
								extendedTimePeriod.getStartTime().getMinutes()
										- edge.getDistance());
					}
				}

				Activity activityAfter = dataAccess.getFirstActivityAfter(
						schoolclass, extendedTimePeriod, location, true);
				if (activityAfter != null) {
					if (activityAfter instanceof Pause) {
						// Aktivität danach, die keine Pause ist
						Activity activityAfterPause = dataAccess
								.getFirstActivityAfter(schoolclass,
										extendedTimePeriod, location, false);
						Location otherLocation = dataAccess
								.getOneLocation(activityAfterPause.getRooms()
										.get(0));
						Edge edge = travelTimeList.getEdge(location,
								otherLocation);
						if (edge != null) {
							if (edge.getDistance() > activityAfter.getTime()
									.getDuration()
									&& (edge.getDistance() > activity.getTime()
											.getEndTime().getMinutes()
											- activityAfterPause.getTime()
													.getStartTime()
													.getMinutes())) {
								return false;
							}
						}
					}
					Location otherLocation = dataAccess
							.getOneLocation(activityBefore.getRooms().get(0));
					Edge edge = travelTimeList.getEdge(location, otherLocation);
					if (edge != null) {
						extendedTimePeriod.getEndTime().setMinutes(
								extendedTimePeriod.getEndTime().getMinutes()
										+ edge.getDistance());
					}
				}

			}
			List<Activity> activities = dataAccess.getAllActivities(
					schoolclass, extendedTimePeriod);

			if (!validateActivities(activity, activities)) {
				return false;
			}
		}
		return true;
	}

	private TimePeriod cloneTimePeriod(TimePeriod timePeriod) {
		TimePeriod extendedTimePeriod = new TimePeriod();
		extendedTimePeriod.setDay(timePeriod.getDay());
		Date statingTime = new Date();
		statingTime.setTime(timePeriod.getStartTime().getTime());
		extendedTimePeriod.setStartTime(statingTime);
		Date endingTime = new Date();
		endingTime.setTime(timePeriod.getEndTime().getTime());
		extendedTimePeriod.setEndTime(endingTime);
		return extendedTimePeriod;
	}

	/**
	 * Diese Methode validiert, dass es für den Zeitraum der übergebenen
	 * {@link Activity} für keinen der {@link Activity} zugeordnete {@link Room}
	 * eine bereits existente Aktivität gibt.
	 * 
	 * @param activity
	 *            Die Aktivität, die überprüft werden soll
	 * @param timePeriod
	 *            Der Zeitslot, der überprüft werden soll
	 * @return Warheitswert, ob eine Überschneidung auftritt
	 * @throws DatasetException
	 *             Bei Datenbankzugriffsfehler
	 */
	private Boolean validateRooms(final Activity activity,
			final TimePeriod timePeriod) throws DatasetException {

		for (Room room : activity.getRooms()) {

			List<Activity> activities = dataAccess.getAllActivities(room,
					timePeriod);

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
	private Boolean validateActivities(final Activity activity,
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
