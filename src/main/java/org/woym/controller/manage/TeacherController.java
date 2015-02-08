package org.woym.controller.manage;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import org.woym.common.config.Config;
import org.woym.common.config.DefaultConfigEnum;
import org.woym.common.exceptions.DatasetException;
import org.woym.common.messages.GenericErrorMessage;
import org.woym.common.messages.MessageHelper;
import org.woym.common.objects.ActivityType;
import org.woym.common.objects.Teacher;
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
 * <h1>TeacherController</h1>
 * <p>
 * Dieser Controller ist für die allgemeine Lehrkraftverwaltung zuständig.
 * 
 * @author Tim Hansen (tihansen)
 *
 */
@ViewScoped
@ManagedBean(name = "teacherController")
public class TeacherController implements Serializable {

	private static final long serialVersionUID = -8212155094392549715L;

	private static Logger LOGGER = LogManager.getLogger(TeacherController.class
			.getName());

	private DataAccess dataAccess = DataAccess.getInstance();

	private CommandHandler commandHandler = CommandHandler.getInstance();
	private CommandCreator commandCreator = CommandCreator.getInstance();

	private Teacher teacher;

	private IMemento teacherMemento;

	private DualListModel<ActivityType> activityTypes;

	private boolean hideDeletionDialog;
	private boolean hide;

	private int hourlySettlement;

	@PostConstruct
	public void init() {
		teacher = new Teacher();
		hideDeletionDialog = Config
				.getBooleanValue(DefaultConfigEnum.HIDE_TEACHER_DELETION_DIALOG);
		hide = hideDeletionDialog;
		hourlySettlement = Config
				.getSingleIntValue(DefaultConfigEnum.TEACHER_HOURLY_SETTLEMENT);
	}

	/**
	 * Diese Methode liefert ein Modell zweier Listen zurück. In diesen Listen
	 * befinden sich die verfügbaren und die gewählten Unterrichtsinhalte für
	 * einen Lehrer
	 * 
	 * @return Liste mit Unterrichtstypen
	 */
	public DualListModel<ActivityType> getActivityTypes() {
		List<ActivityType> allActivityTypes;
		List<ActivityType> possibleActivityTypes;

		// Logic to display correct lists
		try {
			allActivityTypes = new ArrayList<>();
			possibleActivityTypes = teacher.getPossibleActivityTypes();

			for (ActivityType activityType : dataAccess.getAllLessonTypes()) {
				if (!possibleActivityTypes.contains(activityType)) {
					allActivityTypes.add(activityType);
				}
			}

			activityTypes = new DualListModel<ActivityType>(allActivityTypes,
					possibleActivityTypes);
		} catch (DatasetException e) {
			LOGGER.error(e);
			FacesMessage msg = MessageHelper.generateMessage(
					GenericErrorMessage.DATABASE_COMMUNICATION_ERROR,
					FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, msg);
			activityTypes = new DualListModel<>();
		}
		// Can return null, that is okay
		return activityTypes;
	}

	public void setActivityTypes(DualListModel<ActivityType> activityTypes) {
		this.activityTypes = activityTypes;
	}

	/**
	 * Event wird beim Transfer im Dialog gefeuert. Enthält Aktivitätstypen.
	 * 
	 * @param event
	 *            Das Event
	 */
	public void onTransfer(TransferEvent event) {
		teacher.setPossibleActivityTypes(activityTypes.getTarget());
	}

	/**
	 * Prüft bei Veränderung der Wochenstunden im Dialog, ob diese kleiner als
	 * die verteilten Stunden der Lehrkraft sind und fügt dem FacesContext in
	 * diesem Falle eine Warn-Message hinzu.
	 * 
	 * @param event
	 */
	public void checkHoursPerWeek(ValueChangeEvent event) {
		BigDecimal newValue = (BigDecimal) event.getNewValue();
		if (newValue == null) {
			return;
		}
		if (newValue.compareTo(teacher.getAllocatedHours()) < 0) {
			FacesMessage msg = MessageHelper.generateMessage(
					GenericErrorMessage.HOURS_PER_WEEK_EXCEEDED,
					FacesMessage.SEVERITY_WARN);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	/**
	 * @see GUIController#refresh(org.woym.common.objects.Entity)
	 */
	public void refresh() {
		GUIController.refresh(teacher);
	}

	/**
	 * Liefert eine Liste mit allen Lehrkräften zurück.
	 * 
	 * @return Liste mit allen Lehrkräften
	 */
	public List<Teacher> getTeachers() {
		try {
			return dataAccess.getAllTeachers();
		} catch (DatasetException e) {
			LOGGER.error(e);
			FacesMessage msg = MessageHelper.generateMessage(
					GenericErrorMessage.DATABASE_COMMUNICATION_ERROR,
					FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return new ArrayList<Teacher>();
		}
	}

	/**
	 * Öffnet einen neuen Dialog, mit dem sich ein Lehrer hinzufügen lässt.
	 */
	public void doBeforeAdd() {
		teacher = new Teacher();
	}

	public void generateTeacherMemento() {
		teacherMemento = teacher.createMemento();
	}

	/**
	 * Speichert einen aktualisierten Lehrer.
	 */
	public void editTeacher() {
		UpdateCommand<Teacher> command = new UpdateCommand<>(teacher,
				teacherMemento);
		IStatus status = commandHandler.execute(command);
		FacesMessage msg = status.report();

		if (status instanceof SuccessStatus) {
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('wEditTeacherDialog').hide();");
		}

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	/**
	 * Löscht den selektierten Lehrer.
	 */
	public void deleteTeacher() {
		if (hideDeletionDialog) {
			Config.updateProperty(
					DefaultConfigEnum.HIDE_TEACHER_DELETION_DIALOG.getPropKey(),
					String.valueOf(hideDeletionDialog));
			hide = hideDeletionDialog;
		}
		MacroCommand macroCommand = commandCreator.createDeleteCommand(teacher);
		IStatus status = commandHandler.execute(macroCommand);
		FacesMessage msg = status.report();

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	/**
	 * Fügt einen Lehrer der Persistenz hinzu. Der momentane Lehrer im
	 * Zwischenspeicher wird mit einem neuen Objekt ersetzt.
	 */
	public void addTeacher() {
		AddCommand<Teacher> command = new AddCommand<>(teacher);
		IStatus status = commandHandler.execute(command);
		FacesMessage msg = status.report();

		if (status instanceof SuccessStatus) {
			teacher = new Teacher();
		}

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		if (teacher != null) {
			this.teacher = teacher;
		}
	}

	public boolean isHideDeletionDialog() {
		return hideDeletionDialog;
	}

	public void setHideDeletionDialog(boolean hideDeletionDialog) {
		this.hideDeletionDialog = hideDeletionDialog;
	}

	public boolean isHide() {
		return hide;
	}

	public int getHourlySettlement() {
		return hourlySettlement;
	}

}
