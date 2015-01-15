package org.woym.objects;

public enum Weekday {

	MONDAY("Montag", 0),

	TUESDAY("Dienstag", 1),

	WEDNESDAY("Mittwoch", 2),

	THURSDAY("Donnerstag", 3),

	FRIDAY("Freitag", 4),

	SATURDAY("Samstag", 5),

	SUNDAY("Sonntag", 6);

	/**
	 * Der Name des Wochentags.
	 */
	private final String name;

	/**
	 * Die Ordinalzahl.
	 */
	private final int ordinal;

	/**
	 * Erzeugt einen neuen Wochentag mit dem übergebenen Namen und der
	 * übergebenen Ordinalzahl.
	 * 
	 * @param name
	 *            - Name des Wochentags
	 * @param ordinal
	 *            - die Ordinalzahl des Wochentags
	 */
	private Weekday(final String name, final int ordinal) {
		this.name = name;
		this.ordinal = ordinal;
	}

	@Override
	public String toString() {
		return name;
	}

	public int getOrdinal() {
		return ordinal;
	}

	public String getName() {
		return name;
	}

	/**
	 * Diese Methode findet den korrekten Wochentag anhand eines übergebenen
	 * Ordinals.
	 * <p>
	 * Wenn kein entsprechender Wochentag gefunden wurde, wird {@code null}
	 * zurückgegeben.
	 * 
	 * @param ordinal
	 *            Das Ordinal des Wochentages
	 * @return Der Wochentag oder {@code null}, wenn kein Wochentag dieses
	 *         Ordinal hat
	 */
	public static Weekday getByOrdinal(int ordinal) {
		for (Weekday weekday : Weekday.values()) {
			if (ordinal == weekday.getOrdinal()) {
				return weekday;
			}
		}
		return null;
	}
}
