package org.woym.objects;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TimePeriod implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2207686775603230048L;

	/**
	 * Die automatisch generierte ID ist der Primärschlüssel für die Datenbank.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	//TODO: Attribute anlegen.
	
	public TimePeriod() {
	}
}
