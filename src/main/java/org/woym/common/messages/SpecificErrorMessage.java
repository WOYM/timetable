package org.woym.common.messages;

import org.woym.common.exceptions.DatasetException;

/**
 * Dieses Enum bietet Statusnachrichten mit einem konkreten Bezug zu einer
 * Klasse.
 * 
 * @author Adrian
 *
 */
public enum SpecificErrorMessage {

	/**
	 * Diese Nachricht wird gewählt, wenn beim Persistieren eines Objektes in
	 * der Datenbank eine {@linkplain DatasetException} aufgetreten ist.
	 */
	ADD_OBJECT_DATASET_EXCEPTION("Datenbankfehler",
			"Beim Hinzufügen %s ist ein Datenbankfehler aufgetreten."),

	/**
	 * Diese Nachricht wird gewählt, wenn beim Aktualisieren eines Objektes in
	 * der Datenbank eine {@linkplain DatasetException} auftritt.
	 */
	UPDATE_OBJECT_DATASET_EXCEPTION("Datenbankfehler",
			"Beim Aktualisieren %s ist ein Datenbankfehler aufgetreten."),

	/**
	 * Diese Nachricht wird gewählt, wenn beim Löschen eines Objektes aus der
	 * Datenbank eine {@linkplain DatasetException} auftritt.
	 */
	DELETE_OBJECT_DATASET_EXCEPTION("Datenbankfehler",
			"Beim Löschen %s ist ein Datenbankfehler aufgetreten."),

	/**
	 * Diese Nachricht wird gewählt, wenn beim Validieren einer Aktivität eine
	 * Überschneidung auftritt.
	 */
	VALIDATE_ACTIVITY_EXCEPTION("Validierungsfehler",
			"Beim Validieren %s ist eine Überschneidung aufgetreten.");

	/**
	 * Zusammenfassung der Nachricht.
	 */
	private final String summary;

	/**
	 * Die Statusnachricht.
	 */
	private final String statusMessage;

	private SpecificErrorMessage(String summary, String statusMessage) {
		this.summary = summary;
		this.statusMessage = statusMessage;
	}

	public String getSummary() {
		return summary;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

}
