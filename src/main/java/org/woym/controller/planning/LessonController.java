package org.woym.controller.planning;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.woym.common.exceptions.DatasetException;
import org.woym.common.objects.Employee;
import org.woym.common.objects.EmployeeTimePeriods;
import org.woym.common.objects.Lesson;
import org.woym.common.objects.LessonType;
import org.woym.common.objects.TimePeriod;
import org.woym.common.objects.Weekday;
import org.woym.logic.CommandHandler;
import org.woym.logic.SuccessStatus;
import org.woym.logic.command.AddCommand;
import org.woym.logic.spec.IStatus;
import org.woym.logic.util.ActivityValidator;
import org.woym.persistence.DataAccess;
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

	private Lesson lesson;

	@PostConstruct
	public void init() {
		lesson = new Lesson();
		Date date = new Date();
		TimePeriod timePeriod = new TimePeriod();
		timePeriod.setStartTime(date);
		timePeriod.setEndTime(date);
		timePeriod.setDay(Weekday.MONDAY);
		lesson.setTime(timePeriod);
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
		IStatus status = activityValidator.validateActivity(lesson);

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

	public Lesson getLesson() {
		return lesson;
	}

	public void setLesson(Lesson lesson) {
		this.lesson = lesson;
	}

	/**
	 * Diese Methode setzt die {@link Employee}s der {@link Lesson}
	 * <p>
	 * TODO Hier sollten die verschiedenen Zeitslots benutzt werden (Kommen und
	 * Gehen von Lehrern während einer Aktivität)
	 * 
	 * @return Liste mit {@link Employee}s
	 */
	public List<Employee> getLessonEmployees() {
		List<Employee> employees = new ArrayList<>();

		for (EmployeeTimePeriods period : lesson.getEmployeeTimePeriods()) {
			if (!employees.contains(period.getEmployee())) {
				employees.add(period.getEmployee());
			}
		}

		return employees;
	}

	/**
	 * Diese Methode setzt die {@link Employee}s an einer {@link Lesson}
	 * 
	 * @param employees
	 *            Die Liste der zu setzenden {@link Employee}s
	 */
	public void setLessonEmployees(List<Employee> employees) {
		List<EmployeeTimePeriods> employeeTimePeriods = new ArrayList<>();

		for (Employee employee : employees) {
			EmployeeTimePeriods periods = new EmployeeTimePeriods();
			periods.setEmployee(employee);

			List<TimePeriod> timePeriods = new ArrayList<>();
			timePeriods.add(lesson.getTime());

			periods.setTimePeriods(timePeriods);

			employeeTimePeriods.add(periods);
		}

		lesson.setEmployeeTimePeriods(employeeTimePeriods);
	}

}
