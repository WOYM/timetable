package org.woym.objects;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Diese Klasse repräsentiert ein Projekt.
 * 
 * @author Adrian
 *
 */
@Entity
@DiscriminatorValue("Project")
public class Project extends Activity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8279381157083717967L;

	public Project() {
	}
}
