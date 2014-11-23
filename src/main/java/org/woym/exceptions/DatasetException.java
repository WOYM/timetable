package org.woym.exceptions;

/**
 * Diese Exception wird geworfen, wenn ein Fehler bei einer Datenbankanfrage
 * auftritt.
 * 
 * @author Adrian
 *
 */
public class DatasetException extends Exception {

	/**
	 * Automatisch generierte ID.
	 */
	private static final long serialVersionUID = -1484024982366879197L;

	/**
	 * Erzeugt eine neue DatasetException mit dem übergebenen String als Nachricht.
	 * 
	 * @param pMessage
	 *            - die Fehlernachricht
	 */
	public DatasetException(final String message) {
		super(message);
	}
}
