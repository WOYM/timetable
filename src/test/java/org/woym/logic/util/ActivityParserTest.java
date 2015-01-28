package org.woym.logic.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.testng.PowerMockTestCase;
import org.primefaces.model.ScheduleModel;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.woym.common.exceptions.DatasetException;
import org.woym.common.objects.Activity;
import org.woym.common.objects.ColorEnum;
import org.woym.common.objects.CompoundLesson;
import org.woym.common.objects.Lesson;
import org.woym.common.objects.LessonType;
import org.woym.common.objects.Meeting;
import org.woym.common.objects.MeetingType;
import org.woym.common.objects.PedagogicAssistant;
import org.woym.common.objects.Room;
import org.woym.common.objects.Schoolclass;
import org.woym.common.objects.Teacher;
import org.woym.common.objects.TimePeriod;
import org.woym.common.objects.Weekday;
import org.woym.persistence.DataAccess;

@Test(groups = "unit")
public class ActivityParserTest extends PowerMockTestCase {

	List<Activity> activities;
	
	@Mock
	DataAccess dataAccess;
	
	@InjectMocks
	ActivityParser activityParser;
	
	@BeforeTest
	public void makeActivities() {
		activities = new ArrayList<>();
		
		Date startDate = new Date();
		Date endDate = new Date();
		Weekday weekday = Weekday.MONDAY;
		
		TimePeriod time = new TimePeriod();
		time.setStartTime(startDate);
		time.setEndTime(endDate);
		time.setDay(weekday);
		
		LessonType lessonType = new LessonType();
		MeetingType meetingType = new MeetingType();
		
		lessonType.setColor(ColorEnum.ORANGE);
		meetingType.setColor(ColorEnum.BLUE_DARK);
		
		Lesson activity1 = new Lesson();
		activity1.setTime(time);
		activity1.setLessonType(lessonType);
		CompoundLesson activity2 = new CompoundLesson();
		activity2.setTime(time);
		Meeting activity3 = new Meeting();
		activity3.setMeetingType(meetingType);
		activity3.setTime(time);
		
		activities.add(activity1);
		activities.add(activity2);
		activities.add(activity3);
		
	}
	
	
	@Test
	public void testGetInstance() {
		AssertJUnit.assertNotNull(ActivityParser.getInstance());
	}
	
	@Test
	public void getActivityModelTeacherTest() throws DatasetException {
		Teacher teacher = Mockito.mock(Teacher.class);
		PowerMockito.when(dataAccess.getAllActivities(teacher)).thenReturn(activities);
		
		ScheduleModel scheduleModel = activityParser.getActivityModel(teacher);
		
		AssertJUnit.assertEquals("The ActivityParser is meant to return as many events as activities are given", activities.size(), scheduleModel.getEvents().size());
	}
	
	@Test
	public void getActivityModelTeacherExceptionTest() throws DatasetException {
		Teacher teacher = Mockito.mock(Teacher.class);
		PowerMockito.when(dataAccess.getAllActivities(teacher)).thenThrow(new DatasetException(""));
		
		ScheduleModel scheduleModel = activityParser.getActivityModel(teacher);
		
		AssertJUnit.assertEquals("The ActivityParser is meant to return an empty list in case of DatasetException", 0, scheduleModel.getEvents().size());
	}
	
	@Test
	public void getActivityModelSchoolclassTest() throws DatasetException {
		Schoolclass schoolclass = Mockito.mock(Schoolclass.class);
		PowerMockito.when(dataAccess.getAllActivities(schoolclass)).thenReturn(activities);
		
		ScheduleModel scheduleModel = activityParser.getActivityModel(schoolclass);
		
		AssertJUnit.assertEquals("The ActivityParser is meant to return as many events as activities are given", activities.size(), scheduleModel.getEvents().size());
	}
	
	@Test
	public void getActivityModelSchoolclassExceptionTest() throws DatasetException {
		Schoolclass schoolclass = Mockito.mock(Schoolclass.class);
		PowerMockito.when(dataAccess.getAllActivities(schoolclass)).thenThrow(new DatasetException(""));
		
		ScheduleModel scheduleModel = activityParser.getActivityModel(schoolclass);
		
		AssertJUnit.assertEquals("The ActivityParser is meant to return an empty list in case of DatasetException", 0, scheduleModel.getEvents().size());
	}
	
	@Test
	public void getActivityModelPATest() throws DatasetException {
		PedagogicAssistant pedagogicAssistant = Mockito.mock(PedagogicAssistant.class);
		PowerMockito.when(dataAccess.getAllActivities(pedagogicAssistant)).thenReturn(activities);
		
		ScheduleModel scheduleModel = activityParser.getActivityModel(pedagogicAssistant);
		
		AssertJUnit.assertEquals("The ActivityParser is meant to return as many events as activities are given", activities.size(), scheduleModel.getEvents().size());
	}
	
	@Test
	public void getActivityModelPAExceptionTest() throws DatasetException {
		PedagogicAssistant pedagogicAssistant = Mockito.mock(PedagogicAssistant.class);
		PowerMockito.when(dataAccess.getAllActivities(pedagogicAssistant)).thenThrow(new DatasetException(""));
		
		ScheduleModel scheduleModel = activityParser.getActivityModel(pedagogicAssistant);
		
		AssertJUnit.assertEquals("The ActivityParser is meant to return an empty list in case of DatasetException", 0, scheduleModel.getEvents().size());
	}
	
	
	@Test
	public void getActivityModelRoomTest() throws DatasetException {
		Room room = Mockito.mock(Room.class);
		PowerMockito.when(dataAccess.getAllActivities(room)).thenReturn(activities);
		
		ScheduleModel scheduleModel = activityParser.getActivityModel(room);
		
		AssertJUnit.assertEquals("The ActivityParser is meant to return as many events as activities are given", activities.size(), scheduleModel.getEvents().size());
	}
	
	@Test
	public void getActivityModelRoomExceptionTest() throws DatasetException {
		Room room = Mockito.mock(Room.class);
		PowerMockito.when(dataAccess.getAllActivities(room)).thenThrow(new DatasetException(""));
		
		ScheduleModel scheduleModel = activityParser.getActivityModel(room);
		
		AssertJUnit.assertEquals("The ActivityParser is meant to return an empty list in case of DatasetException", 0, scheduleModel.getEvents().size());
	}

}
