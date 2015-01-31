package org.woym.controller.planning;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.woym.common.exceptions.DatasetException;
import org.woym.common.objects.AcademicYear;
import org.woym.common.objects.ActivityTO;
import org.woym.common.objects.Employee;
import org.woym.common.objects.EmployeeTimePeriods;
import org.woym.common.objects.Lesson;
import org.woym.common.objects.LessonType;
import org.woym.common.objects.Location;
import org.woym.common.objects.Room;
import org.woym.common.objects.Schoolclass;
import org.woym.common.objects.TimePeriod;
import org.woym.logic.CommandHandler;
import org.woym.logic.SuccessStatus;
import org.woym.logic.command.AddCommand;
import org.woym.logic.spec.IStatus;
import org.woym.logic.util.ActivityValidator;
import org.woym.persistence.DataAccess;
import org.woym.ui.util.ActivityTOHolder;
import org.woym.ui.util.EntityHelper;
import org.woym.ui.util.ScheduleModelHolder;

@ViewScoped
@ManagedBean(name = "lessonController")
public class LessonController implements Serializable {

	private static final long serialVersionUID = -3548148989241300086L;

	private static Logger LOGGER = LogManager.getLogger(LessonController.class);

	private DataAccess dataAccess = DataAccess.getInstance();
	private ActivityValidator activityValidator = ActivityValidator
			.getInstance();
	private ScheduleModelHolder scheduleModelHolder = ScheduleModelHolder
			.getInstance();
	private ActivityTOHolder activityTOHolder = ActivityTOHolder.getInstance();
	private EntityHelper entityHelper = EntityHelper.getInstance();

	private Lesson lesson;

	private AcademicYear academicYear;
	private Location location;

	@PostConstruct
	public void init() {
		lesson = new Lesson();

		ActivityTO activityTO = activityTOHolder.getActivityTO();
		lesson.setTime(activityTO.getTimePeriod());
		if (!getAllLessonTypes().isEmpty()) {
			setLessonLessonType(getAllLessonTypes().get(0));
		}

		if (entityHelper.getTeacher() != null) {
			setLessonEmployee(entityHelper.getTeacher());
		}

		if (entityHelper.getSchoolclass() != null) {
			academicYear = entityHelper.getAcademicYear();
			setLessonSchoolclass(entityHelper.getSchoolclass());
		}

		if (entityHelper.getRoom() != null) {
			location = entityHelper.getLocation();
			setLessonRoom(entityHelper.getRoom());
		}

	}

	/**
	 * Diese Methode erzwingt eine Initialisierung der Bean bei jedem Rendern.
	 * 
	 * @param event
	 *            Das Event
	 */
	public void doPreRender(ComponentSystemEvent event) {
		init();
	}

	/**
	 * Diese Methode gibt alle bekannten {@link LessonTypes} zurück
	 * 
	 * @return Liste mit allen bekannten {@link LessonTypes}
	 */
	public List<LessonType> getAllLessonTypes() {
		List<LessonType> lessonTypes = new ArrayList<>();

		try {
			lessonTypes = dataAccess.getAllLessonTypes();
		} catch (DatasetException e) {
			LOGGER.error(e);
		}

		return lessonTypes;
	}

	public void addLesson() {
		IStatus status = activityValidator.validateActivity(lesson,
				lesson.getTime());

		if (status instanceof SuccessStatus) {
			AddCommand<Lesson> command = new AddCommand<Lesson>(lesson);
			status = CommandHandler.getInstance().execute(command);

			if (status instanceof SuccessStatus) {
				init();
			}
		}

		scheduleModelHolder.updateScheduleModel();

		FacesMessage message = status.report();
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	/**
	 * Diese Methode setzt die {@link Employee}s der {@link Lesson}
	 * <p>
	 * TODO Hier sollten die verschiedenen Zeitslots benutzt werden (Kommen und
	 * Gehen von Lehrern während einer Aktivität)
	 * 
	 * @return Liste mit {@link Employee}s
	 */
	public Employee getLessonEmployee() {
		if (lesson.getEmployeeTimePeriods().size() > 0) {
			return lesson.getEmployeeTimePeriods().get(0).getEmployee();
		}
		return null;
	}

	/**
	 * Diese Methode setzt den {@link Employee} an einer {@link Lesson}, die in
	 * dieser Bean verwaltet wird.
	 * 
	 * @param employee
	 *            Der {@link Employee}
	 */
	public void setLessonEmployee(Employee employee) {
		List<EmployeeTimePeriods> employeeTimePeriods = new ArrayList<>();

		EmployeeTimePeriods periods = new EmployeeTimePeriods();
		periods.setEmployee(employee);

		List<TimePeriod> timePeriods = new ArrayList<>();
		timePeriods.add(lesson.getTime());

		periods.setTimePeriods(timePeriods);

		employeeTimePeriods.add(periods);

		lesson.setEmployeeTimePeriods(employeeTimePeriods);
	}

	public void setLessonLessonType(LessonType lessonType) {
		lesson.setLessonType(lessonType);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(lesson.getTime().getStartTime());
		calendar.set(Calendar.MINUTE,
				(calendar.get(Calendar.MINUTE) + lessonType
						.getTypicalDuration()));

		TimePeriod timePeriod = lesson.getTime();
		timePeriod.setEndTime(calendar.getTime());
		lesson.setTime(timePeriod);
	}

	public Schoolclass getLessonSchoolclass() {
		if (lesson.getSchoolclasses().size() > 0) {
			return lesson.getSchoolclasses().get(0);
		}
		
		if (getSchoolclassesForAcademicYear().size() > 0) {
			return getSchoolclassesForAcademicYear().get(0);
		}
		
		return null;
	}

	public void setLessonSchoolclass(Schoolclass schoolclass) {
		if (schoolclass != null) {
			List<Schoolclass> schoolclasses = new ArrayList<>();
			schoolclasses.add(schoolclass);
			lesson.setSchoolclasses(schoolclasses);
		}
	}

	public Room getLessonRoom() {
		if (lesson.getRooms().size() > 0) {
			return lesson.getRooms().get(0);
		}

		if (getRoomsForLocation().size() > 0) {
			return getRoomsForLocation().get(0);
		}

		return null;
	}

	public void setLessonRoom(Room room) {
		if (room != null) {
			List<Room> rooms = new ArrayList<>();
			rooms.add(room);
			lesson.setRooms(rooms);
		}
	}

	public List<Schoolclass> getSchoolclassesForAcademicYear() {
		return academicYear.getSchoolclasses();
	}

	public List<Room> getRoomsForLocation() {
		return location.getRooms();
	}

	public LessonType getLessonLessonType() {
		return lesson.getLessonType();
	}

	public Lesson getLesson() {
		return lesson;
	}

	public void setLesson(Lesson lesson) {
		this.lesson = lesson;
	}

	public AcademicYear getAcademicYear() {
		return academicYear;
	}

	public void setAcademicYear(AcademicYear academicYear) {
		this.academicYear = academicYear;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

}
