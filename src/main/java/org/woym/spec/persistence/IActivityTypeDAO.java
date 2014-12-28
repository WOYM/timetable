package org.woym.spec.persistence;

import java.util.List;

import org.woym.exceptions.DatasetException;
import org.woym.objects.ActivityType;
import org.woym.objects.LessonType;
import org.woym.objects.MeetingType;
import org.woym.objects.ProjectType;
import org.woym.objects.Room;

/**
 * Dieses Interface beschreibt Methoden, die von einem {@linkplain ActivityType}
 * Data-Access-Object zu implementieren wären.
 * 
 * @author adrian
 *
 */
public interface IActivityTypeDAO {

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
	 * Sucht in der Datenbank nach {@linkplain ActivityType}-Objekten, wo der
	 * übergebene Raum als möglicher Raum angegeben ist.
	 * 
	 * @param room
	 *            - Raum, für den alle Aktivitätstypen gesucht werden sollen
	 * @return Liste aller Aktivitätstypen, bei welchen der übergebene Raum als
	 *         möglicher Raum gespeichert ist
	 * @throws DatasetException
	 */
	public List<ActivityType> getAllActivityTypes(Room room)
			throws DatasetException;

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
	 * {@code null}. Groß- und Kleinschreibung wird ignoriert.
	 * 
	 * @param name
	 *            - der Name des gesuchten Aktivitätstyps
	 * @return den gesuchten Aktivitätstypen oder {@code nulls}
	 * @throws DatasetException
	 */
	public ActivityType getOneActivityType(String name) throws DatasetException;

}
