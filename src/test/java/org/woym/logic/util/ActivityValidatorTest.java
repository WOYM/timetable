package org.woym.logic.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;
import org.woym.common.exceptions.DatasetException;
import org.woym.common.objects.Activity;
import org.woym.common.objects.Employee;
import org.woym.common.objects.EmployeeTimePeriods;
import org.woym.common.objects.Location;
import org.woym.common.objects.Pause;
import org.woym.common.objects.Room;
import org.woym.common.objects.Schoolclass;
import org.woym.common.objects.TimePeriod;
import org.woym.common.objects.TravelTimeList;
import org.woym.common.objects.TravelTimeList.Edge;
import org.woym.common.objects.Weekday;
import org.woym.logic.FailureStatus;
import org.woym.logic.SuccessStatus;
import org.woym.persistence.DataAccess;

@Test(groups = "unit")
public class ActivityValidatorTest extends PowerMockTestCase {

	@Mock
	DataAccess dataAccess;

	@Mock
	TravelTimeList travelTimeList;

	@InjectMocks
	ActivityValidator activityValidator = Mockito.spy(ActivityValidator
			.getInstance());

	@Test
	public void getInstanceTest() {
		AssertJUnit.assertNotNull(ActivityValidator.getInstance());
	}

	@Test
	public void datasetExceptionEmployeesTest() throws DatasetException {
		// Given
		Activity activity = Mockito.mock(Pause.class);

		EmployeeTimePeriods period = Mockito.mock(EmployeeTimePeriods.class);
		List<EmployeeTimePeriods> periods = new ArrayList<>();
		periods.add(period);
		List<TimePeriod> timePeriods = new ArrayList<TimePeriod>();
		TimePeriod timePeriod = Mockito.mock(TimePeriod.class);
		timePeriods.add(timePeriod);

		Room room = Mockito.mock(Room.class);
		List<Room> rooms = new ArrayList<Room>();
		rooms.add(room);

		// When
		PowerMockito.when(timePeriod.getDay()).thenReturn(Weekday.MONDAY);
		PowerMockito.when(timePeriod.getStartTime()).thenReturn(new Date());
		PowerMockito.when(timePeriod.getEndTime()).thenReturn(new Date());
		PowerMockito.when(activity.getEmployeeTimePeriods())
				.thenReturn(periods);
		PowerMockito.when(period.getTimePeriods()).thenReturn(timePeriods);

		PowerMockito.when(activity.getRooms()).thenReturn(rooms);
		PowerMockito.when(dataAccess.getOneLocation(room)).thenThrow(
				new DatasetException("Exception"));

		// Then
		AssertJUnit
				.assertTrue(
						"A FailureStatus is expected if the database threw an exception",
						activityValidator
								.validateActivity(activity, timePeriod) instanceof FailureStatus);
	}

	@Test
	public void datasetExceptionSchoolclassesTest() throws DatasetException {
		// Given
		Activity activity = Mockito.mock(Activity.class);
		TimePeriod timePeriod = Mockito.mock(TimePeriod.class);

		Schoolclass schoolclass = Mockito.mock(Schoolclass.class);
		List<Schoolclass> schoolclasses = new ArrayList<>();
		schoolclasses.add(schoolclass);

		Room room = Mockito.mock(Room.class);
		List<Room> rooms = new ArrayList<Room>();
		rooms.add(room);

		// When
		PowerMockito.when(timePeriod.getDay()).thenReturn(Weekday.MONDAY);
		PowerMockito.when(timePeriod.getStartTime()).thenReturn(new Date());
		PowerMockito.when(timePeriod.getEndTime()).thenReturn(new Date());
		PowerMockito.when(activity.getSchoolclasses())
				.thenReturn(schoolclasses);
		PowerMockito.when(activity.getRooms()).thenReturn(rooms);
		PowerMockito.when(dataAccess.getOneLocation(room)).thenThrow(
				new DatasetException("Exception"));

		// Then
		AssertJUnit
				.assertTrue(
						"A FailureStatus is expected if the database threw an exception",
						activityValidator
								.validateActivity(activity, timePeriod) instanceof FailureStatus);
	}

	@Test
	public void datasetExceptionRoomsTest() throws DatasetException {
		// Given
		Activity activity = Mockito.mock(Activity.class);
		TimePeriod timePeriod = Mockito.mock(TimePeriod.class);

		Room room = Mockito.mock(Room.class);
		List<Room> rooms = new ArrayList<>();
		rooms.add(room);

		// When
		PowerMockito.when(activity.getRooms()).thenReturn(rooms);
		PowerMockito.when(dataAccess.getAllActivities(room, timePeriod))
				.thenThrow(new DatasetException("Exception"));

		// Then
		AssertJUnit
				.assertTrue(
						"A FailureStatus is expected if the database threw an exception",
						activityValidator
								.validateActivity(activity, timePeriod) instanceof FailureStatus);
	}

