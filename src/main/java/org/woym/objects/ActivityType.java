package org.woym.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;

/**
 * Diese abstrakte Klasse dient als Superklasse für konkrete Aktivitätstypen.
 * Damit sind z.B. Projekte oder Schulfächer gemeint.
 * 
 * @author Adrian
 *
 */
@Inheritance
@Entity
@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.STRING, length = 20)
public abstract class ActivityType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5462510547873626789L;

	/**
	 * Die automatisch generierte ID ist der Primärschlüssel für die Datenbank.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * Der Name des Aktivitätstyps.
	 */
	@Column(nullable = false)
	private String name;

	/**
	 * Die typsiche Dauer dieses Aktivitätstyps in Minuten.
	 */
	private int typicalDuration;

	/**
	 * Eine Liste der Räume, an welchen diese Aktivität typischerweise
	 * abgehalten werden kann.
	 */
	@ManyToMany
	@OrderBy("name")
	private List<Room> rooms = new ArrayList<Room>();

	public ActivityType() {
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

	public int getTypicalDuration() {
		return typicalDuration;
	}

	public void setTypicalDuration(int typicalDuration) {
		this.typicalDuration = typicalDuration;
	}

	public List<Room> getRooms() {
		return rooms;
	}

	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}

	@Override
	public String toString() {
		return name + "(" + typicalDuration + " min.)";
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof ActivityType) {
			return ((ActivityType) object).getName().equals(this.name);
		}
		return false;
	}

	/**
	 * Fügt das übergebenene {@linkplain Room}-Objekt der entsprechenden Liste
	 * hinzu, sofern es noch nicht darin vorhanden ist.
	 * 
	 * @param room
	 *            - das hinzuzufügende Objekt
	 * @return {@code true}, wenn das Objekt sich noch nicht in der Liste
	 *         befindet und hinzugefügt wurde, ansonsten {@code false}
	 */
	public boolean addRoom(final Room room) {
		if (!rooms.contains(room)) {
			rooms.add(room);
			return true;
		}
		return false;
	}

	/**
	 * Entfernt das übergebene {@linkplain Room}-Objekt aus der entsprechenden
	 * Liste.
	 * 
	 * @param room
	 *            - das zu entfernden Objekt
	 * @return {@code true}, wenn das Objekt entfernt wurde, ansonsten
	 *         {@code false}
	 */
	public boolean removeRoom(final Room room) {
		return rooms.remove(room);
	}

	/**
	 * Gibt {@code true} zurück, wenn sich das übergebene {@linkplain Room}
	 * -Objekt in der Liste befindet, ansonsten {@code false}.
	 * 
	 * @param room
	 *            - das Objekt, für das geprüft werden soll, ob es sich in der
	 *            Liste befindet
	 * @return {@code true}, wenn das Objekt sich in der Liste befindet,
	 *         ansonsten {@code false}
	 */
	public boolean containsRoom(final Room room) {
		return rooms.contains(room);
	}
}
