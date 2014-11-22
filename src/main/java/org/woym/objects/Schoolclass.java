package org.woym.objects;

import java.io.Serializable;
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

/**
 * Diese Klasse repräsentiert eine Schulklasse.
 * 
 * @author Adrian
 *
 */
@Entity
public class Schoolclass implements Serializable {

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
	@CollectionTable(name = "SCHOOLCLASS_SUBJECTDEMANDS", joinColumns = @JoinColumn(name = "SCHOOLCLASS"))
	@Column(name = "DEMAND")
	@MapKeyJoinColumn(name = "SUBJECT", referencedColumnName = "ID")
	private Map<Subject, Integer> subjectDemands = new HashMap<>();

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

	public Map<Subject, Integer> getSubjectDemands() {
		return subjectDemands;
	}

	public void setSubjectDemands(Map<Subject, Integer> subjectDemands) {
		this.subjectDemands = subjectDemands;
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

