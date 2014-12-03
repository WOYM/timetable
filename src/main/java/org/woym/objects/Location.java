package org.woym.objects;

import java.io.Serializable;
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
public class Location implements Serializable {

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
	public boolean equals(Object object) {
		if (object instanceof Location) {
			return ((Location) object).getName().equals(this.name);
		}
		return false;
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
