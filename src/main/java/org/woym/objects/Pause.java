package org.woym.objects;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Diese Klasse repräsentiert eine Pause.
 * 
 * @author Adrian
 *
 */
@Entity
@DiscriminatorValue("Pause")
public class Pause extends Activity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -917089197311092866L;

	public Pause() {
	}
}
