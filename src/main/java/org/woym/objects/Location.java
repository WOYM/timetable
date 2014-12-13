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

/**
 * Diese Klasse repräsentiert einen Standort.
 * 
 * @author Adrian
 *
 */
@Entity
public class Location extends org.woym.objects.Entity {

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
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public boolean addRoom(final Room room) {
		if (!rooms.contains(room)) {
			rooms.add(room);
			return true;
		}
		return false;
	}

	public boolean removeRoom(final Room room) {
		return rooms.remove(room);
	}

	public boolean containsRoom(final Room room) {
		return rooms.contains(room);
	}
}
