package org.woym.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 * Diese Klasse repräsentiert eine Aktivität des Personals.
 * 
 * @author Adrian
 *
 */
@Entity
public class Activity implements Serializable {

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
	 * Die Art der Aktivität, z.B. bestimmtes Projekt oder Schulfach.
	 */
	@JoinColumn(nullable = false)
	private ActivityType type;

	/**
	 * Der Zeitraum, in welchem diese Aktivität stattfindet.
	 */
	@OneToOne(cascade = CascadeType.ALL)
	private TimePeriod time;

	/**
	 * Der Raum, in welchem die Aktivität stattfindet.
	 */
	@JoinColumn(nullable = false)
	private Room room;

	/**
	 * Die an der Aktivität teilnehmenden Schulklasse.
	 */
	@ManyToMany
	private List<Schoolclass> schoolclasses = new ArrayList<>();

	/**
	 * Die an der Aktivität teilnehmenden Lehrer über
	 * {@linkplain EmployeeTimePeriods} Zeiträumen zugeordnet.
	 */
	@OneToMany
	@JoinTable(name = "ACTIVITY_EMPLOYEES", joinColumns = @JoinColumn(name = "ACTIVITY"), inverseJoinColumns = @JoinColumn(name = "EMPLOYEETIMEPERIODS"))
	@MapKeyJoinColumn(name = "EMPLOYEE")
	private Map<Employee, EmployeeTimePeriods> employees = new HashMap<>();

	public Activity() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ActivityType getType() {
		return type;
	}

	public void setType(ActivityType type) {
		this.type = type;
	}

	public TimePeriod getTime() {
		return time;
	}

	public void setTime(TimePeriod time) {
		this.time = time;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public List<Schoolclass> getSchoolclasses() {
		return schoolclasses;
	}

	public void setSchoolclasses(List<Schoolclass> schoolclasses) {
		this.schoolclasses = schoolclasses;
	}

	public Map<Employee, EmployeeTimePeriods> getEmployees() {
		return employees;
	}

	public void setEmployees(Map<Employee, EmployeeTimePeriods> employees) {
		this.employees = employees;
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
	 * Fügt ein Mapping für den übergebenen Lehrer mit dem übergebenen
	 * {@linkplain EmployeeTimePeriods}-Objekt ein. ein. Ist bereits ein Mapping
	 * für den übergebenen {@link Employee} vorhanden, wird dieses nicht
	 * überschrieben und die Methode gibt {@code false} zurück. Ansonsten wird
	 * das Mapping eingefügt und {@code true} zurückgegeben.
	 * 
	 * @param employee
	 *            - der Mitarbeiter
	 * @param employeeTimePeriods
	 *            - das zu mappende {@link EmployeeTimePeriods}-Objekt
	 * @return {@code true}, wenn noch kein Mapping vorhanden war und eins
	 *         hinzugefügt wurde, ansonsten {@code false}
	 */
	public boolean addSubjectDemand(final Employee employee,
			EmployeeTimePeriods employeeTimePeriods) {
		if (!employees.containsKey(employee)) {
			employees.put(employee, employeeTimePeriods);
			return true;
		}
		return false;
	}

	/**
	 * Entfernt das Mapping für das übergebene {@linkplain Employee}-Objekt.
	 * 
	 * @param employee
	 *            - der Mitarbeiter, für welchen das Mapping entfernt werden soll
	 * @return EmployeeTimePeriods (Value), wenn ein Mapping bestand, ansonsten {@code null}
	 */
	public EmployeeTimePeriods removeSubjectDemand(final Employee employee) {
		return employees.remove(employee);
	}

	/**
	 * Gibt {@code true} zurück, wenn ein Mapping für das übergebene
	 * {@linkplain Teacher}-Objekt vorhanden ist, ansonsten {@code false}.
	 * 
	 * @param employee
	 *            - der Mitarbeiter, für den eprüft werden soll, ob ein Mapping
	 *            vorhanden ist
	 * @return {@code true}, wenn ein Mapping vorhanden ist, ansonsten
	 *         {@code false}
	 */
	public boolean containsSubjectDemand(final Employee employee) {
		return employees.containsKey(employee);
	}
}
