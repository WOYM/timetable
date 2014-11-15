package org.woym.persistence;

import java.util.List;

import javax.persistence.Query;

import org.woym.exceptions.DatasetException;
import org.woym.objects.Teacher;
import org.woym.spec.persistence.IConcreteDAO;

/**
 * Diese Klasse erweitert die abstrakte Klasse {@linkplain DataHandler} und
 * realisiert alle Datenbankanfragen, die die Lehrer betreffen.
 * 
 * @author Adrian
 *
 */
public class TeacherDAO extends AbstractDAO<Teacher> implements
		IConcreteDAO<Teacher> {

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public List<Teacher> getAll() throws DatasetException {
		try {
			final Query query = entityManager
					.createQuery("SELECT t FROM Teacher t");
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
	public Teacher getById(Long pId) throws DatasetException {
		// TODO Auto-generated method stub
		return null;
	}
}
