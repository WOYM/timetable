package org.woym.spec.persistence;

import java.util.List;

import org.woym.exceptions.DatasetException;
import org.woym.objects.Employee;
import org.woym.objects.PedagogicAssistant;
import org.woym.objects.Teacher;

/**
 * Diese Schnittstelle spezifiziert Methoden für ein Employee Data Access
 * Object.
 * 
 * @author Adrian
 *
 */
public interface IEmployeeDAO extends IAbstractDAO<Employee> {

	
	//TODO: Javadoc
	public List<Teacher> getAllTeachers() throws DatasetException;

	//TODO: Javadoc
	public List<PedagogicAssistant> getAllPAs() throws DatasetException;
	
	
	/**
	 * Führt eine Datenbankanfrage aus, die nach einem Lehrer sucht, der exakt
	 * das übergebene Kürzel hat. Wird ein Lehrer mit dem übergebenen Kürzel
	 * gefunden, wird dieser zurückgegeben, ansonsten {@code null}. Tritt bei
	 * der Anfrage ein Fehler auf, wird eine {@linkplain DatasetException}
	 * geworfen.
	 * 
	 * @param symbol
	 *            - das Kürzel, mit dem gesucht werden soll
	 * @return der gesuchte Lehrer oder {@code null}, wenn nicht vorhanden
	 * @throws DatasetException
	 */
	public Teacher getTeacher(final String symbol) throws DatasetException;

	/**
	 * Führt eine Datenbankanfrage aus, die nach einem päadagogischen
	 * Mitarbeiter sucht, der exakt das übergebene Kürzel hat. Wird ein päd.
	 * Mitarbeiter mit dem übergebenen Kürzel gefunden, wird dieser
	 * zurückgegeben, ansonsten {@code null}. Tritt bei der Anfrage ein Fehler
	 * auf, wird eine {@linkplain DatasetException} geworfen.
	 * 
	 * @param symbol
	 *            - das Kürzel, mit dem gesucht werden soll
	 * @return der gesuchte päd. Mitarbeiter oder {@code null}, wenn nicht
	 *         vorhanden
	 * @throws DatasetException
	 */
	public PedagogicAssistant getPedagogicAssistant(final String symbol)
			throws DatasetException;

	/**
	 * Sucht nach Lehrern, deren Kürzel den übergebenen String enthalten. Tritt
	 * bei der Anfrage ein Fehler auf, wird eine {@linkplain DatasetException}
	 * geworfen.
	 * 
	 * @param searchSymbol
	 *            - String, mit dem die Kürzel durchsucht werden sollen
	 * @return List&lt;E&gt; - leere Liste, wenn keine Lehrer gefunden werden,
	 *         ansonsten Liste mit den Lehrern, die durch die Suche gefunden
	 *         wurden
	 * @throws DatasetException
	 */
	public List<Teacher> searchForTeachers(final String searchSymbol)
			throws DatasetException;

	/**
	 * Sucht nach pädagogischen Mitarbeitern, deren Kürzel den übergebenen
	 * String enthalten. Tritt bei der Anfrage ein Fehler auf, wird eine
	 * {@linkplain DatasetException} geworfen.
	 * 
	 * @param searchSymbol
	 *            - String, mit dem die Kürzel durchsucht werden sollen
	 * @return List&lt;E&gt; - leere Liste, wenn keine päd. Mitarbeiter gefunden
	 *         werden, ansonsten Liste mit den päd. Mitarbeitern, die durch die
	 *         Suche gefunden wurden
	 * @throws DatasetException
	 */
	public List<PedagogicAssistant> searchForPAs(final String searchSymbol)
			throws DatasetException;
}
