package org.woym.spec.persistence;

import org.woym.exceptions.DatasetException;
import org.woym.objects.AcademicYear;

/**
 * Diese Schnittstelle beschreibt Methoden, die von einem AcademicYear Data
 * Access Object zu implementieren sind.
 * 
 * @author Adrian
 *
 */
public interface IAcademicYearDAO extends IGenericDAO<AcademicYear> {

	/**
	 * Gibt den Jahrgang zurück, der den übergebenen int-Wert als Jahr trägt.
	 * Ist kein Jahrgang mit dem übergebenen Wert in der Datenbank vorhanden,
	 * wird {@code null} zurückgegeben. Wird ein Wert kleiner 1 übergeben, wird
	 * eine {@linkplain IllegalArgumentException} geworfen. Tritt bei der Anfrage ein
	 * Fehler auf, wird eine {@linkplain DatasetException} geworfen.
	 * 
	 * @param year
	 *            - das Jahr (1, 2, 3, 4, ...), zu welchem der Jahrgang gesucht
	 *            werden soll
	 * @return den gesuchten Jahrgang oder wenn nicht vorhanden {@code null}
	 * @throws DatasetException
	 */
	public AcademicYear getOne(int year) throws DatasetException;
}
