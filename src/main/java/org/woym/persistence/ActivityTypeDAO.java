package org.woym.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.woym.exceptions.DatasetException;
import org.woym.objects.ActivityType;
import org.woym.objects.Meeting;
import org.woym.objects.Project;
import org.woym.objects.Subject;
import org.woym.spec.persistence.IActivityTypeDAO;

public class ActivityTypeDAO implements IActivityTypeDAO {
	
	private static final ActivityTypeDAO INSTANCE = new ActivityTypeDAO();

	private static final Logger LOGGER = LogManager.getLogger("Persistence");

	private static final String SELECT = "SELECT a FROM ActivityType a";

	private static final String PAUSE_DISC = "Pause";

	private static final String SUBJECT_DISC = "Subject";

	private static final String MEETING_DISC = "Meeting";

	private static final String PROJECT_DISC = "Project";
	
	private ActivityTypeDAO() {
	}
	
	public static ActivityTypeDAO getInstance(){
		return INSTANCE;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Subject> getAllSubjects() throws DatasetException {
		try {
			EntityManager em = DataBase.getEntityManager();
			final Query query = em.createQuery(SELECT + " WHERE a.type = ?1");
			query.setParameter(1, "'" + SUBJECT_DISC + "'");
			List<Subject> subjects = query.getResultList();
			return subjects;
		} catch (Exception e) {
			LOGGER.error("Exception while getting all subjects.", e);
			throw new DatasetException("Error while getting all subjects: "
					+ e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Project> getAllProjects() throws DatasetException {
		try {
			EntityManager em = DataBase.getEntityManager();
			final Query query = em.createQuery(SELECT + " WHERE a.type = ?1");
			query.setParameter(1, "'" + PROJECT_DISC + "'");
			List<Project> projects = query.getResultList();
			return projects;
		} catch (Exception e) {
			LOGGER.error("Exception while getting all projects.", e);
			throw new DatasetException("Error while getting all projects: "
					+ e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Meeting> getAllMeetings() throws DatasetException {
		try {
			EntityManager em = DataBase.getEntityManager();
			final Query query = em.createQuery(SELECT + " WHERE a.type = ?1");
			query.setParameter(1, "'" + MEETING_DISC + "'");
			List<Meeting> meetings = query.getResultList();
			return meetings;
		} catch (Exception e) {
			LOGGER.error("Exception while getting all meetings.", e);
			throw new DatasetException("Error while getting all meetings: "
					+ e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ActivityType> getAll() throws DatasetException {
		try {
			EntityManager em = DataBase.getEntityManager();
			final Query query = em.createQuery(SELECT + " WHERE a.type != ?1");
			query.setParameter(1, "'" + PAUSE_DISC + "'");
			List<ActivityType> types = query.getResultList();
			return types;
		} catch (Exception e) {
			LOGGER.error("Exception while getting all activity types.", e);
			throw new DatasetException(
					"Error while getting all activity types: " + e.getMessage());
		}
	}

	@Override
	public ActivityType getById(Long id) throws DatasetException {
		if (id == null) {
			throw new IllegalArgumentException();
		}
		try {
			EntityManager em = DataBase.getEntityManager();
			return em.find(ActivityType.class, id);
		} catch (Exception e) {
			LOGGER.error("Exception while getting activity type by id " + id, e);
			throw new DatasetException(
					"Error while getting activity type by id: "
							+ e.getMessage());
		}
	}

}
