package org.woym.controller.manage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.woym.common.config.Config;
import org.woym.common.config.DefaultConfigEnum;
import org.woym.common.exceptions.DatasetException;
import org.woym.common.messages.GenericErrorMessage;
import org.woym.common.messages.MessageHelper;
import org.woym.common.objects.Location;
import org.woym.common.objects.MeetingType;
import org.woym.common.objects.Room;
import org.woym.common.objects.spec.IMemento;
import org.woym.controller.GUIController;
import org.woym.logic.CommandHandler;
import org.woym.logic.SuccessStatus;
import org.woym.logic.command.AddCommand;
import org.woym.logic.command.CommandCreator;
import org.woym.logic.command.MacroCommand;
import org.woym.logic.command.UpdateCommand;
import org.woym.logic.spec.IStatus;
import org.woym.persistence.DataAccess;

/**
 * Dieser Controller verwaltet die Personalsitzungen.
 * 
 * @author Adrian
 *
 */
@ViewScoped
@ManagedBean(name = "meetingTypeController")
public class MeetingTypeController implements Serializable {

	private static final long serialVersionUID = 855625183204337451L;

	private static Logger LOGGER = LogManager
			.getLogger(MeetingTypeController.class.getName());

	private MeetingType meetingType;

	private DataAccess dataAccess = DataAccess.getInstance();

	private CommandHandler commandHandler = CommandHandler.getInstance();
	private CommandCreator commandCreator = CommandCreator.getInstance();
	private IMemento meetingTypeMemento;

	private List<Room> rooms;

	private boolean hideDeletionDialog;
	private boolean hide;

	@PostConstruct
	public void init() {
		meetingType = new MeetingType();
		meetingType.setTypicalDuration(getTypicalDuration());
		hideDeletionDialog = Config
				.getBooleanValue(DefaultConfigEnum.HIDE_ACTIVITYTYPE_DELETION_DIALOG);
		hide = hideDeletionDialog;
	}

	/**
	 * Liefert eine Liste mit allen Personalsitzungen zurück.
	 * 
	 * @return Liste mit allen Personalsitzungen
	 */
	public List<MeetingType> getMeetingTypes() {
		try {
			return dataAccess.getAllMeetingTypes();
		} catch (DatasetException e) {
			LOGGER.error(e);
			FacesMessage msg = MessageHelper.generateMessage(
					GenericErrorMessage.DATABASE_COMMUNICATION_ERROR,
					FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, msg);

			return new ArrayList<MeetingType>();
		}
	}

	public List<Room> getAllRooms() {

		try {
			ArrayList<Room> tempRooms = new ArrayList<>();

			for (Location location : dataAccess.getAllLocations()) {
				tempRooms.addAll(location.getRooms());
			}

			return tempRooms;

		} catch (Exception e) {
			LOGGER.error(e);
			FacesMessage msg = MessageHelper.generateMessage(
					GenericErrorMessage.DATABASE_COMMUNICATION_ERROR,
					FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, msg);

			return new ArrayList<Room>();
		}
	}

	/**
	 * Löscht die selektierte Personalsitzung.
	 */
	public void deleteMeetingType() {
		if (hide != hideDeletionDialog) {
			Config.updateProperty(
					DefaultConfigEnum.HIDE_ACTIVITYTYPE_DELETION_DIALOG
							.getPropKey(), String.valueOf(hideDeletionDialog));
		}
		MacroCommand macroCommand = commandCreator
				.createDeleteCommand(meetingType);
		IStatus status = commandHandler.execute(macroCommand);
		FacesMessage msg = status.report();

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	/**
	 * Diese Methode tut alles, was an Modifikationen vor dem Bearbeiten eines
	 * Objektes notwendig ist.
	 * 
	 * Dazu gehören das Erzeugen eines Mementos und das Setzen der Räume.
	 */
	public void doBeforeEdit() {
		meetingTypeMemento = meetingType.createMemento();
		rooms = meetingType.getRooms();
	}

	/**
	 * Bearbeitet ein Personalsitzung.
	 */
	public void editMeetingType() {
		meetingType.setRooms(rooms);
		UpdateCommand<MeetingType> command = new UpdateCommand<>(meetingType,
				meetingTypeMemento);
		IStatus status = commandHandler.execute(command);
		FacesMessage msg = status.report();

		if (status instanceof SuccessStatus) {
			// Reset stuff
			rooms = new ArrayList<Room>();

			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('wEditMeetingTypeDialog').hide();");
		}

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	/**
	 * Fügt eine neue Personalsitzung hinzu.
	 */
	public void addMeetingType() {
		meetingType.setRooms(rooms);

		AddCommand<MeetingType> command = new AddCommand<>(meetingType);
		IStatus status = commandHandler.execute(command);
		FacesMessage msg = status.report();

		if (status instanceof SuccessStatus) {
			meetingType = new MeetingType();
			meetingType.setTypicalDuration(getTypicalDuration());
			rooms = new ArrayList<Room>();
		}

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	/**
	 * @see GUIController#refresh(org.woym.common.objects.Entity)
	 */
	public void refresh() {
		GUIController.refresh(meetingType);
	}

	public MeetingType getMeetingType() {
		return meetingType;
	}

	public void setMeetingType(MeetingType meetingType) {
		this.meetingType = meetingType;
	}

	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}

	public List<Room> getRooms() {
		return rooms;
	}

	public boolean isHideDeletionDialog() {
		return hideDeletionDialog;
	}

	public void setHideDeletionDialog(boolean hideDeletionDialog) {
		this.hideDeletionDialog = hideDeletionDialog;
	}

	private int getTypicalDuration() {
		int typicalDuration = 0;

		// Get prop-value
		String[] typicalDurationString = Config
				.getPropValue(DefaultConfigEnum.TYPICAL_ACTIVITY_DURATION
						.getPropKey());
		// Check for exactly one valid entry in configuration
		if (typicalDurationString.length == 1) {
			try {
				typicalDuration = Integer.parseInt(typicalDurationString[0]);
			} catch (NumberFormatException e) {
				// Do nothing
			}
		}

		return typicalDuration;
	}
}
