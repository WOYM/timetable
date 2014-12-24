package org.woym.controller.manage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.woym.exceptions.DatasetException;
import org.woym.messages.GenericErrorMessage;
import org.woym.objects.Location;
import org.woym.objects.Room;
import org.woym.persistence.DataAccess;

/**
 * <h1>LocationController</h1>
 * <p>
 * This controller manages locations and rooms.
 * 
 * @author Tim Hansen (tihansen)
 *
 */
@SessionScoped
@ManagedBean(name = "locationController")
public class LocationController implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Datenbankinstanz
	 */
	private DataAccess dataAccess = DataAccess.getInstance();
	
	private static Logger LOGGER = LogManager
			.getLogger(LocationController.class);

	private Location location;
	private Room room;

	private Boolean selected = false;

	/**
	 * Liefert eine Liste mit allen Standorten zurück.
	 * 
	 * @return Liste mit allen Standorten
	 */
	public List<Location> getLocations() {
		try {
			return dataAccess.getAllLocations();
		} catch (DatasetException e) {
			FacesMessage msg = new FacesMessage(
					GenericErrorMessage.DATABASE_COMMUNICATION_ERROR
							.getSummary(),
					GenericErrorMessage.DATABASE_COMMUNICATION_ERROR
							.getStatusMessage());
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			return new ArrayList<Location>();
		}
	}

	/**
	 * Liefert eine Liste mit allen Räumen zurück.
	 * 
	 * @return Liste mit allen Räumen
	 */
	public List<Room> getRooms() {
		try {
			return dataAccess.getOneLocation(location.getName()).getRooms();
		} catch (DatasetException e) {
			FacesMessage msg = new FacesMessage(
					GenericErrorMessage.DATABASE_COMMUNICATION_ERROR
							.getSummary(),
					GenericErrorMessage.DATABASE_COMMUNICATION_ERROR
							.getStatusMessage());
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			return new ArrayList<Room>();
		}
	}

	/**
	 * Speichert einen bearbeiteten Standort in die Datenbank.
	 */
	public void editLocation() {
		try {
			dataAccess.update(location);
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Standort aktualisiert", location.getName());
			FacesContext.getCurrentInstance().addMessage(null, message);
		} catch (DatasetException e) {
			FacesMessage msg = new FacesMessage(
					GenericErrorMessage.DATABASE_COMMUNICATION_ERROR
							.getSummary(),
					GenericErrorMessage.DATABASE_COMMUNICATION_ERROR
							.getStatusMessage());
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
		}
	}

	/**
	 * Speichert einen bearbeiteten Raum in die Datenbank.
	 */
	public void editRoom() {
		try {
			dataAccess.update(room);
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Raum aktualisiert", room.getName());
			FacesContext.getCurrentInstance().addMessage(null, message);
		} catch (DatasetException e) {
			FacesMessage msg = new FacesMessage(
					GenericErrorMessage.DATABASE_COMMUNICATION_ERROR
							.getSummary(),
					GenericErrorMessage.DATABASE_COMMUNICATION_ERROR
							.getStatusMessage());
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
		}
	}

	/**
	 * Löscht einen Standort aus der Datenbank.
	 */
	public void deleteLocation() {
		if (location != null) {
			try {
				dataAccess.delete(location);
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_INFO, "Standort gelöscht",
						location.getName());
				FacesContext.getCurrentInstance().addMessage(null, message);
			} catch (DatasetException e) {
				FacesMessage msg = new FacesMessage(
						GenericErrorMessage.DATABASE_COMMUNICATION_ERROR
								.getSummary(),
						GenericErrorMessage.DATABASE_COMMUNICATION_ERROR
								.getStatusMessage());
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			}
		}
	}

	/**
	 * Löscht einen Standort aus der Datenbank.
	 */
	public void deleteRoom() {
		if (room != null) {
			try {
				dataAccess.delete(room);
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_INFO, "Raum gelöscht",
						room.getName());
				FacesContext.getCurrentInstance().addMessage(null, message);
			} catch (DatasetException e) {
				FacesMessage msg = new FacesMessage(
						GenericErrorMessage.DATABASE_COMMUNICATION_ERROR
								.getSummary(),
						GenericErrorMessage.DATABASE_COMMUNICATION_ERROR
								.getStatusMessage());
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			}
		}
	}

	/**
	 * Fügt einen Standort zur Datenbank hinzu
	 */
	public void addLocationFromDialog() {
		try {
			dataAccess.persist(location);
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Lehrer hinzugefügt", location.getName());
			FacesContext.getCurrentInstance().addMessage(null, message);
		} catch (DatasetException e) {
			LOGGER.error(e);
			FacesMessage msg = new FacesMessage(
					GenericErrorMessage.DATABASE_COMMUNICATION_ERROR
							.getSummary(),
					GenericErrorMessage.DATABASE_COMMUNICATION_ERROR
							.getStatusMessage());
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
		}
	}

	/**
	 * Öffnet einen neuen Dialog, mit Hilfe dessen ein neuer Standort
	 * hinzugefügt werden kann.
	 */
	public void addLocationDialog() {
		location = new Location();
		selected = false;
		openDialog("addLocationDialog");
	}

	/**
	 * Öffnet einen neuen Dialog, mit Hilfe dessen ein neuer Raum hinzugefügt
	 * werden kann.
	 */
	public void addRoomDialog() {
		room = new Room();
		// TODO: In persist(room)
		location.addRoom(room);
		openDialog("addRoomDialog");
	}

	/**
	 * Öffnet einen neuen Dialog, mit Hilfe dessen ein neuer Standort bearbeitet
	 * werden kann.
	 */
	public void editLocationDialog() {
		openDialog("editLocationDialog");
	}

	/**
	 * Öffnet einen neuen Dialog, mit Hilfe dessen ein neuer Standort bearbeitet
	 * werden kann.
	 */
	public void editRoomDialog() {
		openDialog("editRoomDialog");
	}

	private void openDialog(String dialog) {
		Map<String, Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("draggable", false);
		options.put("resizable", false);
		options.put("contentHeight", 300);
		options.put("contentWidth", 600);

		RequestContext rc = RequestContext.getCurrentInstance();
		rc.openDialog(dialog, options, null);
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		if (location != null) {
			this.location = location;
			selected = true;
		}
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Boolean getSelected() {
		return selected;
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

}
