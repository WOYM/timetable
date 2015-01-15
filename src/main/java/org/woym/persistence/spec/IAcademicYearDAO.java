package org.woym.persistence.spec;

import java.util.List;

import org.woym.common.exceptions.DatasetException;
import org.woym.common.objects.AcademicYear;
import org.woym.common.objects.Schoolclass;

/**
 * Dieses Interface beschreibt Methoden, die von einem {@linkplain AcademicYear}
 * Data-Access-Object zu implementieren wären.
 * 
 * @author adrian
 *
 */
public interface IAcademicYearDAO {

	/**
	 * Gibt eine Liste aller in der Datenbank vorhandenen
	 * {@linkplain AcademicYear}-Objekte nach Jahr sortiert zurück. Sind keine
	 * vorhanden wird, eine leere Liste zurückgegeben. Tritt bei der Anfrage ein
	 * Fehler auf, wird eine {@linkplain DatasetException} geworfen.
	 * 
	 * @return Liste der vorhandenen {@linkplain AcademicYear}-Objekte nach Jahr
	 *         sortiert oder leere Liste, wenn keine vorhanden sind
	 * @throws DatasetException
	 */
	public List<AcademicYear> getAllAcademicYears() throws DatasetException;

	/**
	 * Gibt den Jahrgang zurück, der den übergebenen int-Wert als Jahr trägt.
	 * Ist kein Jahrgang mit dem übergebenen Wert in der Datenbank vorhanden,
	 * wird {@code null} zurückgegeben. Wird ein Wert kleiner 1 übergeben, wird
	 * eine {@linkplain IllegalArgumentException} geworfen. Tritt bei der
	 * Anfrage ein Fehler auf, wird eine {@linkplain DatasetException} geworfen.
	 * 
	 * @param year
	 *            - das Jahr (1, 2, 3, 4, ...), zu welchem der Jahrgang gesucht
	 *            werden soll
	 * @return den gesuchten Jahrgang oder wenn nicht vorhanden {@code null}
	 * @throws DatasetException
	 */
	public AcademicYear getOneAcademicYear(int year) throws DatasetException;

	/**
	 * Gibt den Jahrgang zurück, zu welchem die übergebene Schulklasse gehört.
	 * Gehört die Klasse zu keinem Jahrgang, wird {@code null} zurückgegeben.
	 * Tritt bei der Datenbankanfrage ein Fehler auf, wirde eine
	 * {@linkplain DatasetException} geworfen.
	 * 
	 * @param schoolclass
	 *            - die Schulklasse, zu welcher der Jahrgang gesucht wird
	 * @return den Jahrgang, zu dem die übergebene Schulklasse gehört oder
	 *         {@code null}, wenn kein solcher existiert
	 * @throws DatasetException
	 */
	public AcademicYear getOneAcademicYear(Schoolclass schoolclass)
			throws DatasetException;
}
