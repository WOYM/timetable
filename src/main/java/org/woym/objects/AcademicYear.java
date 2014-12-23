package org.woym.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.woym.spec.objects.IMemento;
import org.woym.spec.objects.IMementoObject;

/**
 * Diese Klasse repräsentiert einen Jahrgang.
 * 
 * @author Adrian
 *
 */
@Entity
public class AcademicYear extends org.woym.objects.Entity implements
		IMementoObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5889210190170256683L;

	/**
	 * Die automatisch generierte ID ist der Primärschlüssel für die Datenbank.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * Der Jahrgang als Zahl.
	 */
	@Column(nullable = false, unique = true)
	private int academicYear;

	/**
	 * Die zu diesem Jahrgang gehörigen Schulklassen.
	 */
	@OneToMany(cascade = CascadeType.ALL)
	@OrderBy("identifier")
	private List<Schoolclass> schoolclasses = new ArrayList<>();

	/**
	 * Die Unterrichtsbedarfe diesen Jahrgang.
	 */
	@ElementCollection
	@CollectionTable(name = "ACADEMICYEAR_LESSONDEMANDS", joinColumns = @JoinColumn(name = "ACADEMICYEAR"))
	@Column(name = "DEMAND")
	@MapKeyJoinColumn(name = "LESSONTYPE", referencedColumnName = "ID")
	private Map<LessonType, Integer> lessonDemands = new HashMap<>();

	public AcademicYear() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getAcademicYear() {
		return academicYear;
	}

	public void setAcademicYear(int academicYear) {
		this.academicYear = academicYear;
	}

	public List<Schoolclass> getSchoolclasses() {
		return schoolclasses;
	}

	public void setSchoolclasses(List<Schoolclass> schoolclasses) {
		this.schoolclasses = schoolclasses;
	}

	public Map<LessonType, Integer> getLessonDemands() {
		return lessonDemands;
	}

	public void setLessonDemands(Map<LessonType, Integer> lessonDemands) {
		this.lessonDemands = lessonDemands;
	}

	@Override
	public String toString() {
		return String.valueOf(academicYear);
	}

	@Override
	public int hashCode() {
		return academicYear;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AcademicYear other = (AcademicYear) obj;
		if (academicYear != other.academicYear)
			return false;
		return true;
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
	 * Setzt den Status des {@linkplain AcademicYear}-Objektes auf den Status
	 * des übergebenen {@linkplain Memento}-Objektes.
	 * 
	 * @param memento
	 *            - das Memento-Objekt, von welchem das
	 *            {@linkplain AcademicYear}-Objekt den Status annehmen soll
	 */
	public void setMemento(Memento memento) {
		if (memento == null) {
			throw new IllegalArgumentException();
		}
		id = memento.id;
		academicYear = memento.academicYear;
		schoolclasses = memento.schoolclasses;
		lessonDemands = memento.lessonDemands;
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
			academicYear = actualMemento.academicYear;
			schoolclasses = actualMemento.schoolclasses;
			lessonDemands = actualMemento.lessonDemands;
		} else {
			throw new IllegalArgumentException(
					"Only org.woym.objects.AcademicYear.Memento as parameter allowed.");
		}
	}

	/**
	 * Die Memento-Klasse für {@linkplain AcademicYear}.
	 * 
	 * @author adrian
	 *
	 */
	public static class Memento implements IMemento {

		private final Long id;

		private final int academicYear;

		private final List<Schoolclass> schoolclasses;

		private final Map<LessonType, Integer> lessonDemands;

		Memento(AcademicYear originator) {
			if (originator == null) {
				throw new IllegalArgumentException();
			}
			id = originator.id;
			academicYear = originator.academicYear;
			schoolclasses = originator.schoolclasses;
			lessonDemands = originator.lessonDemands;
		}

		public Long getId() {
			return id;
		}
	}
}
