package org.woym.persistence;

import java.util.List;
import java.util.Observer;

import javax.persistence.Query;

import org.woym.exceptions.DatasetException;
import org.woym.objects.ActivityType;
import org.woym.objects.LessonType;
import org.woym.objects.MeetingType;
import org.woym.objects.ProjectType;
import org.woym.spec.persistence.IActivityTypeDAO;

/**
 * Diese Singleton-Klasse implementiert Methoden die in Zusammenhang mit
 * Datenbankanfragen bezüglich {@linkplain ActivityType}-Objekten stehen.
 * 
 * @author Adrian
 *
 */
public class ActivityTypeDAO extends AbstractGenericDAO<ActivityType> implements
		IActivityTypeDAO {

	/**
	 * Singleton-Instanz dieser Klasse.
	 */
	private static final ActivityTypeDAO INSTANCE = new ActivityTypeDAO();

	/**
	 * Der private Konstruktor dieser Klasse. Registriert die Instanz bei
	 * {@linkplain DataBase} als {@linkplain Observer} und ruft
	 * {@linkplain AbstractGenericDAO#setClazz(Class)} mit
	 * {@linkplain ActivityType} als Klasse auf.
	 */
	private ActivityTypeDAO() {
		DataBase.getInstance().addObserver(this);
		setClazz(ActivityType.class);
	}

	/**
	 * Gibt die Singleton-Instanz dieser Klasse zurück.
	 * 
	 * @return Singleton-Instanz dieser Klasse
	 */
	public static ActivityTypeDAO getInstance() {
		return INSTANCE;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ActivityType getOne(String name) throws DatasetException {
		if (name == null) {
			throw new IllegalArgumentException();
		}
		try {
			final Query query = getEm().createQuery(
					"SELECT a from ActivityType a WHERE a.name = ?1");
			query.setParameter(1, name);
			List<ActivityType> result = query.getResultList();
			if (result.isEmpty()) {
				return null;
			}
			return result.get(0);
		} catch (Exception e) {
			LOGGER.error("Exception while getting ActivityType with name "
					+ name, e);
			throw new DatasetException(
					"Error while getting ActivityType with name " + name + " "
							+ e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<LessonType> getAllLessonTypes() throws DatasetException {
		try {
			final Query query = getEm().createQuery(
					"SELECT l FROM LessonType l");
			return (List<LessonType>) query.getResultList();
		} catch (Exception e) {
			LOGGER.error("Exception while getting all lesson types", e);
			throw new DatasetException("Error while getting all lesson types :"
					+ e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ProjectType> getAllProjectTypes() throws DatasetException {
		try {
			final Query query = getEm().createQuery(
					"SELECT p FROM ProjectType p");
			return (List<ProjectType>) query.getResultList();
		} catch (Exception e) {
			LOGGER.error("Exception while getting all project types", e);
			throw new DatasetException("Error while getting all project types :"
					+ e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<MeetingType> getAllMeetingTypes() throws DatasetException {
		try {
			final Query query = getEm().createQuery(
					"SELECT m FROM MeetingType m");
			return (List<MeetingType>) query.getResultList();
		} catch (Exception e) {
			LOGGER.error("Exception while getting all meeting types", e);
			throw new DatasetException("Error while getting all meeting types :"
					+ e.getMessage());
		}
	}

}
