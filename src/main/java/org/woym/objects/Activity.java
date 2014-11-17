package org.woym.objects;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Diese Klasse repräsentiert eine Aktivität des Personals.
 * @author Adrian
 *
 */
@Entity
public class Activity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8778645597768022130L;

	/**
	 * Die automatisch generierte ID ist der Primärschlüssel für die Datenbank.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	public Activity() {
	}
}
