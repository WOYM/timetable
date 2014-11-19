package org.woym.spec.persistence;

import java.util.List;

import org.woym.exceptions.DatasetException;
import org.woym.objects.Employee;

/**
 * Diese Schnittstelle spezifiziert Methoden für ein Teacher Data Access Object.
 * 
 * @author Adrian
 *
 */
public interface IEmployeeDAO<E extends Employee> {

	/**
	 * Führt eine Datenbankanfrage aus, die nach einem Lehrer sucht, der exakt
	 * das übergebene Kürzel hat. Die Länge der zurückgegebenen Liste sollte
	 * immer 0 oder 1 sein. Tritt bei der Anfrage ein Fehler auf, wird eine
	 * {@linkplain DatasetException} geworfen.
	 * 
	 * @param pSymbol
	 *            - das Kürzel, mit dem gesucht werden soll
	 * @return List&lt;Teacher&gt; - leere Liste, wenn Lehrer nicht vorhanden,
	 *         ansonsten Liste mit einem Element
	 * @throws DatasetException
	 */
	public List<E> getBySymbol(final String pSymbol)
			throws DatasetException;

	/**
	 * Sucht nach Lehrern, deren Kürzel den übergebenen String enthalten. Tritt
	 * bei der Anfrage ein Fehler auf, wird eine {@linkplain DatasetException}
	 * geworfen.
	 * 
	 * @param pSearchSymbol
	 *            - String, mit dem die Kürzel durchsucht werden sollen
	 * @return List&lt;Teacher&gt; - leere Liste, wenn kein Lehrer gefunden
	 *         wird, ansonsten Liste mit den Lehrern die durch die Suche
	 *         gefunden wurden
	 * @throws DatasetException
	 */
	public List<E> searchForStaff(final String pSearchSymbol)
			throws DatasetException;

}