	@Test
	public void validActivityEmployeeTest() throws DatasetException {
		// Given
		Activity activity = Mockito.mock(Activity.class);
		Activity beforeActivity = Mockito.mock(Activity.class);
		Activity afterActivity = Mockito.mock(Activity.class);

		EmployeeTimePeriods period = Mockito.mock(EmployeeTimePeriods.class);
		List<EmployeeTimePeriods> periods = new ArrayList<>();
		periods.add(period);

		TimePeriod expectedTimePeriod = Mockito.mock(TimePeriod.class);

		Edge edge = Mockito.mock(Edge.class);

		List<Activity> beforeList = Arrays.asList(beforeActivity);
		List<Activity> afterList = Arrays.asList(afterActivity);
		List<Activity> fullList = new ArrayList<>();
		fullList.addAll(beforeList);
		fullList.addAll(afterList);

		Room room = Mockito.mock(Room.class);
		List<Room> rooms = new ArrayList<Room>();
		rooms.add(room);

		Location location = Mockito.mock(Location.class);

		// When
		PowerMockito.when(expectedTimePeriod.getDay()).thenReturn(
				Weekday.MONDAY);
		PowerMockito.when(expectedTimePeriod.getStartTime()).thenReturn(
				new Date());
		PowerMockito.when(expectedTimePeriod.getEndTime()).thenReturn(
				new Date());
		PowerMockito.when(activity.getRooms()).thenReturn(rooms);
		PowerMockito.when(beforeActivity.getRooms()).thenReturn(rooms);
		PowerMockito.when(afterActivity.getRooms()).thenReturn(rooms);
		PowerMockito.when(dataAccess.getOneLocation(room)).thenReturn(location);
		PowerMockito.when(activity.getEmployeeTimePeriods())
				.thenReturn(periods);
		PowerMockito.when(
				dataAccess.getAllActivitiesBefore(Mockito.any(Employee.class),
						Mockito.any(TimePeriod.class))).thenReturn(beforeList);
		PowerMockito.when(
				dataAccess.getAllActivitiesAfter(Mockito.any(Employee.class),
						Mockito.any(TimePeriod.class))).thenReturn(afterList);
		PowerMockito.when(
				dataAccess.getAllActivities(Mockito.any(Employee.class),
						Mockito.any(Weekday.class))).thenReturn(fullList);
		PowerMockito.when(
				travelTimeList.getEdge(Mockito.any(Location.class),
						Mockito.any(Location.class))).thenReturn(edge);
		PowerMockito.when(edge.getDistance()).thenReturn(0);

		// Then
		AssertJUnit
				.assertTrue(
						"A SuccessStatus is expected if the database returnes the same count of  activities as before and after",
						activityValidator.validateActivityEmployees(activity,
								expectedTimePeriod) instanceof SuccessStatus);

	}

	@Test
	public void invalidActivityEmployeeTest() throws DatasetException {
		// Given
		Activity activity = Mockito.mock(Pause.class);
		Activity anotherActivity = Mockito.mock(Activity.class);
		Activity beforeActivity = Mockito.mock(Activity.class);
		Activity afterActivity = Mockito.mock(Activity.class);

		EmployeeTimePeriods period = Mockito.mock(EmployeeTimePeriods.class);
		List<EmployeeTimePeriods> periods = new ArrayList<>();
		periods.add(period);

		TimePeriod expectedTimePeriod = Mockito.mock(TimePeriod.class);

		List<Activity> beforeList = Arrays.asList(beforeActivity);
		List<Activity> afterList = Arrays.asList(afterActivity);
		List<Activity> fullList = Arrays.asList(beforeActivity,
				anotherActivity, afterActivity);

		Room room = Mockito.mock(Room.class);
		List<Room> rooms = new ArrayList<Room>();
		rooms.add(room);

		Location location = Mockito.mock(Location.class);

		// When
		PowerMockito.when(expectedTimePeriod.getDay()).thenReturn(
				Weekday.MONDAY);
		PowerMockito.when(expectedTimePeriod.getStartTime()).thenReturn(
				new Date());
		PowerMockito.when(expectedTimePeriod.getEndTime()).thenReturn(
				new Date());
		PowerMockito.when(activity.getRooms()).thenReturn(rooms);
		PowerMockito.when(dataAccess.getOneLocation(room)).thenReturn(location);
		PowerMockito.when(beforeActivity.getRooms()).thenReturn(rooms);
		PowerMockito.when(afterActivity.getRooms()).thenReturn(rooms);
		PowerMockito.when(activity.getEmployeeTimePeriods())
				.thenReturn(periods);
		PowerMockito.when(
				dataAccess.getAllActivitiesBefore(Mockito.any(Employee.class),
						Mockito.any(TimePeriod.class))).thenReturn(beforeList);
		PowerMockito.when(
				dataAccess.getAllActivitiesAfter(Mockito.any(Employee.class),
						Mockito.any(TimePeriod.class))).thenReturn(afterList);
		PowerMockito.when(
				dataAccess.getAllActivities(Mockito.any(Employee.class),
						Mockito.any(Weekday.class))).thenReturn(fullList);

		// Then
		AssertJUnit
				.assertTrue(
						"A FailureStatus is expected if the database returnes a bigger count of activities as before and after",
						activityValidator.validateActivity(activity,
								expectedTimePeriod) instanceof FailureStatus);
		AssertJUnit
				.assertTrue(
						"A FailureStatus is expected if the database returnes a bigger count of activities as before and after",
						activityValidator.validateActivityEmployees(activity,
								expectedTimePeriod) instanceof FailureStatus);
	}

