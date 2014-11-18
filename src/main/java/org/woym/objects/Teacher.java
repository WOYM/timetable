package org.woym.objects;

import java.io.Serializable;

import javax.persistence.Entity;

/**
 * Diese Klasse repräsentiert einen Lehrer.
 * @author Adrian
 *
 */
@Entity
public class Teacher extends Employee implements Serializable {

	private static final long serialVersionUID = -2846205796145565740L;

	public Teacher() {	
	}

	@Override
	public boolean addActivityType(ActivityType activityType) {
		// TODO Auto-generated method stub
		return false;
	}
}
