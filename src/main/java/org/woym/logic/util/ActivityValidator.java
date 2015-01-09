package org.woym.logic.util;

import javax.faces.application.FacesMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.woym.exceptions.DatasetException;
import org.woym.logic.FailureStatus;
import org.woym.logic.SuccessStatus;
import org.woym.logic.spec.IStatus;
import org.woym.messages.GenericErrorMessage;
import org.woym.messages.GenericSuccessMessage;
import org.woym.messages.SpecificErrorMessage;
import org.woym.objects.Activity;
import org.woym.objects.Employee;
import org.woym.objects.EmployeeTimePeriods;
import org.woym.objects.Schoolclass;
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
 * @version 0.0.8
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
		IStatus status;

		status = new SuccessStatus(GenericSuccessMessage.VALIDATE_SUCCESS);

		// TODO Validierung an die Reihenfolge im Dialog anpassen, dies spart
		// Ladezeiten
		try {

			status = validateEmployees(activity);
			if (status instanceof FailureStatus) {
				return status;
			}
			status = validateSchoolclasses(activity);
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
	 * Diese Methode validiert, dass es für den Zeitraum der übergebenen
	 * {@link Activity} für keinen der {@link Activity} zugeordneten
	 * {@link Employee} eine bereits existente Aktivität gibt.
	 * 
	 * @param activity
	 *            Die Aktivität, die überprüft werden soll
	 * @return {@link FailureStatus} bei Überschneidung, {@link SuccessStatus}
	 *         sonst
	 * @throws DatasetException
	 *             Bei Datenbankzugriffsfehler
	 */
	private IStatus validateEmployees(Activity activity)
			throws DatasetException {
		for (EmployeeTimePeriods employeeTimePeriods : activity
				.getEmployeeTimePeriods()) {
			if (dataAccess.getAllActivities(employeeTimePeriods.getEmployee(),
					activity.getTime()).size() > 0) {
				return new FailureStatus(
						SpecificErrorMessage.VALIDATE_ACTIVITY_EXCEPTION,
						employeeTimePeriods.getEmployee().getClass(),
						FacesMessage.SEVERITY_ERROR);
			}

		}

		return new SuccessStatus(GenericSuccessMessage.VALIDATE_SUCCESS);
	}

	/**
	 * Diese Methode validiert, dass es für den Zeitraum der übergebenen
	 * {@link Activity} für keinen der {@link Activity} zugeordnete
	 * {@link Schoolclass} eine bereits existente Aktivität gibt.
	 * 
	 * @param activity
	 *            Die Aktivität, die überprüft werden soll
	 * @return {@link FailureStatus} bei Überschneidung, {@link SuccessStatus}
	 *         sonst
	 * @throws DatasetException
	 *             Bei Datenbankzugriffsfehler
	 */
	private IStatus validateSchoolclasses(Activity activity)
			throws DatasetException {
		for (Schoolclass schoolclass : activity.getSchoolclasses()) {
			if (dataAccess.getAllActivities(schoolclass, activity.getTime())
					.size() > 0) {
				return new FailureStatus(
						SpecificErrorMessage.VALIDATE_ACTIVITY_EXCEPTION,
						Schoolclass.class, FacesMessage.SEVERITY_ERROR);
			}

		}

		return new SuccessStatus(GenericSuccessMessage.VALIDATE_SUCCESS);
	}
}