	@Test
	public void validActivityEmployeeTestWithSameActivity()
			throws DatasetException {
		// Given
		Activity activity = Mockito.mock(Activity.class);
		Activity beforeActivity = Mockito.mock(Activity.class);
		Activity afterActivity = Mockito.mock(Activity.class);

		EmployeeTimePeriods period = Mockito.mock(EmployeeTimePeriods.class);
		List<EmployeeTimePeriods> periods = new ArrayList<>();
		periods.add(period);

		TimePeriod expectedTimePeriod = Mockito.mock(TimePeriod.class);

		List<Activity> beforeList = Arrays.asList(beforeActivity);
		List<Activity> afterList = Arrays.asList(afterActivity);
		List<Activity> fullList = Arrays.asList(beforeActivity, activity,
				afterActivity);

		Room room = Mockito.mock(Room.class);
		List<Room> rooms = new ArrayList<Room>();
		rooms.add(room);

		Location location = Mockito.mock(Location.class);

		// When
		PowerMockito.when(expectedTimePeriod.getDay()).thenReturn(
				Weekday.MONDAY);
		PowerMockito.when(expectedTimePeriod.getStartTime()).thenReturn(
				new Date());
		PowerMockito.when(expectedTimePeriod.getEndTime()).thenReturn(
				new Date());
		PowerMockito.when(activity.getEmployeeTimePeriods())
				.thenReturn(periods);
		PowerMockito.when(activity.getRooms()).thenReturn(rooms);
		PowerMockito.when(dataAccess.getOneLocation(room)).thenReturn(location);
		PowerMockito.when(beforeActivity.getRooms()).thenReturn(rooms);
		PowerMockito.when(afterActivity.getRooms()).thenReturn(rooms);
		PowerMockito.when(activity.getEmployeeTimePeriods())
				.thenReturn(periods);
		PowerMockito.when(
				dataAccess.getAllActivitiesBefore(Mockito.any(Employee.class),
						Mockito.any(TimePeriod.class))).thenReturn(beforeList);
		PowerMockito.when(
				dataAccess.getAllActivitiesAfter(Mockito.any(Employee.class),
						Mockito.any(TimePeriod.class))).thenReturn(afterList);
		PowerMockito.when(
				dataAccess.getAllActivities(Mockito.any(Employee.class),
						Mockito.any(Weekday.class))).thenReturn(fullList);

		// Then
		AssertJUnit
				.assertTrue(
						"A FailureStatus is expected if the database returnes a bigger count of activities as before and after",
						activityValidator.validateActivityEmployees(activity,
								expectedTimePeriod) instanceof SuccessStatus);
	}

	@Test
	public void validActivitySchoolclassTest() throws DatasetException {
		// Given
		Activity activity = Mockito.mock(Activity.class);
		Activity beforeActivity = Mockito.mock(Pause.class);
		Activity afterActivity = Mockito.mock(Pause.class);
		TimePeriod timePeriod = Mockito.mock(TimePeriod.class);

		Schoolclass schoolclass = Mockito.mock(Schoolclass.class);
		List<Schoolclass> schoolclasses = new ArrayList<>();
		schoolclasses.add(schoolclass);
		
		Date time = new Date();

		Room room = Mockito.mock(Room.class);
		List<Room> rooms = new ArrayList<Room>();
		rooms.add(room);

		Location location = Mockito.mock(Location.class);

		Edge edge = Mockito.mock(Edge.class);

		// When
		PowerMockito.when(timePeriod.getDay()).thenReturn(Weekday.MONDAY);
		PowerMockito.when(timePeriod.getStartTime()).thenReturn(time);
		PowerMockito.when(timePeriod.getEndTime()).thenReturn(time);
		PowerMockito.when(timePeriod.getDuration()).thenReturn(0);
		PowerMockito.when(activity.getRooms()).thenReturn(rooms);
		PowerMockito.when(beforeActivity.getRooms()).thenReturn(rooms);
		PowerMockito.when(afterActivity.getRooms()).thenReturn(rooms);
		PowerMockito.when(beforeActivity.getTime()).thenReturn(timePeriod);
		PowerMockito.when(afterActivity.getTime()).thenReturn(timePeriod);
		PowerMockito.when(dataAccess.getOneLocation(room)).thenReturn(location);
		PowerMockito.when(activity.getSchoolclasses())
				.thenReturn(schoolclasses);
		PowerMockito.when(
				dataAccess.getFirstActivityBefore(
						Mockito.any(Schoolclass.class),
						Mockito.any(TimePeriod.class),
						Mockito.any(Location.class), Mockito.anyBoolean()))
				.thenReturn(beforeActivity);
		PowerMockito.when(
				dataAccess.getFirstActivityAfter(
						Mockito.any(Schoolclass.class),
						Mockito.any(TimePeriod.class),
						Mockito.any(Location.class), Mockito.anyBoolean()))
				.thenReturn(afterActivity);
		PowerMockito.when(dataAccess.getAllActivities(schoolclass, timePeriod))
				.thenReturn(new ArrayList<Activity>());
		PowerMockito.when(
				travelTimeList.getEdge(Mockito.any(Location.class),
						Mockito.any(Location.class))).thenReturn(edge);
		PowerMockito.when(edge.getDistance()).thenReturn(0);

		// Then
		AssertJUnit
				.assertTrue(
						"A SuccessStatus is expected if the database returned matching activities but the only matching activity is the given activity",
						activityValidator
								.validateActivity(activity, timePeriod) instanceof SuccessStatus);
		AssertJUnit
				.assertTrue(
						"A SuccessStatus is expected if the database returned no matching activities",
						activityValidator.validateActivitySchoolclasses(
								activity, timePeriod) instanceof SuccessStatus);

	}

