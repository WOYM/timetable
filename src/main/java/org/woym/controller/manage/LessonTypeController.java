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
import org.woym.messages.GenericErrorMessage;
import org.woym.messages.MessageHelper;
import org.woym.messages.SuccessMessage;
import org.woym.objects.LessonType;
import org.woym.persistence.DataAccess;
import org.woym.spec.logic.IStatus;

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

	private static final long serialVersionUID = 1L;

	private static Logger LOGGER = LogManager
			.getLogger(LessonTypeController.class);

	private LessonType lessonType;

	private DataAccess dataAccess = DataAccess.getInstance();
	
	private CommandHandler commandHandler = CommandHandler.getInstance();

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
			LOGGER.error(e);
			FacesMessage msg = new FacesMessage(
					GenericErrorMessage.DATABASE_COMMUNICATION_ERROR
							.getSummary(),
					GenericErrorMessage.DATABASE_COMMUNICATION_ERROR
							.getStatusMessage());
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
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
				FacesMessage msg = MessageHelper.generateMessage(SuccessMessage.DELETE_OBJECT_SUCCESS, lessonType, FacesMessage.SEVERITY_INFO);
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
	 * Bearbeitet einen Unterrichtsinhalt
	 */
	public void editLessonType() {
		try {
			dataAccess.update(lessonType);
			FacesMessage msg = MessageHelper.generateMessage(SuccessMessage.UPDATE_OBJECT_SUCCESS, lessonType, FacesMessage.SEVERITY_INFO);
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
	 * Fügt einen neuen Unterrichtsinhalt hinzu
	 */
	public void addLessonType() {
		// Usage of command
		AddCommand<LessonType> command = new AddCommand<LessonType>(lessonType);			
		IStatus status = commandHandler.execute(command);
		FacesMessage msg = status.report();
		
		if(status instanceof SuccessStatus) {
			lessonType = new LessonType();
			lessonType.setTypicalDuration(getTypicalDuration());
		}
		
		FacesContext.getCurrentInstance().addMessage(null, msg);

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
			} catch (NumberFormatException e) {
				// Do nothing
			}
		}

		return typicalDuration;
	}
}
