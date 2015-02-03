package org.woym.persistence.spec;

import java.util.Date;
import java.util.List;

import org.woym.common.exceptions.DatasetException;
import org.woym.common.objects.Activity;
import org.woym.common.objects.CompoundLesson;
import org.woym.common.objects.Employee;
import org.woym.common.objects.EmployeeTimePeriods;
import org.woym.common.objects.Lesson;
import org.woym.common.objects.LessonType;
import org.woym.common.objects.Location;
import org.woym.common.objects.Meeting;
import org.woym.common.objects.MeetingType;
import org.woym.common.objects.Room;
import org.woym.common.objects.Schoolclass;
import org.woym.common.objects.TimePeriod;
import org.woym.common.objects.Weekday;

/**
 * Dieses Interface beschreibt Methoden, die von einem {@linkplain Activity}
 * Data-Access-Object zu implementieren wären.
 * 
 * @author adrian
 *
 */
public interface IActivityDAO {

	/**
	 * Gibt eine Liste aller vorhandenen Aktivitäten zurück. Wirft eine
	 * {@linkplain DatasetException}, wenn dabei ein Fehler auftritt.
	 * 
	 * @return Liste aller in der der Datenbank vorhandenen Aktivitäten
	 * @throws DatasetException
	 */
	public List<Activity> getAllActivities() throws DatasetException;

	/**
	 * Gibt eine Liste aller für den übergebenen Wochentag vorhanden Aktivitäten
	 * zurück.
	 * 
	 * @param weekday
	 *            - der Wochentag, für welchen alle Aktivitäten erwartet werden
	 * @return Liste aller Aktivitäten für den übergebenen Wochentag
	 * @throws DatasetException
	 */
	public List<Activity> getAllActivities(Weekday weekday)
			throws DatasetException;

	/**
	 * Gibt eine Liste aller Aktivitäten zurück, deren Start- und Endzeit nicht
	 * zwischen der übergebenen Start- und Endzeit liegen.
	 * 
	 * @param startTime
	 *            - Startzeit
	 * @param endTime
	 *            - Endzeit
	 * @return Liste aller Aktivitäten zurück, deren Start- und Endzeit nicht
	 *         zwischen der übergebenen Start- und Endzeit liegen
	 * @throws DatasetException
	 */
	public List<Activity> getAllActivitiesNotBetween(Date startTime,
			Date endTime) throws DatasetException;

	/**
	 * Wird {@code null} übergeben, wird eine
	 * {@linkplain IllegalArgumentException} geworfen. Ansonsten wird in der
	 * Datenbank nach allen Aktivitäten des übergebenen Mitarbeiters gesucht und
	 * diese als Liste zurückgegeben. Tritt dabei ein Fehler auf, wird eine
	 * {@linkplain DatasetException} geworfen.
	 * 
	 * @param employee
	 *            - Mitarbeiter, für den alle Aktivitäten gesucht werden sollen
	 * @param refreshCache
	 *            - {@code true}, wenn die Objekte im Cache aktualisiert werden
	 *            sollen
	 * @return Liste aller Aktivitäten des übergebenen Mitarbeiters, kann auch
	 *         leer sein, wenn der Mitarbeiter an keiner Aktivität teilnimmt
	 * @throws DatasetException
	 */
	public List<Activity> getAllActivities(Employee employee,
			boolean refreshCache) throws DatasetException;

	/**
	 * Wird {@code null} übergeben, wird eine
	 * {@linkplain IllegalArgumentException} geworfen. Ansonsten wird in der
	 * Datenbank nach allen Aktivitäten der übergebenen Schulklasse gesucht und
	 * diese als Liste zurückgegeben. Tritt dabei ein Fehler auf, wird eine
	 * {@linkplain DatasetException} geworfen.
	 * 
	 * @param schoolclass
	 *            - Schulklasse, für die alle Aktivitäten gesucht werden sollen
	 * @param refreshCache
	 *            - {@code true}, wenn die Objekte im Cache aktualisiert werden
	 *            sollen
	 * @return Liste aller Aktivitäten der übergebenen Schulklasse, kann auch
	 *         leer sein, wenn die Schulklasse an keiner Aktivität teilnimmt.
	 * @throws DatasetException
	 */
	public List<Activity> getAllActivities(Schoolclass schoolclass,
			boolean refreshCache) throws DatasetException;