	@Test
	public void invalidActivitySchoolclassBeforeActivtyPauseTest() throws DatasetException {
		// Given
		Activity activity = Mockito.mock(Activity.class);
		Activity beforeActivity = Mockito.mock(Pause.class);
		Activity afterActivity = Mockito.mock(Activity.class);
		TimePeriod timePeriod = Mockito.mock(TimePeriod.class);

		Schoolclass schoolclass = Mockito.mock(Schoolclass.class);
		List<Schoolclass> schoolclasses = new ArrayList<>();
		schoolclasses.add(schoolclass);

		Activity activity2 = Mockito.mock(Activity.class);
		List<Activity> activities = new ArrayList<>();
		activities.add(activity2);

		Room room = Mockito.mock(Room.class);
		List<Room> rooms = new ArrayList<Room>();
		rooms.add(room);
		
		Date time = new Date();

		Location location = Mockito.mock(Location.class);
		
		Edge edge = Mockito.mock(Edge.class);

		// When
		PowerMockito.when(timePeriod.getDay()).thenReturn(Weekday.MONDAY);
		PowerMockito.when(timePeriod.getStartTime()).thenReturn(time);
		PowerMockito.when(timePeriod.getEndTime()).thenReturn(time);
		PowerMockito.when(timePeriod.getDuration()).thenReturn(0);
		PowerMockito.when(activity.getRooms()).thenReturn(rooms);
		PowerMockito.when(activity.getTime()).thenReturn(timePeriod);
		PowerMockito.when(beforeActivity.getRooms()).thenReturn(rooms);
		PowerMockito.when(afterActivity.getRooms()).thenReturn(rooms);
		PowerMockito.when(beforeActivity.getTime()).thenReturn(timePeriod);
		PowerMockito.when(afterActivity.getTime()).thenReturn(timePeriod);
		PowerMockito.when(dataAccess.getOneLocation(room)).thenReturn(location);
		PowerMockito.when(activity.getSchoolclasses())
				.thenReturn(schoolclasses);
		PowerMockito.when(
				dataAccess.getFirstActivityBefore(
						Mockito.any(Schoolclass.class),
						Mockito.any(TimePeriod.class),
						Mockito.any(Location.class), Mockito.anyBoolean()))
				.thenReturn(beforeActivity);
		PowerMockito.when(
				dataAccess.getFirstActivityAfter(
						Mockito.any(Schoolclass.class),
						Mockito.any(TimePeriod.class),
						Mockito.any(Location.class), Mockito.anyBoolean()))
				.thenReturn(afterActivity);
		PowerMockito.when(
				dataAccess.getAllActivities(Mockito.any(Schoolclass.class),
						Mockito.any(TimePeriod.class))).thenReturn(activities);
		PowerMockito.when(beforeActivity.getRooms()).thenReturn(rooms);
		PowerMockito.when(afterActivity.getRooms()).thenReturn(rooms);
		PowerMockito.when(
				travelTimeList.getEdge(Mockito.any(Location.class),
						Mockito.any(Location.class))).thenReturn(edge);
		PowerMockito.when(edge.getDistance()).thenReturn(10);
		// Then
		AssertJUnit
				.assertTrue(
						"A FailureStatus is expected if the database returned matching activities",
						activityValidator
								.validateActivity(activity, timePeriod) instanceof FailureStatus);
	}
	
