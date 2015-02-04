package org.woym.controller.planning;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.woym.common.config.Config;
import org.woym.common.config.DefaultConfigEnum;
import org.woym.common.exceptions.DatasetException;
import org.woym.common.objects.AcademicYear;
import org.woym.common.objects.Activity;
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
import org.woym.logic.command.CommandCreator;
import org.woym.logic.command.MacroCommand;
import org.woym.logic.spec.IStatus;
import org.woym.logic.util.ActivityValidator;
import org.woym.persistence.DataAccess;
import org.woym.ui.util.ActivityTOHolder;
import org.woym.ui.util.EntityHelper;
import org.woym.ui.util.ScheduleModelHolder;

/**
 * <h1>LessonController</h1>
 * <p>
 * Diese Controller ist dafür zuständig, {@link Activity}-Objekte des Types
 * {@link Lesson} zu konfigurieren.
 * 
 * @author Tim Hansen (tihansen)
 * @version 0.2.0
 * @since 0.1.0
 *
 * @see PlanningController
 * @see ActivityValidator
 * @see EntityHelper
 * @see ScheduleModelHolder
 * @see ActivityTOHolder
 * @see ActivityTO
 */
@ViewScoped
@ManagedBean(name = "lessonController")
public class LessonController implements Serializable {

	private static final long serialVersionUID = -3548148989241300086L;

	private static Logger LOGGER = LogManager.getLogger(LessonController.class);

	private final CommandCreator commandCreator = CommandCreator.getInstance();

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

	/**
	 * Diese Methode initialisiert die Bean und erzeugt eine neue {@link Lesson}
	 * , die danach von dieser Bean verwaltet wird.
	 * <p>
	 * Es wird anhand der Daten der {@link EntityHelper}-Instanz ein erster
	 * Datensatz für das Objekt erzeugt.
	 */
	@PostConstruct
	public void init() {
		lesson = new Lesson();

		ActivityTO activityTO = activityTOHolder.getActivityTO();
		lesson.setTime(activityTO.getTimePeriod());
		if (getAllLessonTypes().size() > 0) {
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

	/**
	 * Diese Methode fügt mit Hilfe des {@link CommandHandler}s ein neues
	 * {@link Activity}-Objekt des Types {@link Lesson} der Persistenz hinzu.
	 */
	public void addLesson() {
		IStatus status = activityValidator.validateActivity(lesson,
				lesson.getTime());

		if (status instanceof SuccessStatus) {
			MacroCommand macro = commandCreator
					.createEmployeeUpdateAddWorkingHours(lesson);
			macro.add(new AddCommand<Lesson>(lesson));
			status = CommandHandler.getInstance().execute(macro);

			if (status instanceof SuccessStatus) {
				init();
				RequestContext.getCurrentInstance().execute("PF('wAddActivityDialog').hide()");
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

	/**
	 * Diese Methode setzt den {@link LessonType} der in dieser Bean verwalteten
	 * {@link Activity}.
	 * <p>
	 * Dabei wird außerdem die Endzeit der {@link Activity} angepasst.
	 * 
	 * @param lessonType
	 *            Der {@link LessonType}
	 */
	public void setLessonLessonType(LessonType lessonType) {
		if (lessonType == null) {
			return;
		}

		lesson.setLessonType(lessonType);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(lesson.getTime().getStartTime());
		calendar.set(Calendar.MINUTE,
				(calendar.get(Calendar.MINUTE) + lessonType
						.getTypicalDuration()));

		Date endTime = calendar.getTime();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(sdf.parse(Config
					.getSingleStringValue(DefaultConfigEnum.WEEKDAY_ENDTIME)));
			if (calendar2.get(Calendar.HOUR_OF_DAY) < calendar
					.get(Calendar.HOUR_OF_DAY)
					|| calendar2.get(Calendar.HOUR_OF_DAY) == calendar
							.get(Calendar.HOUR_OF_DAY)
					&& calendar2.get(Calendar.MINUTE) < calendar
							.get(Calendar.MINUTE)) {
				endTime = calendar2.getTime();
			}
		} catch (ParseException e) {
			LOGGER.error(e);
		}

		TimePeriod timePeriod = lesson.getTime();
		timePeriod.setEndTime(endTime);
		lesson.setTime(timePeriod);
		
		if(!lessonType.getRooms().isEmpty()) {
			location = lessonType.getRooms().get(0).getLocation();
			setLessonRoom(lessonType.getRooms().get(0));
		} else {
			for(Schoolclass schoolclass : lesson.getSchoolclasses()) {
				if(schoolclass.getRoom() != null) {
					location = schoolclass.getRoom().getLocation();
					setLessonRoom(schoolclass.getRoom());
					break;
				}
			}
		}
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

	/**
	 * Diese Methode setzt eine einzelne {@link Schoolclass}.
	 * <p>
	 * Da eine Liste im Objekt vorgegeben ist, wird hier das Hinzufügen eines
	 * einzelnen Objektes erzwungen.
	 * 
	 * @param schoolclass
	 */
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
