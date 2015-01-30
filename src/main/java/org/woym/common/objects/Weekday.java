package org.woym.common.objects;

import java.util.Calendar;
import java.util.Date;

public enum Weekday {

	MONDAY("Montag", 0),

	TUESDAY("Dienstag", 1),

	WEDNESDAY("Mittwoch", 2),

	THURSDAY("Donnerstag", 3),

	FRIDAY("Freitag", 4),

	SATURDAY("Samstag", 5),

	SUNDAY("Sonntag", 6);

	private static final int DAY_DELTA = 2;
	
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

	/**
	 * Diese Methode gibt das Ordinal eines Wochentages für ein übergebenes
	 * {@link Date} zurück.
	 * 
	 * @param date
	 *            Das {@link Date} für dessen Tag es Ordinal bestimmt werden
	 *            soll
	 * @return Das Ordinal
	 * 
	 * @throws IllegalArgumentException
	 *             Wenn das {@link Date} {@code null} ist
	 */
	public static Weekday getByDate(Date date) {
		if (date == null) {
			throw new IllegalArgumentException("Date was null!");
		}

		Weekday weekday = MONDAY;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		for (Weekday day : Weekday.values()) {
			int myOrd = day.getOrdinal();
			int theirOrd = (calendar.get(Calendar.DAY_OF_WEEK) - DAY_DELTA);
			
			if (myOrd == theirOrd) {
				weekday = day;
			}
		}

		return weekday;
	}
}
