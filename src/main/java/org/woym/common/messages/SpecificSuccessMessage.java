package org.woym.common.messages;

/**
 * Dieses Enum enthält spezifische Erfolgsmeldungen, die sich auf ein bestimmtes
 * Objekt beziehen.
 * 
 * @author adrian
 *
 */
public enum SpecificSuccessMessage {

	/**
	 * Diese Nachricht wird gewählt, wenn ein Objekt erfolgreich der Datenbank
	 * hinzugefügt wurde.
	 */
	ADD_OBJECT_SUCCESS("%s hinzugefügt", "%s %s wurde erfolgreich hinzugefügt."),

	/**
	 * Diese Nachricht wird gewählt, wenn ein Objekt erfolgreich aktualisiert
	 * wurde.
	 */
	UPDATE_OBJECT_SUCCESS("%s aktualisiert",
			"%s %s wurde erfolgreich aktualisiert."),

	/**
	 * Diese Nachricht wird gewählt, wenn ein Objekt erfolgreich gelöscht wurde.
	 */
	DELETE_OBJECT_SUCCESS("%s gelöscht", "%s %s wurde erfolgreich gelöscht.");

	/**
	 * Die Zusammenfassung der Nachricht.
	 */
	private final String summary;

	/**
	 * Die detaillierte Nachricht.
	 */
	private final String message;

	private SpecificSuccessMessage(String summary, String message) {
		this.summary = summary;
		this.message = message;
	}

	public String getSummary() {
		return summary;
	}

	public String getMessage() {
		return message;
	}

}
