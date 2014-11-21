package org.woym.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Diese Klasse ordnet einem {@link Employee}-Objekt eine Liste von
 * {@link TimePeriod}-Objekten zu.
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
	 * Der Mitarbeiter, welchem eine Liste von {@link TimePeriod}-Objekten
	 * zugeordnet werden soll.
	 */
	private Employee employee;

	/**
	 * Die Liste von zuzuordnenden {@link TimePeriod}-Objekten.
	 */
	@OneToMany(cascade = CascadeType.ALL)
	private List<TimePeriod> timePeriods = new ArrayList<>();

	public EmployeeTimePeriods() {
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public List<TimePeriod> getTimePeriods() {
		return timePeriods;
	}
	
	public void setTimePeriods(List<TimePeriod> timePeriods) {
		this.timePeriods = timePeriods;
	}

	public boolean addTimePeriod(final TimePeriod timePeriod) {
		if (!timePeriods.contains(timePeriod)) {
			timePeriods.add(timePeriod);
			return true;
		}
		return false;
	}

	public boolean removeTimePeriod(final TimePeriod timePeriod) {
		return timePeriods.remove(timePeriod);
	}

	public boolean containsTimePeriod(final TimePeriod timePeriod) {
		return timePeriods.contains(timePeriod);
	}
}
