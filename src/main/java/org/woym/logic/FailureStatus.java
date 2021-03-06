package org.woym.logic;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;

import org.woym.common.messages.GenericErrorMessage;
import org.woym.common.messages.MessageHelper;
import org.woym.common.messages.SpecificErrorMessage;
import org.woym.common.objects.Entity;
import org.woym.logic.spec.IStatus;

/**
 * Repräsentiert einen Fehlerstatus.
 * 
 * @author JurSch, adrian
 *
 */
public class FailureStatus implements IStatus {

	/**
	 * Die FacesMessage zum aufgetretenen Fehler.
	 */
	private final FacesMessage facesMessage;

	private List<Exception> exceptions = new ArrayList<Exception>();

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
	 * Erzeugt ein neues {@linkplain FailureStatus}-Objekt, dass eine
	 * {@linkplain FacesMessage} mit den übergebenen Parametern enthält.
	 * 
	 * @param summary
	 *            - Zusammenfassung der Nachricht
	 * @param message
	 *            - Detaillierte Nachricht
	 * @param severity
	 *            - Schweregrad der Nachricht
	 */
	public FailureStatus(String summary, String message,
			FacesMessage.Severity severity) {
		if (summary == null || message == null || severity == null) {
			throw new IllegalArgumentException();
		}
		facesMessage = new FacesMessage(severity, summary, message);
	}

	public List<Exception> getExceptions() {
		return exceptions;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FacesMessage report() {
		return facesMessage;
	}

	public void add(Exception exception) {
		exceptions.add(exception);
	}
}
