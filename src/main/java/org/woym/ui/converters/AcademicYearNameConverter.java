package org.woym.ui.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.woym.exceptions.DatasetException;
import org.woym.objects.AcademicYear;
import org.woym.persistence.DataAccess;

@FacesConverter("org.woym.AcademicYearNameConverter")
public class AcademicYearNameConverter implements Converter  {

	DataAccess dataAccess = DataAccess.getInstance();
	
	private static Logger LOGGER = LogManager
			.getLogger(AcademicYearNameConverter.class);

	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		try {
			Long id = Long.valueOf(value);
			
			return dataAccess.getById(AcademicYear.class, id);
		} catch (NumberFormatException | DatasetException e) {
			LOGGER.error(e);
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		AcademicYear academicYear = (AcademicYear) value;
		
		if(academicYear == null) {
			return "";
		}
		
		String id = academicYear.getId().toString();
		
		return id;
	}

}