package org.woym.spec.logic;

import javax.faces.application.FacesMessage;

public interface IStatus {

	/**
	 * Erzeugt eine {@linkplain FacesMessage}, welche dem Kunden angezeigt wird.
	 * 
	 * @return eine FacesMessage
	 * 
	 */
	public FacesMessage report();

}
