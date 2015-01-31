package org.woym.controller.planning;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.woym.common.objects.CompoundLesson;
import org.woym.common.objects.Employee;
import org.woym.common.objects.EmployeeTimePeriods;
import org.woym.common.objects.Location;
import org.woym.common.objects.Room;
import org.woym.common.objects.TimePeriod;
import org.woym.ui.util.ActivityTOHolder;
import org.woym.ui.util.EntityHelper;

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
	
	private ActivityTOHolder activityTOHolder = ActivityTOHolder.getInstance();
	private EntityHelper entityHelper = EntityHelper.getInstance();
	
	private CompoundLesson compoundLesson;
	
	private Location location;
	
	@PostConstruct
	public void init() {
		compoundLesson = new CompoundLesson();
		compoundLesson.setTime(activityTOHolder.getActivityTO().getTimePeriod());
		
		if(entityHelper.getTeacher() != null) {
			List<Employee> employees = new ArrayList<>();
			employees.add(entityHelper.getTeacher());
			setCompoundLessonEmployees(employees);
		}
		
		if(entityHelper.getPedagogicAssistant() != null) {
			List<Employee> employees = new ArrayList<>();
			employees.add(entityHelper.getPedagogicAssistant());
			setCompoundLessonEmployees(employees);
		}
		
		if(entityHelper.getRoom() != null) {
			location = entityHelper.getLocation();
			List<Room> rooms = new ArrayList<>();
			rooms.add(entityHelper.getRoom());
			compoundLesson.setRooms(rooms);
		}
	}
	
	public void setCompoundLessonEmployees(List<Employee> employees) {
		List<EmployeeTimePeriods> employeeTimePeriods = new ArrayList<>();
		for(Employee employee : employees) {
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
	
	public List<Room> getRoomsForLocation() {
		return location.getRooms();
	}
	
	public List<Employee> getCompoundLessonEmployees() {
		List<Employee> employees = new ArrayList<>();
		for(EmployeeTimePeriods employeeTimePeriods : compoundLesson.getEmployeeTimePeriods()) {
			employees.add(employeeTimePeriods.getEmployee());
		}
		return employees;
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