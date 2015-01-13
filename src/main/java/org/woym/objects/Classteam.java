package org.woym.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.woym.objects.spec.IMemento;
import org.woym.objects.spec.IMementoObject;

/**
 * Diese Klasse repräsentiert ein Klassen-Team. Ein Klassen-Team besteht aus
 * mindestens einem Lehrer und beliebig vielen weiteren Lehrern oder päd.
 * Mitarbeitern.
 * 
 * @author adrian
 *
 */
@Entity
public class Classteam extends org.woym.objects.Entity implements Serializable,
		IMementoObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7860958206425966964L;

	/**
	 * Die automatisch generierte ID ist der Primärschlüssel für die Datenbank.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * Die Mitarbeiter eines Klassenteams. Darunter muss sich mindestens eine
	 * Lehrkraft befinden.
	 */
	@ManyToMany
	@OrderBy("symbol")
	private List<Employee> employees = new ArrayList<Employee>();

	/**
	 * Die Schulklassen, für die dieses Klassenteam besteht.
	 */
	@OneToMany
	private List<Schoolclass> schoolclasses = new ArrayList<Schoolclass>();

	public Classteam() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	public List<Schoolclass> getSchoolclasses() {
		return schoolclasses;
	}

	public void setSchoolclasses(List<Schoolclass> schoolclasses) {
		this.schoolclasses = schoolclasses;
	}

	@Override
	public String toString() {
		String classes = "";
		for (int i = 0; i < schoolclasses.size(); i++) {
			if (i == schoolclasses.size() - 1) {
				classes += schoolclasses.get(i).toString();
				return classes;
			}
			classes += schoolclasses.get(i).toString() + ", ";
		}
		return classes;
	}

	public boolean addEmployee(Employee employee) {
		if (!employees.contains(employee)) {
			return employees.add(employee);
		}
		return false;
	}

	public boolean remove(Employee employee) {
		return employees.remove(employee);
	}

	public boolean addSchoolclass(Schoolclass schoolclass) {
		if (!schoolclasses.contains(schoolclass)) {
			return schoolclasses.add(schoolclass);
		}
		return false;
	}

	public boolean remove(Schoolclass schoolclass) {
		return schoolclasses.remove(schoolclass);
	}

	/**
	 * Prüft, ob in der Liste der Mitarbeiter noch mindestens ein Lehrer
	 * vorhanden ist. Ist dies der Fall, wird {@code true} zurückgegeben,
	 * ansonsten {@code false}.
	 * 
	 * @return {@code true}, wenn das Klassenteam noch mindestens einen Lehrer
	 *         besitzt, ansonsten {@code false}
	 */
	public boolean teacherLeft() {
		for (Employee e : employees) {
			if (e instanceof Teacher) {
				return true;
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IMemento createMemento() {
		return new Memento(this);
	}

	/**
	 * Bei Übergabe von {@code null} oder einem Parameter, der nicht vom Typ
	 * {@linkplain Memento} ist, wird eine {@linkplain IllegalArgumentException}
	 * geworfen. Ansonsten wird der Status des Objektes auf den des übergebenen
	 * Memento-Objektes gesetzt.
	 * 
	 * @param memento
	 *            - das {@linkplain Memento}-Objekt, von welchem dieses Objekt
	 *            den Status annehmen soll
	 */
	@Override
	public void setMemento(IMemento memento) {
		if (memento == null) {
			throw new IllegalArgumentException("Parameter is null.");
		}
		if (memento instanceof Memento) {
			Memento actualMemento = (Memento) memento;
			this.id = actualMemento.id;
			this.employees = new ArrayList<Employee>(actualMemento.employees);
			this.schoolclasses = new ArrayList<Schoolclass>(
					actualMemento.schoolclasses);
		} else {
			throw new IllegalArgumentException("Only " + Memento.class
					+ " as parameter allowed.");
		}

	}

	/**
	 * Die Memento-Klasse zu {@linkplain Classteam}.
	 * 
	 * @author adrian
	 *
	 */
	public static class Memento implements IMemento {

		private final Long id;

		private final List<Employee> employees;

		private final List<Schoolclass> schoolclasses;

		Memento(Classteam originator) {
			if (originator == null) {
				throw new IllegalArgumentException();
			}
			this.id = originator.id;
			this.employees = new ArrayList<Employee>(originator.employees);
			this.schoolclasses = new ArrayList<Schoolclass>(
					originator.schoolclasses);
		}

	}

}
