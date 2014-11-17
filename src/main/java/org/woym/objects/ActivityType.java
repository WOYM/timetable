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
import javax.persistence.Inheritance;
import javax.persistence.ManyToMany;

/**
 * Diese abstrakte Klasse dient als Superklasse für konkrete Aktivitätstypen.
 * Damit sind z.B. Projekte oder Schulfächer gemeint.
 * 
 * @author Adrian
 *
 */
@Inheritance
@Entity
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
	@ManyToMany(cascade = CascadeType.ALL)
	private List<Room> location = new ArrayList<Room>();

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

	// TODO: Methoden um Räume hinzuzufügen, löschen, etc.
}
