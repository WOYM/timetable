package org.woym.objects;

public enum Week {

	A("A", 0),

	B("B", 1);

	/**
	 * Name der Woche.
	 */
	private final String name;

	/**
	 * Ordinalzahl der Woche.
	 */
	private final int ordinal;

	private Week(final String name, final int ordinal) {
		this.name = name;
		this.ordinal = ordinal;
	}

	@Override
	public String toString() {
		return "Woche " + name;
	}

	public int getOrdinal() {
		return ordinal;
	}
}
