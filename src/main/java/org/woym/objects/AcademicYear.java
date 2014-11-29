package org.woym.objects;

import java.io.Serializable;
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

/**
 * Diese Klasse repräsentiert einen Jahrgang.
 * 
 * @author Adrian
 *
 */
@Entity
public class AcademicYear implements Serializable {

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
	@CollectionTable(name = "ACADEMICYEAR_SUBJECTDEMANDS", joinColumns = @JoinColumn(name = "ACADEMICYEAR"))
	@Column(name = "DEMAND")
	@MapKeyJoinColumn(name = "SUBJECT", referencedColumnName = "ID")
	private Map<Subject, Integer> subjectDemands = new HashMap<>();

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

	public Map<Subject, Integer> getSubjectDemands() {
		return subjectDemands;
	}

	public void setSubjectDemands(Map<Subject, Integer> subjectDemands) {
		this.subjectDemands = subjectDemands;
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
	 * Fügt ein Mapping für das übergebene Subject mit dem übergebenen int-Wert
	 * ein. Ist bereits ein Mapping für das übergebene Subject vorhanden, wird
	 * dieses nicht überschrieben und die Methode gibt {@code false} zurück.
	 * Ansonsten wird das Mapping eingefügt und {@code true} zurückgegeben.
	 * 
	 * @param subject
	 *            - das Schulfach
	 * @param demand
	 *            - der zu mappende Bedarf
	 * @return {@code true}, wenn noch kein Mapping vorhanden war und eins
	 *         hinzugefügt wurde, ansonsten {@code false}
	 */
	public boolean addSubjectDemand(final Subject subject, int demand) {
		if (!subjectDemands.containsKey(subject)) {
			subjectDemands.put(subject, demand);
			return true;
		}
		return false;
	}

	/**
	 * Entfernt das Mapping für das übergebene {@linkplain Subject}-Objekt.
	 * 
	 * @param subject
	 *            - das Unterrichtsfache, für welches das Mapping entfernt
	 *            werden soll
	 * @return Integer (Value), wenn ein Mapping bestand, ansonsten {@code null}
	 */
	public Integer removeSubjectDemand(final Subject subject) {
		return subjectDemands.remove(subject);
	}

	/**
	 * Gibt {@code true} zurück, wenn ein Mapping für das übergebene
	 * {@linkplain Subject}-Objekt vorhanden ist, ansonsten {@code false}.
	 * 
	 * @param subject
	 *            - das Schulfach, für das geprüft werden soll, ob ein Mapping
	 *            vorhanden ist
	 * @return {@code true}, wenn ein Mapping vorhanden ist, ansonsten
	 *         {@code false}
	 */
	public boolean containsSubjectDemand(final Subject subject) {
		return subjectDemands.containsKey(subject);
	}
}
