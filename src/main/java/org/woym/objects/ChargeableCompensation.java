package org.woym.objects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Diese Klasse repräsentiert eine anrechenbare Ersatzleistung des Personals.
 * 
 * @author Adrian
 *
 */
@Embeddable
public class ChargeableCompensation implements Serializable {

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + value;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChargeableCompensation other = (ChargeableCompensation) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (value != other.value)
			return false;
		return true;
	}
}
