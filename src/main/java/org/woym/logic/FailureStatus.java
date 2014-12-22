/**
 * 
 */
package org.woym.logic;

import javax.faces.application.FacesMessage;

import org.woym.messages.GenericStatusMessage;
import org.woym.messages.MessageHelper;
import org.woym.messages.SpecificStatusMessage;
import org.woym.objects.Entity;
import org.woym.spec.logic.IStatus;

/**
 * @author Whatever
 *
 */
public class FailureStatus implements IStatus {

	private final FacesMessage facesMessage;
	
	public FailureStatus(SpecificStatusMessage message,
			Class<? extends Entity> clazz, FacesMessage.Severity severity) {
		if (message == null || clazz == null || severity == null) {
			throw new IllegalArgumentException();
		}
		facesMessage = MessageHelper.generateMessage(message, clazz, severity);
	}

	public FailureStatus(GenericStatusMessage message,
			FacesMessage.Severity severity) {
		if (message == null || severity == null) {
			throw new IllegalArgumentException();
		}
		facesMessage = MessageHelper.generateMessage(message, severity);
	}

	@Override
	public FacesMessage report() {
		return facesMessage;
	}
}
