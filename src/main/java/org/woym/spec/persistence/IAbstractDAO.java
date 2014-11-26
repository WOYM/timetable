package org.woym.spec.persistence;

import java.util.List;

import org.woym.exceptions.DatasetException;

public interface IAbstractDAO<E> {

	/**
	 * Gibt eine Liste aller in der Datenbank vorhandenen Objekte vom Typ E
	 * zurück. Tritt dabei ein Fehler auf, wird eine {@link DatasetException}
	 * geworfen.
	 * 
	 * @return Liste mit allen vorhanden Objekten vom Typ E oder leere Liste
	 * @throws DatasetException
	 */
	public List<E> getAll() throws DatasetException;

	/**
	 * Gibt ein Objekt vom Typ E mit der übergebenen ID zurück oder null, falls
	 * ein solches nicht existiert.
	 * 
	 * @param id
	 *            - ID des gesuchten Objektes
	 * @return Objekt vom Typ E, falls ein Objekt mit der übergebenen ID in der
	 *         Datenbank vorhanden ist, ansonsten null
	 * @throws DatasetException
	 */
	public E getById(Long id) throws DatasetException;
}
