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
 * Diese Klasse repräsentiert einen Jahrgang.
 * @author Adrian
 *
 */
@Entity
public class AcademicYear implements Serializable{

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
	private List<Schoolclass> schoolclasses = new ArrayList<>();
	
	/**
	 * Die Unterrichtsbedarfe für diesen Jahrgang.
	 */
	@OneToMany(cascade = CascadeType.ALL)
	private List<SubjectDemand> subjectDemands = new ArrayList<>();

	public AcademicYear(){
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

	public List<SubjectDemand> getSubjectDemands() {
		return subjectDemands;
	}

	public void setSubjectDemands(List<SubjectDemand> subjectDemands) {
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
	 * Fügt das übergebenene {@linkplain SubjectDemand}-Objekt der entsprechenden
	 * Liste hinzu, sofern es noch nicht darin vorhanden ist.
	 * 
	 * @param subjectDemand
	 *            - das hinzuzufügende Objekt
	 * @return {@code true}, wenn das Objekt sich noch nicht in der Liste
	 *         befindet und hinzugefügt wurde, ansonsten {@code false}
	 */
	public boolean addSubjectDemand(final SubjectDemand subjectDemand) {
		if (!subjectDemands.contains(subjectDemand)) {
			subjectDemands.add(subjectDemand);
			return true;
		}
		return false;
	}

	/**
	 * Entfernt das übergebene {@linkplain SubjectDemand}-Objekt aus der
	 * entsprechenden Liste.
	 * 
	 * @param subjectDemand
	 *            - das zu entfernden Objekt
	 * @return {@code true}, wenn das Objekt entfernt wurde, ansonsten
	 *         {@code false}
	 */
	public boolean removeSubjectDemand(final SubjectDemand subjectDemand) {
		return subjectDemands.remove(subjectDemand);
	}

	/**
	 * Gibt {@code true} zurück, wenn sich das übergebene
	 * {@linkplain SubjectDemand}-Objekt in der Liste befindet, ansonsten
	 * {@code false}.
	 * 
	 * @param subjectDemand
	 *            - das Objekt, für das geprüft werden soll, ob es sich in der
	 *            Liste befindet
	 * @return {@code true}, wenn das Objekt sich in der Liste befindet,
	 *         ansonsten {@code false}
	 */
	public boolean containsSubjectDemand(final SubjectDemand subjectDemand) {
		return subjectDemands.contains(subjectDemand);
	}
}
