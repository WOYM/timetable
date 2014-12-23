package org.woym.spec.persistence;

import java.io.Serializable;
import java.util.List;

import org.woym.exceptions.DatasetException;
import org.woym.objects.AcademicYear;
import org.woym.objects.Activity;
import org.woym.objects.ActivityType;
import org.woym.objects.Employee;
import org.woym.objects.LessonType;
import org.woym.objects.Location;
import org.woym.objects.MeetingType;
import org.woym.objects.PedagogicAssistant;
import org.woym.objects.ProjectType;
import org.woym.objects.Room;
import org.woym.objects.Schoolclass;
import org.woym.objects.Teacher;
import org.woym.objects.TravelTimeList;
import org.woym.objects.Weekday;

public interface IDataAccess {

	/**
	 * Persistiert das übergebene Objekt in der Datenbank. Tritt dabei ein
	 * Fehler auf, wird eine {@linkplain DatasetException} geworfen.
	 * 
	 * @param object
	 *            - das zu persistierende Objekt
	 */
	public void persist(Serializable object) throws DatasetException;

	/**
	 * Aktualisiert das Objekt in der Datenbank, welches dem dem übergebenen
	 * entspricht. Tritt beim Merge ein Fehler auf, wird eine
	 * {@linkplain DatasetException} geworfen.
	 * 
	 * @param object
	 *            - das im System bereits aktualisierte, in der Datenbank zu
	 *            persistierende Objekt
	 * @throws DatasetException
	 */
	public void update(Serializable object) throws DatasetException;

	/**
	 * Löscht das Objekt aus der Datenbank, das dem übergebenen entspricht.
	 * Tritt beim Löschen ein Fehler auf, wird eine
	 * {@linkplain DatasetException} geworfen.
	 * 
	 * @param object
	 *            - das zu löschende Objekt
	 * @throws DatasetException
	 */
	public void delete(Serializable object) throws DatasetException;

	/**
	 * Gibt eine Liste aller in der Datenbank vorhandenen
	 * {@linkplain AcademicYear}-Objekte nach Jahr sortiert zurück. Sind keine
	 * vorhanden wird, eine leere Liste zurückgegeben. Tritt bei der Anfrage ein
	 * Fehler auf, wird eine {@linkplain DatasetException} geworfen.
	 * 
	 * @return Liste der vorhandenen {@linkplain AcademicYear}-Objekte nach Jahr
	 *         sortiert oder leere Liste, wenn keine vorhanden sind
	 * @throws DatasetException
	 */
	public List<AcademicYear> getAllAcademicYears() throws DatasetException;

	/**
	 * Gibt den Jahrgang zurück, der den übergebenen int-Wert als Jahr trägt.
	 * Ist kein Jahrgang mit dem übergebenen Wert in der Datenbank vorhanden,
	 * wird {@code null} zurückgegeben. Wird ein Wert kleiner 1 übergeben, wird
	 * eine {@linkplain IllegalArgumentException} geworfen. Tritt bei der
	 * Anfrage ein Fehler auf, wird eine {@linkplain DatasetException} geworfen.
	 * 
	 * @param year
	 *            - das Jahr (1, 2, 3, 4, ...), zu welchem der Jahrgang gesucht
	 *            werden soll
	 * @return den gesuchten Jahrgang oder wenn nicht vorhanden {@code null}
	 * @throws DatasetException
	 */
	public AcademicYear getOneAcademicYear(int year) throws DatasetException;

	/**
	 * Gibt eine Liste aller in der Datenbank vorhandenen
	 * {@linkplain Schoolclass} -Objekte zurück. Sind keine vorhanden wird, eine
	 * leere Liste zurückgegeben. Tritt bei der Anfrage ein Fehler auf, wird
	 * eine {@linkplain DatasetException} geworfen.
	 * 
	 * @return Liste der vorhandenen {@linkplain Schoolclass}-Objekte oder leere
	 *         Liste, wenn keine vorhanden sind
	 * @throws DatasetException
	 */
	public List<Schoolclass> getAllSchoolclasses() throws DatasetException;

	/**
	 * Sucht nach einer Klasse im übergebenen Jahrgang mit dem übergebenen char
	 * als Identifier. Wird eine solche Klasse gefunden, wird sie zurückgegeben,
	 * ansonsten {@code null}.
	 * 
	 * @param academicYear
	 *            - der Jahrgang in dem gesucht werden soll
	 * @param identifier
	 *            - der Identifier der Klasse
	 * @return die gesuchte Klasse oder {@code null}, wenn keine solche
	 *         vorhanden ist
	 * @throws DatasetException
	 */
	public Schoolclass getOneSchoolclass(int academicYear, char identifier)
			throws DatasetException;

