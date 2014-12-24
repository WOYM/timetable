/**
 * 
 */
package org.woym.logic;

import javax.faces.application.FacesMessage;

import org.woym.messages.GenericErrorMessage;
import org.woym.messages.MessageHelper;
import org.woym.messages.SpecificErrorMessage;
import org.woym.objects.Entity;
import org.woym.spec.logic.IStatus;

/**
 * @author JurSch, adrian
 *
 */
public class FailureStatus implements IStatus {

	/**
	 * Die FacesMessage zum aufgetretenen Fehler.
	 */
	private final FacesMessage facesMessage;

	/**
	 * Erzeugt ein neues {@linkplain FailureStatus}-Objekt. Wird für einen der
	 * Parameter {@code null} übergeben, wird eine
	 * {@linkplain IllegalArgumentException} geworfen.
	 * 
	 * @param message
	 *            - eine {@linkplain SpecificErrorMessage}
	 * @param clazz
	 *            - die betroffene, {@linkplain Entity} erweiternde Klasse
	 * @param severity
	 *            - die Art der Meldung (Fehler, Info, Warnung)
	 */
	public FailureStatus(SpecificErrorMessage message,
			Class<? extends Entity> clazz, FacesMessage.Severity severity) {
		if (message == null || clazz == null || severity == null) {
			throw new IllegalArgumentException();
		}
		facesMessage = MessageHelper.generateMessage(message, clazz, severity);
	}

	/**
	 * Erzeugt ein neues {@linkplain FailureStatus}-Objekt. Wird für einen der
	 * Parameter {@code null} übergeben, wird eine
	 * {@linkplain IllegalArgumentException} geworfen.
	 * 
	 * @param message
	 *            - eine {@linkplain GenericErrorMessage}
	 * @param severity
	 *            - die Art der Meldung (Fehler, Info, Warnung)
	 */
	public FailureStatus(GenericErrorMessage message,
			FacesMessage.Severity severity) {
		if (message == null || severity == null) {
			throw new IllegalArgumentException();
		}
		facesMessage = MessageHelper.generateMessage(message, severity);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FacesMessage report() {
		return facesMessage;
	}
}
