package org.woym.controller.manage;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.woym.config.Config;
import org.woym.config.DefaultConfigEnum;

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

	private static final long serialVersionUID = 1L;
	private static int MAX_BACKUP_INTERVAL = 1440;
	private static int MIN_BACKUP_INTERVAL = 1;

	private int backupInterval;

	@PostConstruct
	public void init() {
		backupInterval = getConfValInt(DefaultConfigEnum.BACKUP_INTERVAL);
	}

	/**
	 * Diese Methode updated die Einstellungen.
	 */
	public void updateProperties() {
		Boolean works = true;

		if (works) {
			works = Config.updateProperty(
					DefaultConfigEnum.BACKUP_INTERVAL.getPropKey(),
					String.valueOf(backupInterval));
		}

		FacesMessage msg;

		if (works) {
			msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Einstellungen aktualisiert.",
					"Einstellungen wurden erfolgreich aktualisiert.");
		} else {
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Einstellungen nicht aktualisiert.",
					"Beim Aktualisieren der Einstellungen ist ein Fehler aufgetreten");
		}
		
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	/**
	 * Diese Methode lädt eine Einstellung mit Zahlenwert aus der Konfiguration.
	 * 
	 * Prüft nur nach dem ersten Wert des Konfigurationselementes.
	 * 
	 * @param defaultConfigEnum
	 *            Der Key
	 * @return Die Value
	 */
	private int getConfValInt(DefaultConfigEnum defaultConfigEnum) {

		int valInt = 0;

		// Get prop-value
		String[] valIntString = Config.getPropValue(defaultConfigEnum
				.getPropKey());

		try {
			valInt = Integer.parseInt(valIntString[0]);
		} catch (NumberFormatException e) {
			// Do nothing, will return 0
		}

		return valInt;
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

	public int getMaxBackupInterval() {
		return MAX_BACKUP_INTERVAL;
	}

	public int getMinBackupInterval() {
		return MIN_BACKUP_INTERVAL;
	}

}
