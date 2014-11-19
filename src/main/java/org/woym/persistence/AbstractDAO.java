package org.woym.persistence;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.woym.exceptions.DatasetException;
import org.woym.spec.persistence.IAbstractDAO;

/**
 * Eine abstrakte generische Klasse, die als Superklasse für alle anderen
 * Handler dient.
 * 
 * @author Adrian
 *
 * @param <E>
 *            - generische Klasse
 */
public abstract class AbstractDAO<E> implements IAbstractDAO<E>{

	/**
	 * Eine Instanz des EntityManagers von {@linkplain DataBase}, die in den
	 * Subklassen verwendet werden kann.
	 */
	protected final EntityManager entityManager = DataBase.getEntityManager();

	protected static Logger LOGGER = LogManager.getLogger("Persistence");

	/**
	 * {@inheritDoc}
	 */
	public void persistObject(final E object) throws DatasetException {
		if (object == null) {
			throw new IllegalArgumentException("Parameter is null.");
		}
		try {
			entityManager.getTransaction().begin();
			entityManager.persist(object);
			entityManager.getTransaction().commit();
			LOGGER.debug(String.format("%s %s persisted.", object.getClass()
					.getSimpleName(), object));
		} catch (Exception e) {
			LOGGER.error(String.format("Exception while persisting %s: ",
					object.getClass().getSimpleName()), e);
			throw new DatasetException("Error while persisting object: "
					+ e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void updateObject(final E object) throws DatasetException {
		if (object == null) {
			throw new IllegalArgumentException("Parameter is null.");
		}
		try {
			entityManager.getTransaction().begin();
			entityManager.merge(object);
			entityManager.getTransaction().commit();
			LOGGER.debug(String.format("%s %s updated.", object.getClass()
					.getSimpleName(), object));
		} catch (Exception e) {
			LOGGER.error(String.format("Exception while updating %s: ",
					object.getClass().getSimpleName()), e);
			throw new DatasetException("Error while updating object: "
					+ e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void deleteObject(final E object) throws DatasetException {
		if (object == null) {
			throw new IllegalArgumentException("Parameter is null.");
		}
		try {
			entityManager.getTransaction().begin();
			entityManager.remove(object);
			entityManager.getTransaction().commit();
			LOGGER.debug(String.format("%s %s deleted.", object.getClass()
					.getSimpleName(), object));
		} catch (Exception e) {
			LOGGER.error(String.format("Exception while deleting %s: ",
					object.getClass().getSimpleName()), e);
			throw new DatasetException("Error while deleting objects: "
					+ e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public abstract List<E> getAll() throws DatasetException;

	/**
	 * {@inheritDoc}
	 */
	public abstract List<E> getById(final Long id) throws DatasetException;
}
