package org.woym.common.objects;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("MeetingType")
public class MeetingType extends ActivityType {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4739522394085773847L;

	public MeetingType() {
	}
}
