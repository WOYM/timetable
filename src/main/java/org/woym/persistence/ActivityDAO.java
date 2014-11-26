package org.woym.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.woym.exceptions.DatasetException;
import org.woym.objects.Activity;
import org.woym.spec.persistence.IActivityDAO;

/**
 * 
 * Diese Singleton-Klasse bietet Methoden, die in Zusammenhang mit
 * Datenbankanfragen bezüglich Aktivitäten stehen.
 * 
 * @author adrian
 *
 */
public class ActivityDAO implements IActivityDAO {

	/**
	 * Die Singleton-Instanz dieser Klasse.
	 */
	private static final ActivityDAO INSTANCE = new ActivityDAO();

	/**
	 * Der Logger dieser Klasse.
	 */
	private static final Logger LOGGER = LogManager.getLogger("Persistence");

	/**
	 * Die Select-Abfrage, um alle Aktivitäten zu bekommen.
	 */
	private static final String SELECT = "SELECT a FROM Activity a";

	/**
	 * Der private Konstruktor.
	 */
	private ActivityDAO() {
	}

	public static ActivityDAO getInstance() {
		return INSTANCE;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Activity> getAll() throws DatasetException {
		try {
			EntityManager em = DataBase.getEntityManager();
			final Query query = em.createQuery(SELECT);
			List<Activity> activities = query.getResultList();
			return activities;
		} catch (Exception e) {
			LOGGER.error("Exception while getting all activities.", e);
			throw new DatasetException("Error while getting all activities: "
					+ e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Activity getById(Long id) throws DatasetException {
		if (id == null) {
			throw new IllegalArgumentException();
		}
		try {
			EntityManager em = DataBase.getEntityManager();
			return em.find(Activity.class, id);
		} catch (Exception e) {
			LOGGER.error("Exception while getting activity by id " + id, e);
			throw new DatasetException("Error while getting activity by id: "
					+ e.getMessage());
		}
	}

}
