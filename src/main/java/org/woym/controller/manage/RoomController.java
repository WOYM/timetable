package org.woym.controller.manage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.woym.exceptions.DatasetException;
import org.woym.logic.CommandHandler;
import org.woym.logic.SuccessStatus;
import org.woym.logic.command.DeleteCommand;
import org.woym.logic.command.UpdateCommand;
import org.woym.messages.GenericErrorMessage;
import org.woym.messages.MessageHelper;
import org.woym.objects.Location;
import org.woym.objects.Room;
import org.woym.persistence.DataAccess;
import org.woym.spec.logic.IStatus;
import org.woym.spec.objects.IMemento;

@ViewScoped
@ManagedBean(name = "roomController")
public class RoomController implements Serializable {

	private static final long serialVersionUID = 1L;
	private Room room;
	private Location location;

	private IMemento roomMemento;
	private IMemento locationMemento;

	private CommandHandler commandHandler = CommandHandler.getInstance();

	private DataAccess dataAccess = DataAccess.getInstance();

	/**
	 * Liefert eine Liste mit allen Räumen eines Standortes zurück.
	 * 
	 * @return Liste mit allen Räumen eines Standortes
	 */
	public List<Room> getRooms() {
		FacesMessage msg;
		try {
			if (location != null) {
				return dataAccess.getOneLocation(location.getName()).getRooms();
			} else {
				return new ArrayList<>();
			}
		} catch (DatasetException e) {
			msg = MessageHelper.generateMessage(
					GenericErrorMessage.DATABASE_COMMUNICATION_ERROR,
					FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return new ArrayList<>();
		}
	}

	/**
	 * Diese Methode erzeugt ein neues Memento.
	 */
	public void generateRoomMemento() {
		roomMemento = room.createMemento();
	}

	/**
	 * Fügt einen Raum zur Datenbank hinzu. Hierbei wird der Standort
	 * aktualisiert und dessen Liste von Räumen ein neuer Raum angehängt.
	 */
	public void addRoom() {
		locationMemento = location.createMemento();
		location.add(room);
		UpdateCommand<Location> updateCommand = new UpdateCommand<>(location,
				locationMemento);
		IStatus status = commandHandler.execute(updateCommand);
		FacesMessage msg = status.report();

		if (status instanceof SuccessStatus) {
			room = new Room();
		}

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	/**
	 * Speichert einen bearbeiteten Raum in die Datenbank.
	 */
	public void editRoom() {
		UpdateCommand<Room> command = new UpdateCommand<>(room, roomMemento);
		IStatus status = commandHandler.execute(command);
		FacesMessage msg = status.report();

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	/**
	 * Löscht einen Raum aus der Datenbank.
	 */
	public void deleteRoom() {
		DeleteCommand<Room> command = new DeleteCommand<>(room);
		IStatus status = commandHandler.execute(command);
		FacesMessage msg = status.report();

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	/**
	 * Öffnet einen neuen Dialog, mit dem sich ein Raum hinzufügen lässt.
	 */
	public void addRoomDialog() {
		room = new Room();
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('wAddRoomDialog').show();");
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

}
