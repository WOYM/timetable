package org.woym.spec.persistence;

import java.io.Serializable;
import java.util.List;

import org.woym.exceptions.DatasetException;

public interface IAbstractDAO<E> extends Serializable {

	/**
	 * Persistiert das übergebene Objekt in der Datenbank. Tritt dabei ein
	 * Fehler auf, wird eine {@linkplain DatasetException} geworfen.
	 * 
	 * @param object
	 *            - das zu persistierende Objekt
	 */
	public void persistObject(final E object) throws DatasetException;

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
	public void updateObject(final E object) throws DatasetException;

	/**
	 * Löscht das Objekt aus der Datenbank, das dem übergebenen entspricht.
	 * Tritt beim Löschen ein Fehler auf, wird eine
	 * {@linkplain DatasetException} geworfen.
	 * 
	 * @param object
	 *            - das zu löschende Objekt
	 * @throws DatasetException
	 */
	public void deleteObject(final E object) throws DatasetException;

	/**
	 * Gibt eine Liste aller in der Datenbank vorhandenen Objekte vom Typ E
	 * zurück. Tritt dabei ein Fehler auf, wird eine
	 * {@linkplain DatasetException} geworfen.
	 * 
	 * @return List&lt;E&gt; - Liste aller Objekte vom Typ E, kann auch leer
	 *         sein
	 * 
	 * @throws DatasetException
	 */
	public abstract List<E> getAll() throws DatasetException;

	/**
	 * Wird null übergeben, wird eine {@linkplain IllegalArgumentException}
	 * geworfen. Ansonsten wird, wenn ein Objekt vom Typ E mit der übergebenen
	 * ID in der Datenbank vorhanden ist, dieses Objekt zurückgegeben. Ist kein
	 * Objekt mit der ID vorhanden, wird {@code null} zurückgegeben. Tritt bei
	 * der Datenbankanfrage ein Fehler auf, wird eine
	 * {@linkplain DatasetException} geworfen.
	 * 
	 * @param id
	 *            - ID des gesuchten Elementes
	 * @return Objekt vom Typ E mit der übergebenen ID oder {@code null}
	 * @throws DatasetException
	 */
	public abstract E getById(final Long id) throws DatasetException;
}
