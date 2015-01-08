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
import org.woym.config.Config;
import org.woym.config.DefaultConfigEnum;
import org.woym.exceptions.DatasetException;
import org.woym.logic.CommandHandler;
import org.woym.logic.SuccessStatus;
import org.woym.logic.command.AddCommand;
import org.woym.logic.command.CommandCreator;
import org.woym.logic.command.MacroCommand;
import org.woym.logic.command.UpdateCommand;
import org.woym.logic.spec.IStatus;
import org.woym.messages.GenericErrorMessage;
import org.woym.messages.MessageHelper;
import org.woym.objects.LessonType;
import org.woym.objects.Location;
import org.woym.objects.Room;
import org.woym.objects.spec.IMemento;
import org.woym.persistence.DataAccess;

/**
 * <h1>LessonTypeController</h1>
 * <p>
 * Dieser Controller verwaltet die verschiedenen Unterrichtsinhalte.
 * 
 * @author Tim Hansen (tihansen)
 *
 */
@ViewScoped
@ManagedBean(name = "lessonTypeController")
public class LessonTypeController implements Serializable {

	private static final long serialVersionUID = -3486820231036039086L;

	private static Logger LOGGER = LogManager
			.getLogger(LessonTypeController.class);

	private LessonType lessonType;

	private DataAccess dataAccess = DataAccess.getInstance();

	private CommandHandler commandHandler = CommandHandler.getInstance();
	private CommandCreator commandCreator = CommandCreator.getInstance();
	private IMemento lessonTypeMemento;

	private List<Room> rooms;

	private boolean hideDeletionDialog;
	private boolean hide;

	@PostConstruct
	public void init() {
		lessonType = new LessonType();
		lessonType.setTypicalDuration(getTypicalDuration());
		hideDeletionDialog = Config
				.getBooleanValue(DefaultConfigEnum.HIDE_ACTIVITYTYPE_DELETION_DIALOG);
		hide = hideDeletionDialog;
	}

	/**
	 * Liefert eine Liste mit allen Unterrichtsinhalten zurück.
	 * 
	 * @return Liste mit allen Unterrichtsinhalten
	 */
	public List<LessonType> getLessonTypes() {
		try {
			return dataAccess.getAllLessonTypes();
		} catch (DatasetException e) {
			LOGGER.error(e);
			FacesMessage msg = MessageHelper.generateMessage(
					GenericErrorMessage.DATABASE_COMMUNICATION_ERROR,
					FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, msg);

			return new ArrayList<LessonType>();
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
	 * Setzt die Werte zum Bearbeiten eines neuen Unterrichtstypen.
	 */
	public void addLessonTypeDialog() {

		lessonType = new LessonType();
		lessonType.setTypicalDuration(getTypicalDuration());
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('wAddLessonTypeDialog').show();");

	}

	/**
	 * Löscht den selektierten Unterrichtsinhalt.
	 */
	public void deleteLessonType() {
		if (hide != hideDeletionDialog) {
			Config.updateProperty(
					DefaultConfigEnum.HIDE_ACTIVITYTYPE_DELETION_DIALOG
							.getPropKey(), String.valueOf(hideDeletionDialog));
		}
		MacroCommand macroCommand = commandCreator
				.createDeleteCommand(lessonType);
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
		lessonTypeMemento = lessonType.createMemento();
		rooms = lessonType.getRooms();
	}

	/**
	 * Bearbeitet einen Unterrichtsinhalt.
	 */
	public void editLessonType() {
		lessonType.setRooms(rooms);
		UpdateCommand<LessonType> command = new UpdateCommand<>(lessonType,
				lessonTypeMemento);
		IStatus status = commandHandler.execute(command);
		FacesMessage msg = status.report();

		if (status instanceof SuccessStatus) {
			// Reset stuff
			rooms = new ArrayList<Room>();

			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('wEditLessonTypeDialog').hide();");
		}

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	/**
	 * Fügt einen neuen Unterrichtsinhalt hinzu.
	 */
	public void addLessonType() {
		lessonType.setRooms(rooms);

		AddCommand<LessonType> command = new AddCommand<>(lessonType);
		IStatus status = commandHandler.execute(command);
		FacesMessage msg = status.report();

		if (status instanceof SuccessStatus) {
			lessonType = new LessonType();
			lessonType.setTypicalDuration(getTypicalDuration());
			rooms = new ArrayList<Room>();
		}

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public LessonType getLessonType() {
		return lessonType;
	}

	public void setLessonType(LessonType lessonType) {
		this.lessonType = lessonType;
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
