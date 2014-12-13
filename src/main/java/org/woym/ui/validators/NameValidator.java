package org.woym.ui.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.h2.util.StringUtils;
import org.woym.messages.StatusMessageEnum;

/**
 * <h1>NameValidator</h1>
 * <p>
 * This validator validates a name.
 * <p>
 * A name is valid if it is
 * <ul>
 * <li>
 * <b>Not</b> null or empty, eg. "" or " "</li>
 * </ul>
 * 
 * @author Tim Hansen (tihansen)
 *
 */
@FacesValidator("org.woym.NameValidator")
public class NameValidator implements Validator {

	@Override
	public void validate(FacesContext context, UIComponent uiComponent,
			Object value) throws ValidatorException {

		if (StringUtils.isNullOrEmpty(value.toString())) {
			FacesMessage msg = new FacesMessage(
					StatusMessageEnum.INVALID_NAME.getSummary(),
					StatusMessageEnum.INVALID_NAME.getStatusMessage());
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		}
	}
}
