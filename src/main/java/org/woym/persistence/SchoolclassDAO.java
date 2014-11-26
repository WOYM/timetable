package org.woym.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.woym.exceptions.DatasetException;
import org.woym.objects.Schoolclass;
import org.woym.spec.persistence.ISchoolclassDAO;

public class SchoolclassDAO implements ISchoolclassDAO {
	
	private static final SchoolclassDAO INSTANCE = new SchoolclassDAO();
	
	private static final Logger LOGGER = LogManager.getLogger("Persistence");

	private static final String SELECT = "SELECT s FROM Schoolclass s";

	private SchoolclassDAO(){
	}
	
	public static SchoolclassDAO getInstance(){
		return INSTANCE;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Schoolclass> getAll() throws DatasetException {
		try {
			EntityManager em = DataBase.getEntityManager();
			final Query query = em.createQuery(SELECT);
			List<Schoolclass> schoolclasses = query.getResultList();
			return schoolclasses;
		} catch (Exception e) {
			LOGGER.error("Exception while getting all schoolclasses.", e);
			throw new DatasetException(
					"Error while getting all schoolclasses: " + e.getMessage());
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
			EntityManager em = DataBase.getEntityManager();
			return em.find(Schoolclass.class, id);
		} catch (Exception e) {
			LOGGER.error("Exception while getting schoolclass by id " + id, e);
			throw new DatasetException(
					"Error while getting schoolclass by id: " + e.getMessage());
		}
	}

}
