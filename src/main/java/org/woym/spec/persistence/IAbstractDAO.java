package org.woym.spec.persistence;

import org.woym.exceptions.DatasetException;

/**
 * Schnittstelle für einen DataHandler, der Anfragen an die Datenbank umsetzt.
 * 
 * @author Adrian
 *
 * @param <E>
 */
public interface IAbstractDAO<E> {

	/**
	 * Persistiert das übergebene Objekt in der Datenbank. Tritt dabei ein
	 * Fehler auf, wird eine {@linkplain DatasetException} geworfen.
	 * 
	 * @param pObject
	 *            - das zu persistierende Objekt
	 */
	public void persistObject(E pObject) throws DatasetException;

	/**
	 * Aktualisiert das Objekt in der Datenbank, welches dem dem übergebenen
	 * entspricht. Tritt beim Merge ein Fehler auf, wird eine
	 * {@linkplain DatasetException} geworfen.
	 * 
	 * @param pObject
	 *            - das im System bereits aktualisierte, in der Datenbank zu
	 *            persistierende Objekt
	 * @throws DatasetException
	 */
	public void updateObject(E pObject) throws DatasetException;

	/**
	 * Löscht das Objekt aus der Datenbank, das dem übergebenen entspricht.
	 * Tritt beim Löschen ein Fehler auf, wird eine
	 * {@linkplain DatasetException} geworfen.
	 * 
	 * @param pObject
	 *            - das zu löschende Objekt
	 * @throws DatasetException
	 */
	public void deleteObject(E pObject) throws DatasetException;
}