	@Test
	public void invalidActivitySchoolclassAfterActivtyPauseTest() throws DatasetException {
		// Given
		Activity activity = Mockito.mock(Activity.class);
		Activity beforeActivity = Mockito.mock(Activity.class);
		Activity afterActivity = Mockito.mock(Pause.class);
		TimePeriod timePeriod = Mockito.mock(TimePeriod.class);

		Schoolclass schoolclass = Mockito.mock(Schoolclass.class);
		List<Schoolclass> schoolclasses = new ArrayList<>();
		schoolclasses.add(schoolclass);

		Activity activity2 = Mockito.mock(Activity.class);
		List<Activity> activities = new ArrayList<>();
		activities.add(activity2);

		Room room = Mockito.mock(Room.class);
		List<Room> rooms = new ArrayList<Room>();
		rooms.add(room);
		
		Date time = new Date();

		Location location = Mockito.mock(Location.class);
		
		Edge edge = Mockito.mock(Edge.class);

		// When
		PowerMockito.when(timePeriod.getDay()).thenReturn(Weekday.MONDAY);
		PowerMockito.when(timePeriod.getStartTime()).thenReturn(time);
		PowerMockito.when(timePeriod.getEndTime()).thenReturn(time);
		PowerMockito.when(timePeriod.getDuration()).thenReturn(0);
		PowerMockito.when(activity.getRooms()).thenReturn(rooms);
		PowerMockito.when(activity.getTime()).thenReturn(timePeriod);
		PowerMockito.when(beforeActivity.getRooms()).thenReturn(rooms);
		PowerMockito.when(afterActivity.getRooms()).thenReturn(rooms);
		PowerMockito.when(beforeActivity.getTime()).thenReturn(timePeriod);
		PowerMockito.when(afterActivity.getTime()).thenReturn(timePeriod);
		PowerMockito.when(dataAccess.getOneLocation(room)).thenReturn(location);
		PowerMockito.when(activity.getSchoolclasses())
				.thenReturn(schoolclasses);
		PowerMockito.when(
				dataAccess.getFirstActivityBefore(
						Mockito.any(Schoolclass.class),
						Mockito.any(TimePeriod.class),
						Mockito.any(Location.class), Mockito.anyBoolean()))
				.thenReturn(beforeActivity);
		PowerMockito.when(
				dataAccess.getFirstActivityAfter(
						Mockito.any(Schoolclass.class),
						Mockito.any(TimePeriod.class),
						Mockito.any(Location.class), Mockito.anyBoolean()))
				.thenReturn(afterActivity);
		PowerMockito.when(
				dataAccess.getAllActivities(Mockito.any(Schoolclass.class),
						Mockito.any(TimePeriod.class))).thenReturn(activities);
		PowerMockito.when(beforeActivity.getRooms()).thenReturn(rooms);
		PowerMockito.when(afterActivity.getRooms()).thenReturn(rooms);
		PowerMockito.when(
				travelTimeList.getEdge(Mockito.any(Location.class),
						Mockito.any(Location.class))).thenReturn(edge);
		PowerMockito.when(edge.getDistance()).thenReturn(10);
		// Then
		AssertJUnit
				.assertTrue(
						"A FailureStatus is expected if the database returned matching activities",
						activityValidator
								.validateActivity(activity, timePeriod) instanceof FailureStatus);
	}
	
	@Test
	public void invalidActivitySchoolclassWithoutPauseTest() throws DatasetException {
		// Given
		Activity activity = Mockito.mock(Activity.class);
		Activity beforeActivity = Mockito.mock(Activity.class);
		Activity afterActivity = Mockito.mock(Activity.class);
		TimePeriod timePeriod = Mockito.mock(TimePeriod.class);

		Schoolclass schoolclass = Mockito.mock(Schoolclass.class);
		List<Schoolclass> schoolclasses = new ArrayList<>();
		schoolclasses.add(schoolclass);

		Activity activity2 = Mockito.mock(Activity.class);
		List<Activity> activities = new ArrayList<>();
		activities.add(activity2);

		Room room = Mockito.mock(Room.class);
		List<Room> rooms = new ArrayList<Room>();
		rooms.add(room);
		
		Date time = new Date();

		Location location = Mockito.mock(Location.class);
		
		Edge edge = Mockito.mock(Edge.class);

		// When
		PowerMockito.when(timePeriod.getDay()).thenReturn(Weekday.MONDAY);
		PowerMockito.when(timePeriod.getStartTime()).thenReturn(time);
		PowerMockito.when(timePeriod.getEndTime()).thenReturn(time);
		PowerMockito.when(timePeriod.getDuration()).thenReturn(0);
		PowerMockito.when(activity.getRooms()).thenReturn(rooms);
		PowerMockito.when(activity.getTime()).thenReturn(timePeriod);
		PowerMockito.when(beforeActivity.getRooms()).thenReturn(rooms);
		PowerMockito.when(afterActivity.getRooms()).thenReturn(rooms);
		PowerMockito.when(dataAccess.getOneLocation(room)).thenReturn(location);
		PowerMockito.when(activity.getSchoolclasses())
				.thenReturn(schoolclasses);
		PowerMockito.when(
				dataAccess.getFirstActivityBefore(
						Mockito.any(Schoolclass.class),
						Mockito.any(TimePeriod.class),
						Mockito.any(Location.class), Mockito.anyBoolean()))
				.thenReturn(beforeActivity);
		PowerMockito.when(
				dataAccess.getFirstActivityAfter(
						Mockito.any(Schoolclass.class),
						Mockito.any(TimePeriod.class),
						Mockito.any(Location.class), Mockito.anyBoolean()))
				.thenReturn(afterActivity);
		PowerMockito.when(
				dataAccess.getAllActivities(Mockito.any(Schoolclass.class),
						Mockito.any(TimePeriod.class))).thenReturn(activities);
		PowerMockito.when(beforeActivity.getRooms()).thenReturn(rooms);
		PowerMockito.when(afterActivity.getRooms()).thenReturn(rooms);
		PowerMockito.when(
				travelTimeList.getEdge(Mockito.any(Location.class),
						Mockito.any(Location.class))).thenReturn(edge);
		PowerMockito.when(edge.getDistance()).thenReturn(10);
		// Then
		AssertJUnit
				.assertTrue(
						"A FailureStatus is expected if the database returned matching activities",
						activityValidator
								.validateActivity(activity, timePeriod) instanceof FailureStatus);
	}

