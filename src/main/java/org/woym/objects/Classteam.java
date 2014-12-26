package org.woym.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.woym.spec.objects.IMemento;
import org.woym.spec.objects.IMementoObject;

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
	 * Der Lehrer, der verpflichtend Teil eines Klassenteams sein muss.
	 */
	@JoinColumn(nullable = false)
	private Teacher teacher;

	/**
	 * Die zusätzlichen zugeordneten Mitarbeiter.
	 */
	@ManyToMany
	private List<Employee> employees = new ArrayList<Employee>();

	/**
	 * Die Schulklassen, für die dieses Klassenteam besteht.
	 */
	@OneToMany
	private List<Schoolclass> schoolclasses = new ArrayList<Schoolclass>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
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
			this.teacher = actualMemento.teacher;
			this.employees = new ArrayList<Employee>(actualMemento.employees);
			this.schoolclasses = new ArrayList<Schoolclass>(
					actualMemento.schoolclasses);
		} else {
			throw new IllegalArgumentException(
					"Only org.woym.objects.Classteam.Memento as parameter allowed.");
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

		private final Teacher teacher;

		private final List<Employee> employees;

		private final List<Schoolclass> schoolclasses;

		Memento(Classteam originator) {
			if (originator == null) {
				throw new IllegalArgumentException();
			}
			this.id = originator.id;
			this.teacher = originator.teacher;
			this.employees = new ArrayList<Employee>(originator.employees);
			this.schoolclasses = new ArrayList<Schoolclass>(
					originator.schoolclasses);
		}

	}

}
