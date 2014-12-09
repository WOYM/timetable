package org.woym.spec.persistence;

import java.util.List;

import org.woym.exceptions.DatasetException;
import org.woym.objects.ActivityType;
import org.woym.objects.LessonType;
import org.woym.objects.MeetingType;
import org.woym.objects.ProjectType;

public interface IActivityTypeDAO extends IGenericDAO<ActivityType> {

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
	public ActivityType getOne(String name) throws DatasetException;

	/**
	 * Gibt eine Liste mit allen in der Datenbank vorhandenen
	 * {@linkplain LessonType}-Objekten zurück. Sind keine vorhanden, wird eine
	 * leere Liste zurückgegeben. Tritt bei der Anfrage ein Fehler auf, wird
	 * eine {@linkplain DatasetException} geworfen.
	 * 
	 * @return Liste mit allen in der Datenbank vorhandenen
	 *         {@linkplain LessonType}-Objekten oder leere Liste, wenn keine
	 *         vorhanden
	 * @throws DatasetException
	 */
	public List<LessonType> getAllLessonTypes() throws DatasetException;

	/**
	 * Gibt eine Liste mit allen in der Datenbank vorhandenen
	 * {@linkplain ProjectType}-Objekten zurück. Sind keine vorhanden, wird eine
	 * leere Liste zurückgegeben. Tritt bei der Anfrage ein Fehler auf, wird
	 * eine {@linkplain DatasetException} geworfen.
	 * 
	 * @return Liste mit allen in der Datenbank vorhandenen
	 *         {@linkplain ProjectType}-Objekten oder leere Liste, wenn keine
	 *         vorhanden
	 * @throws DatasetException
	 */
	public List<ProjectType> getAllProjectTypes() throws DatasetException;

	/**
	 * Gibt eine Liste mit allen in der Datenbank vorhandenen
	 * {@linkplain MeetingType}-Objekten zurück. Sind keine vorhanden, wird eine
	 * leere Liste zurückgegeben. Tritt bei der Anfrage ein Fehler auf, wird
	 * eine {@linkplain DatasetException} geworfen.
	 * 
	 * @return Liste mit allen in der Datenbank vorhandenen
	 *         {@linkplain MeetingType}-Objekten oder leere Liste, wenn keine
	 *         vorhanden
	 * @throws DatasetException
	 */
	public List<MeetingType> getAllMeetingTypes() throws DatasetException;
}
