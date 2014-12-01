package org.woym.objects;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Diese Klasse repräsentiert eine Sitzung des Personals.
 * 
 * @author Adrian
 *
 */
@Entity
@DiscriminatorValue("Meeting")
public class Meeting extends Activity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3347521812061561725L;

	private MeetingType meetingType;
	
	public Meeting() {
	}

	public MeetingType getMeetingType() {
		return meetingType;
	}

	public void setMeetingType(MeetingType meetingType) {
		this.meetingType = meetingType;
	}
}
