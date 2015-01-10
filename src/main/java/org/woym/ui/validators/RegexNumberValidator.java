package org.woym.ui.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("org.woym.RegexNumberValidator")
public class RegexNumberValidator implements Validator {

	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		String text = (String) value;

		if (!text.matches("\\d+")) {
			throw new ValidatorException(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Keine gültige Zahl.",
					"Geben Sie eine gültige Zahl für die Wegzeit an."));
		}
	}

}
