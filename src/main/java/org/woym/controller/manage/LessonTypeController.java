package org.woym.controller.manage;

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
import org.woym.config.Config;
import org.woym.config.DefaultConfigEnum;
import org.woym.exceptions.DatasetException;
import org.woym.objects.LessonType;
import org.woym.persistence.DataAccess;

/**
 * <h1>LessonTypeController</h1>
 * <p>
 * Dieser Controller verwaltet die verschiedenen Unterrichtsinhalte.
 * 
 * @author Tim Hansen (tihansen)
 *
 */
@SessionScoped
@ManagedBean(name = "lessonTypeController")
public class LessonTypeController {

	private static Logger LOGGER = LogManager
			.getLogger(LessonTypeController.class);

	private LessonType lessonType;
	
	private DataAccess dataAccess = DataAccess.getInstance();

	/**
	 * Liefert eine Liste mit allen Unterrichtsinhalten zurück.
	 * 
	 * @return Liste mit allen Unterrichtsinhalten
	 */
	public List<LessonType> getLessonTypes() {
		try {
			return dataAccess.getAllLessonTypes();
		} catch (DatasetException e) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Datenbankfehler",
					"Bei der Kommunikation mit der Datenbank ist ein Fehler aufgetreten.");
			FacesContext.getCurrentInstance().addMessage(null, message);
			return new ArrayList<LessonType>();
		}
	}

	/**
	 * Öffnet einen neuen Dialog, in dem ein Unterrichtsinhalt hinzugefügt
	 * werden kann.
	 */
	public void addLessonTypeDialog() {

		lessonType = new LessonType();
		lessonType.setTypicalDuration(getTypicalDuration());

		openDialog("addLessonTypeDialog");
	}

	public void editLessonTypeDialog() {

		openDialog("editLessonTypeDialog");
	}

	private void openDialog(String dialog) {

		Map<String, Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("draggable", false);
		options.put("resizable", false);
		options.put("contentHeight", 400);
		options.put("contentWidth", 600);

		RequestContext rc = RequestContext.getCurrentInstance();
		rc.openDialog(dialog, options, null);
	}

	/**
	 * Löscht den selektierten Unterrichtsinhalt
	 */
	public void deleteLessonType() {
		if (lessonType != null) {
			try {
				dataAccess.delete(lessonType);
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_INFO,
						"Unterrichtsinhalt gelöscht", lessonType.getName());
				FacesContext.getCurrentInstance().addMessage(null, message);
			} catch (DatasetException e) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Fehler beim Löschen des Unterrichtsinhaltes", "");
				FacesContext.getCurrentInstance().addMessage(null, message);
			}
		}
	}

	/**
	 * Bearbeitet einen Unterrichtsinhalt
	 */
	public void editLessonType() {
		try {
			dataAccess.update(lessonType);
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Unterrichtsinhalt aktualisiert", lessonType.getName());
			FacesContext.getCurrentInstance().addMessage(null, message);
		} catch (DatasetException e) {
			LOGGER.error(e);
		}
	}

	/**
	 * Fügt einen neuen Unterrichtsinhalt hinzu
	 */
	public void addLessonTypeFromDialog() {

		try {
			dataAccess.persist(lessonType);
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Unterrichtsinhalt hinzugefügt", lessonType.getName());
			FacesContext.getCurrentInstance().addMessage(null, message);
			lessonType = new LessonType();
			lessonType.setTypicalDuration(getTypicalDuration());
		} catch (DatasetException e) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Datenbankfehler",
					"Bei der Kommunikation mit der Datenbank ist ein Fehler aufgetreten.");
			FacesContext.getCurrentInstance().addMessage(null, message);
			return;
		}

		// RequestContext.getCurrentInstance().closeDialog(addTeacher);
	}

	public LessonType getLessonType() {
		return lessonType;
	}

	public void setLessonType(LessonType lessonType) {
		if (lessonType != null) {
			this.lessonType = lessonType;
		}
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
				// Do nothing
			} catch (NumberFormatException e) {
			}
		}

		return typicalDuration;
	}
}
