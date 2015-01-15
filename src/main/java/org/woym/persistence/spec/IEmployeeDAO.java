package org.woym.persistence.spec;

import java.util.List;

import org.woym.common.exceptions.DatasetException;
import org.woym.common.objects.ActivityType;
import org.woym.common.objects.Employee;
import org.woym.common.objects.PedagogicAssistant;
import org.woym.common.objects.Teacher;

/**
 * Dieses Interface beschreibt Methoden, die von einem {@linkplain Employee}
 * Data-Access-Object zu implementieren wären.
 * 
 * @author adrian
 *
 */
public interface IEmployeeDAO {

	/**
	 * Gibt eine Liste aller Mitarbeiter zurück, bei welchen das übergebene
	 * {@linkplain ActivityType}-Objekt als möglicher Stundeninhalt gespeichert
	 * ist. Tritt dabei ein Fehler auf, wird eine {@linkplain DatasetException}
	 * geworfen.
	 * 
	 * @param activityType
	 *            - {@linkplain ActivityType}-Objekt, welches Teil des Lehrers
	 *            sein soll
	 * @return Liste aller {@linkplain Employee}-Objekte, von welchen das
	 *         übergebene {@linkplain ActivityType}-Objekt Teil ist
	 * @throws DatasetException
	 */
	public List<Employee> getAllEmployees(ActivityType activityType)
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
}
