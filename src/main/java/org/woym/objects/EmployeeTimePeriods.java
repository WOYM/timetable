package org.woym.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

	@OneToMany
	private List<TimePeriod> timePeriods = new ArrayList<>();

	public List<TimePeriod> getTimePeriods() {
		return timePeriods;
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
