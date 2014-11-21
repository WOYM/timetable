package org.woym.objects;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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
	 * Der Jahrgang, zu welchem diese Klasse gehört.
	 */
	@ManyToOne
	@JoinColumn
	@Column(nullable = false)
	private AcademicYear academicYear;

	/**
	 * Character unter dem die Klasse identifziert werden kann.
	 */
	@Column(nullable = false)
	private char identifier;

	/**
	 * Die Unterrichtsbedarfe dieser Klasse. Können angepasst sein oder
	 * entsprechen denen des Jahrgangs ({@linkplain AcademicYear}).
	 */
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
	
	public AcademicYear getAcademicYear() {
		return academicYear;
	}

	public void setAcademicYear(AcademicYear academicYear) {
		this.academicYear = academicYear;
	}

	public List<SubjectDemand> getSubjectDemands() {
		return subjectDemands;
	}

	public void setSubjectDemands(List<SubjectDemand> subjectDemands) {
		this.subjectDemands = subjectDemands;
	}
	
	//TODO: Listenoperationen für subjectDemands.

}
