package org.woym.ui.converters;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.woym.exceptions.DatasetException;
import org.woym.messages.GenericErrorMessage;
import org.woym.messages.MessageHelper;
import org.woym.objects.Teacher;
import org.woym.persistence.DataAccess;

/**
 * <h1>TeacherNameConverter</h1>
 * <p>
 * Konvertiert intern ein {@link Teacher}-Objekt in das Kürzel des Lehrers und
 * sucht anhand des Kürzels den richtigen Lehrer aus der Datenbank.
 * 
 * @author tihansen
 */
@FacesConverter("org.woym.TeacherNameConverter")
public class TeacherNameConverter implements Converter {

	DataAccess dataAccess = DataAccess.getInstance();

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {

		Teacher teacher = new Teacher();

		try {
			teacher = (Teacher) dataAccess.getOneEmployee(value);
		} catch (DatasetException e) {
			FacesMessage msg = MessageHelper.generateMessage(
					GenericErrorMessage.DATABASE_COMMUNICATION_ERROR,
					FacesMessage.SEVERITY_ERROR);
			throw new ConverterException(msg);
		}

		return teacher;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {

		if (value == null) {
			return null;
		}

		return ((Teacher) value).getSymbol();
	}

}
