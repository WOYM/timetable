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
import org.woym.common.objects.Location;
import org.woym.persistence.DataAccess;

@FacesConverter("org.woym.LocationNameConverter")
public class LocationNameConverter implements Converter {

	DataAccess dataAccess = DataAccess.getInstance();
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		
		Location location = new Location();
		
		try {
			location = dataAccess.getOneLocation(value);
		} catch (DatasetException e) {
			FacesMessage msg = MessageHelper.generateMessage(GenericErrorMessage.DATABASE_COMMUNICATION_ERROR, FacesMessage.SEVERITY_ERROR);
			throw new ConverterException(msg);
		}
		
		return location;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		
		if (value == null) {
			return null;
		}
		
		return ((Location) value).getName();
	}

}
