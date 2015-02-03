package org.woym.controller;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.woym.common.exceptions.DatasetException;
import org.woym.common.messages.GenericErrorMessage;
import org.woym.common.messages.MessageHelper;
import org.woym.common.objects.Entity;
import org.woym.common.objects.Location;
import org.woym.logic.CommandHandler;
import org.woym.logic.spec.IStatus;
import org.woym.persistence.DataAccess;
import org.woym.ui.util.ScheduleModelHolder;

/**
 * <h1>GUIController</h1>
 * <p>
 * Diese Klasse verwaltet den CommandHandler, bzw. die Undo- und
 * Redofunktionalität, die durch den Nutzer ausgelöst werden kann.
 * 
 * @author Tim Hansen (tihansen)
 */
@SessionScoped
@ManagedBean(name = "GUIController")
public class GUIController implements Serializable {

	private static final long serialVersionUID = 8156760488563380338L;

	private static Logger LOGGER = LogManager.getLogger(GUIController.class);

	private CommandHandler commandHandler = CommandHandler.getInstance();

	private DataAccess dataAccess = DataAccess.getInstance();
	private ScheduleModelHolder scheduleModelHolder = ScheduleModelHolder
			.getInstance();

	/**
	 * Diese Methode macht ein Command rückgängig.
	 */
	public void undo() {
		IStatus status = commandHandler.undo();
		scheduleModelHolder.updateScheduleModel();
		FacesContext.getCurrentInstance().addMessage(null, status.report());
	}

	/**
	 * Diese Methode wiederholt ein Command.
	 */
	public void redo() {
		IStatus status = commandHandler.redo();
		scheduleModelHolder.updateScheduleModel();
		FacesContext.getCurrentInstance().addMessage(null, status.report());
	}

	/**
	 * Liefert die Anzahl der dem System bekannten Lehrkräfte zurück.
	 * 
	 * @return Die Anzahl als Zeichenkette
	 */
	public String getAmountOfTeachers() {
		int size = 0;
		try {
			size = dataAccess.getAllTeachers().size();
		} catch (DatasetException e) {
			LOGGER.error(e);
		}
		return String.valueOf(size);
	}

	/**
	 * Liefert die Anzahl der dem System bekannten pädagogischen Mitarbeiter
	 * zurück.
	 * 
	 * @return Die Anzahl als Zeichenkette
	 */
	public String getAmountOfPedagogicAssistants() {
		int size = 0;
		try {
			size = dataAccess.getAllPAs().size();
		} catch (DatasetException e) {
			LOGGER.error(e);
		}
		return String.valueOf(size);
	}

	/**
	 * Liefert die Anzahl der dem System bekannten Mitarbeiter zurück.
	 * 
	 * @return Die Anzahl als Zeichenkette
	 */
	public String getAmountOfEmployees() {
		int size = 0;
		try {
			size = dataAccess.getAllPAs().size();
			size += dataAccess.getAllTeachers().size();
		} catch (DatasetException e) {
			LOGGER.error(e);
		}
		return String.valueOf(size);
	}

	/**
	 * Liefert die Anzahl der dem System bekannten Standorte zurück.
	 * 
	 * @return Die Anzahl als Zeichenkette
	 */
	public String getAmountOfLocations() {
		int size = 0;
		try {
			size = dataAccess.getAllLocations().size();
		} catch (DatasetException e) {
			LOGGER.error(e);
		}
		return String.valueOf(size);
	}

	/**
	 * Liefert die Anzahl der dem System bekannten Räume zurück.
	 * 
	 * @return Die Anzahl als Zeichenkette
	 */
	public String getAmountOfRooms() {
		int size = 0;
		try {
			for (Location location : dataAccess.getAllLocations()) {
				size += location.getRooms().size();
			}
		} catch (DatasetException e) {
			LOGGER.error(e);
		}
		return String.valueOf(size);
	}

	/**
	 * Versucht das übergebene {@linkplain Entity}-Objekt durch den
	 * Datenbankzustand dieses Objektes zu überschreiben.
	 * 
	 * @param entity
	 *            - das zu aktualisierende {@linkplain Entity}-Objekt
	 */
	public static void refresh(Entity entity) {
		try {
			DataAccess.getInstance().refresh(entity);
		} catch (DatasetException e) {
			FacesMessage msg = MessageHelper.generateMessage(
					GenericErrorMessage.DATABASE_COMMUNICATION_ERROR,
					FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}
}
