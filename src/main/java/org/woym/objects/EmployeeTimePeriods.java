package org.woym.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Eine Klasse die eine Liste von TimePeriod-Objekten hält, damit die HashMap in
 * {@linkplain Activity} persistiert werden kann.
 * 
 * @author Adrian
 *
 */
@Entity
public class EmployeeTimePeriods implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5513072845067093706L;

	/**
	 * Die automatisch generierte ID ist der Primärschlüssel für die Datenbank.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * Eine Liste von Zeiträumen.
	 */
	@ElementCollection
	@OneToMany
	private List<TimePeriod> timePeriods = new ArrayList<>();

	public EmployeeTimePeriods() {
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<TimePeriod> getTimePeriods() {
		return timePeriods;
	}
	
	public void setTimePeriods(List<TimePeriod> timePeriods) {
		this.timePeriods = timePeriods;
	}

	public boolean add(final TimePeriod timePeriod) {
		if (!timePeriods.contains(timePeriod)) {
			timePeriods.add(timePeriod);
			return true;
		}
		return false;
	}

	public boolean remove(final TimePeriod timePeriod) {
		return timePeriods.remove(timePeriod);
	}

	public boolean contains(final TimePeriod timePeriod) {
		return timePeriods.contains(timePeriod);
	}
}