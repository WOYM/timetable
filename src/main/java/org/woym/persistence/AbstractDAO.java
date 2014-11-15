package org.woym.persistence;

import javax.persistence.EntityManager;

import org.woym.exceptions.DatasetException;
import org.woym.spec.persistence.IAbstractDAO;

/**
 * Eine konkrete Implementierung von {@linkplain IAbstractDataHandler} als abstrakte
 * Klasse, die als Superklasse für alle anderen Handler dient.
 * 
 * @author Adrian
 *
 * @param <E>
 *            - generische Klasse
 */
public abstract class AbstractDAO<E> implements IAbstractDAO<E> {

	/**
	 * Eine Instanz des EntityManagers von {@linkplain DataBase}, die in den
	 * Subklassen verwendet werden kann.
	 */
	protected final EntityManager entityManager = DataBase.getEntityManager();

	/**
	 * {@inheritDoc}
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
	 * {@inheritDoc}
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
	 * {@inheritDoc}
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
			throw new DatasetException("Error while updating objects: "
					+ e.getMessage());
		}
	}
}
