package org.woym.common.objects;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.eclipse.persistence.annotations.PrivateOwned;
import org.woym.common.objects.spec.IActivityObject;
import org.woym.common.objects.spec.IMemento;
import org.woym.common.objects.spec.IMementoObject;

/**
 * Diese Klasse repräsentiert eine Aktivität innerhalb eines Stundenplans.
 * 
 * @author Adrian
 *
 */
@Entity
@Inheritance
@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.STRING, length = 20)
public abstract class Activity extends org.woym.common.objects.Entity implements
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
	private List<Room> rooms = new ArrayList<Room>();

	/**
	 * Die an der Aktivität teilnehmenden Schulklasse.
	 */
	@ManyToMany
	private List<Schoolclass> schoolclasses = new ArrayList<>();

	/**
	 * Die an der Aktivität teilnehmenden Lehrer über
	 * {@linkplain EmployeeTimePeriods} Zeiträumen zugeordnet.
	 */
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@PrivateOwned
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
	 * Gibt das zum übergebenen Lehrer gehörige {@linkplain EmployeeTimePeriods}
	 * -Objekt zurück. Ist keins für den übergebenen Lehrer vorhanden, wird
	 * {@code null} zurückgegeben.
	 * 
	 * @param employee
	 *            - Lehrer, für welchen das {@linkplain EmployeeTimePeriods
	 *            -Objekt} erwartet wird
	 * @return EmployeeTimePeriods-Objekt für den übergebenen Lehrer oder
	 *         {@code null}, wenn kein solches vorhanden
	 */
	public EmployeeTimePeriods getEmployeeTimePeriods(Employee employee) {
		EmployeeTimePeriods employeeTimePeriods = new EmployeeTimePeriods();
		employeeTimePeriods.setEmployee(employee);
		int index = this.employeeTimePeriods.indexOf(employeeTimePeriods);
		if (index >= 0) {
			return this.employeeTimePeriods.get(index);
		}
		return null;
	}

	@Override
	public String toString() {
		String day = time.getDay().toString();
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		String starttime = df.format(time.getStartTime());
		String endtime = df.format(time.getEndTime());
		return String.format("%s (%s-%s Uhr)", day, starttime, endtime);
	}

	/**
	 * Fügt das übergebenene {@linkplain Room}-Objekt der entsprechenden Liste
	 * hinzu, sofern es noch nicht darin vorhanden ist.
	 * 
	 * @param room
	 *            - das hinzuzufügende Objekt
	 * @return {@code true}, wenn das Objekt sich noch nicht in der Liste
	 *         befindet und hinzugefügt wurde, ansonsten {@code false}
	 */
	public boolean add(Room room) {
		if (!rooms.contains(room)) {
			return rooms.add(room);
		}
		return false;
	}

	/**
	 * Gibt {@code true} zurück, wenn sich das übergebene {@linkplain Room}
	 * -Objekt in der Liste befindet, ansonsten {@code false}.
	 * 
	 * @param room
	 *            - das Objekt, für das geprüft werden soll, ob es sich in der
	 *            Liste befindet
	 * @return {@code true}, wenn das Objekt sich in der Liste befindet,
	 *         ansonsten {@code false}
	 */
	public boolean contains(Room room) {
		return rooms.contains(room);
	}

	/**
	 * Entfernt den übergebenen Raum aus der Liste der Räume, sofern er darin
	 * vorhanden ist und gibt die Größe der Liste nach dem Entfernen zurück.
	 * 
	 * @param room
	 *            - der zu entfernende Raum
	 * @return die Größe der Liste nach dem Entfernen
	 */
	public int remove(Room room) {
		rooms.remove(room);
		return rooms.size();
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
	public boolean add(final Schoolclass schoolclass) {
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
	 * @return die Größe der Liste nach dem Entfernen
	 */
	public int remove(final Schoolclass schoolclass) {
		schoolclasses.remove(schoolclass);
		return schoolclasses.size();
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
	public boolean contains(final Schoolclass schoolclass) {
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
	public boolean add(EmployeeTimePeriods employeeTimePeriods) {
		if (!this.employeeTimePeriods.contains(employeeTimePeriods)) {
			this.employeeTimePeriods.add(employeeTimePeriods);
			return true;
		}
		return false;
	}

	/**
	 * Entfernt das das zum übergebenen {@linkplain Employee}-Objekt gehörende
	 * {@linkplain EmployeeTimePeriods}-Objekt aus der entsprechenden Liste.
	 * 
	 * @param employee
	 *            - der Mitarbeiter, für welchen das Mapping entfernt werden
	 *            soll
	 * @return die Größe der Liste nach dem Entfernen
	 */
	public int remove(Employee employee) {
		EmployeeTimePeriods employeeTimePeriods = new EmployeeTimePeriods();
		employeeTimePeriods.setEmployee(employee);
		this.employeeTimePeriods.remove(employeeTimePeriods);
		return this.employeeTimePeriods.size();
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
	public boolean contains(final Employee employee) {
		EmployeeTimePeriods employeeTimePeriods = new EmployeeTimePeriods();
		employeeTimePeriods.setEmployee(employee);
		return this.employeeTimePeriods.contains(employeeTimePeriods);
	}

	/**
	 * Wrapper Zugriffsklasse.
	 * 
	 * @param object
	 *            Zu löschende {@link IActivityObject}
	 * 
	 * @return Im erfollgt {@code >= 0}, beim misserfolgt {@code -1}
	 */
	public int remove(final IActivityObject object) {
		if (object instanceof Employee) {
			return remove((Employee) object);
		} else if (object instanceof Schoolclass) {
			return remove((Schoolclass) object);
		} else if (object instanceof Room) {
			return remove((Room) object);
		} else {
			return -1;
		}
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
			rooms = new ArrayList<Room>(actualMemento.rooms);
			schoolclasses = new ArrayList<Schoolclass>(
					actualMemento.schoolclasses);
			employeeTimePeriods = new ArrayList<EmployeeTimePeriods>();
			for (IMemento m : actualMemento.employeeTimePeriods) {
				EmployeeTimePeriods e = new EmployeeTimePeriods();
				e.setMemento(m);
				employeeTimePeriods.add(e);
			}
		} else {
			throw new IllegalArgumentException("Only " + Memento.class
					+ " as parameter allowed.");

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

		private final List<IMemento> employeeTimePeriods;

		Memento(Activity originator) {
			if (originator == null) {
				throw new IllegalArgumentException();
			}
			id = originator.id;
			time = originator.time;
			rooms = new ArrayList<Room>(originator.rooms);
			schoolclasses = new ArrayList<Schoolclass>(originator.schoolclasses);
			employeeTimePeriods = new ArrayList<IMemento>();
			for (EmployeeTimePeriods e : originator.employeeTimePeriods) {
				employeeTimePeriods.add(e.createMemento());
			}
		}
	}
}
