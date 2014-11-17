package org.woym.objects;

import java.io.Serializable;
import java.util.HashMap;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Diese Klasse repräsentiert eine Schulklasse.
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
	private char identifier;

	/**
	 * Die Unterrichtsbedarfe dieser Klasse.
	 */
	private HashMap<Subject, Integer> subjectDemands = new HashMap<Subject, Integer>();

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

	/**
	 * Fügt das übergebene {@linkplain Subject} mit dem übergebenen Bedarf in
	 * die HashMap ein.
	 * 
	 * @param subject
	 *            - das Unterrichtsfach, das mit einem Bedarf gemappt werden
	 *            soll
	 * @param demand
	 *            - der Bedarf an diesem Fach in Minuten
	 * @return {@code true}, wenn das übergebene {@linkplain Subject} noch nicht
	 *         gemappt wurde, ansonsten false
	 */
	public boolean putSubjectDemand(final Subject subject, final int demand) {
		if (!subjectDemands.containsKey(subject)) {
			subjectDemands.put(subject, new Integer(demand));
			return true;
		}
		return false;
	}

	/**
	 * Ersetzt den Unterrichtsbedarf für das übergebene {@linkplain Subject}.
	 * 
	 * @param subject
	 *            - das Unterrichtsfach, für das der Bedarf geändert werden soll
	 * @param demand
	 *            - der neue Unterrichtsbedarf in Minuten
	 * @return den alten Unterrichtsbedarf als {@code Integer}, falls ein
	 *         Mapping bestand, ansonsten {@code null}
	 */
	public Integer replaceSubjectDemand(final Subject subject, final int demand) {
		return subjectDemands.replace(subject, new Integer(demand));
	}

	/**
	 * Gibt den Unterrichtsbedarf für das übergebene {@linkplain Subject} als
	 * Integer in Minuten zurück oder {@code null}, wenn kein Unterrichtsbedarf
	 * für dieses {@linkplain Subject} angegeben ist.
	 * 
	 * @param subject
	 *            - das Unterrichtsfach, für das der Bedarf abgefragt werden
	 *            soll
	 * @return den Unterrichtsbedarf als Integer in Minuten oder {@code null},
	 *         wenn kein Mapping für das übergebene {@linkplain Subject} besteht
	 */
	public Integer getSubjectDemand(final Subject subject) {
		return subjectDemands.get(subject);
	}
}
