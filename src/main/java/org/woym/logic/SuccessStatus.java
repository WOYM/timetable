/**
 * 
 */
package org.woym.logic;

import javax.faces.application.FacesMessage;

import org.woym.messages.MessageHelper;
import org.woym.messages.SuccessMessage;
import org.woym.objects.Entity;
import org.woym.spec.logic.IStatus;

/**
 * Representation eines erfolgreichen {@link IStatus}
 * 
 * @author JurSch, adrian
 *
 */
public class SuccessStatus implements IStatus {

	/**
	 * Die FacesMessage zur erfolgreich ausgeführten Aktion.
	 */
	private final FacesMessage facesMessage;

	/**
	 * Erzeugt ein neues {@linkplain SuccessStatus}-Objekt. Wird für einen der
	 * Parameter {@code null} übergeben, wird eine
	 * {@linkplain IllegalArgumentException} geworfen.
	 * 
	 * @param message
	 *            - eine {@linkplain SuccessMessage}
	 * @param entity
	 *            - das betroffene Objekt
	 * @param severity
	 *            - die Art der Meldung (Fehler, Info, Warnung)
	 */
	public SuccessStatus(SuccessMessage message, Entity entity,
			FacesMessage.Severity severity) {
		if (message == null || entity == null || severity == null) {
			throw new IllegalArgumentException();
		}
		facesMessage = MessageHelper.generateMessage(message, entity, severity);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FacesMessage report() {
		return facesMessage;
	}

}
