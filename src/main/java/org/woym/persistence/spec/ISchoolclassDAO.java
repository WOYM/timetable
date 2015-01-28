package org.woym.persistence.spec;

import java.util.List;

import org.woym.common.exceptions.DatasetException;
import org.woym.common.objects.Employee;
import org.woym.common.objects.Room;
import org.woym.common.objects.Schoolclass;
import org.woym.common.objects.Teacher;

/**
 * Dieses Interface beschreibt Methoden, die von einem {@linkplain Schoolclass}
 * Data-Access-Object zu implementieren wären.
 * 
 * @author adrian
 *
 */
public interface ISchoolclassDAO {

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
	 * Gibt eine Liste aller Schulklassen zurück, von welchen der übergebene
	 * Lehrer der Klassenlehrer ist. Tritt dabei ein Fehler auf, wird eine
	 * {@linkplain DatasetException} geworfen.
	 * 
	 * @param teacher
	 *            - Lehrer, für welchen alle Schulklassen gesucht werden sollen
	 * @return Liste aller Schulklassen, wo der übergebene Lehrer Klassenlehrer
	 *         ist
	 * @throws DatasetException
	 */
	public List<Schoolclass> getAllSchoolclasses(Teacher teacher)
			throws DatasetException;

	/**
	 * Gibt eine Liste aller Schulklassen zurück, mit welchen der übergebene
	 * Mitarbeiter gemeinsame Aktivitäten besitzt. Tritt dabei ein Fehler auf,
	 * wird eine {@linkplain DatasetException} geworfen.
	 * 
	 * @param employee
	 *            - Mitarbeiter, für den alle Schulklassen gesucht werden
	 *            sollen, mit welchen er gemeinsame Aktivitäten besitzt
	 * @return Liste aller Schulklassen, mit welchen der übergebene Mitarbeiter
	 *         gemeinsame Aktivitäten besitzt. Kann auch leer sein
	 * @throws DatasetException
	 */
	public List<Schoolclass> getAllSchoolclasses(Employee employee)
			throws DatasetException;

	/**
	 * Gibt eine Liste aller Schulklassen zurück, die nicht Teil eines
	 * Klassenteams sind. Tritt dabei ein Fehler auf, wird eine
	 * {@linkplain DatasetException} geworfen.
	 * 
	 * @return Liste aller Schulklassen, die nicht Teil eines Klassenteams sind
	 * @throws DatasetException
	 */
	public List<Schoolclass> getAllSchoolclassesWithoutClassteam()
			throws DatasetException;

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
	 * Gibt eine Schulklasse zurück, welche den übergebenen Raum als Klassenraum
	 * hat oder {@code null}, falls keine Schulklasse diesen Raum als
	 * Klassenraum hat.
	 * 
	 * @param room
	 *            - der Raum, für welchen die Schulklasse gesucht wird, welche
	 *            dort ihren Klassenraum hat
	 * @return die Schulklasse, welche den übergebenen Raum als Klassenraum hat
	 *         oder {@code null}, falls keine Klasse ihn als Klassenraum hat
	 * @throws DatasetException
	 */
	public Schoolclass getOneSchoolclass(Room room) throws DatasetException;
}
