package org.woym.ui.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.woym.common.exceptions.DatasetException;
import org.woym.common.objects.Location;
import org.woym.common.objects.Room;
import org.woym.persistence.DataAccess;

@FacesConverter("org.woym.RoomNameConverter")
public class RoomNameConverter implements Converter  {

	DataAccess dataAccess = DataAccess.getInstance();
	
	private static Logger LOGGER = LogManager
			.getLogger(RoomNameConverter.class);

	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		try {
			Long id = Long.valueOf(value);
			
			return dataAccess.getById(Room.class, id);
		} catch (NumberFormatException | DatasetException e) {
			LOGGER.error(e);
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		Room room = (Room) value;
		
		try {
			for (Location location : dataAccess.getAllLocations()) {
				if (location.getRooms().contains(room)) {
					return room.getId().toString();
				}
			}
			
		} catch (DatasetException e) {
			LOGGER.error(e);
		}
		
		return "";
	}

}