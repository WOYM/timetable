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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.woym.common.config.Config;
import org.woym.common.config.DefaultConfigEnum;
import org.woym.common.exceptions.DatasetException;
import org.woym.common.objects.Activity;
import org.woym.common.objects.ActivityTO;
import org.woym.common.objects.Employee;
import org.woym.common.objects.EmployeeTimePeriods;
import org.woym.common.objects.Location;
import org.woym.common.objects.Meeting;
import org.woym.common.objects.MeetingType;
import org.woym.common.objects.Room;
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
 * <h1>MeetingController</h1>
 * <p>
 * Diese Controller ist dafür zuständig, {@link Activity}-Objekte des Types
 * {@link Meeting} zu konfigurieren.
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
@ManagedBean(name = "meetingController")
public class MeetingController implements Serializable {

	private static final long serialVersionUID = 4875106571364509043L;

	private static Logger LOGGER = LogManager
			.getLogger(MeetingController.class);

	private DataAccess dataAccess = DataAccess.getInstance();

	private final CommandCreator commandCreator = CommandCreator.getInstance();

	private ActivityValidator activityValidator = ActivityValidator
			.getInstance();
	private ScheduleModelHolder scheduleModelHolder = ScheduleModelHolder
			.getInstance();

	private ActivityTOHolder activityTOHolder = ActivityTOHolder.getInstance();
	private EntityHelper entityHelper = EntityHelper.getInstance();

	private Meeting meeting;

	private Location location;

	/**
	 * Diese Methode initialisiert die Bean und erzeugt eine neue
	 * {@link Meeting}, die danach von dieser Bean verwaltet wird.
	 * <p>
	 * Es wird anhand der Daten der {@link EntityHelper}-Instanz ein erster
	 * Datensatz für das Objekt erzeugt.
	 */
	@PostConstruct
	public void init() {
		meeting = new Meeting();

		ActivityTO activityTO = activityTOHolder.getActivityTO();
		meeting.setTime(activityTO.getTimePeriod());
		if (getAllMeetingTypes().size() > 0) {
			setMeetingMeetingType(getAllMeetingTypes().get(0));
		}

		if (entityHelper.getTeacher() != null) {
			List<TimePeriod> timePeriods = new ArrayList<>();
			timePeriods.add(activityTO.getTimePeriod());
			List<EmployeeTimePeriods> employeeTimePeriods = new ArrayList<>();
			EmployeeTimePeriods employeeTimePeriod = new EmployeeTimePeriods();
			employeeTimePeriod.setTimePeriods(timePeriods);
			employeeTimePeriod.setEmployee(entityHelper.getTeacher());
			employeeTimePeriods.add(employeeTimePeriod);
			meeting.setEmployeeTimePeriods(employeeTimePeriods);
		}

		if (entityHelper.getRoom() != null) {
			location = entityHelper.getLocation();
			setMeetingRoom(entityHelper.getRoom());
		}
	}

	/**
	 * Diese Methode fügt mit Hilfe des {@link CommandHandler}s ein neues
	 * {@link Activity}-Objekt des Types {@link Meeting} der Persistenz hinzu.
	 */
	public void addMeeting() {
		IStatus status = activityValidator.validateActivity(meeting,
				meeting.getTime());

		if (status instanceof SuccessStatus) {
			MacroCommand macro = commandCreator
					.createEmployeeUpdateAddWorkingHours(meeting);
			macro.add(new AddCommand<Meeting>(meeting));
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

	public void setMeetingRoom(Room room) {
		if (room != null) {
			List<Room> rooms = new ArrayList<>();
			rooms.add(room);
			meeting.setRooms(rooms);
		}

	}

	public Room getMeetingRoom() {
		if (meeting.getRooms().size() > 0) {
			return meeting.getRooms().get(0);
		}

		if (getRoomsForLocation().size() > 0) {
			return getRoomsForLocation().get(0);
		}

		return null;
	}

	public List<Room> getRoomsForLocation() {
		return location.getRooms();
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
	public void setMeetingEmployees(List<Employee> employees) {
		List<EmployeeTimePeriods> employeeTimePeriods = new ArrayList<>();
		for (Employee employee : employees) {
			List<TimePeriod> timePeriods = new ArrayList<>();
			timePeriods.add(meeting.getTime());

			EmployeeTimePeriods periods = new EmployeeTimePeriods();
			periods.setTimePeriods(timePeriods);
			periods.setEmployee(employee);

			employeeTimePeriods.add(periods);
		}
		meeting.setEmployeeTimePeriods(employeeTimePeriods);
	}

	public List<Employee> getMeetingEmployees() {
		List<Employee> employees = new ArrayList<>();
		for (EmployeeTimePeriods employeeTimePeriods : meeting
				.getEmployeeTimePeriods()) {
			Employee employee = employeeTimePeriods.getEmployee();
			if (!employees.contains(employee)) {
				employees.add(employee);
			}
		}

		return employees;
	}

	public List<MeetingType> getAllMeetingTypes() {
		List<MeetingType> meetingTypes = new ArrayList<>();

		try {
			meetingTypes = dataAccess.getAllMeetingTypes();
		} catch (DatasetException e) {
			LOGGER.error(e);
		}

		return meetingTypes;
	}

	public void setMeetingMeetingType(MeetingType meetingType) {
		if (meetingType == null) {
			return;
		}

		meeting.setMeetingType(meetingType);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(meeting.getTime().getStartTime());
		calendar.set(Calendar.MINUTE,
				(calendar.get(Calendar.MINUTE) + meetingType
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
		
		if(!meetingType.getRooms().isEmpty()){
			location = meetingType.getRooms().get(0).getLocation();
			setMeetingRoom(meetingType.getRooms().get(0));
		}

		TimePeriod timePeriod = meeting.getTime();
		timePeriod.setEndTime(endTime);
		meeting.setTime(timePeriod);
	}

	public MeetingType getMeetingMeetingType() {
		return meeting.getMeetingType();
	}

	public Meeting getMeeting() {
		return meeting;
	}

	public void setMeeting(Meeting meeting) {
		this.meeting = meeting;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

}
