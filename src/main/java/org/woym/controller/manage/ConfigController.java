package org.woym.controller.manage;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.woym.common.config.Config;
import org.woym.common.config.DefaultConfigEnum;
import org.woym.common.objects.Weekday;
import org.woym.logic.spec.IStatus;
import org.woym.logic.util.ConfigControllerUtil;

/**
 * <h1>AcademicYearAndClassController</h1>
 * <p>
 * Dieser Controller verwaltet die verschiedenen Jahrgänge und Klassen der
 * Jahrgänge.
 * 
 * @author Tim Hansen (tihansen)
 *
 */
@ViewScoped
@ManagedBean(name = "configController")
public class ConfigController implements Serializable {

	private static final long serialVersionUID = -5701369389504640808L;

	private static final int MAX_SPINNER_VALUE = 1440;
	private static final int MIN_SPINNER_VALUE = 1;

	private static final String LOWER_CASE = Config.IDENTIFIER_LOWER_CASE;
	private static final String UPPER_CASE = Config.IDENTIFIER_UPPER_CASE;
	private static final String BOTH_CASES = Config.IDENTIFIER_BOTH_CASES;

	private static final int MAX_DAY_VALUE = 30;

	public static final String DISABLED_BACKUPS = "disabled";
	public static final String MINUTE_BACKUPS = "minutes";
	public static final String DAILY_BACKUPS = "daily";

	private List<Weekday> weekdays = Arrays.asList(Weekday.values());
	private List<Weekday> selectedWeekdays;

	private Date weekdayStartTime;
	private Date weekdayEndTime;
	private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

	private int timetableGrid;

	private int teacherSettlement;
	private int paSettlement;

	private int typicalActivityDuration;

	private String identifierCase;

	/**
	 * Interner Wert für das Backup-Interval
	 */
	private int backupIntervalValue;

	/**
	 * Backup-Interval bei minütlicher Auswahl.
	 */
	private int backupInterval;

	/**
	 * Backup Startzeit bei Auswahl täglicher Backups.
	 */
	private Date backupStartTime;

	/**
	 * Intervall-Typ "minutes" oder "daily"
	 */
	private String intervalType;

	private int selectedDayValue = 1;