	@Test
	public void validActivitySchoolclassSameActivityTest()
			throws DatasetException {
		// Given
		Activity activity = Mockito.mock(Activity.class);
		Activity beforeActivity = Mockito.mock(Activity.class);
		Activity afterActivity = Mockito.mock(Activity.class);
		TimePeriod timePeriod = Mockito.mock(TimePeriod.class);

		Schoolclass schoolclass = Mockito.mock(Schoolclass.class);
		List<Schoolclass> schoolclasses = new ArrayList<>();
		schoolclasses.add(schoolclass);

		List<Activity> activities = new ArrayList<>();
		activities.add(activity);

		Room room = Mockito.mock(Room.class);
		List<Room> rooms = new ArrayList<Room>();
		rooms.add(room);

		Location location = Mockito.mock(Location.class);

		// When
		PowerMockito.when(timePeriod.getDay()).thenReturn(Weekday.MONDAY);
		PowerMockito.when(timePeriod.getStartTime()).thenReturn(new Date());
		PowerMockito.when(timePeriod.getEndTime()).thenReturn(new Date());
		PowerMockito.when(activity.getRooms()).thenReturn(rooms);
		PowerMockito.when(beforeActivity.getRooms()).thenReturn(rooms);
		PowerMockito.when(afterActivity.getRooms()).thenReturn(rooms);
		PowerMockito.when(dataAccess.getOneLocation(room)).thenReturn(location);
		PowerMockito.when(activity.getSchoolclasses())
				.thenReturn(schoolclasses);
		PowerMockito.when(
				dataAccess.getFirstActivityBefore(
						Mockito.any(Schoolclass.class),
						Mockito.any(TimePeriod.class),
						Mockito.any(Location.class), Mockito.anyBoolean()))
				.thenReturn(beforeActivity);
		PowerMockito.when(
				dataAccess.getFirstActivityAfter(
						Mockito.any(Schoolclass.class),
						Mockito.any(TimePeriod.class),
						Mockito.any(Location.class), Mockito.anyBoolean()))
				.thenReturn(afterActivity);
		PowerMockito.when(activity.getSchoolclasses())
				.thenReturn(schoolclasses);
		PowerMockito.when(dataAccess.getAllActivities(schoolclass, timePeriod))
				.thenReturn(activities);

		// Then
		AssertJUnit
				.assertTrue(
						"A SuccessStatus is expected if the database returned matching activities but the only matching activity is the given activity",
						activityValidator
								.validateActivity(activity, timePeriod) instanceof SuccessStatus);
	}

	@Test
	public void validActivityRoomTest() throws DatasetException {
		// Given
		Activity activity = Mockito.mock(Activity.class);
		TimePeriod timePeriod = Mockito.mock(TimePeriod.class);

		Room room = Mockito.mock(Room.class);
		List<Room> rooms = new ArrayList<>();
		rooms.add(room);

		// When
		PowerMockito.when(activity.getRooms()).thenReturn(rooms);
		PowerMockito.when(dataAccess.getAllActivities(room, timePeriod))
				.thenReturn(new ArrayList<Activity>());

		// Then
		AssertJUnit
				.assertTrue(
						"A SuccessStatus is expected if the database returned matching activities but the only matching activity is the given activity",
						activityValidator
								.validateActivity(activity, timePeriod) instanceof SuccessStatus);
		AssertJUnit
				.assertTrue(
						"A SuccessStatus is expected if the database returned no matching activities",
						activityValidator.validateActivityRooms(activity,
								timePeriod) instanceof SuccessStatus);

	}

