package org.woym.objects;

import java.io.Serializable;

import javax.persistence.Entity;

/**
 * Diese Klasse repräsentiert einen pädagogischen Mitarbeiter.
 * @author Adrian
 *
 */
@Entity
public class PedagogicAssistant extends Staff implements Serializable{

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
