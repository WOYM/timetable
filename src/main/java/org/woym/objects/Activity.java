package org.woym.objects;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.woym.spec.objects.IMemento;
import org.woym.spec.objects.IMementoObject;

/**
 * Diese Klasse repräsentiert eine Aktivität innerhalb eines Stundenplans.
 * 
 * @author Adrian
 *
 */
@Entity
@Inheritance
@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.STRING, length = 20)
public abstract class Activity extends org.woym.objects.Entity implements
		IMementoObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8778645597768022130L;

	/**
	 * Die automatisch generierte ID ist der Primärschlüssel für die Datenbank.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * Der Zeitraum, in welchem diese Aktivität stattfindet.
	 */

	@Embedded
	private TimePeriod time;

	/**
	 * Die Räume, in welchen die Aktivität stattfindet. Diese befinden sich alle
	 * am selben Standort.
	 */
	@ManyToMany
	@OrderBy("name")
	private List<Room> rooms;

	/**
	 * Die an der Aktivität teilnehmenden Schulklasse.
	 */
	@ManyToMany
	private List<Schoolclass> schoolclasses = new ArrayList<>();

	/**
	 * Die an der Aktivität teilnehmenden Lehrer über
	 * {@linkplain EmployeeTimePeriods} Zeiträumen zugeordnet.
	 */
	@OneToMany(cascade = CascadeType.ALL)
	private List<EmployeeTimePeriods> employeeTimePeriods = new ArrayList<EmployeeTimePeriods>();

	public Activity() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TimePeriod getTime() {
		return time;
	}

	public void setTime(TimePeriod time) {
		this.time = time;
	}

	public List<Room> getRooms() {
		return rooms;
	}

	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}

	public List<Schoolclass> getSchoolclasses() {
		return schoolclasses;
	}

	public void setSchoolclasses(List<Schoolclass> schoolclasses) {
		this.schoolclasses = schoolclasses;
	}

	public List<EmployeeTimePeriods> getEmployeeTimePeriods() {
		return employeeTimePeriods;
	}

	public void setEmployeeTimePeriods(
			List<EmployeeTimePeriods> employeeTimePeriods) {
		this.employeeTimePeriods = employeeTimePeriods;
	}

	/**
	 * Fügt das übergebenene {@linkplain Schoolclass}-Objekt der entsprechenden
	 * Liste hinzu, sofern es noch nicht darin vorhanden ist.
	 * 
	 * @param schoolclass
	 *            - das hinzuzufügende Objekt
	 * @return {@code true}, wenn das Objekt sich noch nicht in der Liste
	 *         befindet und hinzugefügt wurde, ansonsten {@code false}
	 */
	public boolean addSchoolclass(final Schoolclass schoolclass) {
		if (!schoolclasses.contains(schoolclass)) {
			schoolclasses.add(schoolclass);
			return true;
		}
		return false;
	}

	/**
	 * Entfernt das übergebene {@linkplain Schoolclass}-Objekt aus der
	 * entsprechenden Liste.
	 * 
	 * @param schoolclass
	 *            - das zu entfernden Objekt
	 * @return {@code true}, wenn das Objekt entfernt wurde, ansonsten
	 *         {@code false}
	 */
	public boolean removeSchoolclass(final Schoolclass schoolclass) {
		return schoolclasses.remove(schoolclass);
	}

	/**
	 * Gibt {@code true} zurück, wenn sich das übergebene
	 * {@linkplain Schoolclass}-Objekt in der Liste befindet, ansonsten
	 * {@code false}.
	 * 
	 * @param schoolclass
	 *            - das Objekt, für das geprüft werden soll, ob es sich in der
	 *            Liste befindet
	 * @return {@code true}, wenn das Objekt sich in der Liste befindet,
	 *         ansonsten {@code false}
	 */
	public boolean containsSchoolclass(final Schoolclass schoolclass) {
		return schoolclasses.contains(schoolclass);
	}

	/**
	 * Fügt der Liste von {@linkplain EmployeeTimePeriods}-Objekten das
	 * übergebene hinzu und gibt {@code true} zurück, sofern es noch nicht
	 * vorhanden war. Ansonsten wird es nicht hinzugefügt und {@code false}
	 * zurückgegeben.
	 * 
	 * @param employeeTimePeriods
	 *            - das hinzuzufügende {@linkplain EmployeeTimePeriods}-Objekt
	 * @return {@code true}, wenn das übergebene
	 *         {@linkplain EmployeeTimePeriods}-Objekt noch nicht vorhanden war
	 *         und neu hinzugefügt wurde, ansonsten {@code false}
	 */
	public boolean addEmployeeTimePeriods(
			EmployeeTimePeriods employeeTimePeriods) {
		if (!this.employeeTimePeriods.contains(employeeTimePeriods)) {
			this.employeeTimePeriods.add(employeeTimePeriods);
			return true;
		}
		return false;
	}

	/**
	 * Entfernt das das übergebene {@linkplain EmployeeTimePeriods}-Objekt, gibt
	 * {@code true} zurück, wenn das Entfernen erfolgreich war (das Objekt
	 * vorhanden war), ansonsten {@code false}.
	 * 
	 * @param employee
	 *            - der Mitarbeiter, für welchen das Mapping entfernt werden
	 *            soll
	 * @return {@code true}, wenn übergebenes Objekt gelöscht, ansonsten
	 *         {@code false}
	 */
	public boolean removeEmployeeTimePeriods(
			EmployeeTimePeriods employeeTimePeriods) {
		return this.employeeTimePeriods.remove(employeeTimePeriods);
	}

	/**
	 * Gibt {@code true} zurück, wenn ein {@linkplain EmployeeTimePeriods} für
	 * den übergebenen Lehrer vorhanden ist, ansonsten {@code false}.<br>
	 * 
	 * Funktioniert nur, wenn {@linkplain EmployeeTimePeriods#equals(Object)}
	 * über das in EmployeeTimePeriods vorhandene {@linkplain Employee}-Objekt
	 * definiert ist.
	 * 
	 * @param employee
	 *            - der Mitarbeiter, für den eprüft werden soll, ein
	 *            {@linkplain EmployeeTimePeriods}-Objekt vorhanden ist
	 * @return {@code true}, wenn ein {@linkplain EmployeeTimePeriods}-Objekt
	 *         vorhanden ist, ansonsten {@code false}
	 */
	public boolean containsEmployee(final Employee employee) {
		EmployeeTimePeriods employeeTimePeriods = new EmployeeTimePeriods();
		employeeTimePeriods.setEmployee(employee);
		return this.employeeTimePeriods.contains(employeeTimePeriods);
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
			throw new IllegalArgumentException("Paramater is null.");
		}
		if (memento instanceof Memento) {
			Memento actualMemento = (Memento) memento;
			id = actualMemento.id;
			time = actualMemento.time;
			rooms = actualMemento.rooms;
			schoolclasses = actualMemento.schoolclasses;
			employeeTimePeriods = actualMemento.employeeTimePeriods;
		} else {
			throw new IllegalArgumentException(
					"Parameter must from type org.woym.objects.Activity.Memento");

		}

	}

	/**
	 * Eine Memento-Klasse für {@linkplain Activity}-Objekte.
	 * 
	 * @author adrian
	 *
	 */
	public static class Memento implements IMemento {

		private final Long id;

		private final TimePeriod time;

		private final List<Room> rooms;

		private final List<Schoolclass> schoolclasses;

		private final List<EmployeeTimePeriods> employeeTimePeriods;

		Memento(Activity originator) {
			if (originator == null) {
				throw new IllegalArgumentException();
			}
			id = originator.id;
			time = originator.time;
			rooms = originator.rooms;
			schoolclasses = originator.schoolclasses;
			employeeTimePeriods = originator.employeeTimePeriods;
		}

		Long getId() {
			return id;
		}

		TimePeriod getTime() {
			return time;
		}

		List<Room> getRooms() {
			return rooms;
		}

		List<Schoolclass> getSchoolclasses() {
			return schoolclasses;
		}

		List<EmployeeTimePeriods> getEmployeeTimePeriods() {
			return employeeTimePeriods;
		}
	}
}
