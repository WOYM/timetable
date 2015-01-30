package org.woym.controller.planning;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.woym.common.exceptions.DatasetException;
import org.woym.common.objects.Meeting;
import org.woym.common.objects.MeetingType;
import org.woym.common.objects.TimePeriod;
import org.woym.common.objects.Weekday;
import org.woym.persistence.DataAccess;

@ViewScoped
@ManagedBean(name = "meetingController")
public class MeetingController implements Serializable {

	private static final long serialVersionUID = 4875106571364509043L;
	
	private static Logger LOGGER = LogManager.getLogger(MeetingController.class);
	
	private DataAccess dataAccess = DataAccess.getInstance();

	private Meeting meeting;

	@PostConstruct
	public void init() {
		meeting = new Meeting();
		MeetingType meetingType = getAllMeetingTypes().get(0);

		Calendar calendar = Calendar.getInstance();
		TimePeriod timePeriod = new TimePeriod();
		timePeriod.setStartTime(calendar.getTime());
		calendar.set(Calendar.MINUTE,
				(calendar.get(Calendar.MINUTE) + meetingType
						.getTypicalDuration()));
		timePeriod.setEndTime(calendar.getTime());
		timePeriod.setDay(Weekday.MONDAY);
		meeting.setTime(timePeriod);
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

}
