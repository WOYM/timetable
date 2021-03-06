package org.woym.common.objects;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.woym.common.objects.spec.IMemento;

/**
 * Eine Klasse die eine Liste die einen Mitarbeiter und eine Liste von
 * Zeiträumen enhält.
 * 
 * @author Adrian
 *
 */
@Entity
public class EmployeeTimePeriods extends org.woym.common.objects.Entity {

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
	@OneToMany(fetch = FetchType.EAGER)
	@OrderBy("day, startTime")
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

	/**
	 * Gibt {@code true} zurück, wenn das übergebene Object == diesem Objekt
	 * oder eine Instanz von {@linkplain EmployeeTimePeriods} ist und
	 * {@linkplain EmployeeTimePeriods#employee} nach
	 * {@linkplain Employee#equals(Object)} gleich ist. Ansonsten wird
	 * {@code false} zurückgegeben.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		EmployeeTimePeriods other = (EmployeeTimePeriods) obj;
		if (employee == null) {
			if (other.employee != null) {
				return false;
			}
		} else if (!employee.equals(other.employee)) {
			return false;
		}
		return true;
	}

	/**
	 * Fügt das übergebene {@linkplain TimePeriod}-Objekt der entsprechenden
	 * Liste hinzu, sofern es gemäß {@linkplain TimePeriod#equals(Object)} noch
	 * nicht vorhanden ist und gibt {@code true} zurück. Ist es bereits
	 * vorhanden, wird {@code false} zurückgegeben.
	 * 
	 * @param timePeriod
	 *            - das hinzuzufügende {@linkplain TimePeriod}-Objekt
	 * @return {@code true}, wenn nicht vorhanden und hinzugefügt, ansonsten
	 *         {@code false}
	 */
	public boolean add(final TimePeriod timePeriod) {
		if (!timePeriods.contains(timePeriod)) {
			timePeriods.add(timePeriod);
			return true;
		}
		return false;
	}

	/**
	 * Entfernt das übergebene {@linkplain TimePeriod}-Objekt aus der
	 * entsprechenden Liste, sofern es vorhanden ist. Gibt {@code true} zurück,
	 * wenn es vorhanden war, ansonsten {@code false}.
	 * 
	 * @param timePeriod
	 *            - das zu entfernende {@linkplain TimePeriod}-Objekt
	 * @return {@code true}, wenn es vorhanden war, ansonsten {@code false}
	 */
	public boolean remove(final TimePeriod timePeriod) {
		return timePeriods.remove(timePeriod);
	}

	/**
	 * Prüft, ob sich das übergebene {@linkplain TimePeriod}-Objekt in der
	 * entsprechenden Liste befindet. Ist dies der Fall, wird {@code true}
	 * zurückgegeben, ansonsten {@code false}.
	 * 
	 * @param timePeriod
	 *            - das zu prüfende {@linkplain TimePeriod}-Objekt
	 * @return {@code true}, wenn es in der entsprechenden Liste vorhanden ist,
	 *         ansonsten {@code false}
	 */
	public boolean contains(final TimePeriod timePeriod) {
		return timePeriods.contains(timePeriod);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IMemento createMemento() {
		return new Memento(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMemento(IMemento memento) {
		if (memento == null) {
			throw new IllegalArgumentException();
		}
		if (memento instanceof Memento) {
			Memento actualMemento = (Memento) memento;
			this.id = actualMemento.id;
			this.employee = actualMemento.employee;
			this.timePeriods = new ArrayList<TimePeriod>(
					actualMemento.timePeriods);
		}
	}

	public static class Memento implements IMemento {

		private final Long id;

		private final Employee employee;

		private final List<TimePeriod> timePeriods;

		Memento(EmployeeTimePeriods originator) {
			this.id = originator.id;
			this.employee = originator.employee;
			this.timePeriods = new ArrayList<TimePeriod>(originator.timePeriods);
		}
	}
}