package org.woym.objects;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyJoinColumn;

import org.woym.spec.objects.IActivityObject;
import org.woym.spec.objects.IMemento;
import org.woym.spec.objects.IMementoObject;

/**
 * Diese Klasse repräsentiert eine Schulklasse.
 * 
 * @author Adrian
 *
 */
@Entity
public class Schoolclass extends org.woym.objects.Entity implements
		IActivityObject, IMementoObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7834492276089995782L;

	/**
	 * Die automatisch generierte ID ist der Primärschlüssel für die Datenbank.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * Character unter dem die Klasse identifziert werden kann.
	 */
	@Column(nullable = false)
	private char identifier;

	/**
	 * Die Unterrichtsbedarfe dieser Klasse. Können angepasst werden,
	 * entsprechend sonst denen des Jahrgangs ({@linkplain AcademicYear}), zu
	 * dem diese Klasse gehört.
	 */
	@ElementCollection
	@CollectionTable(name = "SCHOOLCLASS_LESSONDEMANDS", joinColumns = @JoinColumn(name = "SCHOOLCLASS"))
	@Column(name = "DEMAND")
	@MapKeyJoinColumn(name = "LESSONTYPE", referencedColumnName = "ID")
	private Map<LessonType, Integer> lessonDemands = new HashMap<>();

	/**
	 * Der Klassenraum dieser Klasse.
	 */
	@JoinColumn
	private Room room;

	/**
	 * Der Klassenlehrer dieser Klasse.
	 */
	@JoinColumn
	private Teacher teacher;

	public Schoolclass() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public char getIdentifier() {
		return identifier;
	}

	public void setIdentifier(char identifier) {
		this.identifier = identifier;
	}

	public Map<LessonType, Integer> getLessonDemands() {
		return lessonDemands;
	}

	public void setLessonDemands(Map<LessonType, Integer> lessonDemands) {
		this.lessonDemands = lessonDemands;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	/**
	 * Fügt ein Mapping für den übergebenen {@linkplain LessonType} mit dem
	 * übergebenen int-Wert ein. Ist bereits ein Mapping für das übergebene
	 * Subject vorhanden, wird dieses nicht überschrieben und die Methode gibt
	 * {@code false} zurück. Ansonsten wird das Mapping eingefügt und
	 * {@code true} zurückgegeben.
	 * 
	 * @param lessonType
	 *            - der Unterrichtstyp, für den ein Bedarf angegeben werden soll
	 * @param demand
	 *            - der zu mappende Bedarf
	 * @return {@code true}, wenn noch kein Mapping vorhanden war und eins
	 *         hinzugefügt wurde, ansonsten {@code false}
	 */
	public boolean addLessonDemand(final LessonType lessonType, int demand) {
		if (!lessonDemands.containsKey(lessonType)) {
			lessonDemands.put(lessonType, demand);
			return true;
		}
		return false;
	}

	/**
	 * Entfernt das Mapping für das übergebene {@linkplain LessonType}-Objekt.
	 * 
	 * @param lessonType
	 *            - der Unterrichtstyp, für welchen das Mapping entfernt werden
	 *            soll werden soll
	 * @return Integer (Value), wenn ein Mapping bestand, ansonsten {@code null}
	 */
	public Integer removeSubjectDemand(final LessonType lessonType) {
		return lessonDemands.remove(lessonType);
	}

	/**
	 * Gibt {@code true} zurück, wenn ein Mapping für das übergebene
	 * {@linkplain LessonType}-Objekt vorhanden ist, ansonsten {@code false}.
	 * 
	 * @param lessonType
	 *            - der Unterrichtstyp, für welchen geprüft werden soll, ob ein
	 *            Mapping vorhanden ist
	 * @return {@code true}, wenn ein Mapping vorhanden ist, ansonsten
	 *         {@code false}
	 */
	public boolean containsSubjectDemand(final LessonType lessonType) {
		return lessonDemands.containsKey(lessonType);
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
			identifier = actualMemento.identifier;
			lessonDemands = actualMemento.lessonDemands;
			room = actualMemento.room;
			teacher = actualMemento.teacher;
		} else {
			throw new IllegalArgumentException(
					"Only org.woym.objects.Schooclass.Memento as parameter allowed.");
		}

	}

	/**
	 * Die Memento-Klasse zu {@linkplain Schoolclass}.
	 * 
	 * @author adrian
	 *
	 */
	public static class Memento implements IMemento {

		private final Long id;

		private final char identifier;

		private final Map<LessonType, Integer> lessonDemands;

		private final Room room;

		private final Teacher teacher;

		Memento(Schoolclass originator) {
			if (originator == null) {
				throw new IllegalArgumentException();
			}
			id = originator.id;
			identifier = originator.identifier;
			lessonDemands = originator.lessonDemands;
			room = originator.room;
			teacher = originator.teacher;
		}
	}
}
