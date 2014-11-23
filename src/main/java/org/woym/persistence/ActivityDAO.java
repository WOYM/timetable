package org.woym.persistence;

import java.util.List;

import javax.persistence.Query;

import org.woym.exceptions.DatasetException;
import org.woym.objects.Activity;
import org.woym.spec.persistence.IActivityDAO;

public class ActivityDAO extends AbstractDAO<Activity> implements IActivityDAO{

	private static final String SELECT = "SELECT a FROM Activity a";
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Activity> getAll() throws DatasetException {
		try {
			final Query query = entityManager.createQuery(SELECT);
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
			return entityManager.find(Activity.class, id);
		} catch (Exception e) {
			LOGGER.error("Exception while getting activity by id " + id, e);
			throw new DatasetException("Error while getting activity by id: "
					+ e.getMessage());
		}
	}

}
