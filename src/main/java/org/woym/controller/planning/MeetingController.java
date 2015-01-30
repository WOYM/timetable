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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.woym.common.exceptions.DatasetException;
import org.woym.common.objects.ActivityTO;
import org.woym.common.objects.EmployeeTimePeriods;
import org.woym.common.objects.Location;
import org.woym.common.objects.Meeting;
import org.woym.common.objects.MeetingType;
import org.woym.common.objects.Room;
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
@ManagedBean(name = "meetingController")
public class MeetingController implements Serializable {

	private static final long serialVersionUID = 4875106571364509043L;
	
	private static Logger LOGGER = LogManager.getLogger(MeetingController.class);
	
	private DataAccess dataAccess = DataAccess.getInstance();
	private ActivityValidator activityValidator = ActivityValidator.getInstance();
	private ScheduleModelHolder scheduleModelHolder = ScheduleModelHolder.getInstance();
	
	private ActivityTOHolder activityTOHolder = ActivityTOHolder.getInstance();
	private EntityHelper entityHelper = EntityHelper.getInstance();

	private Meeting meeting;
	
	private Location location;

	@PostConstruct
	public void init() {
		meeting = new Meeting();
		
		ActivityTO activityTO = activityTOHolder.getActivityTO();
		meeting.setTime(activityTO.getTimePeriod());
		meeting.setMeetingType(getAllMeetingTypes().get(0));

		if(entityHelper.getTeacher() != null) {
			EmployeeTimePeriods employeeTimePeriods = new EmployeeTimePeriods();
			employeeTimePeriods.add(activityTO.getTimePeriod());
			meeting.add(employeeTimePeriods);
		}
		
		if(entityHelper.getRoom() != null) {
			location = entityHelper.getLocation();
			setMeetingRoom(entityHelper.getRoom());
		}
	}
	
	public void addMeeting() {
		IStatus status = activityValidator.validateActivity(meeting,
				meeting.getTime());

		if (status instanceof SuccessStatus) {
			AddCommand<Meeting> command = new AddCommand<Meeting>(meeting);
			status = CommandHandler.getInstance().execute(command);

			if (status instanceof SuccessStatus) {
				init();
			}
		}

		scheduleModelHolder.updateScheduleModel();

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
		return getRoomsForLocation().get(0);
	}
	
	public List<Room> getRoomsForLocation() {
		return location.getRooms();
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
		meeting.setMeetingType(meetingType);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(meeting.getTime().getStartTime());
		calendar.set(Calendar.MINUTE,
				(calendar.get(Calendar.MINUTE) + meetingType
						.getTypicalDuration()));

		TimePeriod timePeriod = meeting.getTime();
		timePeriod.setEndTime(calendar.getTime());
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
