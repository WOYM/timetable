package org.woym.config;

/**
 * Dieses Enum enthält die Default-Einstellungen der Software.
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
	BACKUP_TIME("backup_time", null),

	/**
	 * Die Uhrzeit, zu welcher die Planung montags erlaubt ist.
	 */
	MONDAY_TIME("monday_time", "08:00,16:00"),

	TUESDAY_STARTTIME("tuesday_time", "08:00,16:00"),

	WEDNESDAY_TIME("wednesday_time", "08:00,16:00"),

	THURSDAY_TIME("thursday_time", "08:00,16:00"),

	FRIDAY_TIME("friday_time", "08:00,16:00"),

	SATURDAY_TIME("saturday_time", null),

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
			"pedagogic_assistant_hourly_settlement", "60");

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
