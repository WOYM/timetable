package org.woym.spec.persistence;

import java.util.List;

import org.woym.exceptions.DatasetException;
import org.woym.objects.ActivityType;
import org.woym.objects.Meeting;
import org.woym.objects.Project;
import org.woym.objects.Subject;

public interface IActivityTypeDAO extends IAbstractDAO<ActivityType>{

	public List<Subject> getAllSubjects() throws DatasetException;
	
	public List<Project> getAllProjects() throws DatasetException;
	
	public List<Meeting> getAllMeetings() throws DatasetException;
	
}
