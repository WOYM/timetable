package org.woym.objects;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Diese Klasse repräsentiert eine Schulklasse.
 * 
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
	 * Die Unterrichtsbedarfe dieser Klasse. Können angepasst sein oder
	 * entsprechen denen des Jahrgangs ({@linkplain AcademicYear}).
	 */
	@OneToMany(cascade = CascadeType.ALL)
	private List<SubjectDemand> subjectDemands;

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

	public List<SubjectDemand> getSubjectDemands() {
		return subjectDemands;
	}

	public void setSubjectDemands(List<SubjectDemand> subjectDemands) {
		this.subjectDemands = subjectDemands;
	}
	
	//TODO: Listenoperationen für subjectDemands.

}
