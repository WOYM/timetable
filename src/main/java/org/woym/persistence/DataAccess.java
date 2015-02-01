package org.woym.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.woym.common.exceptions.DatasetException;
import org.woym.common.objects.AcademicYear;
import org.woym.common.objects.Activity;
import org.woym.common.objects.ActivityType;
import org.woym.common.objects.Classteam;
import org.woym.common.objects.CompoundLesson;
import org.woym.common.objects.Employee;
import org.woym.common.objects.EmployeeTimePeriods;
import org.woym.common.objects.Entity;
import org.woym.common.objects.Lesson;
import org.woym.common.objects.LessonType;
import org.woym.common.objects.Location;
import org.woym.common.objects.Meeting;
import org.woym.common.objects.MeetingType;
import org.woym.common.objects.PedagogicAssistant;
import org.woym.common.objects.ProjectType;
import org.woym.common.objects.Room;
import org.woym.common.objects.Schoolclass;
import org.woym.common.objects.Teacher;
import org.woym.common.objects.TimePeriod;
import org.woym.common.objects.TravelTimeList;
import org.woym.common.objects.Weekday;
import org.woym.common.objects.spec.IActivityObject;
import org.woym.persistence.spec.IDataAccess;
import org.woym.persistence.spec.IEmployeeDAO;

/**
 * Diese Singleton-Klasse bietet mit ihren Methoden Zugriff auf
 * Datenbankoperationen.
 * 
 * @author Adrian
 *
 */
public class DataAccess implements IDataAccess, Observer {

	/**
	 * Die Singleton-Instanz dieser Klasse.
	 */
	private static final DataAccess INSTANCE = new DataAccess();

	/**
	 * Der Logger dieser Klasse.
	 */
	private static final Logger LOGGER = LogManager.getLogger("Persistence");

	/**
	 * Der verwendete EntityManager.
	 */
	private EntityManager em = DataBase.getInstance().getEntityManager();

	/**
	 * Der private Konstruktur. Die Klasse wird als Observer bei
	 * {@linkplain DataBase} registriert.
	 */
	private DataAccess() {
		DataBase.getInstance().addObserver(this);
	}

	/**
	 * Gibt die Singleton-Instanz dieser Klasse zurück.
	 * 
	 * @return die Singleton-Instanz dieser Klasse
	 */
	public static DataAccess getInstance() {
		return INSTANCE;
	}