	/**
	 * Gibt eine Liste aller in der Datenbank vorhandenen
	 * {@linkplain PedagogicAssistant}-Objekte nach Kürzel sortiert zurück. Sind
	 * keine vorhanden wird, eine leere Liste zurückgegeben. Tritt bei der
	 * Anfrage ein Fehler auf, wird eine {@linkplain DatasetException} geworfen.
	 * 
	 * @return Liste der vorhandenen {@linkplain PedagogicAssistant}-Objekte
	 *         nach Kürzel sortiert oder leere Liste, wenn keine vorhanden sind
	 * @throws DatasetException
	 */
	public List<PedagogicAssistant> getAllPAs() throws DatasetException;

	/**
	 * Gibt eine Liste aller in der Datenbank vorhandenen {@linkplain Teacher}
	 * -Objekte nach Kürzel sortiert zurück. Sind keine vorhanden wird, eine
	 * leere Liste zurückgegeben. Tritt bei der Anfrage ein Fehler auf, wird
	 * eine {@linkplain DatasetException} geworfen.
	 * 
	 * @return Liste der vorhandenen {@linkplain Teacher}-Objekte nach Kürzel
	 *         sortiert oder leere Liste, wenn keine vorhanden sind
	 * @throws DatasetException
	 */
	public List<Teacher> getAllTeachers() throws DatasetException;

	/**
	 * Führt eine Datenbankanfrage aus, die nach einem Mitarbeiter sucht, der
	 * bei ignorierter Groß- und Kleinschreibung exakt das übergebene Kürzel
	 * hat. Wird ein Mitarbeiter mit dem übergebenen Kürzel gefunden, wird
	 * dieser zurückgegeben, ansonsten {@code null}. Tritt bei der Anfrage ein
	 * Fehler auf, wird eine {@linkplain DatasetException} geworfen.
	 * 
	 * @param symbol
	 *            - das Kürzel, mit dem gesucht werden soll
	 * @return der gesuchte Mitarbeiter oder {@code null}, wenn nicht vorhanden
	 * @throws DatasetException
	 */
	public Employee getOneEmployee(String symbol) throws DatasetException;

	/**
	 * Sucht nach Mitarbeitern, deren Kürzel den übergebenen String enthalten.
	 * Groß- und Kleinschreibung wird ignoriert. Tritt bei der Anfrage ein
	 * Fehler auf, wird eine {@linkplain DatasetException} geworfen.
	 * 
	 * @param searchSymbol
	 *            - String, mit dem die Kürzel durchsucht werden sollen
	 * @return eine Liste von Mitarbeitern, deren Kürzel den übergebenen String
	 *         enthalten
	 * @throws DatasetException
	 */
	public List<Employee> searchEmployees(String searchSymbol)
			throws DatasetException;

	/**
	 * Sucht nach Lehrern, deren Kürzel den übergebenen String enthalten. Groß-
	 * und Kleinschreibung wird ignoriert. Tritt bei der Anfrage ein Fehler auf,
	 * wird eine {@linkplain DatasetException} geworfen.
	 * 
	 * @param searchSymbol
	 *            - String, mit dem die Kürzel durchsucht werden sollen
	 * @return eine Liste von Lehrern, deren Kürzel den übergebenen String
	 *         enthalten
	 * @throws DatasetException
	 */
	public List<Teacher> searchTeachers(String searchSymbol)
			throws DatasetException;

	/**
	 * Sucht nach pädagogischen Mitarbeitern, deren Kürzel den übergebenen
	 * String enthalten. Groß- und Kleinschreibung wird ignoriert.Tritt bei der
	 * Anfrage ein Fehler auf, wird eine {@linkplain DatasetException} geworfen.
	 * 
	 * @param searchSymbol
	 *            - String, mit dem die Kürzel durchsucht werden sollen
	 * @return eine Liste von pädagogischen Mitarbeitern, deren Kürzel den
	 *         übergebenen String enthalten
	 * @throws DatasetException
	 */
	public List<PedagogicAssistant> searchPAs(String searchSymbol)
			throws DatasetException;

	/**
	 * Gibt eine Liste aller in der Datenbank vorhandenen {@linkplain Location}
	 * -Objekte nach Namen sortiert zurück. Sind keine vorhanden wird, eine
	 * leere Liste zurückgegeben. Tritt bei der Anfrage ein Fehler auf, wird
	 * eine {@linkplain DatasetException} geworfen.
	 * 
	 * @return Liste der vorhandenen {@linkplain Location}-Objekte nach Namen
	 *         sortiert oder leere Liste, wenn keine vorhanden sind
	 * @throws DatasetException
	 */
	public List<Location> getAllLocations() throws DatasetException;