	/**
	 * Wird {@code null} übergeben, wird eine
	 * {@linkplain IllegalArgumentException} geworfen. Ansonsten wird in der
	 * Datenbank nach allen Aktivitäten gesucht, die im übergebenen Raum
	 * stattfinden und diese als Liste zurückgegeben. Tritt dabei ein Fehler
	 * auf, wird eine {@linkplain DatasetException} geworfen.
	 * 
	 * @param room
	 *            - Raum, für welchen alle Aktivitäten gesucht werden sollen
	 * @param refreshCache
	 *            - {@code true}, wenn die Objekte im Cache aktualisiert werden
	 *            sollen
	 * @return Liste aller Aktivitäten, die im übergebenen Raum stattfinden,
	 *         kann auch leer sein
	 * @throws DatasetException
	 */
	public List<Activity> getAllActivities(Room room, boolean refreshCache) throws DatasetException;

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
	 * Gibt eine Liste aller Aktivitäten für den übergebenen Mitarbeiter zurück,
	 * die mit dem übergebenen Zeitraum kollidieren. Wobei es nicht als
	 * Kollision gewertet wird, wenn der Startzeitpunkt einer Aktivität des
	 * Mitarbeiters gleich dem Endzeitpunkt der übergebenen Aktivität oder der
	 * Endzeitpunkt einer Aktivität des Mitarbeiters gleich dem Startzeitpunkt
	 * der übergebenen Aktivität bzw. der Startzeitpunkt der übergebenen
	 * Aktivität gleich dem Endzeitpunkt einer Aktivität des Mitarbeiters oder
	 * der Endzeitpunkt der übergebenen Aktivität gleich dem Startzeitpunkt
	 * einer Aktivität des Mitarbeiters ist.
	 * 
	 * @param employee
	 *            - Mitarbeiter, für welchen kollidierende Aktivitäten gesucht
	 *            werden sollen
	 * @param timePeriod
	 *            - Zeitraum, für welchen kollidierende Aktivitäten gesucht
	 *            werden sollen
	 * @return eine Liste der mit dem übergebenen Zeitraum kollidiernden
	 *         Aktivitäten des übergebenen Mitarbeiters. Eine leere Liste, wenn
	 *         keine Kollisionen vorliegen.
	 * @throws DatasetException
	 */
	public List<Activity> getAllActivities(Employee employee,
			TimePeriod timePeriod) throws DatasetException;

	/**
	 * Gibt eine Liste aller Aktivitäten für die übergebene Schulklasse zurück,
	 * die mit dem übergebenen Zeitraum kollidieren. Wobei es nicht als
	 * Kollision gewertet wird, wenn der Startzeitpunkt einer Aktivität der
	 * Schulklasse gleich dem Endzeitpunkt der übergebenen Aktivität oder der
	 * Endzeitpunkt einer Aktivität der Schulklasse gleich dem Startzeitpunkt
	 * der übergebenen Aktivität bzw. der Startzeitpunkt der übergebenen
	 * Aktivität gleich dem Endzeitpunkt einer Aktivität der Schulklasse oder
	 * der Endzeitpunkt der übergebenen Aktivität gleich dem Startzeitpunkt
	 * einer Aktivität der Schulklasse ist.
	 * 
	 * @param schoolclass
	 *            - Schulklasse, für welchen kollidierende Aktivitäten gesucht
	 *            werden sollen
	 * @param timePeriod
	 *            - Zeitraum, für welchen kollidierende Aktivitäten gesucht
	 *            werden sollen
	 * @return eine Liste der mit dem übergebenen Zeitraum kollidiernden
	 *         Aktivitäten der übergebenen Schulklasse. Eine leere Liste, wenn
	 *         keine Kollisionen vorliegen.
	 * @throws DatasetException
	 */
	public List<Activity> getAllActivities(Schoolclass schoolclass,
			TimePeriod timePeriod) throws DatasetException;

