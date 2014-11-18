package org.woym.objects;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Diese Klasse speichert Zuordnungen von {@linkplain Teacher}-Objekten zu
 * {@linkplain TimePeriod}-Objekten, in einer Form, die eine einfache
 * Speicherung innerhalb der Datenbank erlaubt.
 * 
 * @author Adrian
 *
 */
public class TeacherParticipitions implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1473391110791608439L;
	
	/**
	 * Die automatisch generierte ID ist der Primärschlüssel für die Datenbank.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

}
