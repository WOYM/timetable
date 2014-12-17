package org.woym.messages;

/**
 * Dieses Enum bietet Statusnachrichten mit einem konkreten Bezug zu einer
 * Klasse.
 * 
 * @author Adrian
 *
 */
public enum SpecificStatusMessage {

	ADD_OBJECT_DATASET_EXCEPTION(0, "Datenbankfehler",
			"Beim Hinzufügen %s ist ein Datenbankfehler aufgetreten."),

	UPDATE_OBJECT_DATASET_EXCEPTION(1, "Datenbankfehler",
			"Beim Aktualisieren %s ist ein Datenbankfehler aufgetreten."),

	DELETE_OBJECT_DATASET_EXCEPTION(2, "Datenbankfehler",
			"Beim Löschen %s ist ein Datenbankfehler aufgetreten.")

	;
	/**
	 * Die Ordinalzahl.
	 */
	private final int ordinal;

	/**
	 * Zusammenfassung der Nachricht.
	 */
	private final String summary;

	/**
	 * Die Statusnachricht.
	 */
	private final String statusMessage;

	private SpecificStatusMessage(int ordinal, String summary,
			String statusMessage) {
		this.ordinal = ordinal;
		this.summary = summary;
		this.statusMessage = statusMessage;
	}

	public int getOrdinal() {
		return ordinal;
	}

	public String getSummary() {
		return summary;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

}
