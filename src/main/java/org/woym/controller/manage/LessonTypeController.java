package org.woym.controller.manage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
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

	private static final long serialVersionUID = 1L;

	private static Logger LOGGER = LogManager
			.getLogger(LessonTypeController.class);

	private LessonType lessonType;

	private DataAccess dataAccess = DataAccess.getInstance();

	@PostConstruct
	public void init() {
		lessonType = new LessonType();
		lessonType.setTypicalDuration(getTypicalDuration());
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
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Datenbankfehler",
					"Bei der Kommunikation mit der Datenbank ist ein Fehler aufgetreten.");
			FacesContext.getCurrentInstance().addMessage(null, message);
			return new ArrayList<LessonType>();
		}
	}

	/**
	 * Setzt die Werte zum Bearbeiten eines neuen Unterrichtstypen
	 */
	public void addLessonTypeDialog() {

		lessonType = new LessonType();
		lessonType.setTypicalDuration(getTypicalDuration());
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('wAddLessonTypeDialog').show();");

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
	public void addLessonType() {

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

	}

	public LessonType getLessonType() {
		return lessonType;
	}

	public void setLessonType(LessonType lessonType) {
		this.lessonType = lessonType;
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
