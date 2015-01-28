package org.woym.controller.manage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.woym.common.config.Config;
import org.woym.common.config.DefaultConfigEnum;
import org.woym.common.exceptions.DatasetException;
import org.woym.common.messages.GenericErrorMessage;
import org.woym.common.messages.MessageHelper;
import org.woym.common.objects.Location;
import org.woym.common.objects.Room;
import org.woym.common.objects.spec.IMemento;
import org.woym.logic.CommandHandler;
import org.woym.logic.SuccessStatus;
import org.woym.logic.command.CommandCreator;
import org.woym.logic.command.MacroCommand;
import org.woym.logic.command.UpdateCommand;
import org.woym.logic.spec.IStatus;
import org.woym.persistence.DataAccess;

/**
 * <h1>TeacherController</h1>
 * <p>
 * Dieser Controller ist für die allgemeine Raumverwaltung in Standorten
 * zuständig.
 * 
 * @author Tim Hansen (tihansen)
 *
 */
@ViewScoped
@ManagedBean(name = "roomController")
public class RoomController implements Serializable {

	private static final long serialVersionUID = 572818674185743147L;

	private Room room;
	private Location location;

	private IMemento roomMemento;
	private IMemento locationMemento;

	private CommandHandler commandHandler = CommandHandler.getInstance();
	private CommandCreator commandCreator = CommandCreator.getInstance();

	private DataAccess dataAccess = DataAccess.getInstance();

	private boolean hideDeletionDialog;
	private boolean hide;

	@PostConstruct
	public void init() {
		hideDeletionDialog = Config
				.getBooleanValue(DefaultConfigEnum.HIDE_ROOM_DELETION_DIALOG);
		hide = hideDeletionDialog;
	}

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

		if (status instanceof SuccessStatus) {
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('wEditRoomDialog').hide();");
		}

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	/**
	 * Löscht einen Raum aus der Datenbank.
	 */
	public void deleteRoom() {
		if (hide != hideDeletionDialog) {
			Config.updateProperty(
					DefaultConfigEnum.HIDE_ROOM_DELETION_DIALOG.getPropKey(),
					String.valueOf(hideDeletionDialog));
		}
		MacroCommand macroCommand = commandCreator.createDeleteCommand(room);
		IStatus status = commandHandler.execute(macroCommand);
		FacesMessage msg = status.report();

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	/**
	 * Öffnet einen neuen Dialog, mit dem sich ein Raum hinzufügen lässt.
	 */
	public void doBeforeAdd() {
		room = new Room();
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

	public boolean isHideDeletionDialog() {
		return hideDeletionDialog;
	}

	public void setHideDeletionDialog(boolean hideDeletionDialog) {
		this.hideDeletionDialog = hideDeletionDialog;
	}

}
