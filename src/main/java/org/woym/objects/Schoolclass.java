package org.woym.objects;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyJoinColumn;

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
	@ElementCollection
	@CollectionTable(name="SCHOOLCLASS_SUBJECTDEMANDS",
	joinColumns=@JoinColumn(name="SCHOOLCLASS"))
	@Column(name="DEMAND")
	@MapKeyJoinColumn(name="SUBJECT", referencedColumnName="ID")
	private Map<Subject, Integer> subjectDemands = new HashMap<>(); 
	
	
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
	
	public Map<Subject, Integer> getSubjectDemands() {
		return subjectDemands;
	}

}