	/**
	 * Gibt eine Liste aller Aktivitäten für den übergebenen Raum zurück, die
	 * mit dem übergebenen Zeitraum kollidieren. Wobei es nicht als Kollision
	 * gewertet wird, wenn der Startzeitpunkt einer Aktivität des Raumes gleich
	 * dem Endzeitpunkt der übergebenen Aktivität oder der Endzeitpunkt einer
	 * Aktivität des Raumes gleich dem Startzeitpunkt der übergebenen Aktivität
	 * bzw. der Startzeitpunkt der übergebenen Aktivität gleich dem Endzeitpunkt
	 * einer Aktivität des Raumes oder der Endzeitpunkt der übergebenen
	 * Aktivität gleich dem Startzeitpunkt einer Aktivität des Raumes ist.
	 * 
	 * @param room
	 *            - Raum, für welchen kollidierende Aktivitäten gesucht werden
	 *            sollen
	 * @param timePeriod
	 *            - Zeitraum, für welchen kollidierende Aktivitäten gesucht
	 *            werden sollen
	 * @return eine Liste der mit dem übergebenen Zeitraum kollidiernden
	 *         Aktivitäten des übergebenen Raumes. Eine leere Liste, wenn keine
	 *         Kollisionen vorliegen.
	 * @throws DatasetException
	 */
	public List<Activity> getAllActivities(Room room, TimePeriod timePeriod)
			throws DatasetException;

	/**
	 * Gibt eine Liste aller in der Datenbank vorhandenen
	 * {@linkplain CompoundLesson}-Objekte zurück, wo das übergebene
	 * {@linkplain LessonType}-Objekt Teil ist. Tritt dabei ein Fehler auf, wird
	 * eine {@linkplain DatasetException} geworfen.
	 * 
	 * @param lessonType
	 *            - {@linkplain LessonType}-Objekt, das Teil der CompoundLessons
	 *            sein soll
	 * @return Liste der {@linkplain CompoundLesson}-Objekte, wo das übergebene
	 *         {@linkplain LessonType}-Objekt Teil ist.
	 * @throws DatasetException
	 */
	public List<CompoundLesson> getAllCompoundLessons(LessonType lessonType)
			throws DatasetException;

	/**
	 * Gibt eine Liste aller in der Datenbank vorhandenen {@linkplain Lesson}
	 * -Objekte zurück, wo das übergebene {@linkplain LessonType}-Objekt Teil
	 * ist. Tritt dabei ein Fehler auf, wird eine {@linkplain DatasetException}
	 * geworfen.
	 * 
	 * @param lessonType
	 *            - {@linkplain LessonType}-Objekt, das Teil der
	 *            {@linkplain Lesson}-Objekte sein soll
	 * @return Liste der {@linkplain Lesson}-Objekte, wo das übergebene
	 *         {@linkplain LessonType}-Objekt Teil ist.
	 * @throws DatasetException
	 */
	public List<Lesson> getAllLessons(LessonType lessonType)
			throws DatasetException;

	/**
	 * Gibt eine Liste aller in der Datenbank vorhandenen {@linkplain Meeting}
	 * -Objekte zurück, wo das übergebene {@linkplain MeetingType}-Objekt Teil
	 * ist. Tritt dabei ein Fehler auf, wird eine {@linkplain DatasetException}
	 * geworfen.
	 * 
	 * @param meetingType
	 *            - {@linkplain MeetingType}-Objekt, das Teil der
	 *            {@linkplain Meeting}-Objekte sein soll
	 * @return Liste der {@linkplain Meeting}-Objekte, wo das übergebene
	 *         {@linkplain MeetingType}-Objekt Teil ist.
	 * @throws DatasetException
	 */
	public List<Meeting> getAllMeetings(MeetingType meetingType)
			throws DatasetException;

