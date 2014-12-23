package org.woym.controller.manage;

import java.util.HashMap;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.woym.exceptions.DatasetException;
import org.woym.messages.GenericStatusMessage;
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
public class LocationController {
	private static Logger LOGGER = LogManager.getLogger(LocationController.class);

	private DataAccess dataAccess = DataAccess.getInstance();
	
	private Location location;
	private Room room;

	/**
	 * Saves an edited teacher to the database.
	 */
	public void editLocation() {
		try {
			dataAccess.update(location);
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Standort aktualisiert", location.getName());
			FacesContext.getCurrentInstance().addMessage(null, message);
		} catch (DatasetException e) {
			LOGGER.error(e);
		}
	}

	/**
	 * Deletes the selected teacher.
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
						GenericStatusMessage.DATABASE_COMMUNICATION_ERROR.getSummary(),
						GenericStatusMessage.DATABASE_COMMUNICATION_ERROR
								.getStatusMessage());
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			}
		}
	}
	
	/**
	 * Öffnet einen neuen Dialog, mit Hilfe dessen ein neuer Standort
	 * hinzugefügt werden kann.
	 */
	public void addLocationDialog() {
		location = new Location();
		openDialog("addLocationDialog");
	}
	
	/**
	 * Öffnet einen neuen Dialog, mit Hilfe dessen ein neuer Raum
	 * hinzugefügt werden kann.
	 */
	public void addRoomDialog() {
		room = new Room();
		location.addRoom(room);
		openDialog("addRoomDialog");
	}
	
	/**
	 * Öffnet einen neuen Dialog, mit Hilfe dessen ein neuer Standort
	 * bearbeitet werden kann.
	 */
	public void editLocationDialog() {
		openDialog("addLocationDialog");
	}
	
	/**
	 * Öffnet einen neuen Dialog, mit Hilfe dessen ein neuer Standort
	 * bearbeitet werden kann.
	 */
	public void editRoomDialog() {
		openDialog("addRoomDialog");
	}

	private void openDialog(String dialog) {
		location = new Location();
		Map<String, Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("draggable", false);
		options.put("resizable", false);
		options.put("contentHeight", 600);
		options.put("contentWidth", 800);

		RequestContext rc = RequestContext.getCurrentInstance();
		rc.openDialog(dialog, options, null);
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

}
