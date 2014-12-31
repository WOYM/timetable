package org.woym.objects;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.woym.objects.spec.IMemento;
import org.woym.objects.spec.IMementoObject;

/**
 * Diese Klasse repräsentiert einen Standort.
 * 
 * @author Adrian
 *
 */
@Entity
public class Location extends org.woym.objects.Entity implements IMementoObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5060682933291643092L;

	/**
	 * Die automatisch generierte ID ist der Primärschlüssel für die Datenbank.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * Der Name des Standortes. Dieser ist einzigartig.
	 */
	@Column(unique = true)
	private String name;

	/**
	 * Die zum Standort gehörigen Räume.
	 */
	@OneToMany(cascade = CascadeType.ALL)
	private List<Room> rooms = new ArrayList<Room>();

	public Location() {
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

	public List<Room> getRooms() {
		return rooms;
	}

	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/**
	 * Gibt {@code true} zurück, wenn das übergebene Object == diesem Objekt
	 * oder eine Instanz von {@linkplain Location} ist und
	 * {@linkplain Location#name} bei ignorierter Groß- und Kleinschreibung
	 * denselben Wert besitzt. Ansonsten wird {@code false} zurückgegeben.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.toUpperCase().equals(other.name.toUpperCase()))
			return false;
		return true;
	}

	/**
	 * Fügt das übergebene {@linkplain Room}-Objekt diesem Standort hinzu, wenn
	 * es gemäß {@linkplain Room#equals(Object)} noch nicht vorhanden ist und
	 * gibt {@code true} zurück. Ist es bereits vorhanden, wird lediglich
	 * {@code false} zurückgegeben.
	 * 
	 * @param room
	 *            - der hinzuzufügende Raum
	 * @return {@code true}, wenn der Raum nicht vorhanden war und hinzugefügt
	 *         wurde, ansonsten {@code false}
	 */
	public boolean add(final Room room) {
		if (!rooms.contains(room)) {
			rooms.add(room);
			return true;
		}
		return false;
	}

	/**
	 * Entfernt das übergebene {@linkplain Room}-Objekt, von diesem Standort,
	 * sofern es vorhanden war und gibt {@code true} zurück. War es nicht
	 * vorhanden, ist es nicht vorhanden, wird {@code false} zurückgegeben.
	 * 
	 * @param room
	 *            - der zu entfernende Raum
	 * @return {@code true}, wenn der zu entfernden Raum vorhanden war und
	 *         entfernt wurde, ansonsten {@code false}
	 */
	public boolean remove(final Room room) {
		return rooms.remove(room);
	}

	/**
	 * Gibt {@code true} zurück, wenn das übergebene {@linkplain Room}-Objekt
	 * Teil des Standortes ist, ansonsten {@code false}.
	 * 
	 * @param room
	 *            - der zur prüfende Raum
	 * @return {@code true}, wenn der Raum Teil des Standortes ist, ansonsten
	 *         {@code false}
	 */
	public boolean contains(final Room room) {
		return rooms.contains(room);
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
			throw new IllegalArgumentException("Parameter is null");
		}
		if (memento instanceof Memento) {
			Memento actualMemento = (Memento) memento;
			id = actualMemento.id;
			name = actualMemento.name;
			rooms = new ArrayList<Room>(actualMemento.rooms);
		} else {
			throw new IllegalArgumentException(
					"Only org.woym.objects.Location.Memento as parameter allowed.");
		}

	}

	/**
	 * Die Memento-Klasse zu {@linkplain Location}.
	 * 
	 * @author adrian
	 *
	 */
	public static class Memento implements IMemento {

		private final Long id;

		private final String name;

		private final List<Room> rooms;

		Memento(Location originator) {
			if (originator == null) {
				throw new IllegalArgumentException();
			}
			id = originator.id;
			name = originator.name;
			rooms = new ArrayList<Room>(originator.rooms);
		}
	}
}
