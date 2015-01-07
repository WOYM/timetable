package org.woym.ui.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.woym.exceptions.DatasetException;
import org.woym.objects.Schoolclass;
import org.woym.persistence.DataAccess;

/**
 * <h1>SchoolclassNameConverter</h1>
 * <p>
 * Dieser Converter konvertiert eine Schulklasse in ihre ID und wieder zurück.
 * 
 * @author Tim Hansen (tihansen)
 */
@FacesConverter("org.woym.SchoolclassNameConverter")
public class SchoolclassNameConverter implements Converter  {

	DataAccess dataAccess = DataAccess.getInstance();
	
	private static Logger LOGGER = LogManager
			.getLogger(SchoolclassNameConverter.class);

	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		try {
			Long id = Long.valueOf(value);
			
			return dataAccess.getById(Schoolclass.class, id);
		} catch (NumberFormatException | DatasetException e) {
			LOGGER.error(e);
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		Schoolclass schoolclass = (Schoolclass) value;
		
		if(schoolclass == null) {
			return "";
		}
		
		String id = schoolclass.getId().toString();
		
		return id;
	}

}