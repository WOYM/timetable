package org.woym.persistence;

import java.util.List;

import javax.persistence.Query;

import org.woym.exceptions.DatasetException;
import org.woym.objects.Schoolclass;
import org.woym.spec.persistence.ISchoolclassDAO;

public class SchoolclassDAO extends AbstractDAO<Schoolclass> implements
		ISchoolclassDAO {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6987428883976412402L;
	
	
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
	public Schoolclass getById(Long id) throws DatasetException {
		if (id == null) {
			throw new IllegalArgumentException();
		}
		try {
			return entityManager.find(Schoolclass.class, id);
		} catch (Exception e) {
			LOGGER.error("Exception while getting schoolclass by id " + id, e);
			throw new DatasetException("Error while getting schoolclass by id: "
					+ e.getMessage());
		}
	}

}
