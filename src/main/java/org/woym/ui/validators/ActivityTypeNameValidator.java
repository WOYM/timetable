package org.woym.ui.validators;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.util.StringUtils;
import org.woym.exceptions.DatasetException;
import org.woym.messages.StatusMessageEnum;
import org.woym.objects.ActivityType;
import org.woym.objects.LessonType;
import org.woym.persistence.DataAccess;

/**
 * <h1>ActivityTypeNameValidator</h1>
 * <p>
 * Dieser Validator prüft den Bezeichner eines Aktivitätstypen auf Gültigkeit.
 * <p>
 * Ein Bezeichner ist dann gültig, wenn er...:
 * <ul>
 * <li>
 * <b>Nicht</b> {@code null} oder leer ist.</li>
 * <li>
 * <b>Nicht</b> bereits von einem anderen Objekt verwendet wird.</li>
 * </ul>
 * <p>
 * <h2>Zusätzliche Informationen</h2>
 * Vor der Validierung wird ein Bezeichner getrimmt, das heißt, es werden
 * vorrangehende oder nachfolgende Leerzeichen entfernt.<br>
 * Ein Leerzeichen im Inneren eines Symbols ist gültig.<br>
 * Ein Bezeichner wird in Groß- und Kleinschreibung nicht verändert.
 * 
 * @author Tim Hansen (tihansen)
 *
 */
@FacesValidator("org.woym.ActivityTypeNameValidator")
public class ActivityTypeNameValidator implements Validator {
	
	public static final String BEAN_NAME = "lessonBean";

	private static Logger LOGGER = LogManager
			.getLogger(ActivityTypeNameValidator.class);
	
	private DataAccess dataAccess = DataAccess.getInstance(); 

	@Override
	public void validate(FacesContext context, UIComponent uiComponent,
			Object value) throws ValidatorException {

		String name = value.toString();
		ELContext elContext = context.getELContext();
		ValueExpression valueExpression = uiComponent.getValueExpression(BEAN_NAME);
		Object lessonBean = valueExpression.getValue(elContext);
		
		if(lessonBean instanceof LessonType)
		if (StringUtils.isNullOrEmpty(name)) {
			FacesMessage msg = new FacesMessage(
					StatusMessageEnum.NAME_IS_EMPTY.getSummary(),
					StatusMessageEnum.NAME_IS_EMPTY.getStatusMessage());
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		}

		name = name.trim();

		if (StringUtils.isNullOrEmpty(name)) {
			FacesMessage msg = new FacesMessage(
					StatusMessageEnum.NAME_IS_EMPTY.getSummary(),
					StatusMessageEnum.NAME_IS_EMPTY.getStatusMessage());
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		}

		try {
			ActivityType activityType = dataAccess.getOneActivityType(name);
			if (activityType != null && activityType != lessonBean) {
				FacesMessage msg = new FacesMessage(
						StatusMessageEnum.NAME_ALREADY_EXISTS.getSummary(),
						StatusMessageEnum.NAME_ALREADY_EXISTS
								.getStatusMessage());
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(msg);
			}

		} catch (DatasetException e) {
			LOGGER.error(e);
			FacesMessage msg = new FacesMessage(
					StatusMessageEnum.DATABASE_COMMUNICATION_ERROR.getSummary(),
					StatusMessageEnum.DATABASE_COMMUNICATION_ERROR
							.getStatusMessage());
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		}

	}

}
