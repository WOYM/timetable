package org.woym.objects;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.woym.spec.objects.IMemento;

/**
 * Eine Klasse die eine Liste die einen Mitarbeiter und eine Liste von Zeiträumen
 * enhält.
 * 
 * @author Adrian
 *
 */
@Entity
public class EmployeeTimePeriods extends org.woym.objects.Entity {

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
	 * Der Lehrer.
	 */
	private Employee employee;

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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((employee == null) ? 0 : employee.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmployeeTimePeriods other = (EmployeeTimePeriods) obj;
		if (employee == null) {
			if (other.employee != null)
				return false;
		} else if (!employee.equals(other.employee))
			return false;
		return true;
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

	/**
	 * @deprecated In dieser Klasse ohne Funktion.
	 */
	@Override
	@Deprecated
	public IMemento createMemento() {
		return null;
	}

	/**
	 * @deprecated In dieser Klasse ohne Funktion.
	 */
	@Override
	@Deprecated
	public void setMemento(IMemento memento) {
	}
}