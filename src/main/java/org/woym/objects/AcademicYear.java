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
	@Column(nullable = false)
	private int academicYear;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Schoolclass> schoolclasses = new ArrayList<>();

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
	
	//TODO: Methoden um Schulklassen hinzuzufügen, entfernen, etc.
}
