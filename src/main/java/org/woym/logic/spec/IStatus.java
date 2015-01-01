package org.woym.logic.spec;

import javax.faces.application.FacesMessage;

public interface IStatus {

	/**
	 * Gibt die bei Erzeugung des IStatus-Objektes generierte
	 * {@linkplain FacesMessage} zurück
	 * 
	 * @return eine FacesMessage
	 * 
	 */
	public FacesMessage report();

}
