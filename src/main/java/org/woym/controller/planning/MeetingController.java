package org.woym.controller.planning;

import java.io.Serializable;
import java.util.Calendar;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.woym.common.objects.Meeting;
import org.woym.common.objects.MeetingType;
import org.woym.common.objects.TimePeriod;
import org.woym.common.objects.Weekday;

@ViewScoped
@ManagedBean(name = "meetingController")
public class MeetingController implements Serializable {

	private static final long serialVersionUID = 4875106571364509043L;

	private Meeting meeting;

	@PostConstruct
	public void init() {
		meeting = new Meeting();
		MeetingType meetingType = new MeetingType();
		meeting.setMeetingType(meetingType);

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

}
