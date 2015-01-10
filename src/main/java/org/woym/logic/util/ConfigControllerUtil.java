package org.woym.logic.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;

import org.woym.config.Config;
import org.woym.config.DefaultConfigEnum;
import org.woym.controller.manage.ConfigController;
import org.woym.exceptions.DatasetException;
import org.woym.logic.BackupRestoreHandler;
import org.woym.logic.CommandHandler;
import org.woym.logic.FailureStatus;
import org.woym.logic.SuccessStatus;
import org.woym.logic.command.CommandCreator;
import org.woym.logic.command.MacroCommand;
import org.woym.logic.spec.IStatus;
import org.woym.messages.GenericErrorMessage;
import org.woym.objects.Activity;
import org.woym.objects.Weekday;
import org.woym.persistence.DataAccess;

/**
 * Eine Hilfsklasse, die Methoden für {@linkplain ConfigController}
 * bereitstellt.
 * 
 * @author Adrian
 *
 */
public abstract class ConfigControllerUtil {

	/**
	 * Löscht alle vorhandenen Aktivitäten und gibt ein entsprechendes
	 * IStatus-Objekt zurück.
	 * 
	 * @return
	 */
	public static IStatus deleteAllActivities() {
		try {
			List<Activity> activities = DataAccess.getInstance()
					.getAllActivities();
			if (activities.isEmpty()) {
				return new SuccessStatus("Keine Aktivitäten zu löschen.",
						"Es sind keine zu löschenden Aktivitäten vorhanden.",
						FacesMessage.SEVERITY_INFO);
			}
			MacroCommand macro = new MacroCommand();
			for (Activity a : activities) {
				macro.add(CommandCreator.getInstance().createDeleteCommand(a));
			}
			return CommandHandler.getInstance().execute(macro);
		} catch (DatasetException e) {
			return new FailureStatus(
					GenericErrorMessage.DATABASE_COMMUNICATION_ERROR,
					FacesMessage.SEVERITY_ERROR);
		}
	}

	/**
	 * Diese Methode deaktiviert die automatischen Backups, wird an einer Stelle
	 * von einer der Methoden von {@linkplain Config} {@code false}
	 * zurückgegeben, gibt diese Methode zum Schluss auch {@code false} zurück.
	 * Zuvor werden jedoch alle Operationen durchgeführt.
	 * 
	 * @return {@code false}, wenn bei einem der Teilschritte {@code false}
	 *         zurückgegeben wurde
	 */
	public static boolean disableBackups() {
		boolean works = true;
		if (Config.getSingleStringValue(DefaultConfigEnum.BACKUP_NEXTDATE) != null) {
			Config.clearProperty(DefaultConfigEnum.BACKUP_NEXTDATE.getPropKey());
		}

		// Um zu verhindern, dass der Scheduler neu gestartet wird, wenn die
		// Einstellungen nicht geändert wurden
		if (Config.getSingleIntValue(DefaultConfigEnum.BACKUP_INTERVAL) > 0) {
			works = Config.updateProperty(
					DefaultConfigEnum.BACKUP_INTERVAL.getPropKey(),
					String.valueOf(0));
			BackupRestoreHandler.restartScheduler();
		}
		return works;
	}

	/**
	 * Diese Methode aktualisiert die Backup-Einstellungen auf minütliche
	 * Backups mit dem übergebenen Intervall in Minuten. Es werden alle
	 * Teilschritte durchgeführt, wenn jedoch bei einem der Teilschritte von
	 * einer Methode von {@linkplain Config} {@code false} zurückgegeben wird,
	 * gibt auch diese Methode {@code false} zurück.
	 * 
	 * @param interval
	 *            - das Backup-Intervall in Minuten
	 * @return {@code false}, wenn bei einem der Teilschritte {@code false}
	 *         zurückgegeben wurde
	 */
	public static boolean minuteBackups(int interval) {
		boolean works = true;
		if (Config.getSingleStringValue(DefaultConfigEnum.BACKUP_NEXTDATE) != null) {
			Config.clearProperty(DefaultConfigEnum.BACKUP_NEXTDATE.getPropKey());
		}
		int currentInterval = Config
				.getSingleIntValue(DefaultConfigEnum.BACKUP_INTERVAL);

		// Um zu verhindern, dass der Scheduler neu gestartet wird, wenn die
		// Einstellungen nicht geändert wurden
		if (currentInterval <= 0 || currentInterval >= 1440) {
			works = Config.updateProperty(
					DefaultConfigEnum.BACKUP_INTERVAL.getPropKey(),
					String.valueOf(interval));
			BackupRestoreHandler.restartScheduler();
		}

		return works;
	}

