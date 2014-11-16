package org.woym.objects;

import java.io.Serializable;

import javax.persistence.Entity;

@Entity
public class Teacher extends Staff implements Serializable {

	private static final long serialVersionUID = -2846205796145565740L;

	public Teacher() {
		
	}
}
