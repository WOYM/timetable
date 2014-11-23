package org.woym.objects;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Diese Klasse repräsentiert einen pädagogischen Mitarbeiter.
 * 
 * @author Adrian
 *
 */
@Entity
@DiscriminatorValue("PA")
public class PedagogicAssistant extends Employee {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1928908422354690632L;

	public PedagogicAssistant() {
	}

	@Override
	public boolean addActivityType(ActivityType activityType) {
		// TODO Auto-generated method stub
		return false;
	}

}
