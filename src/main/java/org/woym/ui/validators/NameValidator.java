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
import org.woym.common.exceptions.DatasetException;
import org.woym.common.messages.GenericErrorMessage;
import org.woym.common.messages.MessageHelper;
import org.woym.common.objects.ActivityType;
import org.woym.common.objects.Employee;
import org.woym.common.objects.Location;
import org.woym.common.objects.Room;
import org.woym.persistence.DataAccess;

/**
 * <h1>LocationNameValidator</h1>
 * <p>
 * Dieser Validator prüft einen Bezeichner auf Gültigkeit.
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
@FacesValidator("org.woym.NameValidator")
public class NameValidator implements Validator {

	public static final String BEAN_NAME = "validateBean";
	public static final String PARENT_BEAN = "parentBean";

	private static Logger LOGGER = LogManager.getLogger(NameValidator.class);

	private DataAccess dataAccess = DataAccess.getInstance();

	@Override
	public void validate(FacesContext context, UIComponent uiComponent,
			Object value) throws ValidatorException {

		String text = value.toString();
		ELContext elContext = context.getELContext();
		ValueExpression valueExpression = uiComponent
				.getValueExpression(BEAN_NAME);
		Object bean = valueExpression.getValue(elContext);

		// Previous check so that .trim() does not throw a NullPointer
		if (StringUtils.isNullOrEmpty(text)) {
			throw new ValidatorException(getNameIsEmptyMessage());
		}

		text = text.trim();

		if (StringUtils.isNullOrEmpty(text)) {
			throw new ValidatorException(getNameIsEmptyMessage());
		}

		// Check the different types of objects
		// Allows easy adding of different validation-rules
		try {
			if (bean instanceof Location) {
				Location location = dataAccess.getOneLocation(text);
				if (location != null && location != bean) {
					throw new ValidatorException(getNameAlreadyExistsMessage());
				}
			} else if (bean instanceof Room) {
				// Rooms can not be checked directly in database
				ValueExpression locationExpression = uiComponent
						.getValueExpression(PARENT_BEAN);
				Object locationBean = locationExpression.getValue(elContext);

				for (Room room : ((Location) locationBean).getRooms()) {
					if ((room.getName().equals(text)) && (room != bean)) {
						throw new ValidatorException(
								getNameAlreadyExistsMessage());
					}
				}

			} else if (bean instanceof Employee) {
				Employee employee = dataAccess.getOneEmployee(text);
				if (employee != null && employee != bean) {
					throw new ValidatorException(getNameAlreadyExistsMessage());
				}
			} else if (bean instanceof ActivityType) {
				ActivityType activityType = dataAccess.getOneActivityType(text);
				if (activityType != null && activityType != bean) {
					throw new ValidatorException(getNameAlreadyExistsMessage());
				}
			}

		} catch (DatasetException e) {
			LOGGER.error(e);
			FacesMessage msg = new FacesMessage(
					GenericErrorMessage.DATABASE_COMMUNICATION_ERROR
							.getSummary(),
					GenericErrorMessage.DATABASE_COMMUNICATION_ERROR
							.getStatusMessage());
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		}

	}

	private FacesMessage getNameIsEmptyMessage() {
		FacesMessage msg = MessageHelper.generateMessage(
				GenericErrorMessage.NAME_IS_EMPTY, FacesMessage.SEVERITY_ERROR);

		return msg;
	}

	private FacesMessage getNameAlreadyExistsMessage() {
		FacesMessage msg = MessageHelper.generateMessage(
				GenericErrorMessage.NAME_ALREADY_EXISTS,
				FacesMessage.SEVERITY_ERROR);

		return msg;
	}

}
