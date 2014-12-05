package org.woym.spec.persistence;

import java.util.List;

import org.woym.exceptions.DatasetException;
import org.woym.objects.Activity;
import org.woym.objects.Employee;
import org.woym.objects.Schoolclass;

/**
 * Diese Schnittstelle beschreibt Methoden, die von einem Activity Data Access
 * Object zu implementieren sind.
 * 
 * @author Adrian
 *
 */
public interface IActivityDAO extends IGenericDAO<Activity> {

	/**
	 * Wird {@code null} übergeben, wird eine {@linkplaien
	 * IllegalArgumentException} geworfen. Ansonsten wird in der Datenbank nach
	 * allen Aktivitäten des übergebenen Mitarbeiters gesucht und diese als
	 * Liste zurückgegeben. Tritt dabei ein Fehler auf, wird eine
	 * {@linkplain DatasetException} geworfen.
	 * 
	 * @param employee
	 *            - Mitarbeiter, für den alle Aktivitäten gesucht werden sollen
	 * @return Liste aller Aktivitäten des übergebenen Mitarbeiters, kann auch
	 *         leer sein, wenn der Mitarbeiter an keiner Aktivität teilnimmt
	 * @throws DatasetException
	 */
	public List<Activity> getAll(Employee employee) throws DatasetException;

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
	public List<Activity> getAll(Schoolclass schoolclass)
			throws DatasetException;
}