	/**
	 * Sucht in der Datenbank nach einem Standort mit dem übergebenen Namen.
	 * Existiert kein Standort mit dem übergebenen Namen wird {@code null}
	 * zurückgegeben. Ansonsten der gefundene Standort.
	 * 
	 * @param name
	 *            - Name des gesuchten Standortes
	 * @return den Standort mit dem übergebenen Namen oder {@code null}, wenn
	 *         kein solcher existiert
	 */
	public Location getOneLocation(String name) throws DatasetException;

	/**
	 * Sucht nach einem Raum mit dem übergebenen Namen an einem Standort mit dem
	 * übergebenen Namen. Wird für einen der beiden Parameter {@code null}
	 * übergeben, wirde eine {@linkplain IllegalArgumentException} geworfen.
	 * Existiert der gesuchte Raum wird dieser zurückgegeben, ansonsten
	 * {@code null}. Tritt bei der Anfrage ein Fehler auf, wird eine
	 * {@linkplain DatasetException} geworfen.
	 * 
	 * @param locationName
	 *            - Name des Standortes, an welchem nach dem Raum gesucht werden
	 *            soll
	 * @param roomName
	 *            - Name des Raumes nach dem gesucht werden soll
	 * @return der gesuchte Raum oder {@code null}, wenn kein solcher existiert
	 * @throws DatasetException
	 */
	public Room getOneRoom(String locationName, String roomName)
			throws DatasetException;

	/**
	 * Gibt eine Liste aller in der Datenbank vorhandenen
	 * {@linkplain ActivityType} -Objekte nach Namen sortiert zurück. Sind keine
	 * vorhanden wird, eine leere Liste zurückgegeben. Tritt bei der Anfrage ein
	 * Fehler auf, wird eine {@linkplain DatasetException} geworfen.
	 * 
	 * @return Liste der vorhandenen {@linkplain ActivityType}-Objekte nach
	 *         Namen sortiert oder leere Liste, wenn keine vorhanden sind
	 * @throws DatasetException
	 */
	public List<ActivityType> getAllActivityTypes() throws DatasetException;

	/**
	 * Gibt eine Liste aller in der Datenbank vorhandenen
	 * {@linkplain MeetingType} -Objekte nach Namen sortiert zurück. Sind keine
	 * vorhanden wird, eine leere Liste zurückgegeben. Tritt bei der Anfrage ein
	 * Fehler auf, wird eine {@linkplain DatasetException} geworfen.
	 * 
	 * @return Liste der vorhandenen {@linkplain MeetingType}-Objekte nach Namen
	 *         sortiert oder leere Liste, wenn keine vorhanden sind
	 * @throws DatasetException
	 */
	public List<MeetingType> getAllMeetingTypes() throws DatasetException;

	/**
	 * Gibt eine Liste aller in der Datenbank vorhandenen
	 * {@linkplain LessonType} -Objekte nach Namen sortiert zurück. Sind keine
	 * vorhanden wird, eine leere Liste zurückgegeben. Tritt bei der Anfrage ein
	 * Fehler auf, wird eine {@linkplain DatasetException} geworfen.
	 * 
	 * @return Liste der vorhandenen {@linkplain LessonType}-Objekte nach Namen
	 *         sortiert oder leere Liste, wenn keine vorhanden sind
	 * @throws DatasetException
	 */
	public List<LessonType> getAllLessonTypes() throws DatasetException;

	/**
	 * Gibt eine Liste aller in der Datenbank vorhandenen
	 * {@linkplain ProjectType} -Objekte nach Namen sortiert zurück. Sind keine
	 * vorhanden wird, eine leere Liste zurückgegeben. Tritt bei der Anfrage ein
	 * Fehler auf, wird eine {@linkplain DatasetException} geworfen.
	 * 
	 * @return Liste der vorhandenen {@linkplain ProjectType}-Objekte nach Namen
	 *         sortiert oder leere Liste, wenn keine vorhanden sind
	 * @throws DatasetException
	 */
	public List<ProjectType> getAllProjectTypes() throws DatasetException;

	/**
	 * Sucht nach einem {@linkplain ActivityType} mit dem übergebenen Namen,
	 * wird ein solcher gefunden, wird dieser zurückgegeben, ansonsten
	 * {@code null}.
	 * 
	 * @param name
	 *            - der Name des gesuchten Aktivitätstyps
	 * @return den gesuchten Aktivitätstypen oder {@code nulls}
	 * @throws DatasetException
	 */
	public ActivityType getOneActivityType(String name) throws DatasetException;

