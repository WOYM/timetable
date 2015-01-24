package org.woym.common.objects;

/**
 * Dieses Enum bildet die dem System bekannten {@link ActivityType}s ab, sodass
 * es in der Nutzeroberfläche verwendet werden kann.
 * 
 * @author Tim Hansen (tihansen)
 * @version 0.1.0
 * @since 0.1.0
 */
public enum ActivityTypeEnum {
	LESSON("Unterricht"), 
	COMPOUND_LESSON("Bandunterricht"), 
	MEETING("Teamsitzung"), 
	PROJECT("Projekt");

	private String name;

	private ActivityTypeEnum(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
