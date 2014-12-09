package org.woym.controller.manage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.util.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import org.woym.exceptions.DatasetException;
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
@SessionScoped
@ManagedBean(name = "teacherController")
public class TeacherController implements Serializable {

	private static final long serialVersionUID = -2341971622906815080L;

	private static Logger LOGGER = LogManager
			.getLogger(TeacherController.class);

	private transient DataAccess dataAccess = DataAccess.getInstance();
	private Teacher selectedTeacher;
	private Teacher addTeacher;
	// TODO Move to planningController
	private Teacher selectedTeacherForSearch;
	private String searchSymbol;

	private DualListModel<ActivityType> activityTypes;

	public DualListModel<ActivityType> getActivityTypesForAddTeacher() {
		List<ActivityType> allActivityTypes;
		List<ActivityType> possibleActivityTypes = new ArrayList<>();

		try {
			allActivityTypes = dataAccess.getAllActivityTypes();

			activityTypes = new DualListModel<ActivityType>(allActivityTypes,
					possibleActivityTypes);
		} catch (DatasetException e) {
			LOGGER.error(e);
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Datenbankfehler",
					"Bei der Kommunikation mit der Datenbank ist ein Fehler aufgetreten.");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}

		return activityTypes;
	}

	public void setActivityTypesForAddTeacher(
			DualListModel<ActivityType> activityTypes) {

		this.activityTypes = activityTypes;
	}

	@SuppressWarnings("unchecked")
	public void onTransferAddTeacher(TransferEvent event) {
		addTeacher.setPossibleActivityTypes(((ArrayList<ActivityType>) event.getItems()));
	}
	
	public DualListModel<ActivityType> getActivityTypesForSelectedTeacher() {
		List<ActivityType> allActivityTypes;
		List<ActivityType> possibleActivityTypes;

		try {
			allActivityTypes = dataAccess.getAllActivityTypes();
			possibleActivityTypes = selectedTeacher.getPossibleActivityTypes();

			for(Iterator<ActivityType> activityTypeIter = allActivityTypes.iterator(); activityTypeIter.hasNext();) {
				if(possibleActivityTypes.contains(activityTypeIter.next())) {
					activityTypeIter.remove();
				}
			}
			
			activityTypes = new DualListModel<ActivityType>(allActivityTypes,
					possibleActivityTypes);
		} catch (DatasetException e) {
			LOGGER.error(e);
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Datenbankfehler",
					"Bei der Kommunikation mit der Datenbank ist ein Fehler aufgetreten.");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}

		return activityTypes;
	}

	public void setActivityTypesForSelectedTeacher(
			DualListModel<ActivityType> activityTypes) {

		this.activityTypes = activityTypes;
	}

	@SuppressWarnings("unchecked")
	public void onTransferSelectedTeacher(TransferEvent event) {
		selectedTeacher.setPossibleActivityTypes(((ArrayList<ActivityType>) event.getItems()));
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
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Fehler beim Laden der Lehrer", "");
			FacesContext.getCurrentInstance().addMessage(null, message);
			return new ArrayList<Teacher>();
		}
	}

	/**
	 * Opens a new dialog which enables the user to add a new teacher.
	 */
	public void addTeacherDialog() {

		addTeacher = new Teacher();

		Map<String, Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("draggable", false);
		options.put("resizable", false);
		options.put("contentHeight", 600);
		options.put("contentWidth", 800);

		RequestContext rc = RequestContext.getCurrentInstance();
		rc.openDialog("addTeachersDialog", options, null);
	}

	/**
	 * Opens a new dialog which enables the user to edit a new teacher.
	 */
	public void editTeacherDialog() {

		Map<String, Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("draggable", false);
		options.put("resizable", false);
		options.put("contentHeight", 600);
		options.put("contentWidth", 800);

		RequestContext rc = RequestContext.getCurrentInstance();
		rc.openDialog("editTeacherDialog", options, null);
	}

	/**
	 * Saves an edited teacher to the database.
	 */
	public void editTeacher() {
		try {
			dataAccess.update(selectedTeacher);
		} catch (DatasetException e) {
			LOGGER.error(e);
		}
	}

	/**
	 * Deletes the selected teacher.
	 */
	public void deleteTeacher() {
		if (selectedTeacher != null) {
			try {
				dataAccess.delete(selectedTeacher);
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_INFO, "Lehrer gelöscht",
						selectedTeacher.getName() + " ("
								+ selectedTeacher.getSymbol() + ")");
				FacesContext.getCurrentInstance().addMessage(null, message);
			} catch (DatasetException e) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Fehler beim Löschen des Lehrers", "");
				FacesContext.getCurrentInstance().addMessage(null, message);
			}
		}
	}

	/**
	 * Returns a list of teachers that match the given search-symbol. If no
	 * search-symbol is set the first 5 teachers will be returned.
	 * 
	 * TODO: Move to planningController (Maybe) TODO: Make it search for real
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
	 * Closes the dialog to add a teacher with a null-event.
	 */
	public void addTeacherFromDialog() {
		Teacher teacher = addTeacher;

		try {
			dataAccess.persist(teacher);
			addTeacher = new Teacher();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Lehrer hinzugefügt", teacher.getName() + " ("
							+ teacher.getSymbol() + ")");
			FacesContext.getCurrentInstance().addMessage(null, message);

			// TODO: DatabaseException does not mean that the teacher exists, it
			// just means something went wrong
		} catch (DatasetException e) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Ein Datenbankfehler ist aufgetreten.", "");
			FacesContext.getCurrentInstance().addMessage(null, message);
			return;
		}

		// RequestContext.getCurrentInstance().closeDialog(addTeacher);
	}

	public Teacher getSelectedTeacher() {
		return selectedTeacher;
	}

	public void setSelectedTeacher(Teacher selectedTeacher) {
		this.selectedTeacher = selectedTeacher;
	}

	public Teacher getAddTeacher() {
		return addTeacher;
	}

	public void setAddTeacher(Teacher addTeacher) {
		this.addTeacher = addTeacher;
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
