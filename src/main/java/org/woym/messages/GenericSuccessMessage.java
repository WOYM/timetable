package org.woym.messages;

/**
 * Dieses Enum enthält Erfolgsmeldungen, die keinen Bezug zu einem Objekt oder
 * einer Klasse haben.
 * 
 * @author adrian
 *
 */
public enum GenericSuccessMessage {

	/**
	 * Diese Statusnachricht wird gewählt, wenn ein Backup erfolgreich war.
	 */
	BACKUP_SUCCESS("Backup erfolgreich",
			"Die Backuperstellung war erfolgreich."),

	/**
	 * Diese Statusnachricht wird gewählt, wenn die Wiederherstellung eines
	 * Backups erfolgreich war.
	 */
	RESTORE_SUCCESS("Backup-Wiederherstellung erfolgreich",
			"Das Datenbank-Backup wurde erfolgreich wiederhergestellt.");

	/**
	 * Die Zusammenfassung der Erfolgsmeldung.
	 */
	private final String summary;

	/**
	 * Die detaillierte Nachricht.
	 */
	private final String message;

	private GenericSuccessMessage(String summary, String message) {
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
