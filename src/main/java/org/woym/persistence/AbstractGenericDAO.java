package org.woym.persistence;

import java.io.Serializable;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.woym.exceptions.DatasetException;
import org.woym.spec.persistence.IGenericDAO;

/**
 * Diese abstrakte Klasse realisiert die durch das Interface
 * {@linkplain IGenericDAO} beschriebenen generischen Methoden für DAO-Objekte.
 * Sie implementiert außerdem das Interface {@linkplain Observer}, da alle
 * DAO-Objekte {@linkplain DataBase} beobachten müssen.
 * 
 * @author Adrian
 *
 * @param <E>
 *            die generische Klasse
 */
public class AbstractGenericDAO<E extends Serializable> implements
		IGenericDAO<E>, Observer {

	/**
	 * Der Logger dieser Klasse.
	 */
	protected static final Logger LOGGER = LogManager.getLogger("Persistence");

	/**
	 * Der verwendete EntityManager.
	 */
	private EntityManager em = DataBase.getInstance().getEntityManager();

	/**
	 * Die Klasse, für welche das DAO geschrieben wurde.
	 */
	private Class<E> clazz;

	protected EntityManager getEm(){
		return em;
	}
	
	protected Class<E> getClazz() {
		return clazz;
	}

	protected void setClazz(Class<E> clazz) {
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
			LOGGER.error(String.format("Exception while persisting %s.",
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
			LOGGER.error(String.format("Exception while updating %s.", object
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
			LOGGER.error(String.format("Exception while deleting %s.", object
					.getClass().getSimpleName()), e);
			throw new DatasetException("Error while deleting objects: "
					+ e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<E> getAll(String orderBy) throws DatasetException {
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<E> cq = cb.createQuery(clazz);
			Root<E> root = cq.from(clazz);
			cq.select(root);
			if(orderBy != null){
				cq.orderBy(cb.asc(root.get(orderBy)));
			}
			TypedQuery<E> query = em.createQuery(cq);
			return query.getResultList();
		} catch (Exception e) {
			LOGGER.error(String.format(
					"Exception while getting all objects of %s.",
					clazz.getSimpleName()), e);
			throw new DatasetException(String.format(
					"Error while getting all objects of %s: ",
					clazz.getSimpleName())
					+ e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public E getById(Long id) throws DatasetException {
		if (id == null) {
			throw new IllegalArgumentException();
		}
		try {
			return em.find(clazz, id);
		} catch (Exception e) {
			LOGGER.error(
					String.format("Exception while getting %s by id %s.",
							clazz.getSimpleName(), id), e);
			throw new DatasetException(String.format(
					"Error while getting %s by id: ", clazz.getSimpleName())
					+ e.getMessage());
		}
	}

	/**
	 * Holt den neuen {@linkplain EntityManager} von {@linkplain DataBase}.
	 */
	@Override
	public void update(Observable o, Object arg) {
		em = DataBase.getInstance().getEntityManager();
	}
}
