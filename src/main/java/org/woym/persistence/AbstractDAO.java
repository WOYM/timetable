package org.woym.persistence;

import java.util.List;

import javax.persistence.EntityManager;

import org.woym.exceptions.DatasetException;

/**
 * Eine abstrakte generische Klasse, die als Superklasse für alle anderen
 * Handler dient.
 * 
 * @author Adrian
 *
 * @param <E>
 *            - generische Klasse
 */
public abstract class AbstractDAO<E> {

	/**
	 * Eine Instanz des EntityManagers von {@linkplain DataBase}, die in den
	 * Subklassen verwendet werden kann.
	 */
	protected final EntityManager entityManager = DataBase.getEntityManager();

	/**
	 * Persistiert das übergebene Objekt in der Datenbank. Tritt dabei ein
	 * Fehler auf, wird eine {@linkplain DatasetException} geworfen.
	 * 
	 * @param pObject
	 *            - das zu persistierende Objekt
	 */
	public void persistObject(final E pObject) throws DatasetException {
		if (pObject == null) {
			throw new IllegalArgumentException("Parameter is null.");
		}
		try {
			entityManager.getTransaction().begin();
			entityManager.persist(pObject);
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			throw new DatasetException("Error while persisting object: "
					+ e.getMessage());
		}
	}

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
	public void updateObject(final E pObject) throws DatasetException {
		if (pObject == null) {
			throw new IllegalArgumentException("Parameter is null.");
		}
		try {
			entityManager.getTransaction().begin();
			entityManager.merge(pObject);
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			throw new DatasetException("Error while updating object: "
					+ e.getMessage());
		}
	}

	/**
	 * Löscht das Objekt aus der Datenbank, das dem übergebenen entspricht.
	 * Tritt beim Löschen ein Fehler auf, wird eine
	 * {@linkplain DatasetException} geworfen.
	 * 
	 * @param pObject
	 *            - das zu löschende Objekt
	 * @throws DatasetException
	 */
	public void deleteObject(final E pObject) throws DatasetException {
		if (pObject == null) {
			throw new IllegalArgumentException("Parameter is null.");
		}
		try {
			entityManager.getTransaction().begin();
			entityManager.remove(pObject);
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			throw new DatasetException("Error while deleting objects: "
					+ e.getMessage());
		}
	}

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
	 * Gibt eine Liste mit Objekten vom Typ E zurück. Ist ein Objekt mit der ID
	 * vorhanden, wird eine Liste mit diesem Objekt zurückgegeben. Ist kein
	 * Objekt mit der ID vorhanden, wird eine leere Liste zurückgegeben. Tritt
	 * bei der Datenbankanfrage ein Fehler auf, wird eine
	 * {@linkplain DatasetException} geworfen.
	 * 
	 * @param pId
	 *            - ID des gesuchten Elementes
	 * @return List&lt;E&gt;, leere Liste, falls kein Objekt mit der übergebenen
	 *         ID vorhanden ist, ansonsten eine Liste mit einem Element
	 * @throws DatasetException
	 */
	public abstract List<E> getById(final Long pId) throws DatasetException;
}
