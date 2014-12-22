package org.woym.objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.woym.spec.objects.IActivityObject;

/**
 * Diese Klasse repräsentiert einen Raum.
 * 
 * @author Adrian
 *
 */
@Entity
public class Room extends org.woym.objects.Entity implements IActivityObject {

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
	public String toString() {
		return name + " (" + purpose + ")";
	}

	/**
	 * Erzeugt ein neues {@linkplain Memento} und gibt es zurück.
	 * 
	 * @return ein {@linkplain Memento} mit dem aktuellen Zustand des Objektes
	 */
	public Memento createMemento() {
		return new Memento(this);
	}

	/**
	 * Setzt den Status des {@linkplain Room}-Objektes auf den Status des
	 * übergebenen {@linkplain Memento}-Objektes.
	 * 
	 * @param memento
	 *            - das Memento-Objekt, von welchem das {@linkplain Room}
	 *            -Objekt den Status annehmen soll
	 */
	public void setMemento(Memento memento) {
		id = memento.id;
		name = memento.name;
		purpose = memento.purpose;
		additionalInformation = memento.additionalInformation;
	}

	/**
	 * Die Memento-Klasse zu {@linkplain Room}.
	 * 
	 * @author adrian
	 *
	 */
	public static class Memento {

		private final Long id;

		private final String name;

		private final String purpose;

		private final String additionalInformation;

		public Memento(Room originator) {
			if (originator == null) {
				throw new IllegalArgumentException();
			}
			id = originator.id;
			name = originator.name;
			purpose = originator.purpose;
			additionalInformation = originator.additionalInformation;
		}
	}

}
