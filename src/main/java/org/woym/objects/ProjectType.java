package org.woym.objects;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("ProjectType")
public class ProjectType extends ActivityType{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6492312501506953749L;

	public ProjectType(){
	}
	
}
