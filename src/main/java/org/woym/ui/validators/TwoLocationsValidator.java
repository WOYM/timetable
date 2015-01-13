package org.woym.ui.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.woym.objects.Location;

@FacesValidator("org.woym.TwoLocationsValidator")
public class TwoLocationsValidator implements Validator {

	/**
	 * Dieser Validator validiert zwei Date Objekte, b
	 */
	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		if (value == null) {
			return;
		}

		UIInput locationAValue = (UIInput) component.getAttributes().get(
				"locationA");
		if (locationAValue == null) {
			return;
		}

		Location locationA = (Location) locationAValue.getValue();
		Location locationB = (Location) value;

		if (locationA.equals(locationB)) {
			locationAValue.setValid(false);
			throw new ValidatorException(new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Standorte nicht verschieden.",
					"Wählen Sie zwei verschiedene Standorte aus."));
		}
	}
}
