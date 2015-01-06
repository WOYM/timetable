package org.woym.config;

/**
 * Dieses Enum enthält die Default-Einstellungen der Software. Mehrere Werte zu
 * einem Key müssen mittels Komma getrennt werden.
 * 
 * @author Adrian
 *
 */
public enum DefaultConfigEnum {

	/**
	 * Das Backup-Intervall. Angabe des Intervalls in Minuten.
	 */
	BACKUP_INTERVAL("backup_interval", "30"),

	/**
	 * Die Uhrzeit, zu welcher ein Backup stattfinden soll, falls das
	 * Backup-Intervall >= 1440 ist.
	 */
	BACKUP_TIME("backup_time", "12:00"),

	/**
	 * Die Uhrzeit, zu welcher die Planung montags erlaubt ist. Als Werte
	 * sollten Start- und Endzeit durch Komma getrennt angegeben werden. Ist der
	 * Wert {@code null} ist der Tag nicht in die Planung einzubeziehen.
	 */
	MONDAY_TIME("monday_time", "08:00,16:00"),

	/**
	 * Die Uhrzeit, zu welcher die Planung dienstags erlaubt ist. Als Werte
	 * sollten Start- und Endzeit durch Komma getrennt angegeben werden. Ist der
	 * Wert {@code null} ist der Tag nicht in die Planung einzubeziehen.
	 */
	TUESDAY_STARTTIME("tuesday_time", "08:00,16:00"),

	/**
	 * Die Uhrzeit, zu welcher die Planung mittwochs erlaubt ist. Als Werte
	 * sollten Start- und Endzeit durch Komma getrennt angegeben werden. Ist der
	 * Wert {@code null} ist der Tag nicht in die Planung einzubeziehen.
	 */
	WEDNESDAY_TIME("wednesday_time", "08:00,16:00"),

	/**
	 * Die Uhrzeit, zu welcher die Planung donnerstags erlaubt ist. Als Werte
	 * sollten Start- und Endzeit durch Komma getrennt angegeben werden. Ist der
	 * Wert {@code null} ist der Tag nicht in die Planung einzubeziehen.
	 */
	THURSDAY_TIME("thursday_time", "08:00,16:00"),

	/**
	 * Die Uhrzeit, zu welcher die Planung freitags erlaubt ist. Als Werte
	 * sollten Start- und Endzeit durch Komma getrennt angegeben werden. Ist der
	 * Wert {@code null} ist der Tag nicht in die Planung einzubeziehen.
	 */
	FRIDAY_TIME("friday_time", "08:00,16:00"),

	/**
	 * Die Uhrzeit, zu welcher die Planung samstags erlaubt ist. Als Werte
	 * sollten Start- und Endzeit durch Komma getrennt angegeben werden. Ist der
	 * Wert {@code null} ist der Tag nicht in die Planung einzubeziehen.
	 */
	SATURDAY_TIME("saturday_time", null),

	/**
	 * Die Uhrzeit, zu welcher die Planung sonntags erlaubt ist. Als Werte
	 * sollten Start- und Endzeit durch Komma getrennt angegeben werden. Ist der
	 * Wert {@code null} ist der Tag nicht in die Planung einzubeziehen.
	 */
	SUNDAY_TIME("sunday_time", null),

	/**
	 * Die Größe des Zeitrasters des Stundenplans in Minuten.
	 */
	TIMETABLE_GRID("timetable_grid", "15"),

	/**
	 * Der Standardwert für die typische Dauer aller Aktivitätstypen. Kann von
	 * einzelnen Aktivitätstypen überschrieben werden.
	 */
	TYPICAL_ACTIVITY_DURATION("typical_activity_duration", "45"),

	/**
	 * Die Stundenabrechnung eines Lehrers in Minuten, also die Angabe wie viele
	 * Minuten einer Arbeitsstunde eines Lehrers entsprechen.
	 */
	TEACHER_HOURLY_SETTLEMENT("teacher_hourly_settlement", "45"),

	/**
	 * Die Stundenabrechnung eines päd. Mitarbeiters in Minuten, also die Angabe
	 * wie viele Minuten einer Arbeitsstunde eines päd. Mitarbeiters
	 * entsprechen.
	 */
	PEDAGOGIC_ASSISTANT_HOURLY_SETTLEMENT(
			"pedagogic_assistant_hourly_settlement", "60"),

	/**
	 * True, wenn kein Bestätigungsdialog beim Löschen eines Lehrers angezeigt
	 * werden soll, der auf die Folgen hinweist.
	 */
	HIDE_TEACHER_DELETION_DIALOG("hide_teacher_deletion_dialog", "false"),

	/**
	 * True, wenn kein Bestätigungsdialog beim Löschen eines päd. Mitarbeiters
	 * angezeigt werden soll, der auf die Folgen hinweist.
	 */
	HIDE_PA_DELETION_DIALOG("hide_pa_deletion_dialog", "false"),

	/**
	 * True, wenn kein Bestätigungsdialog beim Löschen einer Schulklasse
	 * angezeigt werden soll, der auf die Folgen hinweist.
	 */
	HIDE_SCHOOLCLASS_DELETION_DIALOG("hide_schoolclass_deletion_dialog",
			"false"),

	/**
	 * True, wenn kein Bestätigungsdialog beim Löschen eines Standortes
	 * angezeigt werden soll, der auf die Folgen hinweist.
	 */
	HIDE_LOCATION_DELETION_DIALOG("hide_location_deletion_dialog", "false"),

	/**
	 * True, wenn kein Bestätigungsdialog beim Löschen eines Raumes angezeigt
	 * werden soll, der auf die Folgen hinweist.
	 */
	HIDE_ROOM_DELETION_DIALOG("hide_room_deletion_dialog", "false"),

	/**
	 * True, wenn kein Bestätigungsdialog beim Löschen eines Aktivitätstypens
	 * angezeigt werden soll, der auf die Folgen hinweist.
	 */
	HIDE_ACTIVITYTYPE_DELETION_DIALOG("hide_activitytype_deletion_dialog",
			"false"),

	/**
	 * both für Groß- und Kleinbuchstaben, als Bezeichner für Schulklassen.
	 * lower nur für Kleinbuchstaben, upper nur für Großbuchstaben.
	 */
	SCHOOLCLASS_IDENTIFIER_CASE("schoolclass_identifier_case",
			Config.IDENTIFIER_UPPER_CASE);

	/**
	 * Der Key der Property.
	 */
	private final String propKey;

	/**
	 * Der Wert der Property.
	 */
	private final String propValue;

	private DefaultConfigEnum(String propKey, String propValue) {
		this.propKey = propKey;
		this.propValue = propValue;
	}

	/**
	 * Gibt den Property-Key zurück.
	 * 
	 * @return den Property-Key
	 */
	public String getPropKey() {
		return propKey;
	}

	/**
	 * Gibt den Property-Wert zurück.
	 * 
	 * @return den Property-Wert
	 */
	public String getPropValue() {
		return propValue;
	}
}
