package org.woym.ui.validators;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.woym.common.objects.Employee;
import org.woym.common.objects.Teacher;

@FacesValidator("org.woym.TeacherListValidator")
public class TeacherListValidator implements Validator {

	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		
		FacesMessage msg = new FacesMessage("Bitte mindestens eine Lehrkraft angeben.", "Bitte mindestens eine Lehrkraft angeben.");
		msg.setSeverity(FacesMessage.SEVERITY_ERROR);
		
		// TODO Zurückgeben einer spezifischeren Meldung
		if(!(value instanceof List<?>)) {
			throw new ValidatorException(msg);
		}
		
		@SuppressWarnings("unchecked")
		List<Employee> employees = (ArrayList<Employee>) value;
		
		Boolean valid = false;
		
		for(Employee employee : employees) {
			if(employee instanceof Teacher) {
				valid = true;
				break;
			}
		}
		
		if(!valid) {
			throw new ValidatorException(msg);
		}
	}
}