	@Test
	public void invalidActivityRoomTest() throws DatasetException {
		// Given
		Activity activity = Mockito.mock(Activity.class);
		TimePeriod timePeriod = Mockito.mock(TimePeriod.class);

		Room room = Mockito.mock(Room.class);
		List<Room> rooms = new ArrayList<>();
		rooms.add(room);

		Activity activity2 = Mockito.mock(Activity.class);
		List<Activity> activities = new ArrayList<>();
		activities.add(activity2);

		// When
		PowerMockito.when(activity.getRooms()).thenReturn(rooms);
		PowerMockito.when(dataAccess.getAllActivities(room, timePeriod))
				.thenReturn(activities);

		// Then
		AssertJUnit
				.assertTrue(
						"A SuccessStatus is expected if the database returned matching activities but the only matching activity is the given activity",
						activityValidator
								.validateActivity(activity, timePeriod) instanceof FailureStatus);
		AssertJUnit
				.assertTrue(
						"A FailureStatus is expected if the database returned matching activities",
						activityValidator.validateActivityRooms(activity,
								timePeriod) instanceof FailureStatus);
	}

	@Test
	public void validActivityRoomSameActivityTest() throws DatasetException {
		// Given
		Activity activity = Mockito.mock(Activity.class);
		TimePeriod timePeriod = Mockito.mock(TimePeriod.class);

		Room room = Mockito.mock(Room.class);
		List<Room> rooms = new ArrayList<>();
		rooms.add(room);

		List<Activity> activities = new ArrayList<>();
		activities.add(activity);

		// When
		PowerMockito.when(activity.getRooms()).thenReturn(rooms);
		PowerMockito.when(dataAccess.getAllActivities(room, timePeriod))
				.thenReturn(activities);

		// Then
		AssertJUnit
				.assertTrue(
						"A SuccessStatus is expected if the database returned matching activities but the only matching activity is the given activity",
						activityValidator
								.validateActivity(activity, timePeriod) instanceof SuccessStatus);
		AssertJUnit
				.assertTrue(
						"A SuccessStatus is expected if the database returned matching activities but the only matching activity is the given activity",
						activityValidator.validateActivityRooms(activity,
								timePeriod) instanceof SuccessStatus);
	}

