package org.woym.ui.converters;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.woym.exceptions.DatasetException;
import org.woym.objects.ActivityType;
import org.woym.objects.LessonType;
import org.woym.persistence.ActivityTypeDAO;

@FacesConverter("org.woym.LessonTypeNameConverter")
public class LessonTypeNameConverter implements Converter{

	ActivityTypeDAO lessonTypeDAO = ActivityTypeDAO.getInstance();
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent uiComponent, String value) throws ConverterException {
		
		ActivityType lessonType = new LessonType();
		
		try {
			lessonType = lessonTypeDAO.getOne(value);
		} catch(DatasetException e) {
			FacesMessage msg = 
					new FacesMessage("Ungültiges Kürzel.", 
							"Kürzel darf nicht leer sein.");
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ConverterException(msg);
		}
		return lessonType;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent uiComponent, Object value) throws ConverterException {
		ActivityType lessonType = (ActivityType) value;
		return lessonType.getName();
	}

}
