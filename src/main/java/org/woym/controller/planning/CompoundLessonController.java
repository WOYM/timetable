package org.woym.controller.planning;

import java.io.Serializable;
import java.util.ArrayList;
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
import org.woym.common.objects.CompoundLesson;
import org.woym.common.objects.Employee;
import org.woym.common.objects.EmployeeTimePeriods;
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

/**
 * <h1>CompoundLessonController</h1>
 * 
 * @author Tim Hansen
 * @version 0.1.0
 * @since 0.1.0
 */
@ViewScoped
@ManagedBean(name = "compoundLessonController")
public class CompoundLessonController implements Serializable {

	private static final long serialVersionUID = 8308234096934826569L;

	private DataAccess dataAccess = DataAccess.getInstance();
	private static Logger LOGGER = LogManager
			.getLogger(CompoundLessonController.class);
	private ActivityValidator activityValidator = ActivityValidator
			.getInstance();
	private ScheduleModelHolder scheduleModelHolder = ScheduleModelHolder
			.getInstance();

	private ActivityTOHolder activityTOHolder = ActivityTOHolder.getInstance();
	private EntityHelper entityHelper = EntityHelper.getInstance();

	private CompoundLesson compoundLesson;

	private Location location;

	@PostConstruct
	public void init() {
		compoundLesson = new CompoundLesson();
		compoundLesson
				.setTime(activityTOHolder.getActivityTO().getTimePeriod());

		if (entityHelper.getTeacher() != null) {
			List<Employee> employees = new ArrayList<>();
			employees.add(entityHelper.getTeacher());
			setCompoundLessonEmployees(employees);
		}

		if (entityHelper.getPedagogicAssistant() != null) {
			List<Employee> employees = new ArrayList<>();
			employees.add(entityHelper.getPedagogicAssistant());
			setCompoundLessonEmployees(employees);
		}

		if (entityHelper.getRoom() != null) {
			location = entityHelper.getLocation();
			List<Room> rooms = new ArrayList<>();
			rooms.add(entityHelper.getRoom());
			compoundLesson.setRooms(rooms);
		}

		if (entityHelper.getSchoolclass() != null) {
			List<Schoolclass> schoolclasses = new ArrayList<>();
			schoolclasses.add(entityHelper.getSchoolclass());
			compoundLesson.setSchoolclasses(schoolclasses);
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

	public void addCompoundLesson() {
		IStatus status = activityValidator.validateActivity(compoundLesson,
				compoundLesson.getTime());

		if (status instanceof SuccessStatus) {
			AddCommand<CompoundLesson> command = new AddCommand<CompoundLesson>(
					compoundLesson);
			status = CommandHandler.getInstance().execute(command);

			if (status instanceof SuccessStatus) {
				init();
			}
		}

		scheduleModelHolder.updateScheduleModel();

		FacesMessage message = status.report();
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void setCompoundLessonEmployees(List<Employee> employees) {
		List<EmployeeTimePeriods> employeeTimePeriods = new ArrayList<>();
		for (Employee employee : employees) {
			TimePeriod timePeriod = compoundLesson.getTime();
			List<TimePeriod> periods = new ArrayList<>();
			periods.add(timePeriod);

			EmployeeTimePeriods employeeTimePeriod = new EmployeeTimePeriods();
			employeeTimePeriod.setTimePeriods(periods);
			employeeTimePeriod.setEmployee(employee);

			employeeTimePeriods.add(employeeTimePeriod);
		}

		compoundLesson.setEmployeeTimePeriods(employeeTimePeriods);
	}

	public List<Schoolclass> getAllSchoolclasses() {
		List<Schoolclass> schoolclasses = new ArrayList<>();

		try {
			schoolclasses = dataAccess.getAllSchoolclasses();
		} catch (DatasetException e) {
			LOGGER.error(e);
		}

		return schoolclasses;
	}

	public List<Employee> getCompoundLessonEmployees() {
		List<Employee> employees = new ArrayList<>();
		for (EmployeeTimePeriods employeeTimePeriods : compoundLesson
				.getEmployeeTimePeriods()) {
			employees.add(employeeTimePeriods.getEmployee());
		}
		return employees;
	}

	public List<Room> getRoomsForLocation() {
		return location.getRooms();
	}

	public CompoundLesson getCompoundLesson() {
		return compoundLesson;
	}

	public void setCompoundLesson(CompoundLesson compoundLesson) {
		this.compoundLesson = compoundLesson;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

}