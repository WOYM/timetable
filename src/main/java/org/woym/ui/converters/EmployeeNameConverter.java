package org.woym.ui.converters;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.woym.common.exceptions.DatasetException;
import org.woym.common.messages.GenericErrorMessage;
import org.woym.common.messages.MessageHelper;
import org.woym.common.objects.Employee;
import org.woym.common.objects.Teacher;
import org.woym.persistence.DataAccess;

/**
 * <h1>EmployeeNameConverter</h1>
 * <p>
 * Konvertiert intern ein {@link Employee}-Objekt in das Kürzel des Mitarbeiters und
 * sucht anhand des Kürzels den richtigen Mitarbeiter aus der Datenbank.
 * 
 * @author tihansen
 */
@FacesConverter("org.woym.EmployeeNameConverter")
public class EmployeeNameConverter implements Converter {

	DataAccess dataAccess = DataAccess.getInstance();

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {

		Employee employee = new Teacher();

		try {
			employee = dataAccess.getOneEmployee(value);
		} catch (DatasetException e) {
			FacesMessage msg = MessageHelper.generateMessage(
					GenericErrorMessage.DATABASE_COMMUNICATION_ERROR,
					FacesMessage.SEVERITY_ERROR);
			throw new ConverterException(msg);
		}

		return employee;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {

		if (value == null) {
			return null;
		}

		return ((Employee) value).getSymbol();
	}

}
