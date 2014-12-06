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
import org.h2.util.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.woym.exceptions.DatasetException;
import org.woym.objects.Teacher;
import org.woym.persistence.TeacherDAO;

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

	private static Logger logger = LogManager.getLogger("teacherController");

	// TODO: transient machen oder TeacherDAO Serializable implementieren lassen
	private TeacherDAO db = TeacherDAO.getInstance();
	private Teacher selectedTeacher;
	private Teacher addTeacher;
	// TODO Move to planningController
	private Teacher selectedTeacherForSearch;
	private String searchSymbol;

	//

	/**
	 * Liefert eine Liste mit allen Lehrkräften zurück.
	 * 
	 * @return Liste mit allen Lehrkräften
	 */
	public List<Teacher> getTeachers() {
		try {
			// Null for unordered
			return db.getAll(null);
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
			db.update(selectedTeacher);
		} catch (DatasetException e) {
			logger.error(e);
		}
	}

	/**
	 * Deletes the selected teacher.
	 */
	public void deleteTeacher() {
		if (selectedTeacher != null) {
			try {
				db.delete(selectedTeacher);
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
			db.persist(teacher);
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