	/**
	 * Diese Methode aktualisiert die Backup-Einstellungen auf tägliche Backups.
	 * Es werden alle Teilschritte ausgeführt, wird jedoch bei einem der
	 * Teilschritte von einer aufgerufenen Methode {@code false} zurückgegeben,
	 * gibt auch diese Methode zum Schluss {@code false} zurück.
	 * 
	 * @param selectedDayValue
	 *            - Tages-Intervall der Backups (ab dem aktuellen Tag)
	 * @param selectedTime
	 *            - Uhrzeit der Backups
	 * @return {@code false}, wenn bei einem der Teilschritte {@code false}
	 *         zurückgegeben wird.
	 */
	public static boolean dailyBackups(int selectedDayValue, Date selectedTime) {
		boolean works = true;
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyy");
		Date startDate = Calendar.getInstance().getTime();
		if (Config.getSingleStringValue(DefaultConfigEnum.BACKUP_NEXTDATE) == null) {
			works = Config.addProperty(
					DefaultConfigEnum.BACKUP_NEXTDATE.getPropKey(),
					df.format(startDate))
					&& works;
		} else {
			works = Config.updateProperty(
					DefaultConfigEnum.BACKUP_NEXTDATE.getPropKey(),
					df.format(startDate))
					&& works;
		}
		int backupIntervalValue = selectedDayValue * 1440;
		works = Config.updateProperty(
				DefaultConfigEnum.BACKUP_INTERVAL.getPropKey(),
				String.valueOf(backupIntervalValue))
				&& works;

		df = new SimpleDateFormat("HH:mm");
		works = Config.updateProperty(
				DefaultConfigEnum.BACKUP_TIME.getPropKey(),
				df.format(selectedTime))
				&& works;
		BackupRestoreHandler.restartScheduler();
		return works;
	}

	/**
	 * 
	 * Fügt die in der Properties-Datei mit {@code true} stehenden Wochentage,
	 * der Liste von selektierten Wochentagen hinzu und gibt diese Liste zurück.
	 *
	 * @return Liste der in der Properties-Datei mit {@code true} angegebenen
	 *         Wochentage
	 */
	public static List<Weekday> getSelectedWeekdays() {
		List<Weekday> selectedWeekdays = new ArrayList<Weekday>();
		if (Config.getBooleanValue(DefaultConfigEnum.WEEKDAY_MONDAY)) {
			selectedWeekdays.add(Weekday.MONDAY);
		}
		if (Config.getBooleanValue(DefaultConfigEnum.WEEKDAY_TUESDAY)) {
			selectedWeekdays.add(Weekday.TUESDAY);
		}
		if (Config.getBooleanValue(DefaultConfigEnum.WEEKDAY_WEDNESDAY)) {
			selectedWeekdays.add(Weekday.WEDNESDAY);
		}
		if (Config.getBooleanValue(DefaultConfigEnum.WEEKDAY_THURSDAY)) {
			selectedWeekdays.add(Weekday.THURSDAY);
		}
		if (Config.getBooleanValue(DefaultConfigEnum.WEEKDAY_FRIDAY)) {
			selectedWeekdays.add(Weekday.FRIDAY);
		}
		if (Config.getBooleanValue(DefaultConfigEnum.WEEKDAY_SATURDAY)) {
			selectedWeekdays.add(Weekday.SATURDAY);
		}
		if (Config.getBooleanValue(DefaultConfigEnum.WEEKDAY_SUNDAY)) {
			selectedWeekdays.add(Weekday.SUNDAY);
		}
		return selectedWeekdays;
	}

