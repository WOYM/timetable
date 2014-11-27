package org.woym.spec.persistence;

import java.util.List;

import org.woym.exceptions.DatasetException;
import org.woym.objects.Employee;

/**
 * Diese Schnittstelle spezifiziert Methoden für ein Employee Data Access
 * Object.
 * 
 * @author Adrian
 *
 */
public interface IEmployeeDAO<E extends Employee> extends IGenericDAO<E> {

	/**
	 * Führt eine Datenbankanfrage aus, die nach einem Mitarbeiter generischen
	 * Typs sucht, der exakt das übergebene Kürzel hat. Wird ein Mitarbeiter mit
	 * dem übergebenen Kürzel gefunden, wird dieser zurückgegeben, ansonsten
	 * {@code null}. Tritt bei der Anfrage ein Fehler auf, wird eine
	 * {@linkplain DatasetException} geworfen.
	 * 
	 * @param symbol
	 *            - das Kürzel, mit dem gesucht werden soll
	 * @return der gesuchte Mitarbeiter oder {@code null}, wenn nicht vorhanden
	 * @throws DatasetException
	 */
	public E getOne(final String symbol) throws DatasetException;

	/**
	 * Sucht nach Mitarbeitern generischen Typs, deren Kürzel den übergebenen
	 * String enthalten. Tritt bei der Anfrage ein Fehler auf, wird eine
	 * {@linkplain DatasetException} geworfen.
	 * 
	 * @param searchSymbol
	 *            - String, mit dem die Kürzel durchsucht werden sollen
	 * @return eine Liste von Mitarbeitern des generischen Typs, deren Kürzel
	 *         den übergebenen String enthalten
	 * @throws DatasetException
	 */
	public List<E> search(final String searchSymbol) throws DatasetException;
}
