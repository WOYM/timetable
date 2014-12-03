package org.woym.objects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Diese Klasse repräsentiert einen Raum.
 * @author Adrian
 *
 */
@Entity
public class Room implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1530008000910481387L;
	/**
	 * Die automatisch generierte ID ist der Primärschlüssel für die Datenbank.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	/**
	 * Name des Raumes.
	 */
	@Column(nullable = false)
	private String name;
	
	/**
	 * Funktion des Raumes.
	 */
	private String purpose;
	
	/**
	 * Zusätzliche Informationen zu dem Raum.
	 */
	private String additionalInformation;

	public Room() {
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getAdditionalInformation() {
		return additionalInformation;
	}

	public void setAdditionalInformation(String additionalInformation) {
		this.additionalInformation = additionalInformation;
	}
	
	@Override
	public String toString(){
		return name + " (" + purpose + ")";
	}
}
