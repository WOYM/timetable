package org.woym.controller.planning;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.woym.common.exceptions.DatasetException;
import org.woym.common.objects.Activity;
import org.woym.common.objects.ActivityTO;
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
import org.woym.logic.command.CommandCreator;
import org.woym.logic.command.MacroCommand;
import org.woym.logic.spec.IStatus;
import org.woym.logic.util.ActivityValidator;
import org.woym.persistence.DataAccess;
import org.woym.ui.util.ActivityTOHolder;
import org.woym.ui.util.EntityHelper;
import org.woym.ui.util.ScheduleModelHolder;

/**
 * <h1>CompoundLessonController</h1>
 * <p>
 * Diese Controller ist dafür zuständig, {@link Activity}-Objekte des Types
 * {@link CompoundLesson} zu konfigurieren.
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
@ManagedBean(name = "compoundLessonController")
public class CompoundLessonController implements Serializable {

	private static final long serialVersionUID = 8308234096934826569L;

	private DataAccess dataAccess = DataAccess.getInstance();
	private final CommandCreator commandCreator = CommandCreator.getInstance();

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

	/**
	 * Diese Methode initialisiert die Bean und erzeugt eine neue
	 * {@link CompoundLesson}, die danach von dieser Bean verwaltet wird.
	 * <p>
	 * Es wird anhand der Daten der {@link EntityHelper}-Instanz ein erster
	 * Datensatz für das Objekt erzeugt.
	 */
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
	 * Diese Methode fügt mit Hilfe des {@link CommandHandler}s ein neues
	 * {@link Activity}-Objekt des Types {@link CompoundLesson} der Persistenz
	 * hinzu.
	 */
	public void addCompoundLesson() {
		IStatus status = activityValidator.validateActivity(compoundLesson,
				compoundLesson.getTime());

		if (status instanceof SuccessStatus) {
			MacroCommand macro = commandCreator
					.createEmployeeUpdateAddWorkingHours(compoundLesson);
			macro.add(new AddCommand<CompoundLesson>(compoundLesson));
			status = CommandHandler.getInstance().execute(macro);

			if (status instanceof SuccessStatus) {
				init();
				RequestContext.getCurrentInstance().execute(
						"PF('wAddActivityDialog').hide()");
			}

			scheduleModelHolder.updateScheduleModel();
		}

		FacesMessage message = status.report();
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	/**
	 * Diese Methode setzt eine Liste von {@link Employee}-Objekten an dem
	 * {@link Activity}-Objekt der Bean.
	 * <p>
	 * Da eine Liste von {@link EmployeeTimePeriods} vorgegeben ist, wird hier
	 * eine Liste gesetzt, die jedem {@link Employee} einen Zeitslot der
	 * Gesamtdauer der {@link Activity} zuordnet.
	 * 
	 * @param employees
	 *            Die Liste der zu setzenden {@link Employee}-Objekte
	 */
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