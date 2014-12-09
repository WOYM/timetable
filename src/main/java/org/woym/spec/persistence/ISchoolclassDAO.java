package org.woym.spec.persistence;

import org.woym.exceptions.DatasetException;
import org.woym.objects.Schoolclass;

/**
 * Diese Schnittstelle beschreibt Methoden, die von einem Schoolclass Data
 * Access Object implementiert werden müssen.
 * 
 * @author Adrian
 *
 */
public interface ISchoolclassDAO extends IGenericDAO<Schoolclass> {

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
	public Schoolclass getOne(int academicYear, char identifier)
			throws DatasetException;
}
