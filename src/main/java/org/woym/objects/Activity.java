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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;

/**
 * Diese Klasse repräsentiert eine Aktivität des Personals.
 * 
 * @author Adrian
 *
 */
@Entity
public class Activity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8778645597768022130L;

	/**
	 * Die automatisch generierte ID ist der Primärschlüssel für die Datenbank.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * Die Art der Aktivität, z.B. bestimmtes Projekt oder Schulfach.
	 */
	@JoinColumn(nullable = false)
	private ActivityType type;

	/**
	 * Der Zeitraum, in welchem diese Aktivität stattfindet.
	 */
	@JoinColumn(nullable = false)
	private TimePeriod time;

	/**
	 * Der Raum, in welchem die Aktivität stattfindet.
	 */
	@JoinColumn(nullable = false)
	private Room room;

	/**
	 * Die an der Aktivität teilnehmenden Schulklasse.
	 */
	@ManyToMany(cascade = CascadeType.ALL)
	private List<Schoolclass> schoolclasses = new ArrayList<>();

	/**
	 * Die an der Aktivität teilnehmenden Lehrer über
	 * {@linkplain TeacherParticipitions} einem Zeitraum zugeordnet.
	 */
	private TeacherParticipitions teachers;

	public Activity() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ActivityType getType() {
		return type;
	}

	public void setType(ActivityType type) {
		this.type = type;
	}

	public TimePeriod getTime() {
		return time;
	}

	public void setTime(TimePeriod time) {
		this.time = time;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}
	
	public List<Schoolclass> getSchoolclasses(){
		return schoolclasses;
	}
	
	public TeacherParticipitions getTeachers() {
		return teachers;
	}

	public void setTeachers(TeacherParticipitions teachers) {
		this.teachers = teachers;
	}
	
	/**
	 * Fügt das übergebenene {@linkplain Schoolclass}-Objekt der entsprechenden
	 * Liste hinzu, sofern es noch nicht darin vorhanden ist.
	 * 
	 * @param schoolclass
	 *            - das hinzuzufügende Objekt
	 * @return {@code true}, wenn das Objekt sich noch nicht in der Liste
	 *         befindet und hinzugefügt wurde, ansonsten {@code false}
	 */
	public boolean addSchoolclass(final Schoolclass schoolclass) {
		if (!schoolclasses.contains(schoolclass)) {
			schoolclasses.add(schoolclass);
			return true;
		}
		return false;
	}

	/**
	 * Entfernt das übergebene {@linkplain Schoolclass}-Objekt aus der
	 * entsprechenden Liste.
	 * 
	 * @param schoolclass
	 *            - das zu entfernden Objekt
	 * @return {@code true}, wenn das Objekt entfernt wurde, ansonsten
	 *         {@code false}
	 */
	public boolean removeSchoolclass(final Schoolclass schoolclass) {
		return schoolclasses.remove(schoolclass);
	}

	/**
	 * Gibt {@code true} zurück, wenn sich das übergebene
	 * {@linkplain Schoolclass}-Objekt in der Liste befindet, ansonsten
	 * {@code false}.
	 * 
	 * @param schoolclass
	 *            - das Objekt, für das geprüft werden soll, ob es sich in der
	 *            Liste befindet
	 * @return {@code true}, wenn das Objekt sich in der Liste befindet,
	 *         ansonsten {@code false}
	 */
	public boolean containsSchoolclass(final Schoolclass schoolclass) {
		return schoolclasses.contains(schoolclass);
	}
}
