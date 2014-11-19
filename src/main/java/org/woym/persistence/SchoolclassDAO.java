package org.woym.persistence;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Query;

import org.woym.exceptions.DatasetException;
import org.woym.objects.Schoolclass;
import org.woym.spec.persistence.ISchoolclassDAO;

public class SchoolclassDAO extends AbstractDAO<Schoolclass> implements
		ISchoolclassDAO, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3975349764322792900L;
	
	
	private static final String SELECT = "SELECT s FROM Schoolclass s";
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Schoolclass> getAll() throws DatasetException {
		try {
			final Query query = entityManager.createQuery(SELECT);
			List<Schoolclass> schoolclasses = query.getResultList();
			return schoolclasses;
		} catch (Exception e) {
			LOGGER.error("Exception while getting all schoolclasses.", e);
			throw new DatasetException("Error while getting all schoolclasses: "
					+ e.getMessage());
		}
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Schoolclass> getById(Long id) throws DatasetException {
		if (id == null) {
			throw new IllegalArgumentException();
		}
		try {
			final Query query = entityManager.createQuery(SELECT
					+ " WHERE s.id = ?1");
			query.setParameter(1, id);
			List<Schoolclass> schoolclasses = query.getResultList();
			return schoolclasses;
		} catch (Exception e) {
			LOGGER.error("Exception while getting schoolclass by id " + id, e);
			throw new DatasetException("Error while getting schoolclass by id: "
					+ e.getMessage());
		}
	}

}
