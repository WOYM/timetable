package org.woym.objects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

/**
 * Diese Klasse repräsentiert einen Unterrichtsbedarf.
 * 
 * @author Adrian
 *
 */
@Entity
public class SubjectDemand implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3397696987190394762L;

	/**
	 * Die automatisch generierte ID ist der Primärschlüssel für die Datenbank.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * Das Schulfach, welchem Bedarf besteht.
	 */
	@JoinColumn(nullable = false)
	private Subject subject;

	/**
	 * Der Bedarf an diesem Schulfach in Schulstunden.
	 */
	@Column(nullable = false)
	private int demand;

	public SubjectDemand() {
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public int getDemand() {
		return demand;
	}

	public void setDemand(int demand) {
		this.demand = demand;
	}

}
