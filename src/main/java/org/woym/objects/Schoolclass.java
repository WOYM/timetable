package org.woym.objects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Diese Klasse repräsentiert eine Schulklasse.
 * @author Adrian
 *
 */
@Entity
public class Schoolclass implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7834492276089995782L;

	/**
	 * Die automatisch generierte ID ist der Primärschlüssel für die Datenbank.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * Character unter dem die Klasse identifziert werden kann.
	 */
	@Column(nullable = false)
	private char identifier;

	/**
	 * Die Unterrichtsbedarfe dieser Klasse.
	 */
	private SubjectDemands subjectDemands;

	public Schoolclass() {
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public char getIdentifier() {
		return identifier;
	}

	public void setIdentifier(char identifier) {
		this.identifier = identifier;
	}
	
	public SubjectDemands getSubjectDemands() {
		return subjectDemands;
	}

	public void setSubjectDemands(SubjectDemands subjectDemands) {
		this.subjectDemands = subjectDemands;
	}
}
