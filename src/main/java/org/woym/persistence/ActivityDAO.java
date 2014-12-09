package org.woym.persistence;

import java.util.List;

import javax.persistence.Query;

import org.woym.exceptions.DatasetException;
import org.woym.objects.Activity;
import org.woym.objects.Employee;
import org.woym.objects.Schoolclass;
import org.woym.spec.persistence.IActivityDAO;

/**
 * 
 * Diese Singleton-Klasse erweitert {@linkplain AbstractGenericDAO} und
 * implementiert {@linkplain IActivityDAO}. Sie bietet Methoden, die in
 * Zusammenhang mit Datenbankanfragen bezüglich Aktivitäten stehen.
 * 
 * @author Adrian
 *
 */
public class ActivityDAO extends AbstractGenericDAO<Activity> implements
		IActivityDAO {

	/**
	 * Die Singleton-Instanz dieser Klasse.
	 */
	private static final ActivityDAO INSTANCE = new ActivityDAO();

	/**
	 * Der private Konstruktor.
	 */
	private ActivityDAO() {
		DataBase.getInstance().addObserver(this);
		setClazz(Activity.class);
	}

	public static ActivityDAO getInstance() {
		return INSTANCE;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Activity> getAll(Employee employee) throws DatasetException {
		/*
		 * if (employee == null) { throw new IllegalArgumentException(); } try {
		 * final Query query = getEm() .createQuery(
		 * "SELECT a from Activity a WHERE a IN (SELECT ae FROM a.employees ae) "
		 * + "AND ?1 IN (SELECT ae FROM a.employees ae)"); query.setParameter(1,
		 * employee); return (List<Activity>) query.getResultList(); } catch
		 * (Exception e) {
		 * LOGGER.error("Exception while getting all activities for " +
		 * employee, e); throw new DatasetException(
		 * "Error while getting all activities for " + employee + ": " +
		 * e.getMessage()); }
		 */
		// TODO: Funktioniert noch nicht, muss noch recherchieren, woran dies
		// liegen könnte.
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Activity> getAll(Schoolclass schoolclass)
			throws DatasetException {
		if (schoolclass == null) {
			throw new IllegalArgumentException();
		}
		try {
			final Query query = getEm()
					.createQuery(
							"SELECT a FROM Activity a WHERE ?1 MEMBER OF a.schoolclasses");
			query.setParameter(1, schoolclass);
			return (List<Activity>) query.getResultList();
		} catch (Exception e) {
			LOGGER.error(
					"Exception while getting all activities for schoolclass "
							+ schoolclass, e);
			throw new DatasetException(String.format(
					"Error while getting all activities for schoolclass %s : ",
					schoolclass)
					+ e.getMessage());
		}
	}
}