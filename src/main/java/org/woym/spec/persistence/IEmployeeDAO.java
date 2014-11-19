package org.woym.spec.persistence;

import java.util.List;

import org.woym.exceptions.DatasetException;
import org.woym.objects.Employee;

/**
 * Diese Schnittstelle spezifiziert Methoden für ein Employee Data Access Object.
 * 
 * @author Adrian
 *
 */
public interface IEmployeeDAO<E extends Employee> extends IAbstractDAO<E> {

	/**
	 * Führt eine Datenbankanfrage aus, die nach einem Mitarbeiter sucht, der
	 * exakt das übergebene Kürzel hat. Die Länge der zurückgegebenen Liste
	 * sollte immer 0 oder 1 sein. Tritt bei der Anfrage ein Fehler auf, wird
	 * eine {@linkplain DatasetException} geworfen.
	 * 
	 * @param symbol
	 *            - das Kürzel, mit dem gesucht werden soll
	 * @return List&lt;E&gt; - leere Liste, wenn kein Mitarbeiter gefunden,
	 *         ansonsten Liste mit genau einem Mitarbeiter
	 * @throws DatasetException
	 */
	public List<E> getBySymbol(final String symbol) throws DatasetException;

	/**
	 * Sucht nach Mitarbeitern, deren Kürzel den übergebenen String enthalten.
	 * Tritt bei der Anfrage ein Fehler auf, wird eine
	 * {@linkplain DatasetException} geworfen.
	 * 
	 * @param searchSymbol
	 *            - String, mit dem die Kürzel durchsucht werden sollen
	 * @return List&lt;E&gt; - leere Liste, wenn keine Mitarbeiter gefunden
	 *         werden, ansonsten Liste mit den Mitarbeitern, die durch die Suche
	 *         gefunden wurden
	 * @throws DatasetException
	 */
	public List<E> searchForEmployees(final String searchSymbol)
			throws DatasetException;

}
