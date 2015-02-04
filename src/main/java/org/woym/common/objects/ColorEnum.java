package org.woym.common.objects;

import org.testng.util.Strings;

public enum ColorEnum {

	DEFAULT("Standard", "default-color"),

	PURPLE("Violett", "purple"),

	BLUE_DARK("Dunkelblau", "dark-blue"),

	CYAN("Cyan", "cyan"),

	GREEN_DARK("Dunkelgrün", "dark-green"),

	GREEN_LIGHT("Hellgrün", "light-green"),

	RED_DARK("Dunkelrot", "dark-red"),

	RED_LIGHT("Hellrot", "light-red"),

	ORANGE("Orange", "orange"),

	YELLOW("Gelb", "yellow");

	/**
	 * Name der Farbe.
	 */
	private final String name;

	/**
	 * StyleClass-Name des CSS-Eintrags.
	 */
	private final String styleClass;

	private ColorEnum(String name, String styleClass) {
		this.name = name;
		this.styleClass = styleClass;
	}

	public String getName() {
		return name;
	}

	public String getStyleClass() {
		return styleClass;
	}

	/**
	 * Liefert anhand der übergebenen Style-Class das entsprechende
	 * {@link ColorEnum} zurück
	 * 
	 * @param styleClass
	 *            Die Style-Class
	 * @return Das {@link ColorEnum}
	 */
	public static ColorEnum getByStyleClass(String styleClass) {
		if(Strings.isNullOrEmpty(styleClass)) {
			throw new IllegalArgumentException("Unknown style-class. This is a config-problem.");
		}
		
		for (ColorEnum colorEnum : ColorEnum.values()) {
			if (colorEnum.getStyleClass().equals(styleClass)) {
				return colorEnum;
			}
		}

		return null;
	}

}
