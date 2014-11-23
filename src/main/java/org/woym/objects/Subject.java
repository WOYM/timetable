package org.woym.objects;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Diese Klasse repräsentiert ein Unterrichtsfach.
 * @author Adrian
 *
 */
@Entity
@DiscriminatorValue("Subject")
public class Subject extends ActivityType{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2876347057241150863L;

	
	public Subject() {
	}
}
