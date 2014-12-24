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
import org.h2.util.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import org.woym.exceptions.DatasetException;
import org.woym.messages.GenericErrorMessage;
import org.woym.messages.MessageHelper;
import org.woym.messages.SuccessMessage;
import org.woym.objects.ActivityType;
import org.woym.objects.Teacher;
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

	private static final long serialVersionUID = 1L;

	private static Logger LOGGER = LogManager
			.getLogger(TeacherController.class);

	private DataAccess dataAccess = DataAccess.getInstance();

	private Teacher teacher;

	// TODO Move to planningController
	private Teacher selectedTeacherForSearch;
	private String searchSymbol;

	private DualListModel<ActivityType> activityTypes;

	@PostConstruct
	public void init() {
		teacher = new Teacher();
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

			for (ActivityType activityType : dataAccess.getAllActivityTypes()) {
				if (!possibleActivityTypes.contains(activityType)) {
					allActivityTypes.add(activityType);
				}
			}

			activityTypes = new DualListModel<ActivityType>(allActivityTypes,
					possibleActivityTypes);
		} catch (DatasetException e) {
			LOGGER.error(e);
			FacesMessage msg = new FacesMessage(
					GenericErrorMessage.DATABASE_COMMUNICATION_ERROR
							.getSummary(),
					GenericErrorMessage.DATABASE_COMMUNICATION_ERROR
							.getStatusMessage());
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
		}

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
	 * Liefert eine Liste mit allen Lehrkräften zurück.
	 * 
	 * @return Liste mit allen Lehrkräften
	 */
	public List<Teacher> getTeachers() {
		try {
			return dataAccess.getAllTeachers();
		} catch (DatasetException e) {
			LOGGER.error(e);
			FacesMessage msg = new FacesMessage(
					GenericErrorMessage.DATABASE_COMMUNICATION_ERROR
							.getSummary(),
					GenericErrorMessage.DATABASE_COMMUNICATION_ERROR
							.getStatusMessage());
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			return new ArrayList<Teacher>();
		}
	}

	/**
	 * Öffnet einen neuen Dialog, mit dem sich ein Lehrer hinzufügen lässt.
	 */
	public void addTeacherDialog() {
		teacher = new Teacher();
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('wAddTeacherDialog').show();");
	}

	/**
	 * Speichert einen aktualisierten Lehrer.
	 */
	public void editTeacher() {
		try {
			dataAccess.update(teacher);
			FacesMessage msg = MessageHelper.generateMessage(SuccessMessage.UPDATE_OBJECT_SUCCESS, teacher, FacesMessage.SEVERITY_INFO);
			FacesContext.getCurrentInstance().addMessage(null, msg);
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
	 * Löscht den selektierten Lehrer.
	 */
	public void deleteTeacher() {
		if (teacher != null) {
			try {
				dataAccess.delete(teacher);
				FacesMessage msg = MessageHelper.generateMessage(
						SuccessMessage.DELETE_OBJECT_SUCCESS, teacher,
						FacesMessage.SEVERITY_INFO);
				FacesContext.getCurrentInstance().addMessage(null, msg);
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
	}

	/**
	 * Returns a list of teachers that match the given search-symbol. If no
	 * search-symbol is set the first 5 teachers will be returned.
	 * 
	 * TODO: Move to planningController (Maybe)
	 * 
	 * @return
	 */
	public ArrayList<Teacher> getTeachersForSearch() {

		ArrayList<Teacher> tempList = new ArrayList<>();
		if (StringUtils.isNullOrEmpty(searchSymbol)) {
			for (Teacher teacher : getTeachers()) {
				tempList.add(teacher);

				if (tempList.size() >= 5) {
					return tempList;
				}

			}

			return tempList;
		}

		for (Teacher teacher : getTeachers()) {

			if (teacher.getSymbol().contains(searchSymbol)) {

				tempList.add(teacher);

				if (tempList.size() >= 5) {
					return tempList;
				}
			}
		}

		return tempList;
	}

	/**
	 * Fügt einen Lehrer der Persistenz hinzu. Der momentane Lehrer im
	 * Zwischenspeicher wird mit einem neuen Objekt ersetzt.
	 */
	public void addTeacher() {
		try {
			dataAccess.persist(teacher);
			FacesMessage msg = MessageHelper.generateMessage(
					SuccessMessage.ADD_OBJECT_SUCCESS, teacher,
					FacesMessage.SEVERITY_INFO);
			FacesContext.getCurrentInstance().addMessage(null, msg);
			teacher = new Teacher();
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

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		if (teacher != null) {
			this.teacher = teacher;
		}
	}

	public String getSearchSymbol() {
		return searchSymbol;
	}

	public void setSearchSymbol(String searchSymbol) {
		this.searchSymbol = searchSymbol;
	}

	public Teacher getSelectedTeacherForSearch() {
		return selectedTeacherForSearch;
	}

	public void setSelectedTeacherForSearch(Teacher selectedTeacherForSearch) {
		this.selectedTeacherForSearch = selectedTeacherForSearch;
	}
}
