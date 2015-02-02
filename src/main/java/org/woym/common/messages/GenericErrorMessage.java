package org.woym.common.messages;

import org.woym.common.exceptions.DatasetException;

/**
 * Dieses Enum bietet generische Statusnachrichten, also solche, die keinen
 * Bezug zu einer konkreten Klasse haben, an.
 * 
 * @author Adrian
 *
 */

public enum GenericErrorMessage {
	/**
	 * Diese Statusnachricht wird gewählt, wenn ein Fehler bei der Kommunikation
	 * mit der Datenbank aufgetreten ist, z.B. wenn eine Liste aller Objekte
	 * einer Klasse geholt werden sollte und dabei eine
	 * {@linkplain DatasetException} auftrat.
	 */
	DATABASE_COMMUNICATION_ERROR("Datenbankfehler",
			"Bei der Kommunikation mit der Datenbank ist ein Fehler aufgetreten."),

	/**
	 * Diese Statusnachricht wird gewählt, wenn beim Aktualisieren der
	 * Konfiguration ein Fehler aufgetreten ist.
	 */
	CONFIG_UPDATE_ERROR("Konfigurationsfehler",
			"Beim Aktualisieren der Konfiguration ist ein Fehler aufgetreten."),

	/**
	 * Diese Statusnachricht wird gewählt, wenn ein Textfeld mit der Bezeichnung
	 * "Name" leer gelassen wurde, dort aber ein Wert eingetragen werden muss.
	 */
	NAME_IS_EMPTY("Ungültiger Name.", "Name darf nicht leer sein."),

	/**
	 * Diese Statusnachricht wird gewählt, wenn in ein Textfeld mit der
	 * Bezeichnung "Name" ein eindeutiger Wert einzutragen ist, aber ein Wert
	 * eingetragen wurde, der bereits im System existiert.
	 */
	NAME_ALREADY_EXISTS("Ungültiger Name.",
			"Dieser Name wird bereits verwendet."),

	/**
	 * Diese Statusnachricht wird gewählt, wenn ein Textfeld mit der Bezeichnung
	 * "Kürzel" leer gelassen wurde, dort aber ein Wert eingetragen werden muss.
	 */
	SYMBOL_IS_EMPTY("Ungültiges Kürzel.", "Kürzel darf nicht leer sein."),

	/**
	 * Diese Statusnachricht wird gewählt, wenn in ein Textfeld mit der
	 * Bezeichnung "Kürzel" ein eindeutiger Wert einzutragen ist, aber ein Wert
	 * eingetragen wurde, der bereits im System existiert.
	 */
	SYMBOL_ALREADY_EXISTS("Ungültiges Kürzel.",
			"Dieses Kürzel wird bereits verwendet."),

	/**
	 * Diese Statusnachricht wird gewählt, wenn die Undo-Liste leer ist.
	 */
	UNDO_EMPTY("Rückgängigmachen nicht möglich.",
			"Es ist keine rückgängig zu machende Aktion vorhanden."),

	/**
	 * Diese Statusnachricht wird gewählt, wenn die Redo-Liste leer ist.
	 */
	REDO_EMPTY("Wiederholung nicht möglich.",
			"Es ist keine zu wiederholende Aktion vorhanden."),

	/**
	 * Diese Statusnachricht wird gewählt, wenn beim Erzeugen eines Backups ein
	 * Fehler aufgetreten ist.
	 */
	BACKUP_FAILURE("Backup fehlgeschlagen.",
			"Das Backup des Systems ist fehlgeschlagen."),

	/**
	 * Diese Statusnachricht wird gewählt, wenn das Wiederherstellen eines
	 * Backups fehlgeschlagen ist.
	 */
	RESTORE_FAILURE("Backup-Wiederherstellung fehlgeschlagen.",
			"Die Wiederherstellung des Backups ist fehlgeschlagen."),

	/**
	 * Diese Statusnachricht wird gewählt, wenn beim Wiederherstellen eines
	 * Backups das Wiederherstellen der Systemeinstellungen fehlgeschlagen ist.
	 */
	NOT_FULLY_RESTORED(
			"System nicht vollständig wiederhergestellt",
			"Die Systemeinstellungen konnten nicht erfolgreich wiederhergestellt werden. "
					+ "Kopieren Sie sie manuell aus dem gezippten Backup in das Arbeitsverzeichnis."),

	/**
	 * Diese Statusnachricht wird gewählt, wenn an einer Stelle eine ungültige
	 * Datei gewählt wurde.
	 */
	INVALID_FILE("Ungültige Datei", "Die ausgewählte Datei ist ungültig."),

	/**
	 * Diese Statusnachricht wird gewählt, wenn keine Aktivität gelöscht werden
	 * könnte.
	 */
	NO_ACTIVITIES_TO_DELETE("Keine Aktivitäten zu löschen.",
			"Es sind keine zu löschenden Aktivitäten vorhanden."),

	/**
	 * Diese Statusnachricht wird gewählt, wenn ein unvorhergesehener Fehler
	 * beim Drag&Drop von Aktivitäten auftritt.
	 */
	INVALID_WEEKDAY_IN_DISPLAY("Es wurde ein ungültiger Tag übergeben.",
			"Die Aktivität kann nicht aktualisiert werden."),

	/**
	 * Diese Statusnachricht wird gewählt, wenn ein Wochentag, der als nicht zu
	 * verplanend in den Systemeinstellungen eingetragen ist, gewählt wurde.
	 */
	INVALID_WEEKDAY(
			"Der Wochentag ist ungültig.",
			"Der gewählte Wochentage wurde in den Systemeinstellungen nicht als zu verplanend angegeben."),

	/**
	 * Diese Statusnachricht wird gewählt, wenn die Wochenstunden eines
	 * Mitarbeiters überschritten werden.
	 */
	HOURS_PER_WEEK_EXCEEDED("Wochenstunden werden überschritten.",
			"Die Wochenstunden des Mitarbeiters werden überschritten."),

	/**
	 * Diese Statusnachricht wird gewählt, wenn ein Zeitraum außerhalb der in
	 * den Einstellungen definierten Zeitgrenzen liegt.
	 */
	TIME_OUTSIDE_LIMIT("Zeitpunkt außerhalb der Grenzen.",
			"Der gewünschte Zeitpunkt befindet sich außerhalb"
					+ " der in den Einstellungen festgelegten Grenzen.");

	/**
	 * Zusammenfassung der Nachricht.
	 */
	private final String summary;

	/**
	 * Die Statusnachricht.
	 */
	private final String statusMessage;

	private GenericErrorMessage(String summary, String statusMessage) {
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
