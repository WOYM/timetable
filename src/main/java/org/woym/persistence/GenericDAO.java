package org.woym.persistence;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.woym.exceptions.DatasetException;
import org.woym.spec.persistence.IGenericDAO;

public class GenericDAO implements IGenericDAO {

	/**
	 * Die Singleton-Instanz dieser Klasse.
	 */
	private static final GenericDAO INSTANCE = new GenericDAO();

	/**
	 * Der Logger dieser Klasse.
	 */
	private static final Logger LOGGER = LogManager.getLogger("Persistence");

	private GenericDAO() {
	}

	/**
	 * Gibt die Singleton-Instanz zurück.
	 * 
	 * @return GenericDAO - die Singleton-Instanz
	 */
	public static GenericDAO getInstance() {
		return INSTANCE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void persist(Object object) throws DatasetException {
		if (object == null) {
			throw new IllegalArgumentException("Parameter is null.");
		}
		try {
			EntityManager em = DataBase.getEntityManager();
			em.getTransaction().begin();
			em.persist(object);
			em.getTransaction().commit();
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
	@Override
	public void merge(Object object) throws DatasetException {
		if (object == null) {
			throw new IllegalArgumentException("Parameter is null.");
		}
		try {
			EntityManager em = DataBase.getEntityManager();
			em.getTransaction().begin();
			em.merge(object);
			em.getTransaction().commit();
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
	@Override
	public void delete(Object object) throws DatasetException {
		if (object == null) {
			throw new IllegalArgumentException("Parameter is null.");
		}
		try {
			EntityManager em = DataBase.getEntityManager();
			em.getTransaction().begin();
			em.remove(object);
			em.getTransaction().commit();
			LOGGER.debug(String.format("%s %s deleted.", object.getClass()
					.getSimpleName(), object));
		} catch (Exception e) {
			LOGGER.error(String.format("Exception while deleting %s: ",
					object.getClass().getSimpleName()), e);
			throw new DatasetException("Error while deleting objects: "
					+ e.getMessage());
		}
	}

}