	/**
	 * Gibt eine Liste aller {@linkplain EmployeeTimePeriods}-Objekte von
	 * Aktivitäten für den übergebenen {@linkplain Employee} zurück, bei welchen
	 * weitere Mitarbeiter außer dem übergebenen {@linkplain Employee}
	 * teilnehmen.
	 * 
	 * @param employee
	 *            - {@linkplain Employee}, für welchen die
	 *            {@linkplain EmployeeTimePeriods}-Objekte erwartet werden
	 * @return Liste aller {@linkplain EmployeeTimePeriods}-Objekte, des
	 *         übergebenen {@linkplain Employee} von Aktivitäten, an welchen er
	 *         nicht als einziger Mitarbeiter teilnimmt
	 * @throws DatasetException
	 */
	public List<EmployeeTimePeriods> getEmployeeTimePeriods(Employee employee)
			throws DatasetException;

	/**
	 * Summiert die Dauer aller {@linkplain Lesson}-Objekte des übergebenen
	 * {@linkplain LessonType}, an welchen der übergebene {@linkplain Employee}
	 * und die übergebene {@linkplain Schoolclass} teilnehmen. Tritt dabei ein
	 * Fehler auf, wird eine {@linkplain DatasetException} geworfen.
	 * 
	 * @param employee
	 *            - der Mitarbeiter
	 * @param schoolclass
	 *            - die Schulklasse
	 * @param lessonType
	 *            - der Unterrichtstyp
	 * @return Summe der Dauer der Aktivitäten als Long - Wert
	 * @throws DatasetException
	 */
	public Long sumLessonDuration(Employee employee, Schoolclass schoolclass,
			LessonType lessonType) throws DatasetException;

	/**
	 * Gibt eine Aktivitität zurück, an welcher der übergebene Mitarbeiter
	 * teilnimmt. Die Aktivität findet am selben Tag wie dem des übergebenen
	 * {@linkplain TimePeriod}-Objektes statt und eine Endzeit der
	 * {@linkplain EmployeeTimePeriods}-Objekte besitzt die geringste Differenz
	 * zur Startzeit des übergebenen {@linkplain TimePeriod} -Objektes. Zudem
	 * findet die Aktivität an einem Standort ungleich dem übergebenen
	 * {@linkplain Location}-Objekt statt. Tritt bei der Datenbankanfrage ein
	 * Fehler auf, wird eine {@linkplain DatasetException} geworfen.
	 * 
	 * @param employee
	 *            - der Mitarbeiter
	 * @param timePeriod
	 *            - der Zeitraum
	 * @param location
	 *            - der Standort
	 * @return gibt eine Aktivität mit oben beschriebenen Eigenschaften oder
	 *         {@code null} zurück, falls keine solche existiert
	 * @throws DatasetException
	 */
	public Activity getFirstActivityBefore(Employee employee,
			TimePeriod timePeriod, Location location) throws DatasetException;

	/**
	 * Gibt eine Aktivitität zurück, an welcher der übergebene Mitarbeiter
	 * teilnimmt. Die Aktivität findet am selben Tag wie dem des übergebenen
	 * {@linkplain TimePeriod}-Objektes statt und eine Startzeit der
	 * {@linkplain EmployeeTimePeriods}-Objekte besitzt die geringste Differenz
	 * zur Endzeit des übergebenen {@linkplain TimePeriod} -Objektes. Zudem
	 * findet die Aktivität an einem Standort ungleich dem übergebenen
	 * {@linkplain Location}-Objekt statt. Tritt bei der Datenbankanfrage ein
	 * Fehler auf, wird eine {@linkplain DatasetException} geworfen.
	 * 
	 * @param employee
	 *            - der Mitarbeiter
	 * @param timePeriod
	 *            - der Zeitraum
	 * @param location
	 *            - der Standort
	 * @return gibt eine Aktivität mit oben beschriebenen Eigenschaften oder
	 *         {@code null} zurück, falls keine solche existiert
	 * @throws DatasetException
	 */
	public Activity getFirstActivityAfter(Employee employee,
			TimePeriod timePeriod, Location location) throws DatasetException;

