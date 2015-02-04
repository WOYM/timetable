package org.woym.common.objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.woym.common.exceptions.DatasetException;
import org.woym.common.objects.spec.IActivityObject;
import org.woym.common.objects.spec.IMemento;
import org.woym.common.objects.spec.IMementoObject;
import org.woym.persistence.DataAccess;

/**
 * Diese Klasse repräsentiert einen Raum.
 * 
 * @author Adrian
 *
 */
@Entity
public class Room extends org.woym.common.objects.Entity implements
		IActivityObject, IMementoObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1530008000910481387L;

	public static final String DEFAULT_PURPOSE = "Standardraum";

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
	private String purpose = DEFAULT_PURPOSE;

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
		return purpose == null ? name : name + " (" + purpose + ")";
	}

	/**
	 * Gibt den Namen des Standortes, zu dem dieser Raum gehört, zurück. Gehört
	 * der Raum zu keinem Standort oder tritt bei der Suche nach dem Standort
	 * ein Fehler auf, wird ein leerer String zurückgegeben.
	 * 
	 * @return Name des Standortes oder ein leerer String, wenn der Raum zu
	 *         keinem Standort gehört oder ein Fehler auftritt
	 */
	public String getLocationName() {

		return getLocation() != null ? getLocation().getName() : "";

	}

	public Location getLocation() {
		try {
			Location location = DataAccess.getInstance().getOneLocation(this);
			return location;
		} catch (DatasetException e) {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Memento createMemento() {
		return new Memento(this);
	}

	/**
	 * Bei Übergabe von {@code null} oder einem Parameter, der nicht vom Typ
	 * {@linkplain Memento} ist, wird eine {@linkplain IllegalArgumentException}
	 * geworfen. Ansonsten wird der Status des Objektes auf den des übergebenen
	 * Memento-Objektes gesetzt.
	 * 
	 * @param memento
	 *            - das {@linkplain Memento}-Objekt, von welchem dieses Objekt
	 *            den Status annehmen soll
	 */
	@Override
	public void setMemento(IMemento memento) {
		if (memento == null) {
			throw new IllegalArgumentException("Parameter is null.");
		}
		if (memento instanceof Memento) {
			Memento actualMemento = (Memento) memento;
			id = actualMemento.id;
			name = actualMemento.name;
			purpose = actualMemento.purpose;
			additionalInformation = actualMemento.additionalInformation;
		} else {
			throw new IllegalArgumentException("Only " + Memento.class
					+ " as parameter allowed.");
		}

	}

	/**
	 * Die Memento-Klasse zu {@linkplain Room}.
	 * 
	 * @author adrian
	 *
	 */
	public static class Memento implements IMemento {

		private final Long id;

		private final String name;

		private final String purpose;

		private final String additionalInformation;

		Memento(Room originator) {
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
