package org.woym.common.objects;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("LessonType")
public class LessonType extends ActivityType {

	private static final long serialVersionUID = -4953764389342004235L;
	
	public LessonType(){
	}	
	
}
