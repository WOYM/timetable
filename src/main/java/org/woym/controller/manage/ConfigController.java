package org.woym.controller.manage;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.woym.config.Config;
import org.woym.config.DefaultConfigEnum;
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

	private static final int MAX_BACKUP_INTERVAL = 1440;
	private static final int MIN_BACKUP_INTERVAL = 1;

	private static final String LOWER_CASE = Config.IDENTIFIER_LOWER_CASE;
	private static final String UPPER_CASE = Config.IDENTIFIER_UPPER_CASE;
	private static final String BOTH_CASES = Config.IDENTIFIER_BOTH_CASES;

	private int timetableGrid;

	private int teacherSettlement;
	private int paSettlement;

	private int typicalActivityDuration;

	private String identifierCase;

	private int backupInterval;

	@PostConstruct
	public void init() {
		timetableGrid = Config.getSingleIntValue(DefaultConfigEnum.TIMETABLE_GRID);
		backupInterval = Config
				.getSingleIntValue(DefaultConfigEnum.BACKUP_INTERVAL);
		teacherSettlement = Config
				.getSingleIntValue(DefaultConfigEnum.TEACHER_HOURLY_SETTLEMENT);
		paSettlement = Config
				.getSingleIntValue(DefaultConfigEnum.PEDAGOGIC_ASSISTANT_HOURLY_SETTLEMENT);
		typicalActivityDuration = Config
				.getSingleIntValue(DefaultConfigEnum.TYPICAL_ACTIVITY_DURATION);
		identifierCase = Config
				.getSingleStringValue(DefaultConfigEnum.SCHOOLCLASS_IDENTIFIER_CASE);
	}

	/**
	 * Diese Methode updated die Einstellungen.
	 */
	public void updateProperties() {
		Boolean works = true;

		works = singleSwitch(
				works,
				Config.updateProperty(DefaultConfigEnum.TIMETABLE_GRID.getPropKey(),
						String.valueOf(timetableGrid)));

		works = singleSwitch(
				works,
				Config.updateProperty(
						DefaultConfigEnum.BACKUP_INTERVAL.getPropKey(),
						String.valueOf(backupInterval)));

		works = singleSwitch(works, Config.updateProperty(
				DefaultConfigEnum.TEACHER_HOURLY_SETTLEMENT.getPropKey(),
				String.valueOf(teacherSettlement)));

		works = singleSwitch(works, Config.updateProperty(
				DefaultConfigEnum.PEDAGOGIC_ASSISTANT_HOURLY_SETTLEMENT
						.getPropKey(), String.valueOf(paSettlement)));

		works = singleSwitch(works, Config.updateProperty(
				DefaultConfigEnum.TYPICAL_ACTIVITY_DURATION.getPropKey(),
				String.valueOf(typicalActivityDuration)));

		works = singleSwitch(works, Config.updateProperty(
				DefaultConfigEnum.SCHOOLCLASS_IDENTIFIER_CASE.getPropKey(),
				identifierCase));

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

	private Boolean singleSwitch(Boolean bool, Boolean bool1) {
		if (bool && bool1) {
			return true;
		}

		return false;
	}

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

	public void setBackupInterval(int backupInterval) {

		if (backupInterval < MIN_BACKUP_INTERVAL) {
			backupInterval = MIN_BACKUP_INTERVAL;
		}

		if (backupInterval > MAX_BACKUP_INTERVAL) {
			backupInterval = MAX_BACKUP_INTERVAL;
		}

		this.backupInterval = backupInterval;
	}

	public int getBackupInterval() {
		return backupInterval;
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

	public int getMaxBackupInterval() {
		return MAX_BACKUP_INTERVAL;
	}

	public int getMinBackupInterval() {
		return MIN_BACKUP_INTERVAL;
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

}
