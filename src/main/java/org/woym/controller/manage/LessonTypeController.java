package org.woym.controller.manage;

import java.io.Serializable;
import java.math.BigDecimal;
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
public class LessonTypeController implements Serializable {
	private static final long serialVersionUID = -2341971622906815080L;

	private static Logger LOGGER = LogManager
			.getLogger(LessonTypeController.class);

	private transient DataAccess dataAccess = DataAccess.getInstance();
	private LessonType selectedLessonType;
	private LessonType addLessonType;

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
					FacesMessage.SEVERITY_ERROR,
					"Datenbankfehler", "Bei der Kommunikation mit der Datenbank ist ein Fehler aufgetreten.");
			FacesContext.getCurrentInstance().addMessage(null, message);
			return new ArrayList<LessonType>();
		}
	}

	/**
	 * Öffnet einen neuen Dialog, in dem ein Unterrichtsinhalt hinzugefügt
	 * werden kann.
	 */
	public void addLessonTypeDialog() {

		addLessonType = new LessonType();
		int typicalDuration = 0; 
		
		// Get prop-value
		String[] typicalDurationString = Config.getPropValue(DefaultConfigEnum.TYPICAL_ACTIVITY_DURATION.getPropKey());
		// Check for exactly one valid entry in configuration
		if(typicalDurationString.length == 1) {
			try {
				typicalDuration = Integer.parseInt(typicalDurationString[0]);
			  // Do nothing
			} catch (NumberFormatException e) {}
		}
		
		addLessonType.setTypicalDuration(typicalDuration);
		
		Map<String, Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("draggable", false);
		options.put("resizable", false);
		options.put("contentHeight", 400);
		options.put("contentWidth", 600);

		RequestContext rc = RequestContext.getCurrentInstance();
		rc.openDialog("addLessonTypeDialog", options, null);
	}

	public void editLessonTypeDialog() {

		Map<String, Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("draggable", false);
		options.put("resizable", false);
		options.put("contentHeight", 400);
		options.put("contentWidth", 600);

		RequestContext rc = RequestContext.getCurrentInstance();
		rc.openDialog("editLessonTypeDialog", options, null);
	}

	/**
	 * Löscht den selektierten Unterrichtsinhalt
	 */
	public void deleteLessonType() {
		if (selectedLessonType != null) {
			try {
				dataAccess.delete(selectedLessonType);
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_INFO,
						"Unterrichtsinhalt gelöscht",
						selectedLessonType.getName());
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
	 * Fügt einen neuen Unterrichtsinhalt hinzu
	 */
	public void addLessonTypeFromDialog() {
		LessonType lessonType = addLessonType;

		try {
			dataAccess.persist(lessonType);
			addLessonType = new LessonType();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Unterrichtsinhalt hinzugefügt", lessonType.getName());
			FacesContext.getCurrentInstance().addMessage(null, message);
		} catch (DatasetException e) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Datenbankfehler", "Bei der Kommunikation mit der Datenbank ist ein Fehler aufgetreten.");
			FacesContext.getCurrentInstance().addMessage(null, message);
			return;
		}

		// RequestContext.getCurrentInstance().closeDialog(addTeacher);
	}

	public LessonType getSelectedLessonType() {
		return selectedLessonType;
	}

	public void setSelectedLessonType(LessonType selectedLessonType) {
		this.selectedLessonType = selectedLessonType;
	}

	public LessonType getAddLessonType() {
		return addLessonType;
	}

	public void setAddLessonType(LessonType addLessonType) {
		this.addLessonType = addLessonType;
	}

}
