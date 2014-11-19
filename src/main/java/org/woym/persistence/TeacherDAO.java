package org.woym.persistence;

import java.io.Serializable;
import java.util.List;

import javax.activation.DataHandler;
import javax.persistence.Query;

import org.woym.exceptions.DatasetException;
import org.woym.objects.Teacher;
import org.woym.spec.persistence.IEmployeeDAO;

/**
 * Diese Klasse erweitert die abstrakte Klasse {@linkplain DataHandler} und
 * realisiert alle Datenbankanfragen, die die Lehrer betreffen.
 * 
 * @author Adrian
 *
 */
public class TeacherDAO extends AbstractDAO<Teacher> implements
		IEmployeeDAO<Teacher>, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6895052561483060606L;
	
	
	private static final String SELECT = "SELECT t FROM Teacher t";

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Teacher> getAll() throws DatasetException {
		try {
			final Query query = entityManager.createQuery(SELECT);
			List<Teacher> teachers = query.getResultList();
			return teachers;
		} catch (Exception e) {
			LOGGER.error("Exception while getting all teachers.", e);
			throw new DatasetException("Error while getting all teachers: "
					+ e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Teacher> getById(Long id) throws DatasetException {
		if (id == null) {
			throw new IllegalArgumentException();
		}
		try {
			final Query query = entityManager.createQuery(SELECT
					+ " WHERE t.id = ?1");
			query.setParameter(1, id);
			List<Teacher> teachers = query.getResultList();
			return teachers;
		} catch (Exception e) {
			LOGGER.error("Exception while getting teacher by id " + id, e);
			throw new DatasetException("Error while getting all teachers: "
					+ e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public List<Teacher> getBySymbol(String symbol) throws DatasetException {
		if (symbol == null) {
			throw new IllegalArgumentException();
		}
		try {
			final Query query = entityManager.createQuery(SELECT
					+ "WHERE t.symbol = ?1");
			query.setParameter(1, symbol);
			List<Teacher> teachers = query.getResultList();
			return teachers;
		} catch (Exception e) {
			LOGGER.error("Exception while getting teacher with symbol " + symbol, e);
			throw new DatasetException(
					"Error while getting teacher for symbol " + symbol + ": "
							+ e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public List<Teacher> searchForStaff(String searchSymbol)
			throws DatasetException {
		if (searchSymbol == null) {
			throw new IllegalArgumentException();
		}
		try {
			final Query query = entityManager.createQuery(SELECT
					+ "WHERE UPPER(t.symbol) LIKE ?1");
			query.setParameter(1, "%" + searchSymbol.toUpperCase() + "%");
			List<Teacher> teachers = query.getResultList();
			return teachers;
		} catch (Exception e) {
			LOGGER.error("Exception while searching for teachers whose symbol contains " + searchSymbol, e);
			throw new DatasetException(
					"Error while getting teachers whose symbol contains "
							+ searchSymbol + ": " + e.getMessage());
		}
	}
}
