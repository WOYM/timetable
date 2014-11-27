package org.woym.persistence;

import java.io.Serializable;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.woym.exceptions.DatasetException;
import org.woym.spec.persistence.IGenericDAO;

public class AbstractGenericDAO<E extends Serializable> implements
		IGenericDAO<E>, Observer {

	/**
	 * Der Logger dieser Klasse.
	 */
	protected static final Logger LOGGER = LogManager.getLogger("Persistence");

	protected EntityManager em = DataBase.getInstance().getEntityManager();

	private Class<E> clazz;
	
	protected Class<E> getClazz(){
		return clazz;
	}
	
	protected void setClazz(Class<E> clazz){
		this.clazz = clazz;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void persist(E object) throws DatasetException {
		if (object == null) {
			throw new IllegalArgumentException("Parameter is null.");
		}
		try {
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
	public final void update(E object) throws DatasetException {
		if (object == null) {
			throw new IllegalArgumentException("Parameter is null.");
		}
		try {
			em.getTransaction().begin();
			em.merge(object);
			em.getTransaction().commit();
			LOGGER.debug(String.format("%s %s updated.", object.getClass()
					.getSimpleName(), object));
		} catch (Exception e) {
			LOGGER.error(String.format("Exception while updating %s: ", object
					.getClass().getSimpleName()), e);
			throw new DatasetException("Error while updating object: "
					+ e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void delete(E object) throws DatasetException {
		if (object == null) {
			throw new IllegalArgumentException("Parameter is null.");
		}
		try {
			em.getTransaction().begin();
			em.remove(object);
			em.getTransaction().commit();
			LOGGER.debug(String.format("%s %s deleted.", object.getClass()
					.getSimpleName(), object));
		} catch (Exception e) {
			LOGGER.error(String.format("Exception while deleting %s: ", object
					.getClass().getSimpleName()), e);
			throw new DatasetException("Error while deleting objects: "
					+ e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public List<E> getAll() throws DatasetException{
		try {
			final Query query = em.createQuery("SELECT x FROM "
					+ clazz.getSimpleName() + " x");
			List<E> objects = query.getResultList();
			return objects;
		} catch (Exception e) {
			LOGGER.error(String.format(
					"Exception while getting all objects of %s ",
					clazz.getSimpleName()), e);
			throw new DatasetException(String.format("Error while getting all objects of %s: ", clazz.getSimpleName())
					+ e.getMessage());
		}
	}

	@Override
	public E getById(Long id) throws DatasetException {
		if (id == null) {
			throw new IllegalArgumentException();
		}
		try {
			return em.find(clazz, id);
		} catch (Exception e) {
			LOGGER.error(
					String.format("Exception while getting %s by id %s ",
							clazz.getSimpleName(), id), e);
			throw new DatasetException(String.format(
					"Error while getting %s by id: ", clazz.getSimpleName())
					+ e.getMessage());
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		em = DataBase.getInstance().getEntityManager();
	}
}
