package org.woym.objects;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Diese Klasse repräsentiert eine Sitzung des Personals.
 * 
 * @author Adrian
 *
 */
@Entity
@DiscriminatorValue("Meeting")
public class Meeting extends ActivityType {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3347521812061561725L;

	public Meeting() {
	}
}