	/**
	 * Gibt alle Aktivitäten des übergebenen Mitarbeiters zurück, die (am selben
	 * Tag) vor dem übergebenen {@linkplain TimePeriod}-Objekt. Die Aktivitäten
	 * werden nach Endzeit sortiert. Tritt dabei ein Fehler auf, wird eine
	 * {@linkplain DatasetException} geworfen.
	 * 
	 * @param employee
	 *            - der Mitarbeiter, für welchen die Aktivitäten gesucht werden
	 * @param timePeriod
	 *            - der Zeitraum, vor welchem die Aktivitäten liegen sollen
	 * @return Liste aller vor dem übergebenen Zeitraum liegenden Aktivitäten,
	 *         an denen der übergebene Mitarbeiter teilnimmt
	 * @throws DatasetException
	 */
	public List<Activity> getAllActivitiesBefore(Employee employee,
			TimePeriod timePeriod) throws DatasetException;

	/**
	 * Gibt alle Aktivitäten des übergebenen Mitarbeiters zurück, die (am selben
	 * Tag) nach dem übergebenen {@linkplain TimePeriod}-Objekt. Die Aktivitäten
	 * werden nach Startzeit sortiert. Tritt dabei ein Fehler auf, wird eine
	 * {@linkplain DatasetException} geworfen.
	 * 
	 * @param employee
	 *            - der Mitarbeiter, für welchen die Aktivitäten gesucht werden
	 * @param timePeriod
	 *            - der Zeitraum, nach welchem die Aktivitäten liegen sollen
	 * @return Liste aller nach dem übergebenen Zeitraum liegenden Aktivitäten,
	 *         an denen der übergebene Mitarbeiter teilnimmt
	 * @throws DatasetException
	 */
	public List<Activity> getAllActivitiesAfter(Employee employee,
			TimePeriod timePeriod) throws DatasetException;

	/**
	 * Gibt eine Aktivitität zurück, an welcher die übergebene Schulklasse
	 * teilnimmt. Die Aktivität findet am selben Tag wie dem des übergebenen
	 * {@linkplain TimePeriod}-Objektes statt und die Endzeit der Aktivität
	 * besitzt die geringste Differenz zur Startzeit des übergebenen
	 * {@linkplain TimePeriod} -Objektes. Zudem findet die Aktivität an einem
	 * Standort ungleich dem übergebenen {@linkplain Location}-Objekt statt.
	 * Tritt bei der Datenbankanfrage ein Fehler auf, wird eine
	 * {@linkplain DatasetException} geworfen.
	 * 
	 * @param schoolclass
	 *            - die Schulklasse
	 * @param timePeriod
	 *            - der Zeitraum
	 * @param location
	 *            - der Standort
	 * @return gibt eine Aktivität mit oben beschriebenen Eigenschaften oder
	 *         {@code null} zurück, falls keine solche existiert
	 * @throws DatasetException
	 */
	public Activity getFirstActivityBefore(Schoolclass schoolclass,
			TimePeriod timePeriod, Location location) throws DatasetException;

	/**
	 * Gibt eine Aktivitität zurück, an welcher die übergebene Schulklasse
	 * teilnimmt. Die Aktivität findet am selben Tag wie dem des übergebenen
	 * {@linkplain TimePeriod}-Objektes statt und die Startzeit der Aktivität
	 * besitzt die geringste Differenz zur Endzeit des übergebenen
	 * {@linkplain TimePeriod} -Objektes. Zudem findet die Aktivität an einem
	 * Standort ungleich dem übergebenen {@linkplain Location}-Objekt statt.
	 * Tritt bei der Datenbankanfrage ein Fehler auf, wird eine
	 * {@linkplain DatasetException} geworfen.
	 * 
	 * @param schoolclass
	 *            - die Schulklasse
	 * @param timePeriod
	 *            - der Zeitraum
	 * @param location
	 *            - der Standort
	 * @return gibt eine Aktivität mit oben beschriebenen Eigenschaften oder
	 *         {@code null} zurück, falls keine solche existiert
	 * @throws DatasetException
	 */
	public Activity getFirstActivityAfter(Schoolclass schoolclass,
			TimePeriod timePeriod, Location location) throws DatasetException;

}
