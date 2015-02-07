package org.woym.logic.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockTestCase;
import org.primefaces.model.ScheduleModel;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.woym.common.config.Config;
import org.woym.common.config.DefaultConfigEnum;
import org.woym.common.exceptions.DatasetException;
import org.woym.common.objects.Activity;
import org.woym.common.objects.ColorEnum;
import org.woym.common.objects.CompoundLesson;
import org.woym.common.objects.Lesson;
import org.woym.common.objects.LessonType;
import org.woym.common.objects.Meeting;
import org.woym.common.objects.MeetingType;
import org.woym.common.objects.Pause;
import org.woym.common.objects.PedagogicAssistant;
import org.woym.common.objects.Room;
import org.woym.common.objects.Schoolclass;
import org.woym.common.objects.Teacher;
import org.woym.common.objects.TimePeriod;
import org.woym.common.objects.Weekday;
import org.woym.persistence.DataAccess;
import org.woym.ui.util.ActivityParser;

@Test(groups = "unit")
@PowerMockIgnore("javax.management.*")
@PrepareForTest(Config.class)
public class ActivityParserTest extends PowerMockTestCase {

	List<Activity> activities;

	@Mock
	DataAccess dataAccess;

	@InjectMocks
	ActivityParser activityParser;
	
	TimePeriod time;

	@BeforeTest
	public void makeActivities() {
		activities = new ArrayList<>();

		Date startDate = new Date();
		Date endDate = new Date();
		Weekday weekday = Weekday.MONDAY;

		time = new TimePeriod();
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
		Pause activity4 = new Pause();
		activity4.setTime(time);

		activities.add(activity1);
		activities.add(activity2);
		activities.add(activity3);
		activities.add(activity4);
	}

	@Test
	public void testGetInstance() {
		AssertJUnit.assertNotNull(ActivityParser.getInstance());
	}
	
	@Test
	public void testgetEventUnknowActivity() throws Exception {

		Activity activity5 = Mockito.mock(Activity.class);
		
		List<Activity> act = Arrays.asList(activity5);
		
		Mockito.when(activity5.getTime()).thenReturn(time);
		ScheduleModel scheduleModel = activityParser.getActivityModel(act, true);
		
		AssertJUnit
		.assertEquals(
				"The ActivityParser is meant to return as many events as activities are given",
				act.size(), scheduleModel.getEvents().size());
	}

	@Test
	public void getActivityModelTeacherTest() throws DatasetException {
		PowerMockito.mockStatic(Config.class);
		PowerMockito.when(
				Config.getSingleStringValue(Mockito
						.any(DefaultConfigEnum.class))).thenReturn(
				ColorEnum.DEFAULT.getStyleClass());
		Teacher teacher = Mockito.mock(Teacher.class);
		PowerMockito.when(dataAccess.getAllActivities(teacher, true))
				.thenReturn(activities);

		ScheduleModel scheduleModel = activityParser.getActivityModel(teacher);

		AssertJUnit
				.assertEquals(
						"The ActivityParser is meant to return as many events as activities are given",
						activities.size(), scheduleModel.getEvents().size());
	}

	@Test
	public void getActivityModelTeacherExceptionTest() throws DatasetException {
		Teacher teacher = Mockito.mock(Teacher.class);
		PowerMockito.when(dataAccess.getAllActivities(teacher, true))
				.thenThrow(new DatasetException(""));

		ScheduleModel scheduleModel = activityParser.getActivityModel(teacher);

		AssertJUnit
				.assertEquals(
						"The ActivityParser is meant to return an empty list in case of DatasetException",
						0, scheduleModel.getEvents().size());
	}

	@Test
	public void getActivityModelSchoolclassTest() throws DatasetException {
		PowerMockito.mockStatic(Config.class);
		PowerMockito.when(
				Config.getSingleStringValue(Mockito
						.any(DefaultConfigEnum.class))).thenReturn(
				ColorEnum.DEFAULT.getStyleClass());
		Schoolclass schoolclass = Mockito.mock(Schoolclass.class);
		PowerMockito.when(dataAccess.getAllActivities(schoolclass, true))
				.thenReturn(activities);

		ScheduleModel scheduleModel = activityParser
				.getActivityModel(schoolclass);

		AssertJUnit
				.assertEquals(
						"The ActivityParser is meant to return as many events as activities are given",
						activities.size(), scheduleModel.getEvents().size());
	}

	@Test
	public void getActivityModelSchoolclassExceptionTest()
			throws DatasetException {
		Schoolclass schoolclass = Mockito.mock(Schoolclass.class);
		PowerMockito.when(dataAccess.getAllActivities(schoolclass, true))
				.thenThrow(new DatasetException(""));

		ScheduleModel scheduleModel = activityParser
				.getActivityModel(schoolclass);

		AssertJUnit
				.assertEquals(
						"The ActivityParser is meant to return an empty list in case of DatasetException",
						0, scheduleModel.getEvents().size());
	}

	@Test
	public void getActivityModelPATest() throws DatasetException {
		PowerMockito.mockStatic(Config.class);
		PowerMockito.when(
				Config.getSingleStringValue(Mockito
						.any(DefaultConfigEnum.class))).thenReturn(
				ColorEnum.DEFAULT.getStyleClass());
		PedagogicAssistant pedagogicAssistant = Mockito
				.mock(PedagogicAssistant.class);
		PowerMockito
				.when(dataAccess.getAllActivities(pedagogicAssistant, true))
				.thenReturn(activities);
		ScheduleModel scheduleModel = activityParser
				.getActivityModel(pedagogicAssistant);

		AssertJUnit
				.assertEquals(
						"The ActivityParser is meant to return as many events as activities are given",
						activities.size(), scheduleModel.getEvents().size());
	}

	@Test
	public void getActivityModelPAExceptionTest() throws DatasetException {
		PedagogicAssistant pedagogicAssistant = Mockito
				.mock(PedagogicAssistant.class);
		PowerMockito
				.when(dataAccess.getAllActivities(pedagogicAssistant, true))
				.thenThrow(new DatasetException(""));

		ScheduleModel scheduleModel = activityParser
				.getActivityModel(pedagogicAssistant);

		AssertJUnit
				.assertEquals(
						"The ActivityParser is meant to return an empty list in case of DatasetException",
						0, scheduleModel.getEvents().size());
	}

	@Test
	public void getActivityModelRoomTest() throws DatasetException {
		PowerMockito.mockStatic(Config.class);
		PowerMockito.when(
				Config.getSingleStringValue(Mockito
						.any(DefaultConfigEnum.class))).thenReturn(
				ColorEnum.DEFAULT.getStyleClass());
		Room room = Mockito.mock(Room.class);
		PowerMockito.when(dataAccess.getAllActivities(room, true)).thenReturn(
				activities);

		ScheduleModel scheduleModel = activityParser.getActivityModel(room);

		AssertJUnit
				.assertEquals(
						"The ActivityParser is meant to return as many events as activities are given",
						activities.size(), scheduleModel.getEvents().size());
	}

	@Test
	public void getActivityModelRoomExceptionTest() throws DatasetException {
		Room room = Mockito.mock(Room.class);
		PowerMockito.when(dataAccess.getAllActivities(room, true)).thenThrow(
				new DatasetException(""));

		ScheduleModel scheduleModel = activityParser.getActivityModel(room);

		AssertJUnit
				.assertEquals(
						"The ActivityParser is meant to return an empty list in case of DatasetException",
						0, scheduleModel.getEvents().size());
	}

}