	@PostConstruct
	public void init() {
		try {
			weekdayStartTime = sdf.parse(Config
					.getSingleStringValue(DefaultConfigEnum.WEEKDAY_STARTTIME));
			weekdayEndTime = sdf.parse(Config
					.getSingleStringValue(DefaultConfigEnum.WEEKDAY_ENDTIME));
			selectedWeekdays = Config.getValidWeekdays();
			timetableGrid = Config
					.getSingleIntValue(DefaultConfigEnum.TIMETABLE_GRID);

			teacherSettlement = Config
					.getSingleIntValue(DefaultConfigEnum.TEACHER_HOURLY_SETTLEMENT);
			paSettlement = Config
					.getSingleIntValue(DefaultConfigEnum.PEDAGOGIC_ASSISTANT_HOURLY_SETTLEMENT);

			typicalActivityDuration = Config
					.getSingleIntValue(DefaultConfigEnum.TYPICAL_ACTIVITY_DURATION);

			identifierCase = Config
					.getSingleStringValue(DefaultConfigEnum.SCHOOLCLASS_IDENTIFIER_CASE);

			backupIntervalValue = Config
					.getSingleIntValue(DefaultConfigEnum.BACKUP_INTERVAL);

			selectBackupSettings();
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Fehler beim Laden der Einstellungen",
					"Beim Laden der Einstellungen ist ein Fehler aufgetreten.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	/**
	 * Diese Methode updated die Einstellungen.
	 */
	public void updateProperties() {

		if (!validateBackupSettings()) {
			return;
		}

		Boolean works = true;

		works = ConfigControllerUtil.updateWeekdays(selectedWeekdays) && works;

		works = Config.updateProperty(
				DefaultConfigEnum.WEEKDAY_STARTTIME.getPropKey(),
				sdf.format(weekdayStartTime))
				&& works;
		
		works = Config.updateProperty(
				DefaultConfigEnum.WEEKDAY_ENDTIME.getPropKey(),
				sdf.format(weekdayEndTime))
				&& works;
		
		works = Config.updateProperty(
				DefaultConfigEnum.WEEKDAY_ENDTIME.getPropKey(),
				sdf.format(weekdayEndTime)) && works;

		works = Config.updateProperty(
				DefaultConfigEnum.TIMETABLE_GRID.getPropKey(),
				String.valueOf(timetableGrid))
				&& works;

		works = Config.updateProperty(
				DefaultConfigEnum.TEACHER_HOURLY_SETTLEMENT.getPropKey(),
				String.valueOf(teacherSettlement))
				&& works;

		works = Config.updateProperty(
				DefaultConfigEnum.PEDAGOGIC_ASSISTANT_HOURLY_SETTLEMENT
						.getPropKey(), String.valueOf(paSettlement))
				&& works;

		works = Config.updateProperty(
				DefaultConfigEnum.TYPICAL_ACTIVITY_DURATION.getPropKey(),
				String.valueOf(typicalActivityDuration))
				&& works;

		works = Config.updateProperty(
				DefaultConfigEnum.SCHOOLCLASS_IDENTIFIER_CASE.getPropKey(),
				identifierCase)
				&& works;

		works = updateBackupInterval() && works;

		List<Weekday> toBeClearedWeekdays = new ArrayList<Weekday>(weekdays);
		toBeClearedWeekdays.removeAll(selectedWeekdays);
		works = ConfigControllerUtil.deleteActivitiesOutOfDateRange(
				toBeClearedWeekdays, weekdayStartTime, weekdayEndTime) && works;

		FacesMessage msg;

		if (works) {
			msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Einstellungen aktualisiert.",
					"Einstellungen wurden erfolgreich aktualisiert.");
		} else {
			msg = new FacesMessage(
					FacesMessage.SEVERITY_WARN,
					"Fehler beim Aktualisieren",
					"Beim Aktualisieren der Einstellungen ist ein Fehler aufgetreten. "
							+ "Es wurden ggf. nicht alle Einstellungen aktualisiert.");
		}

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	/**
	 * Löscht alle Aktivitäten mithilfe von
	 * {@linkplain ConfigController#deleteAllActivities()} und gibt eine
	 * entsprechende {@linkplain FacesMessage} aus.
	 */
	public void deleteAllActivities() {
		IStatus status = ConfigControllerUtil.deleteAllActivities();
		FacesMessage msg = status.report();
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	/**
	 * Setzt alle Dialoge zurück, so dass zukünftig wieder alle Dialoge
	 * angezeigt werden.
	 */
	public void resetDialogs() {
		for (DefaultConfigEnum value : DefaultConfigEnum.values()) {
			String key = value.getPropKey();
			if (key.contains(Config.RESET_DIALOGS_IDENTFIER_STRING)) {
				Config.updateProperty(key, "false");
			}
		}
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Dialoge zurückgesetzt",
				"Die Dialoge wurden erfolgreich zurückgesetzt.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	/**
	 * Selektiert die korrekten Backup-Einstellungen ja nach Wert von
	 * {@linkplain ConfigController#backupIntervalValue}.
	 * 
	 * @throws ParseException
	 */
	private void selectBackupSettings() throws ParseException {
		if (backupIntervalValue == 0) {
			intervalType = DISABLED_BACKUPS;
		} else if (backupIntervalValue < 1440) {
			intervalType = MINUTE_BACKUPS;
			backupInterval = backupIntervalValue;
		} else {
			intervalType = DAILY_BACKUPS;
			selectedDayValue = (backupIntervalValue / 1440);
			if (selectedDayValue > MAX_DAY_VALUE) {
				selectedDayValue = MAX_DAY_VALUE;
			}
		}
		backupStartTime = sdf.parse(Config
				.getSingleStringValue(DefaultConfigEnum.BACKUP_TIME));
	}

	/**
	 * Aktualisiert die Backup-Einstellungen, tritt bei einem
	 * Aktualisierungsversuch ein Fehler auf, wird nachdem alle Aktualisierungen
	 * durchgeführt wurden {@code false} zurückgegeben, ansonsten {@code true}.
	 * 
	 * @return {@code false}, wenn eine Aktualisierung fehlschlägt
	 */
	private boolean updateBackupInterval() {
		if (intervalType.equals(DISABLED_BACKUPS)) {
			return ConfigControllerUtil.disableBackups();
		}
		if (intervalType.equals(MINUTE_BACKUPS)) {
			return ConfigControllerUtil.minuteBackups(backupInterval);
		} else {
			return ConfigControllerUtil.dailyBackups(selectedDayValue,
					backupStartTime);
		}
	}

	/**
	 * Validiert die Backupeinstellungen. Ist eine Einstellung nicht valide,
	 * wird eine entsprechende FacesMessage mit Hinweis darauf erzeugt und dem
	 * FacesContext hinzgefügt. Schließlich wird {@code false} zurückgegeben.
	 * Sind alle Einstellungen valide, wird {@code true} zurückgegeben.
	 * 
	 * @return {@code true} bei validen Einstellungen, ansonsten {@code false}
	 */
	private boolean validateBackupSettings() {
		boolean works = true;
		if (intervalType == null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Keine Backup-Einstellung gewählt",
					"Wählen Sie eine Backup-Einstellung aus.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return false;
		}

		if (intervalType.equals(MINUTE_BACKUPS)
				&& (backupInterval < MIN_SPINNER_VALUE || backupInterval > MAX_SPINNER_VALUE)) {
			works = false;
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Backup-Intervall ungültig.",
					"Geben Sie ein gültiges Backup-Intervall zwischen "
							+ MIN_SPINNER_VALUE + " und " + MAX_SPINNER_VALUE
							+ " an.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		if (intervalType.equals(DAILY_BACKUPS)) {
			if (selectedDayValue > MAX_DAY_VALUE
					|| selectedDayValue < MIN_SPINNER_VALUE) {
				works = false;
				FacesMessage msg = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Ausgewählter täglicher Abstand der Backups ungültig",
						"Wählen Sie einen gültigen täglichen Abstand zwischen "
								+ MIN_SPINNER_VALUE + " und "
								+ MAX_SPINNER_VALUE + ".");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
			if (backupStartTime == null) {
				works = false;
				FacesMessage msg = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Keine Uhrzeit ausgewählt.",
						"Wählen Sie eine Uhrzeit für das Backup aus.");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		}
		return works;
	}

	public int getMaxSpinnerValue() {
		return MAX_SPINNER_VALUE;
	}

	public int getMinSpinnerValue() {
		return MIN_SPINNER_VALUE;
	}

	public String getLowerCase() {
		return LOWER_CASE;
	}

	public String getUpperCase() {
		return UPPER_CASE;
	}

	public String getBothCases() {
		return BOTH_CASES;
	}

	public int getMaxDayValue() {
		return MAX_DAY_VALUE;
	}

	public List<Weekday> getWeekdays() {
		return weekdays;
	}

	public void setWeekdays(List<Weekday> weekdays) {
		this.weekdays = weekdays;
	}

	public List<Weekday> getSelectedWeekdays() {
		return selectedWeekdays;
	}

	public void setSelectedWeekdays(List<Weekday> selectedWeekdays) {
		this.selectedWeekdays = selectedWeekdays;
	}

	public Date getWeekdayStartTime() {
		return weekdayStartTime;
	}

	public void setWeekdayStartTime(Date weekdayStartTime) {
		this.weekdayStartTime = weekdayStartTime;
	}

	public Date getWeekdayEndTime() {
		return weekdayEndTime;
	}

	public void setWeekdayEndTime(Date weekdayEndTime) {
		this.weekdayEndTime = weekdayEndTime;
	}

	public int getTimetableGrid() {
		return timetableGrid;
	}

	public void setTimetableGrid(int timetableGrid) {
		if (timetableGrid < MIN_SPINNER_VALUE) {
			timetableGrid = MIN_SPINNER_VALUE;
		}
		if (timetableGrid > MAX_SPINNER_VALUE) {
			timetableGrid = MAX_SPINNER_VALUE;
		}

		this.timetableGrid = timetableGrid;
	}

	public int getTeacherSettlement() {
		return teacherSettlement;
	}

	public void setTeacherSettlement(int teacherSettlement) {
		if (teacherSettlement < MIN_SPINNER_VALUE) {
			teacherSettlement = MIN_SPINNER_VALUE;
		}
		if (teacherSettlement > MAX_SPINNER_VALUE) {
			teacherSettlement = MAX_SPINNER_VALUE;
		}
		this.teacherSettlement = teacherSettlement;
	}

	public int getPaSettlement() {
		return paSettlement;
	}

	public void setPaSettlement(int paSettlement) {
		if (paSettlement < MIN_SPINNER_VALUE) {
			paSettlement = MIN_SPINNER_VALUE;
		}
		if (paSettlement > MAX_SPINNER_VALUE) {
			paSettlement = MAX_SPINNER_VALUE;
		}
		this.paSettlement = paSettlement;
	}

	public int getTypicalActivityDuration() {
		return typicalActivityDuration;
	}

	public void setTypicalActivityDuration(int typicalActivityDuration) {
		if (typicalActivityDuration < MIN_SPINNER_VALUE) {
			typicalActivityDuration = MIN_SPINNER_VALUE;
		}
		if (typicalActivityDuration > MAX_SPINNER_VALUE) {
			typicalActivityDuration = MAX_SPINNER_VALUE;
		}
		this.typicalActivityDuration = typicalActivityDuration;
	}

	public String getIdentifierCase() {
		return identifierCase;
	}

	public void setIdentifierCase(String identifierCase) {
		this.identifierCase = identifierCase;
	}

	public void setBackupInterval(int backupInterval) {

		if (backupInterval < MIN_SPINNER_VALUE) {
			backupInterval = MIN_SPINNER_VALUE;
		}

		if (backupInterval > MAX_SPINNER_VALUE) {
			backupInterval = MAX_SPINNER_VALUE;
		}

		this.backupInterval = backupInterval;
	}

	public int getBackupInterval() {
		return backupInterval;
	}

	public Date getBackupStartTime() {
		return backupStartTime;
	}

	public void setBackupStartTime(Date backupStartTime) {
		this.backupStartTime = backupStartTime;
	}

	public String getIntervalType() {
		return intervalType;
	}

	public void setIntervalType(String intervalType) {
		this.intervalType = intervalType;
	}

	public int getSelectedDayValue() {
		return selectedDayValue;
	}

	public void setSelectedDayValue(int selectedDayValue) {
		this.selectedDayValue = selectedDayValue;
	}

	public String getDisabledBackups() {
		return DISABLED_BACKUPS;
	}

	public String getMinuteBackups() {
		return MINUTE_BACKUPS;
	}

	public String getDailyBackups() {
		return DAILY_BACKUPS;
	}

}
