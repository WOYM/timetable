package org.woym.persistence;

import java.util.List;

import javax.activation.DataHandler;
import javax.persistence.Query;

import org.woym.exceptions.DatasetException;
import org.woym.objects.Teacher;
import org.woym.spec.persistence.IStaffDAO;

/**
 * Diese Klasse erweitert die abstrakte Klasse {@linkplain DataHandler} und
 * realisiert alle Datenbankanfragen, die die Lehrer betreffen.
 * 
 * @author Adrian
 *
 */
public class TeacherDAO extends AbstractDAO<Teacher> implements
		IStaffDAO<Teacher> {

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
			throw new DatasetException("Error while getting all teachers: "
					+ e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Teacher> getById(Long pId) throws DatasetException {
		if(pId == null){
			throw new IllegalArgumentException();
		}
		try {
			final Query query = entityManager.createQuery(SELECT + " WHERE t.id = ?1");
			query.setParameter(1, pId);
			List<Teacher> teachers = query.getResultList();
			return teachers;
		} catch (Exception e) {
			throw new DatasetException("Error while getting all teachers: "
					+ e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public List<Teacher> getBySymbol(String pSymbol) throws DatasetException {
		if (pSymbol == null) {
			throw new IllegalArgumentException();
		}
		try {
			final Query query = entityManager.createQuery(SELECT
					+ "WHERE t.symbol = ?1");
			query.setParameter(1, pSymbol);
			List<Teacher> teachers = query.getResultList();
			return teachers;
		} catch (Exception e) {
			throw new DatasetException(
					"Error while getting teacher for symbol " + pSymbol + ": "
							+ e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public List<Teacher> searchForStaff(String pSearchSymbol)
			throws DatasetException {
		if (pSearchSymbol == null) {
			throw new IllegalArgumentException();
		}
		try {
			final Query query = entityManager.createQuery(SELECT
					+ "WHERE UPPER(t.symbol) LIKE ?1");
			query.setParameter(1, "%" + pSearchSymbol.toUpperCase() + "%");
			List<Teacher> teachers = query.getResultList();
			return teachers;
		} catch (Exception e) {
			throw new DatasetException(
					"Error while getting teachers whose symbol contains "
							+ pSearchSymbol + ": " + e.getMessage());
		}
	}
}