	@Override
	public void update(Observable o, Object arg) {
		em = DataBase.getInstance().getEntityManager();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void persist(Entity object) throws DatasetException {
		if (object == null) {
			throw new IllegalArgumentException("Parameter is null.");
		}
		try {
			em.getTransaction().begin();
			em.persist(object);
			em.getTransaction().commit();
			LOGGER.debug(String.format("%s %s persisted.", object.getClass()
					.getSimpleName(), object));
		} catch (Exception e) {
			LOGGER.error(String.format("Exception while persisting %s.", object
					.getClass().getSimpleName()), e);
			throw new DatasetException("Error while persisting object: "
					+ e.getMessage());
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(Entity object) throws DatasetException {
		if (object == null) {
			throw new IllegalArgumentException("Parameter is null.");
		}
		try {
			em.getTransaction().begin();
			em.merge(object);
			em.getTransaction().commit();
			LOGGER.debug(String.format("Existing %s updated to %s.", object
					.getClass().getSimpleName(), object));
		} catch (Exception e) {
			LOGGER.error(String.format("Exception while updating %s.", object
					.getClass().getSimpleName()), e);
			throw new DatasetException("Error while updating object: "
					+ e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void refresh(Entity entity) throws DatasetException {
		if (entity == null) {
			throw new IllegalArgumentException("Parameter was null.");
		}
		try {
			em.refresh(entity);
			LOGGER.debug(entity + " refreshed.");
		} catch (Exception e) {
			LOGGER.error("Exception while refreshing " + entity, e);
			throw new DatasetException("Error while refreshing " + entity
					+ ": " + e.getMessage());
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(Entity object) throws DatasetException {
		if (object == null) {
			throw new IllegalArgumentException("Parameter is null.");
		}
		try {
			em.getTransaction().begin();
			em.remove(object);
			em.getTransaction().commit();
			LOGGER.debug(String.format("%s %s deleted.", object.getClass()
					.getSimpleName(), object));
		} catch (Exception e) {
			LOGGER.error(String.format("Exception while deleting %s.", object
					.getClass().getSimpleName()), e);
			throw new DatasetException("Error while deleting objects: "
					+ e.getMessage());
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	// IAcademicYearDAO
	// ////////////////////////////////////////////////////////////////////////////////////

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<AcademicYear> getAllAcademicYears() throws DatasetException {
		return (List<AcademicYear>) getAll(AcademicYear.class, "academicYear");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public AcademicYear getOneAcademicYear(int year) throws DatasetException {
		try {
			final Query query = em
					.createQuery("SELECT a FROM AcademicYear a WHERE a.academicYear = ?1");
			query.setParameter(1, year);
			List<AcademicYear> result = query.getResultList();
			return result.isEmpty() ? null : result.get(0);
		} catch (Exception e) {
			LOGGER.error("Exception while getting academic year " + year, e);
			throw new DatasetException("Error while getting academic year "
					+ year + " " + e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public AcademicYear getOneAcademicYear(Schoolclass schoolclass)
			throws DatasetException {
		if (schoolclass == null) {
			throw new IllegalArgumentException("Parameter was null");
		}
		try {
			final Query query = em
					.createQuery("SELECT a FROM AcademicYear a WHERE ?1 MEMBER OF a.schoolclasses");
			query.setParameter(1, schoolclass);
			List<AcademicYear> result = query.getResultList();
			return result.isEmpty() ? null : result.get(0);
		} catch (Exception e) {
			LOGGER.error(
					"Exception while getting academic year which contains schoolclass"
							+ schoolclass, e);
			throw new DatasetException(
					"Error while getting academic year which contains schoolclass"
							+ schoolclass + " " + e.getMessage());
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	// ISchoolclassDAO
	// ////////////////////////////////////////////////////////////////////////////////////

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Schoolclass> getAllSchoolclasses() throws DatasetException {
		return (List<Schoolclass>) getAll(Schoolclass.class, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Schoolclass> getAllSchoolclasses(Teacher teacher)
			throws DatasetException {
		if (teacher == null) {
			throw new IllegalArgumentException("Parameter was null");
		}
		try {
			final Query query = em
					.createQuery("SELECT s FROM Schoolclass s WHERE s.teacher = ?1");
			query.setParameter(1, teacher);
			return query.getResultList();
		} catch (Exception e) {
			LOGGER.error(
					"Exception while getting all schoolclasses for teacher "
							+ teacher, e);
			throw new DatasetException(
					"Error while getting all schoolclasses for teacher "
							+ teacher + ": " + e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Schoolclass> getAllSchoolclasses(Employee employee)
			throws DatasetException {
		if (employee == null) {
			throw new IllegalArgumentException("Parameter was null.");
		}
		try {
			final Query query = em
					.createQuery("SELECT DISTINCT a.schoolclasses FROM Activity a INNER JOIN a.employeeTimePeriods e"
							+ " WHERE e.employee = ?1 AND a.schoolclasses <> null");
			query.setParameter(1, employee);
			return query.getResultList();
		} catch (Exception e) {
			LOGGER.error(
					"Exception while getting all schoolclasses for employee "
							+ employee, e);
			throw new DatasetException(
					"Error while getting all schoolclasses for employee "
							+ employee + " :" + e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Schoolclass> getAllSchoolclassesWithoutClassteam()
			throws DatasetException {
		try {
			final Query query = em
					.createQuery("SELECT s FROM Schoolclass s WHERE s"
							+ " NOT IN (SELECT DISTINCT s FROM Schoolclass s"
							+ " INNER JOIN Classteam c WHERE s MEMBER OF c.schoolclasses)");
			return query.getResultList();
		} catch (Exception e) {
			LOGGER.error(
					"Exception while getting all schoolclasses without classteam.",
					e);
			throw new DatasetException(
					"Error while getting all schoolclasses without classteam: "
							+ e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Schoolclass getOneSchoolclass(int academicYear, char identifier)
			throws DatasetException {
		try {
			final Query query = em
					.createQuery("SELECT s FROM Schoolclass s, AcademicYear a WHERE a.academicYear = ?1 "
							+ "AND s.identifier = ?2 AND s MEMBER OF a.schoolclasses");
			query.setParameter(1, academicYear);
			query.setParameter(2, identifier);
			List<Schoolclass> result = query.getResultList();
			return result.isEmpty() ? null : result.get(0);
		} catch (Exception e) {
			LOGGER.error("Exception while getting schoolclass with identifier "
					+ identifier + " from academic year " + academicYear, e);
			throw new DatasetException(String.format(
					"Exception while getting schoolclass with identifier %s"
							+ " from academic year %s: " + e.getMessage(),
					identifier, academicYear));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Schoolclass getOneSchoolclass(Room room) throws DatasetException {
		if (room == null) {
			throw new IllegalArgumentException("Parameter was null");
		}
		try {
			final Query query = em
					.createQuery("SELECT s FROM Schoolclass s WHERE s.room = ?1");
			query.setParameter(1, room);
			List<Schoolclass> result = query.getResultList();
			return result.isEmpty() ? null : result.get(0);
		} catch (Exception e) {
			LOGGER.error(
					"Exception while getting schoolclass for room " + room, e);
			throw new DatasetException(
					"Error while getting schoolclass for room " + room + ": "
							+ e.getMessage());
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	// IEmployeeDAO
	// ////////////////////////////////////////////////////////////////////////////////////

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Employee> getAllEmployees(ActivityType activityType)
			throws DatasetException {
		if (activityType == null) {
			throw new IllegalArgumentException("Parameter was null");
		}
		try {
			final Query query = em
					.createQuery("SELECT e FROM Employee e WHERE ?1 MEMBER OF e.possibleActivityTypes");
			query.setParameter(1, activityType);
			return query.getResultList();
		} catch (Exception e) {
			LOGGER.error(
					"Exception while getting all employees for activity type "
							+ activityType, e);
			throw new DatasetException(
					"Exception while getting all employees for activity type "
							+ activityType + ": " + e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<PedagogicAssistant> getAllPAs() throws DatasetException {
		return (List<PedagogicAssistant>) getAll(PedagogicAssistant.class,
				"symbol");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Teacher> getAllTeachers() throws DatasetException {
		return (List<Teacher>) getAll(Teacher.class, "symbol");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Employee getOneEmployee(String symbol) throws DatasetException {
		if (symbol == null) {
			throw new IllegalArgumentException("Parameter was null");
		}
		try {
			final Query query = em
					.createQuery("SELECT e FROM Employee e WHERE UPPER(e.symbol) = ?1");
			query.setParameter(1, symbol.toUpperCase());
			List<Employee> result = query.getResultList();
			return result.isEmpty() ? null : result.get(0);
		} catch (Exception e) {
			LOGGER.error("Exception while getting employee with symbol "
					+ symbol, e);
			throw new DatasetException(String.format(
					"Error while getting employee for symbol %s: ", symbol)
					+ e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Employee> searchEmployees(String searchSymbol)
			throws DatasetException {
		return (List<Employee>) searchEmployees(Employee.class, searchSymbol);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Teacher> searchTeachers(String searchSymbol)
			throws DatasetException {
		return (List<Teacher>) searchEmployees(Teacher.class, searchSymbol);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PedagogicAssistant> searchPAs(String searchSymbol)
			throws DatasetException {
		return (List<PedagogicAssistant>) searchEmployees(
				PedagogicAssistant.class, searchSymbol);
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	// ILocationDAO
	// ////////////////////////////////////////////////////////////////////////////////////

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Location> getAllLocations() throws DatasetException {
		return (List<Location>) getAll(Location.class, "name");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Location getOneLocation(String name) throws DatasetException {
		if (name == null) {
			throw new IllegalArgumentException("Parameter was null");
		}
		try {
			final Query query = em
					.createQuery("SELECT l FROM Location l WHERE UPPER(l.name) = ?1");
			query.setParameter(1, name.toUpperCase());
			List<Location> result = query.getResultList();
			return result.isEmpty() ? null : result.get(0);
		} catch (Exception e) {
			LOGGER.error("Exception while getting location with name " + name,
					e);
			throw new DatasetException(
					"Error while getting location with name " + name + ": "
							+ e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Location getOneLocation(Room room) throws DatasetException {
		if (room == null) {
			throw new IllegalArgumentException("Parameter was null");
		}
		try {
			final Query query = em
					.createQuery("SELECT l FROM Location l WHERE ?1 MEMBER OF l.rooms");
			query.setParameter(1, room);
			List<Location> result = query.getResultList();
			return result.isEmpty() ? null : result.get(0);
		} catch (Exception e) {
			LOGGER.error("Exception while getting location for room " + room, e);
			throw new DatasetException("Error while getting location for room "
					+ room + ": " + e.getMessage());
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	// IRoomDAO
	// ////////////////////////////////////////////////////////////////////////////////////

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Room> getAllRooms(String purpose) throws DatasetException {
		if (purpose == null) {
			throw new IllegalArgumentException("Parameter was null");
		}
		try {
			final Query query = em
					.createQuery("SELECT r FROM Room r WHERE UPPER(r.purpose) = ?1");
			query.setParameter(1, purpose.toUpperCase());
			List<Room> result = query.getResultList();
			return result;
		} catch (Exception e) {
			LOGGER.error("Exception while getting all rooms with purpose "
					+ purpose, e);
			throw new DatasetException(
					"Error while getting all rooms with purpose " + purpose
							+ ": " + e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Room getOneRoom(String locationName, String roomName)
			throws DatasetException {
		if (locationName == null || roomName == null) {
			throw new IllegalArgumentException("Parameter was null");
		}
		try {
			final String select = "SELECT r FROM Room r, Location l WHERE "
					+ "UPPER(r.name) = ?1 AND UPPER(l.name) = ?2 AND r MEMBER OF l.rooms";
			final Query query = em.createQuery(select);
			query.setParameter(1, roomName.toUpperCase());
			query.setParameter(2, locationName.toUpperCase());
			List<Room> result = (List<Room>) query.getResultList();
			return result.isEmpty() ? null : result.get(0);
		} catch (Exception e) {
			LOGGER.error("Exception while checking if room with name "
					+ roomName + " exists in location " + locationName, e);
			throw new DatasetException(
					"Error while checking if room with name " + roomName
							+ " exists in location " + locationName + ": "
							+ e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getRoomPurposes() throws DatasetException {
		try {
			final Query query = em
					.createQuery("SELECT DISTINCT r.purpose FROM Room r");
			List<String> result = query.getResultList();
			return result;
		} catch (Exception e) {
			LOGGER.error("Exception while getting all room purposes.", e);
			throw new DatasetException(
					"Error while getting all room purposes: " + e.getMessage());
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	// IActivityTypeDAO
	// ////////////////////////////////////////////////////////////////////////////////////

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<ActivityType> getAllActivityTypes() throws DatasetException {
		return (List<ActivityType>) getAll(ActivityType.class, "name");
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ActivityType> getAllActivityTypes(Room room)
			throws DatasetException {
		if (room == null) {
			throw new IllegalArgumentException("Parameter was null");
		}
		try {
			final Query query = em
					.createQuery("SELECT a FROM ActivityType a WHERE ?1 MEMBER OF a.rooms");
			query.setParameter(1, room);
			return query.getResultList();
		} catch (Exception e) {
			LOGGER.error("Exception while getting all activtiy types for room "
					+ room, e);
			throw new DatasetException(
					"Error while getting all activity types for room " + room
							+ ": " + e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<MeetingType> getAllMeetingTypes() throws DatasetException {
		return (List<MeetingType>) getAll(MeetingType.class, "name");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<LessonType> getAllLessonTypes() throws DatasetException {
		return (List<LessonType>) getAll(LessonType.class, "name");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<ProjectType> getAllProjectTypes() throws DatasetException {
		return (List<ProjectType>) getAll(ProjectType.class, "name");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public ActivityType getOneActivityType(String name) throws DatasetException {
		if (name == null) {
			throw new IllegalArgumentException("Parameter was null");
		}
		try {
			final Query query = em
					.createQuery("SELECT a from ActivityType a WHERE UPPER(a.name) = ?1");
			query.setParameter(1, name.toUpperCase());
			List<ActivityType> result = query.getResultList();
			return result.isEmpty() ? null : result.get(0);
		} catch (Exception e) {
			LOGGER.error("Exception while getting ActivityType with name "
					+ name, e);
			throw new DatasetException(
					"Error while getting ActivityType with name " + name + " "
							+ e.getMessage());
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	// IActivityDAO
	// ////////////////////////////////////////////////////////////////////////////////////

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Activity> getAllActivities() throws DatasetException {
		return (List<Activity>) getAll(Activity.class, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Activity> getAllActivities(Weekday weekday)
			throws DatasetException {
		if (weekday == null) {
			throw new IllegalArgumentException("Parameter was null");
		}
		try {
			final Query query = em
					.createQuery("SELECT a FROM Activity a WHERE a.time.day = ?1");
			query.setParameter(1, weekday);
			return query.getResultList();
		} catch (Exception e) {
			LOGGER.error("Exception while getting all activities for day "
					+ weekday, e);
			throw new DatasetException(
					"Error while getting all activities for day " + weekday
							+ ": " + e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Activity> getAllActivitiesNotBetween(Date startTime,
			Date endTime) throws DatasetException {
		if (startTime == null || endTime == null) {
			throw new IllegalArgumentException("Parameter was null");
		}
		try {
			final Query query = em
					.createQuery("SELECT a FROM Activity a WHERE NOT a.time.startTime BETWEEN ?1 AND ?2"
							+ " OR NOT a.time.endTime BETWEEN ?1 AND ?2 ");
			query.setParameter(1, startTime);
			query.setParameter(2, endTime);
			return query.getResultList();
		} catch (Exception e) {
			LOGGER.error(
					"Exception while getting all activities between given start and endtime.",
					e);
			throw new DatasetException(
					"Error while getting all activities between given start and endtime: "
							+ e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Activity> getAllActivities(Employee employee)
			throws DatasetException {
		if (employee == null) {
			throw new IllegalArgumentException("Parameter was null");
		}
		try {
			final Query query = em
					.createQuery("SELECT DISTINCT a from Activity a INNER JOIN a.employeeTimePeriods e "
							+ "WHERE e.employee.id = ?1 ORDER BY a.time.day, a.time.startTime");
			query.setParameter(1, employee.getId());
			return (List<Activity>) query.getResultList();
		} catch (Exception e) {
			LOGGER.error("Exception while getting all activities for "
					+ employee, e);
			throw new DatasetException(
					"Error while getting all activities for " + employee + ": "
							+ e.getMessage());
		}
	}

	public List<Activity> getAllActivities(final IActivityObject object)
			throws DatasetException {
		if (object instanceof Employee) {
			return getAllActivities((Employee) object);
		} else if (object instanceof Schoolclass) {
			return getAllActivities((Schoolclass) object);
		} else if (object instanceof Room) {
			return getAllActivities((Room) object);
		} else {
			return new ArrayList<Activity>();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Activity> getAllActivities(Schoolclass schoolclass)
			throws DatasetException {
		if (schoolclass == null) {
			throw new IllegalArgumentException("Parameter was null");
		}
		try {
			final Query query = em
					.createQuery("SELECT a FROM Activity a "
							+ "WHERE ?1 MEMBER OF a.schoolclasses ORDER BY a.time.day, a.time.startTime");
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

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Activity> getAllActivities(Room room) throws DatasetException {
		if (room == null) {
			throw new IllegalArgumentException("Parameter was null");
		}
		try {
			final Query query = em
					.createQuery("SELECT a FROM Activity a "
							+ "WHERE ?1 MEMBER OF a.rooms ORDER BY a.time.day, a.time.startTime");
			query.setParameter(1, room);
			return (List<Activity>) query.getResultList();
		} catch (Exception e) {
			LOGGER.error("Exception while getting all activities for room "
					+ room, e);
			throw new DatasetException(String.format(
					"Error while getting all activities for romm %s : ", room)
					+ e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Activity> getAllActivities(Employee employee, Weekday weekday)
			throws DatasetException {
		if (employee == null || weekday == null) {
			throw new IllegalArgumentException("Parameter was null");
		}
		try {
			final Query query = em
					.createQuery("SELECT DISTINCT a from Activity a INNER JOIN a.employeeTimePeriods e "
							+ "WHERE e.employee.id = ?1 AND a.time.day = ?2 ORDER BY a.time.startTime");
			query.setParameter(1, employee.getId());
			query.setParameter(2, weekday);
			return (List<Activity>) query.getResultList();
		} catch (Exception e) {
			LOGGER.error("Exception while getting all activities for "
					+ employee, e);
			throw new DatasetException(
					"Error while getting all activities for " + employee + ": "
							+ e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @deprecated Inkonsistentes Verhalten. Liefert nicht immer dasselbe
	 *             Ergebnis zurück. In diesem Zustand nicht verwenden.
	 */
	@Deprecated
	@SuppressWarnings("unchecked")
	@Override
	public List<Activity> getAllActivities(Employee employee,
			TimePeriod timePeriod) throws DatasetException {
		if (employee == null || timePeriod == null) {
			throw new IllegalArgumentException("Parameter was null");
		}
		try {
			final Query query = em
					.createQuery("SELECT DISTINCT a from Activity a INNER JOIN a.employeeTimePeriods e"
							+ " INNER JOIN e.timePeriods t WHERE "
							+ " e.employee = ?1"
							+ " AND a.time.day = ?2"
							+ " AND ?3 < t.endTime"
							+ " AND ?4 > t.startTime"
							+ " ORDER BY a.time.startTime");
			query.setParameter(1, employee);
			query.setParameter(2, timePeriod.getDay());
			query.setParameter(3, timePeriod.getStartTime());
			query.setParameter(4, timePeriod.getEndTime());
			List<Activity> result = query.getResultList();
			return result;
		} catch (Exception e) {
			LOGGER.error(String.format(
					"Exception while getting all activities of employee %s, "
							+ "which overlap time period %s.", employee,
					timePeriod), e);
			throw new DatasetException(
					String.format(
							"Error while getting all activities of employee %s, "
									+ "which overlap time period %s: "
									+ e.getMessage(), employee, timePeriod));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Activity> getAllActivities(Schoolclass schoolclass,
			TimePeriod timePeriod) throws DatasetException {
		if (schoolclass == null || timePeriod == null) {
			throw new IllegalArgumentException("Parameter was null");
		}
		try {
			final Query query = em.createQuery("SELECT a FROM Activity a WHERE"
					+ " ?1 MEMBER OF a.schoolclasses AND a.time.day = ?2"
					+ " AND ?3 < a.time.endTime AND ?4 > a.time.startTime"
					+ " ORDER BY a.time.startTime");
			query.setParameter(1, schoolclass);
			query.setParameter(2, timePeriod.getDay());
			query.setParameter(3, timePeriod.getStartTime());
			query.setParameter(4, timePeriod.getEndTime());
			return query.getResultList();
		} catch (Exception e) {
			LOGGER.error(String.format(
					"Exception while getting all activities of schoolclass %s, "
							+ "which are colliding with time period %s.",
					schoolclass, timePeriod), e);
			throw new DatasetException(String.format(
					"Error while getting all activities of schoolclass %s, "
							+ "which are colliding with time period %s: "
							+ e.getMessage(), schoolclass, timePeriod));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Activity> getAllActivities(Room room, TimePeriod timePeriod)
			throws DatasetException {
		if (room == null || timePeriod == null) {
			throw new IllegalArgumentException("Parameter was null");
		}
		try {
			final Query query = em.createQuery("SELECT a FROM Activity a WHERE"
					+ " ?1 MEMBER OF a.rooms AND a.time.day = ?2"
					+ " AND ?3 < a.time.endTime AND ?4 > a.time.startTime"
					+ " ORDER BY a.time.startTime");
			query.setParameter(1, room);
			query.setParameter(2, timePeriod.getDay());
			query.setParameter(3, timePeriod.getStartTime());
			query.setParameter(4, timePeriod.getEndTime());
			return query.getResultList();
		} catch (Exception e) {
			LOGGER.error(String.format(
					"Exception while getting all activities for room %s, "
							+ "which are colliding with time period %s.", room,
					timePeriod), e);
			throw new DatasetException(String.format(
					"Error while getting all activities for room %s, "
							+ "which are colliding with time period %s: "
							+ e.getMessage(), room, timePeriod));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CompoundLesson> getAllCompoundLessons(LessonType lessonType)
			throws DatasetException {
		if (lessonType == null) {
			throw new IllegalArgumentException("Parameter was null");
		}
		try {
			final Query query = em
					.createQuery("SELECT c FROM CompoundLesson c WHERE ?1 MEMBER OF c.lessonTypes");
			query.setParameter(1, lessonType);
			return query.getResultList();
		} catch (Exception e) {
			LOGGER.error(
					"Exception while getting all compound lessons for lesson type "
							+ lessonType, e);
			throw new DatasetException(
					"Error while getting all compound lessons for lesson type "
							+ lessonType + ": " + e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Lesson> getAllLessons(LessonType lessonType)
			throws DatasetException {
		if (lessonType == null) {
			throw new IllegalArgumentException("Parameter was null");
		}
		try {
			final Query query = em
					.createQuery("SELECT l FROM Lesson l WHERE l.lessonType = ?1");
			query.setParameter(1, lessonType);
			return query.getResultList();
		} catch (Exception e) {
			LOGGER.error("Exception while getting all lessons for lesson type "
					+ lessonType, e);
			throw new DatasetException(
					"Error while getting all lessons for lesson type "
							+ lessonType + ": " + e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Meeting> getAllMeetings(MeetingType meetingType)
			throws DatasetException {
		if (meetingType == null) {
			throw new IllegalArgumentException("Parameter was null");
		}
		try {
			final Query query = em
					.createQuery("SELECT m FROM Meeting m WHERE m.meetingType = ?1");
			query.setParameter(1, meetingType);
			return query.getResultList();
		} catch (Exception e) {
			LOGGER.error(
					"Exception while getting all meetings for meeting type "
							+ meetingType, e);
			throw new DatasetException(
					"Error while getting all meetings for meeting type "
							+ meetingType + ": " + e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<EmployeeTimePeriods> getEmployeeTimePeriods(Employee employee)
			throws DatasetException {
		try {
			final Query query = em
					.createQuery("SELECT DISTINCT e FROM Activity a INNER JOIN a.employeeTimePeriods e "
							+ "WHERE ?1 = e.employee AND SIZE(a.employeeTimePeriods) > 1");
			query.setParameter(1, employee);
			return query.getResultList();
		} catch (Exception e) {
			LOGGER.error(
					"Exception while getting all EmployeeTimePeriods for given employee "
							+ employee, e);
			throw new DatasetException(
					"Error while getting all EmployeeTimePeriods for given employee "
							+ employee + ": " + e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Long sumLessonDuration(Employee employee, Schoolclass schoolclass,
			LessonType lessonType) throws DatasetException {
		if (employee == null || schoolclass == null || lessonType == null) {
			throw new IllegalArgumentException("Parameter was null.");
		}
		try {
			final Query query = em
					.createQuery("SELECT SUM(t.duration) FROM Lesson l INNER JOIN l.employeeTimePeriods e INNER JOIN e.timePeriods t"
							+ " WHERE l.lessonType = ?1 AND e.employee = ?2 AND ?3 MEMBER OF l.schoolclasses");
			query.setParameter(1, lessonType);
			query.setParameter(2, employee);
			query.setParameter(3, schoolclass);
			return (Long) query.getSingleResult();
		} catch (Exception e) {
			LOGGER.error("Exception while counting duration of lessons.", e);
			throw new DatasetException(
					"Error while counting duration of lessons: "
							+ e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @deprecated Inkonsistentes Verhalten. Liefert nicht immer dasselbe
	 *             Ergebnis zurück. In diesem Zustand nicht verwenden.
	 */
	@Deprecated
	@SuppressWarnings("unchecked")
	@Override
	public Activity getFirstActivityBefore(Employee employee,
			TimePeriod timePeriod, Location location) throws DatasetException {
		if (employee == null || timePeriod == null || location == null) {
			throw new IllegalArgumentException("Parameter was null.");
		}
		try {
			final Query query = em
					.createQuery("SELECT DISTINCT a FROM Activity a INNER JOIN a.employeeTimePeriods e INNER JOIN e.timePeriods t INNER JOIN a.rooms r"
							+ " WHERE e.employee = ?1"
							+ " AND t.day = ?2"
							+ " AND t.endTime <= ?3"
							+ " AND NOT EXISTS (SELECT activity FROM Activity activity INNER JOIN activity.employeeTimePeriods ae INNER JOIN ae.timePeriods tp"
							+ " WHERE tp.endTime > t.endTime AND tp.endTime <= ?3)"
							+ " AND r NOT IN ?4");
			query.setParameter(1, employee);
			query.setParameter(2, timePeriod.getDay());
			query.setParameter(3, timePeriod.getStartTime());
			query.setParameter(4, location.getRooms());
			List<Activity> result = query.getResultList();
			return result.isEmpty() ? null : result.get(0);
		} catch (Exception e) {
			LOGGER.error(
					"Exception while executing getFirstActivityBefore for employee",
					e);
			throw new DatasetException(
					"Error while executing getFirstActivityBefore for employee: "
							+ e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @deprecated Inkonsistentes Verhalten. Liefert nicht immer dasselbe
	 *             Ergebnis zurück. In diesem Zustand nicht verwenden.
	 */
	@Deprecated
	@SuppressWarnings("unchecked")
	@Override
	public Activity getFirstActivityAfter(Employee employee,
			TimePeriod timePeriod, Location location) throws DatasetException {
		if (employee == null || timePeriod == null || location == null) {
			throw new IllegalArgumentException("Parameter was null.");
		}
		try {
			final Query query = em
					.createQuery("SELECT DISTINCT a FROM Activity a INNER JOIN a.employeeTimePeriods e INNER JOIN e.timePeriods t INNER JOIN a.rooms r"
							+ " WHERE e.employee = ?1"
							+ " AND a.time.day = ?2"
							+ " AND t.startTime >= ?3"
							+ " AND NOT EXISTS (SELECT activity FROM Activity activity INNER JOIN activity.employeeTimePeriods ae INNER JOIN ae.timePeriods tp"
							+ " WHERE tp.startTime < t.startTime AND tp.startTime >= ?3)"
							+ " AND r NOT IN ?4");
			query.setParameter(1, employee);
			query.setParameter(2, timePeriod.getDay());
			query.setParameter(3, timePeriod.getEndTime());
			query.setParameter(4, location.getRooms());
			List<Activity> result = query.getResultList();
			return result.isEmpty() ? null : result.get(0);
		} catch (Exception e) {
			LOGGER.error(
					"Exception while executing getFirstActivityAfter for employee",
					e);
			throw new DatasetException(
					"Error while executing getFirstActivityAfter for employee: "
							+ e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Activity> getAllActivitiesBefore(Employee employee,
			TimePeriod timePeriod) throws DatasetException {
		if (employee == null || timePeriod == null) {
			throw new IllegalArgumentException("Parameter was null.");
		}
		try {
			final Query query = em
					.createQuery("SELECT a FROM Activity a INNER JOIN a.employeeTimePeriods e"
							+ " WHERE e.employee = ?1 AND a.time.endTime <= ?2 AND a.time.day = ?3 ORDER BY a.time.endTime");
			query.setParameter(1, employee);
			query.setParameter(2, timePeriod.getStartTime());
			query.setParameter(3, timePeriod.getDay());
			return query.getResultList();
		} catch (Exception e) {
			LOGGER.error(
					"Exception while getting all activities before the given timeperiod for employee "
							+ employee, e);
			throw new DatasetException(
					"Error while getting all activities before the given timeperiod for employee "
							+ employee + ": " + e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Activity> getAllActivitiesAfter(Employee employee,
			TimePeriod timePeriod) throws DatasetException {
		if (employee == null || timePeriod == null) {
			throw new IllegalArgumentException("Parameter was null.");
		}
		try {
			final Query query = em
					.createQuery("SELECT a FROM Activity a INNER JOIN a.employeeTimePeriods e"
							+ " WHERE e.employee = ?1 AND a.time.startTime >= ?2 AND a.time.day = ?3 ORDER BY a.time.startTime");
			query.setParameter(1, employee);
			query.setParameter(2, timePeriod.getEndTime());
			query.setParameter(3, timePeriod.getDay());
			return query.getResultList();
		} catch (Exception e) {
			LOGGER.error(
					"Exception while getting all activities after the given timeperiod for employee "
							+ employee, e);
			throw new DatasetException(
					"Error while getting all activities after the given timeperiod for employee "
							+ employee + ": " + e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Activity getFirstActivityBefore(Schoolclass schoolclass,
			TimePeriod timePeriod, Location location) throws DatasetException {
		if (schoolclass == null || timePeriod == null || location == null) {
			throw new IllegalArgumentException("Parameter was null.");
		}
		try {
			final Query query = em
					.createQuery("SELECT a FROM Activity a INNER JOIN a.rooms r WHERE"
							+ " ?1 MEMBER OF a.schoolclasses"
							+ " AND a.time.day = ?2"
							+ " AND a.time.endTime <= ?3"
							+ " AND NOT EXISTS (SELECT activity FROM Activity activity WHERE activity.time.endTime > a.time.endTime AND activity.time.endTime <= ?3)"
							+ " AND r NOT IN ?4");
			query.setParameter(1, schoolclass);
			query.setParameter(2, timePeriod.getDay());
			query.setParameter(3, timePeriod.getStartTime());
			query.setParameter(4, location.getRooms());
			List<Activity> result = query.getResultList();
			return result.isEmpty() ? null : result.get(0);
		} catch (Exception e) {
			LOGGER.error(
					"Exception while executing getFirstActivityBefore for schoolclass",
					e);
			throw new DatasetException(
					"Error while executing getFirstActivityBefore for schoolclass: "
							+ e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Activity getFirstActivityAfter(Schoolclass schoolclass,
			TimePeriod timePeriod, Location location) throws DatasetException {
		if (schoolclass == null || timePeriod == null || location == null) {
			throw new IllegalArgumentException("Parameter was null.");
		}
		try {
			final Query query = em
					.createQuery("SELECT a FROM Activity a INNER JOIN a.rooms r WHERE"
							+ " ?1 MEMBER OF a.schoolclasses"
							+ " AND a.time.day = ?2"
							+ " AND a.time.startTime >= ?3"
							+ " AND NOT EXISTS (SELECT activity FROM Activity activity WHERE activity.time.startTime < a.time.startTime AND activity.time.startTime >= ?3)"
							+ " AND r NOT IN ?4");
			query.setParameter(1, schoolclass);
			query.setParameter(2, timePeriod.getDay());
			query.setParameter(3, timePeriod.getEndTime());
			query.setParameter(4, location.getRooms());
			List<Activity> result = query.getResultList();
			return result.isEmpty() ? null : result.get(0);
		} catch (Exception e) {
			LOGGER.error(
					"Exception while executing getFirstActivityAfter for schoolclass",
					e);
			throw new DatasetException(
					"Error while executing getFirstActivityAfter for schoolclass: "
							+ e.getMessage());
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	// IClassTeamDAO
	// ////////////////////////////////////////////////////////////////////////////////////

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Classteam> getAllClassteams() throws DatasetException {
		return (List<Classteam>) getAll(Classteam.class, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Classteam> getAllClassteams(Employee employee)
			throws DatasetException {
		if (employee == null) {
			throw new IllegalArgumentException("Parameter was null");
		}
		try {
			final Query query = em
					.createQuery("SELECT c FROM Classteam c WHERE ?1 MEMBER OF c.employees");
			query.setParameter(1, employee);
			return query.getResultList();
		} catch (Exception e) {
			LOGGER.error("Exception while getting all classteams for employee "
					+ employee, e);
			throw new DatasetException(
					"Error while getting all classteams for employee "
							+ employee + ": " + e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Classteam getOneClassteam(Schoolclass schoolclass)
			throws DatasetException {
		if (schoolclass == null) {
			throw new IllegalArgumentException("Parameter was null");
		}
		try {
			final Query query = em
					.createQuery("SELECT c FROM Classteam c WHERE ?1 MEMBER OF c.schoolclasses");
			query.setParameter(1, schoolclass);
			List<Classteam> result = query.getResultList();
			if (result.isEmpty()) {
				return null;
			}
			return result.get(0);
		} catch (Exception e) {
			LOGGER.error("Exception while getting classteam for schoolclass "
					+ schoolclass, e);
			throw new DatasetException(
					"Exception while getting classteam for schoolclass "
							+ schoolclass + ": " + e.getMessage());
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	// IDataAccess
	// ////////////////////////////////////////////////////////////////////////////////////

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E> E getById(Class<E> clazz, Long id) throws DatasetException {
		if (id == null) {
			throw new IllegalArgumentException("Parameter was null");
		}
		try {
			return em.find(clazz, id);
		} catch (Exception e) {
			LOGGER.error(
					String.format("Exception while getting %s by id %s",
							clazz.getSimpleName(), id), e);
			throw new DatasetException(String.format(
					"Exception while getting %s by id %s: ",
					clazz.getSimpleName(), id)
					+ e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public TravelTimeList getTravelTimeList() throws DatasetException {
		try {
			final Query query = em
					.createQuery("SELECT t FROM TravelTimeList t");
			List<TravelTimeList> result = query.getResultList();
			if (result.isEmpty()) {
				return null;
			}
			return result.get(0);
		} catch (Exception e) {
			LOGGER.error("Exception while getting TravelTimeList.", e);
			throw new DatasetException("Error while getting TravelTimeList: "
					+ e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Character> getUsedChars(AcademicYear academicYear)
			throws DatasetException {
		if (academicYear == null) {
			throw new IllegalArgumentException("Parameter was null");
		}
		try {
			final Query query = em
					.createQuery("SELECT s.identifier FROM AcademicYear a "
							+ "INNER JOIN a.schoolclasses s WHERE a = ?1");
			query.setParameter(1, academicYear);
			return query.getResultList();
		} catch (Exception e) {
			LOGGER.error(
					"Exception while getting used characters for academic year "
							+ academicYear, e);
			throw new DatasetException(
					"Error while getting used characters for academic year "
							+ academicYear + ": " + e.getMessage());
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	// Private Methoden
	// ////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Allgemeine Implementierung einer Methode die alle Objekte der übergebenen
	 * Klasse in der Datenbank sucht und als Liste zurückgibt.
	 * 
	 * @param clazz
	 *            - Klasse, deren Objekte gesucht werden sollen
	 * @param orderBy
	 *            - Name des Attributes, nach welchem sortiert werden soll oder
	 *            {@code null}, wenn nicht sortiert werden soll
	 * @return eine Liste mit den gesuchten Objekten
	 * @throws DatasetException
	 */
	@SuppressWarnings("unchecked")
	private List<? extends Serializable> getAll(
			Class<? extends Serializable> clazz, String orderBy)
			throws DatasetException {
		if (clazz == null) {
			throw new IllegalArgumentException("Parameter was null");
		}
		try {
			String select = "SELECT x FROM " + clazz.getSimpleName() + " x";
			if (orderBy != null) {
				select += " ORDER BY x." + orderBy;
			}
			final Query query = em.createQuery(select);
			return query.getResultList();
		} catch (Exception e) {
			LOGGER.error(String.format(
					"Exception while getting all objects of %s.",
					clazz.getSimpleName()), e);
			throw new DatasetException(String.format(
					"Error while getting all objects of %s: ",
					clazz.getSimpleName())
					+ e.getMessage());
		}
	}

	/**
	 * Generische Implementierung für
	 * {@linkplain IEmployeeDAO#searchEmployees(String)},
	 * {@linkplain IEmployeeDAO#searchTeachers(String)} und
	 * {@linkplain IEmployeeDAO#searchPAs(String)}.
	 * 
	 * @param clazz
	 *            - {@linkplain Employee} erweiternde Klasse, deren Objekte
	 *            gesucht werden sollen
	 * @param searchSymbol
	 *            - String, anhand dessen gesucht werden soll
	 * @return Liste der gefunden Objekte
	 * @throws DatasetException
	 */
	@SuppressWarnings("unchecked")
	private List<? extends Serializable> searchEmployees(
			Class<? extends Employee> clazz, String searchSymbol)
			throws DatasetException {
		if (searchSymbol == null || clazz == null) {
			throw new IllegalArgumentException("Parameter was null");
		}
		try {
			final Query query = em.createQuery("SELECT x FROM "
					+ clazz.getSimpleName()
					+ " x WHERE UPPER(x.symbol) LIKE ?1 ORDER BY x.symbol");
			query.setParameter(1, "%" + searchSymbol.toUpperCase() + "%");
			return query.getResultList();
		} catch (Exception e) {
			LOGGER.error(String.format(
					"Exception while getting %s whose symbol contain %s",
					clazz.getSimpleName() + "s", searchSymbol), e);
			throw new DatasetException(String.format(
					"Error while getting %s for symbol %s: ",
					clazz.getSimpleName() + "s", searchSymbol)
					+ e.getMessage());
		}
	}

}
