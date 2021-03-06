package org.woym.persistence.spec;

import java.util.List;

import org.woym.common.exceptions.DatasetException;
import org.woym.common.objects.AcademicYear;
import org.woym.common.objects.Entity;
import org.woym.common.objects.TravelTimeList;

public interface IDataAccess extends IAcademicYearDAO, IActivityDAO,
		IActivityTypeDAO, IClassteamDAO, IEmployeeDAO, ILocationDAO, IRoomDAO,
		ISchoolclassDAO {

	/**
	 * Persistiert das übergebene Objekt in der Datenbank. Tritt dabei ein
	 * Fehler auf, wird eine {@linkplain DatasetException} geworfen.
	 * 
	 * @param object
	 *            - das zu persistierende Objekt
	 */
	public void persist(Entity object) throws DatasetException;

	/**
	 * Aktualisiert das Objekt in der Datenbank, welches dem dem übergebenen
	 * entspricht. Tritt beim Merge ein Fehler auf, wird eine
	 * {@linkplain DatasetException} geworfen.
	 * 
	 * @param object
	 *            - das im System bereits aktualisierte, in der Datenbank zu
	 *            persistierende Objekt
	 * @throws DatasetException
	 */
	public void update(Entity object) throws DatasetException;

	/**
	 * Löscht das Objekt aus der Datenbank, das dem übergebenen entspricht.
	 * Tritt beim Löschen ein Fehler auf, wird eine
	 * {@linkplain DatasetException} geworfen.
	 * 
	 * @param object
	 *            - das zu löschende Objekt
	 * @throws DatasetException
	 */
	public void delete(Entity object) throws DatasetException;

	/**
	 * Aktualisiert das übergebene Objekt im Cache aus der Datenbank.
	 * 
	 * @param entity
	 *            - das zu aktualisierende Objekt
	 * @throws DatasetException
	 */
	public void refresh(Entity entity) throws DatasetException;

	/**
	 * Sucht nach einem Objekt der übergebenen Klasse, welches den übergebenen
	 * Long-Wert als Primärschlüssel besitzt. Exisitert kein solches Objekt,
	 * wird {@code null} zurückgegeben. Tritt dabei ein Fehler auf, wird eine
	 * {@linkplain DatasetException} geworfen.
	 * 
	 * @param clazz
	 *            - Klasse, von welcher das Objekt gesucht werden soll
	 * @param id
	 *            - Primärschlüssel des gesuchten Objekts
	 * @return das gesuchte Objekt oder {@code null}, falls nicht vorhanden
	 * @throws DatasetException
	 */
	public <E> E getById(Class<E> clazz, Long id) throws DatasetException;

	/**
	 * Sucht nach allen Objekten der Klasse {@linkplain TravelTimeList} in der
	 * Datenbank. Da es davon nur ein Objekt geben darf, wird entweder
	 * {@code null} zurückgegeben, wenn keins vorhanden ist und ansonsten das
	 * gefundene {@linkplain TravelTimeList}-Objekt.
	 * 
	 * @return das {@linkplain TravelTimeList}-Objekt oder {@code null}, wenn
	 *         nicht vorhanden
	 * 
	 * @throws DatasetException
	 */
	public TravelTimeList getTravelTimeList() throws DatasetException;

	/**
	 * Gibt eine Liste der für den übergebenen Jahrgang als Klassenbezeichner in
	 * Verwendung befindlichen Characters zurück.
	 * 
	 * @param academicYear
	 *            - der Jahrgang, für welche die in Benutzung befindlichen
	 *            Bezeichner erwartet werden
	 * @return Liste der für diesen Jahrgang in Benutzung befindlichen
	 *         Klassenbezeichner
	 * @throws DatasetException
	 */
	public List<Character> getUsedChars(AcademicYear academicYear)
			throws DatasetException;

}
