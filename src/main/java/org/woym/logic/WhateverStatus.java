/**
 * 
 */
package org.woym.logic;

import javax.faces.application.FacesMessage;

import org.woym.spec.logic.IStatus;

/**
 * @author JurSch
 *
 */
public class WhateverStatus implements IStatus {
	
	FacesMessage message;

	public WhateverStatus(final String text) {
		message = new FacesMessage(text,"");
	}
	
	@Override
	public FacesMessage report() {
		
		return message;
	}

}
