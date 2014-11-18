package org.woym.objects;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Diese Klasse speichert Zuordnungen von Unterrichtsbedarfen (in Minuten) zu
 * Schulfächern in einer Form, die eine einfache Speicherung innerhalb der
 * Datenbank ermöglicht.
 * 
 * @author Adrian
 *
 */
@Entity
public class SubjectDemands implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8029068373070643166L;

	/**
	 * Die automatisch generierte ID ist der Primärschlüssel für die Datenbank.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
}
