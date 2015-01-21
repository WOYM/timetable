package org.woym.common.objects;

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

import org.woym.common.objects.spec.IActivityObject;
import org.woym.common.objects.spec.IMemento;
import org.woym.common.objects.spec.IMementoObject;

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
public abstract class ActivityType extends org.woym.common.objects.Entity
		implements IActivityObject, IMementoObject {

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

	/**
	 * Die Farbe dieses Aktivitätstyps als Hexadezimal-String.
	 */
	private ColorEnum color = ColorEnum.DEFAULT;

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

	public ColorEnum getColor() {
		return color;
	}

	public void setColor(ColorEnum color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return name + "(" + typicalDuration + " min.)";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/**
	 * Gibt {@code true} zurück, wenn das übergebene Object == diesem Objekt ist
	 * oder das übergebene Object eine Instanz von {@linkplain ActivityType} ist
	 * und das Attribut {@linkplain ActivityType#name} bei ignorierter Groß- und
	 * Kleinschreibung denselben Wert hat. Ansonsten wird {@code false}
	 * zurückgegeben.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ActivityType other = (ActivityType) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.toUpperCase().equals(other.name.toUpperCase())) {
			return false;
		}
		return true;
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
	public boolean add(final Room room) {
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
	public boolean remove(final Room room) {
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
	public boolean contains(final Room room) {
		return rooms.contains(room);
	}

	/**
	 * Gibt die Dauer in Minuten in einem textual schöneren String
	 * "XX Stunden, XX Minuten", bzw. "XX Minuten" (wenn keine ganze Stunde
	 * erfüllt wird) zurück
	 * 
	 * @return Die typische Dauer in einem lesbaren Format
	 */
	public String getReadableDuration() {
		String readableString = "";
		int minutes;
		int hours;

		if (typicalDuration >= 60) {
			minutes = typicalDuration % 60;
			hours = ((int) (typicalDuration - minutes) / 60);

			if (hours == 1) {
				readableString += hours + " Stunde";
			} else {
				readableString += hours + " Stunden";
			}

		} else {
			minutes = typicalDuration;
			hours = 0;
		}

		if (hours != 0 && minutes != 0) {
			readableString += ", ";
		}

		if (minutes != 0) {
			readableString += minutes + " Minuten";
		}
		return readableString;
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
			typicalDuration = actualMemento.typicalDuration;
			rooms = new ArrayList<Room>(actualMemento.rooms);
			color = actualMemento.color;
		} else {
			throw new IllegalArgumentException("Only " + Memento.class
					+ " as parameter allowed.");
		}

	}

	/**
	 * Die Memento-Klasse zu {@linkplain ActivityType}.
	 * 
	 * @author adrian
	 *
	 */
	public static class Memento implements IMemento {

		private final Long id;

		private final String name;

		private final int typicalDuration;

		private final List<Room> rooms;

		private final ColorEnum color;

		Memento(ActivityType originator) {
			if (originator == null) {
				throw new IllegalArgumentException();
			}
			id = originator.id;
			name = originator.name;
			typicalDuration = originator.typicalDuration;
			rooms = new ArrayList<Room>(originator.rooms);
			color = originator.color;
		}

	}
}
