package org.woym.objects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Diese Klasse repräsentiert eine anrechenbare Ersatzleistung des Personals.
 * @author Adrian
 *
 */
@Entity
public class ChargeableCompensation implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4760121187261419220L;

	/**
	 * Die automatisch generierte ID ist der Primärschlüssel für die Datenbank.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	/**
	 * Die Höhe der anrechenbaren Ersatzleistung in Minuten.
	 */
	@Column(nullable = false)
	private int value;
	
	@Column(nullable = false)
	/**
	 * Eine Beschreibung der anrechenbaren Ersatzleistung.
	 */
	private String description;
	
	public ChargeableCompensation() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