	// @Test
	// public void validExpandTimPeriodWhithTravelTimeWithNoExpansionTest()
	// throws Exception {
	// // Given
	// Activity activity = Mockito.mock(Activity.class);
	// Date date = Mockito.mock(Date.class);
	// Location location = Mockito.mock(Location.class);
	//
	// Room room = Mockito.mock(Room.class);
	// List<Room> rooms = new ArrayList<>();
	// rooms.add(room);
	//
	// EmployeeTimePeriods period = Mockito.mock(EmployeeTimePeriods.class);
	// List<EmployeeTimePeriods> periods = new ArrayList<>();
	// periods.add(period);
	//
	// TimePeriod expectedTimePeriod = Mockito.mock(TimePeriod.class);
	//
	// List<Activity> beforeList = new ArrayList<>();
	// List<Activity> afterList = new ArrayList<>();
	//
	// // When
	// PowerMockito.when(activity.getEmployeeTimePeriods())
	// .thenReturn(periods);
	// PowerMockito.when(
	// dataAccess.getAllActivitiesBefore(Mockito.any(Employee.class),
	// Mockito.any(TimePeriod.class))).thenReturn(beforeList);
	// PowerMockito.when(
	// dataAccess.getAllActivitiesAfter(Mockito.any(Employee.class),
	// Mockito.any(TimePeriod.class))).thenReturn(afterList);
	// PowerMockito.when(expectedTimePeriod.getStartTime()).thenReturn(date);
	// PowerMockito.when(expectedTimePeriod.getEndTime()).thenReturn(date);
	// PowerMockito.when(activity.getRooms()).thenReturn(rooms);
	//
	// PowerMockito.when(dataAccess.getOneLocation(Mockito.any(Room.class)))
	// .thenReturn(location);
	//
	// // Then
	// activityValidator.expandTimPeriodWhithTravelTime(activity,
	// expectedTimePeriod);
	//
	// Mockito.verify(dataAccess, Mockito.times(1)).getOneLocation(
	// Mockito.any(Room.class));
	//
	// }
	//
	// @Test
	// public void validExpandTimPeriodWhithTravelTimeWithNoEdgesTest()
	// throws Exception {
	// // Given
	// Activity activity = Mockito.mock(Activity.class);
	// Activity beforeActivity = Mockito.mock(Activity.class);
	// Activity afterActivity = Mockito.mock(Activity.class);
	// Date date = Mockito.mock(Date.class);
	// Location location = Mockito.mock(Location.class);
	//
	// Room room = Mockito.mock(Room.class);
	// List<Room> rooms = new ArrayList<>();
	// rooms.add(room);
	//
	// EmployeeTimePeriods period = Mockito.mock(EmployeeTimePeriods.class);
	// List<EmployeeTimePeriods> periods = new ArrayList<>();
	// periods.add(period);
	//
	// TimePeriod expectedTimePeriod = Mockito.mock(TimePeriod.class);
	//
	// List<Activity> beforeList = Arrays.asList(beforeActivity);
	// List<Activity> afterList = Arrays.asList(afterActivity);
	//
	// // When
	// PowerMockito.when(activity.getEmployeeTimePeriods())
	// .thenReturn(periods);
	// PowerMockito.when(
	// dataAccess.getAllActivitiesBefore(Mockito.any(Employee.class),
	// Mockito.any(TimePeriod.class))).thenReturn(beforeList);
	// PowerMockito.when(
	// dataAccess.getAllActivitiesAfter(Mockito.any(Employee.class),
	// Mockito.any(TimePeriod.class))).thenReturn(afterList);
	// PowerMockito.when(expectedTimePeriod.getStartTime()).thenReturn(date);
	// PowerMockito.when(expectedTimePeriod.getEndTime()).thenReturn(date);
	// PowerMockito.when(activity.getRooms()).thenReturn(rooms);
	// PowerMockito.when(beforeActivity.getRooms()).thenReturn(rooms);
	// PowerMockito.when(afterActivity.getRooms()).thenReturn(rooms);
	// PowerMockito.when(
	// travelTimeList.getEdge(Mockito.any(Location.class),
	// Mockito.any(Location.class))).thenReturn(null);
	//
	// PowerMockito.when(dataAccess.getOneLocation(Mockito.any(Room.class)))
	// .thenReturn(location);
	//
	// // Then
	// activityValidator.expandTimPeriodWhithTravelTime(activity,
	// expectedTimePeriod);
	//
	// Mockito.verify(dataAccess, Mockito.times(3)).getOneLocation(
	// Mockito.any(Room.class));
	// Mockito.verify(travelTimeList, Mockito.times(2)).getEdge(
	// Mockito.any(Location.class), Mockito.any(Location.class));
	// }
	//
	// @Test
	// public void validExpandTimPeriodWhithTravelTimeWithEdgesTest()
	// throws Exception {
	// // Given
	// Activity activity = Mockito.mock(Activity.class);
	// Activity beforeActivity = Mockito.mock(Activity.class);
	// Activity afterActivity = Mockito.mock(Activity.class);
	// Date date = Mockito.mock(Date.class);
	// Location location = Mockito.mock(Location.class);
	// Edge edge = Mockito.mock(Edge.class);
	//
	// Room room = Mockito.mock(Room.class);
	// List<Room> rooms = new ArrayList<>();
	// rooms.add(room);
	//
	// EmployeeTimePeriods period = Mockito.mock(EmployeeTimePeriods.class);
	// List<EmployeeTimePeriods> periods = new ArrayList<>();
	// periods.add(period);
	//
	// TimePeriod expectedTimePeriod = Mockito.mock(TimePeriod.class);
	//
	// List<Activity> beforeList = Arrays.asList(beforeActivity);
	// List<Activity> afterList = Arrays.asList(afterActivity);
	//
	// // When
	// PowerMockito.when(activity.getEmployeeTimePeriods())
	// .thenReturn(periods);
	// PowerMockito.when(
	// dataAccess.getAllActivitiesBefore(Mockito.any(Employee.class),
	// Mockito.any(TimePeriod.class))).thenReturn(beforeList);
	// PowerMockito.when(
	// dataAccess.getAllActivitiesAfter(Mockito.any(Employee.class),
	// Mockito.any(TimePeriod.class))).thenReturn(afterList);
	// PowerMockito.when(expectedTimePeriod.getStartTime()).thenReturn(date);
	// PowerMockito.when(expectedTimePeriod.getEndTime()).thenReturn(date);
	// PowerMockito.when(activity.getRooms()).thenReturn(rooms);
	// PowerMockito.when(beforeActivity.getRooms()).thenReturn(rooms);
	// PowerMockito.when(afterActivity.getRooms()).thenReturn(rooms);
	// PowerMockito.when(
	// travelTimeList.getEdge(Mockito.any(Location.class),
	// Mockito.any(Location.class))).thenReturn(edge);
	// PowerMockito.when(edge.getDistance()).thenReturn(0);
	//
	// PowerMockito.when(dataAccess.getOneLocation(Mockito.any(Room.class)))
	// .thenReturn(location);
	//
	// // Then
	// activityValidator.expandTimPeriodWhithTravelTime(activity,
	// expectedTimePeriod);
	//
	// Mockito.verify(dataAccess, Mockito.times(3)).getOneLocation(
	// Mockito.any(Room.class));
	// Mockito.verify(travelTimeList, Mockito.times(2)).getEdge(
	// Mockito.any(Location.class), Mockito.any(Location.class));
	// Mockito.verify(edge, Mockito.times(2)).getDistance();
	//
	// }

}