	/**
	 * Wird {@code null} übergeben, wird eine
	 * {@linkplain IllegalArgumentException} geworfen. Ansonsten wird in der
	 * Datenbank nach allen Aktivitäten des übergebenen Mitarbeiters gesucht und
	 * diese als Liste zurückgegeben. Tritt dabei ein Fehler auf, wird eine
	 * {@linkplain DatasetException} geworfen.
	 * 
	 * @param employee
	 *            - Mitarbeiter, für den alle Aktivitäten gesucht werden sollen
	 * @return Liste aller Aktivitäten des übergebenen Mitarbeiters, kann auch
	 *         leer sein, wenn der Mitarbeiter an keiner Aktivität teilnimmt
	 * @throws DatasetException
	 */
	public List<Activity> getAllActivities(Employee employee)
			throws DatasetException;

	/**
	 * Wird {@code null} übergeben, wird eine
	 * {@linkplain IllegalArgumentException} geworfen. Ansonsten wird in der
	 * Datenbank nach allen Aktivitäten der übergebenen Schulklasse gesucht und
	 * diese als Liste zurückgegeben. Tritt dabei ein Fehler auf, wird eine
	 * {@linkplain DatasetException} geworfen.
	 * 
	 * @param schoolclass
	 *            - Schulklasse, für die alle Aktivitäten gesucht werden sollen
	 * @return Liste aller Aktivitäten der übergebenen Schulklasse, kann auch
	 *         leer sein, wenn die Schulklasse an keiner Aktivität teilnimmt.
	 * @throws DatasetException
	 */
	public List<Activity> getAllActivities(Schoolclass schoolclass)
			throws DatasetException;

	/**
	 * Wird {@code null} übergeben, wird eine
	 * {@linkplain IllegalArgumentException} geworfen. Ansonsten wird in der
	 * Datenbank nach allen Aktivitäten gesucht, die im übergebenen Raum
	 * stattfinden und diese als Liste zurückgegeben. Tritt dabei ein Fehler
	 * auf, wird eine {@linkplain DatasetException} geworfen.
	 * 
	 * @param room
	 *            - Raum, für welchen alle Aktivitäten gesucht werden sollen
	 * @return Liste aller Aktivitäten, die im übergebenen Raum stattfinden,
	 *         kann auch leer sein
	 * @throws DatasetException
	 */
	public List<Activity> getAllActivities(Room room) throws DatasetException;

	/**
	 * Wird für einen Parameter{@code null} übergeben, wird eine
	 * {@linkplain IllegalArgumentException} geworfen. Ansonsten wird in der
	 * Datenbank nach allen Aktivitäten gesucht, die am übergebenen Wochentag
	 * stattfinden und an welchen der übergebene Mitarbeiter teilnimmt.
	 * 
	 * @param employee
	 *            - der Mitarbeiter, für welchen nach Aktivitäten gesucht werden
	 *            soll
	 * @param weekday
	 *            - der Tag, an welchem diese Aktivitäten liegen sollen
	 * @return Liste aller Aktivitäten, die am übergebenen Tag mit dem
	 *         übergebenen Mitarbeiter als Teilnehmer stattfinden, kann auch
	 *         leer sein
	 * @throws DatasetException
	 */
	public List<Activity> getAllActivities(Employee employee, Weekday weekday)
			throws DatasetException;

	/**
	 * Sucht nach einem Objekt der übergebenen Klasse, welches den übergebenen
	 * Long-Wert als Primärschlüssel besitzt. Exisitert kein solches Objekt,
	 * wird {@code null} zurückgegeben. Tritt dabei ein Fehler auf, wird eine
	 * {@linkplain DatasetException} geworfen.
	 * 
	 * @param clazz
	 *            - Klasse, von welcher das Objekt gesucht werden soll
	 * @param id
	 *            - Primärschlüssel des gesuchten Objekts
	 * @return das gesuchte Objekt oder {@code null}, falls nicht vorhanden
	 * @throws DatasetException
	 */
	public <E> E getById(Class<E> clazz, Long id) throws DatasetException;

	/**
	 * Sucht nach allen Objekten der Klasse {@linkplain TravelTimeList} in der
	 * Datenbank. Da es davon nur ein Objekt geben darf, wird entweder
	 * {@code null} zurückgegeben, wenn keins vorhanden ist und ansonsten das
	 * gefundene {@linkplain TravelTimeList}-Objekt.
	 * 
	 * @return das {@linkplain TravelTimeList}-Objekt oder {@code null}, wenn
	 *         nicht vorhanden
	 * 
	 * @throws DatasetException
	 */
	public TravelTimeList getTravelTimeList() throws DatasetException;
}
