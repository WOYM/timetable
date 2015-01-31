package org.woym.common.config;

import org.woym.common.objects.ColorEnum;

/**
 * Dieses Enum enthält die Default-Einstellungen der Software. Mehrere Werte zu
 * einem Key müssen mittels Komma getrennt werden.
 * 
 * @author Adrian
 *
 */
public enum DefaultConfigEnum {

	/**
	 * Das Backup-Intervall. Angabe des Intervalls in Minuten. Ein Wert von 0
	 * wird für deaktivierte automatische Backups verwendet.
	 */
	BACKUP_INTERVAL("backup_interval", "0"),

	/**
	 * Die Uhrzeit, zu welcher ein Backup stattfinden soll, falls das
	 * Backup-Intervall >= 1440 ist.
	 */
	BACKUP_TIME("backup_time", "12:00"),

	/**
	 * Datum im Format dd.MM.yyy, dass das letzte Backup-Datum angibt angibt.
	 * Benötigt bei Intervall > 1440.
	 */
	BACKUP_NEXTDATE("backup_nextdate", null),

	/**
	 * Boolean-Wert, der angibt, ob der Tag zu verplanen ist {@code true} oder
	 * nicht {@code false}.
	 */
	WEEKDAY_MONDAY("weekday_monday", "true"),

	/**
	 * Boolean-Wert, der angibt, ob der Tag zu verplanen ist {@code true} oder
	 * nicht {@code false}.
	 */
	WEEKDAY_TUESDAY("weekday_tuesday", "true"),

	/**
	 * Boolean-Wert, der angibt, ob der Tag zu verplanen ist {@code true} oder
	 * nicht {@code false}.
	 */
	WEEKDAY_WEDNESDAY("weekday_wednesday", "true"),

	/**
	 * Boolean-Wert, der angibt, ob der Tag zu verplanen ist {@code true} oder
	 * nicht {@code false}.
	 */

	WEEKDAY_THURSDAY("weekday_thursday", "true"),
	/**
	 * Boolean-Wert, der angibt, ob der Tag zu verplanen ist {@code true} oder
	 * nicht {@code false}.
	 */
	WEEKDAY_FRIDAY("weekday_friday", "true"),

	/**
	 * Boolean-Wert, der angibt, ob der Tag zu verplanen ist {@code true} oder
	 * nicht {@code false}.
	 */
	WEEKDAY_SATURDAY("weekday_saturday", "false"),

	/**
	 * Boolean-Wert, der angibt, ob der Tag zu verplanen ist {@code true} oder
	 * nicht {@code false}.
	 */
	WEEKDAY_SUNDAY("weekday_sunday", "false"),

	/**
	 * Startzeit eines Wochentages im Format HH:mm
	 */
	WEEKDAY_STARTTIME("weekday_starttime", "08:00"),

	/**
	 * Endzeit eines Wochentages im Format HH:mm
	 */
	WEEKDAY_ENDTIME("weekday_endtime", "16:00"),

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
	 * True, wenn kein Bestätigungsdialog beim Löschen eines Klassenteams
	 * angezeigt werden soll, der auf die Folgen hinweist.
	 */
	HIDE_CLASSTEAM_DELETION_DIALOG("hide_classteam_deletion_dialog", "false"),

	/**
	 * both für Groß- und Kleinbuchstaben, als Bezeichner für Schulklassen.
	 * lower nur für Kleinbuchstaben, upper nur für Großbuchstaben.
	 */
	SCHOOLCLASS_IDENTIFIER_CASE("schoolclass_identifier_case",
			Config.IDENTIFIER_UPPER_CASE),

	/**
	 * Speichert die Farbe für eine Pause als StyleClass eines Eintrags aus dem
	 * {@linkplain ColorEnum}.
	 */
	PAUSE_COLOR("pause_color", ColorEnum.ORANGE.getStyleClass()),

	/**
	 * Speichert die Farbe für einen Bandunterricht als StyleClass eines
	 * Eintrags aus dem {@linkplain ColorEnum}.
	 */
	COMPOUND_LESSON_COLOR("compound_lesson_color", ColorEnum.DEFAULT
			.getStyleClass()),

	/**
	 * Speichert die Farbe für eine Sitzung des Personals als StyleClass eines
	 * Eintrags aus dem {@linkplain ColorEnum}.
	 */
	MEETING_COLOR("meeting_color", ColorEnum.YELLOW.getStyleClass()),

	/**
	 * Speichert die Farbe für einen anspannenden Unterricht als StyleClass
	 * eines Eintrags aus dem {@linkplain ColorEnum}.
	 */
	LESSON_STRESSING_COLOR("lesson_stressing_color", ColorEnum.RED_DARK
			.getStyleClass()),

	/**
	 * Speichert die Farbe für einen entspannenden Unterricht als StyleClass
	 * eines Eintrags aus dem {@linkplain ColorEnum}.
	 */
	LESSON_RELAXING_COLOR("lesson_relaxing_color", ColorEnum.GREEN_DARK
			.getStyleClass());

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