	/**
	 * Aktualisiert die Property-Werte für die zu verplanenden Wochentage anhand
	 * der in der übergebenen Liste vorhandenen Wochentage.
	 * 
	 * @param selectedWeekdays
	 *            - Liste der zu verplanenden Wochentage
	 * @return {@code true}, wenn alle Properties erfolgreich aktualisiert
	 *         wurden, {@code false}, falls bei mindestens einem
	 *         Aktualisierungsversuch {@code false} von {@linkplain Config}
	 *         zurückgegeben wurde
	 */
	public static boolean updateWeekdays(List<Weekday> selectedWeekdays) {
		boolean works = true;
		boolean configEntry = selectedWeekdays.contains(Weekday.MONDAY);
		works = Config.updateProperty(
				DefaultConfigEnum.WEEKDAY_MONDAY.getPropKey(),
				String.valueOf(configEntry))
				&& works;

		configEntry = selectedWeekdays.contains(Weekday.TUESDAY);
		works = Config.updateProperty(
				DefaultConfigEnum.WEEKDAY_TUESDAY.getPropKey(),
				String.valueOf(configEntry))
				&& works;

		configEntry = selectedWeekdays.contains(Weekday.WEDNESDAY);
		works = Config.updateProperty(
				DefaultConfigEnum.WEEKDAY_WEDNESDAY.getPropKey(),
				String.valueOf(configEntry))
				&& works;

		configEntry = selectedWeekdays.contains(Weekday.THURSDAY);
		works = Config.updateProperty(
				DefaultConfigEnum.WEEKDAY_THURSDAY.getPropKey(),
				String.valueOf(configEntry))
				&& works;

		configEntry = selectedWeekdays.contains(Weekday.FRIDAY);
		works = Config.updateProperty(
				DefaultConfigEnum.WEEKDAY_FRIDAY.getPropKey(),
				String.valueOf(configEntry))
				&& works;

		configEntry = selectedWeekdays.contains(Weekday.SATURDAY);
		works = Config.updateProperty(
				DefaultConfigEnum.WEEKDAY_SATURDAY.getPropKey(),
				String.valueOf(configEntry))
				&& works;

		configEntry = selectedWeekdays.contains(Weekday.SUNDAY);
		works = Config.updateProperty(
				DefaultConfigEnum.WEEKDAY_SUNDAY.getPropKey(),
				String.valueOf(configEntry))
				&& works;

		return works;
	}

	/**
	 * Löscht alle Aktivitäten, die an einem der übergebenen Wochentage oder
	 * außerhalb dem Bereich zwischen der übergebenen Start- und Endzeit liegen.
	 * 
	 * @param weekdays
	 *            - Wochentage, für die alle Aktivitäten zu löschen sind
	 * @param weekdayStartTime
	 *            - Startzeit
	 * @param weekdayEndTime
	 *            - Endzeit
	 * @return {@code true}, wenn erfolgreich, falls ein Fehler auftritt
	 *         {@code false}
	 */
	public static boolean deleteActivitiesOutOfDateRange(
			List<Weekday> weekdays, Date weekdayStartTime, Date weekdayEndTime) {
		try {
			for (Weekday w : weekdays) {
				List<Activity> activities = DataAccess.getInstance()
						.getAllActivities(w);
				for (Activity a : activities) {
					CommandCreator.getInstance().createDeleteCommand(a)
							.execute();
				}
			}

			List<Activity> activities = DataAccess.getInstance()
					.getAllActivitiesNotBetween(weekdayStartTime,
							weekdayEndTime);
			for (Activity a : activities) {
				CommandCreator.getInstance().createDeleteCommand(a).execute();
			}

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
