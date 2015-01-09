package org.woym.ui.validators;

import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("org.woym.TimeRangeValidator")
public class TimeRangeValidator implements Validator {

	/**
	 * Dieser Validator validiert zwei Date Objekte, b
	 */
	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		if (value == null) {
			return;
		}

		UIInput startTimeValue = (UIInput) component.getAttributes().get(
				"startTime");
		if (startTimeValue == null) {
			return;
		}

		Date startTime = (Date) startTimeValue.getValue();
		Date endTime = (Date) value;

		if (!startTime.before(endTime)) {
			startTimeValue.setValid(false);
			throw new ValidatorException(new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Startzeit muss vor der Endzeit liegen.",
					"Die Startzeit muss vor der Endzeit liegen."));
		}
	}

}
