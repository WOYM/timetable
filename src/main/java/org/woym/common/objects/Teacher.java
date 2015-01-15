package org.woym.common.objects;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Diese Klasse repräsentiert einen Lehrer.
 * @author Adrian
 *
 */
@Entity
@DiscriminatorValue("Teacher")
public class Teacher extends Employee implements Serializable {

	private static final long serialVersionUID = -2846205796145565740L;

	public Teacher() {	
	}
}
