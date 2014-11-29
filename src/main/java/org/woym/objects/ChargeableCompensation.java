package org.woym.objects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Diese Klasse repräsentiert eine anrechenbare Ersatzleistung des Personals.
 * @author Adrian
 *
 */
@Embeddable
public class ChargeableCompensation implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4760121187261419220L;
	
	/**
	 * Die Höhe der anrechenbaren Ersatzleistung in Minuten.
	 */
	@Column(nullable = false)
	private int value;
	
	/**
	 * Eine Beschreibung der anrechenbaren Ersatzleistung.
	 */
	@Column(nullable = false)
	private String description;
	
	public ChargeableCompensation() {
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
