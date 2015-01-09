package org.woym.ui.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.woym.objects.Weekday;

@FacesConverter("org.woym.WeekdayConverter")
public class WeekdayNameConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		for(Weekday w: Weekday.values()){
			if(w.getName().equals(value)){
				return w;
			}
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		return ((Weekday)value).getName();
	}
}
