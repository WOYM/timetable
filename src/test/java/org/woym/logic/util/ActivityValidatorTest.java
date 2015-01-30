package org.woym.logic.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.transformers.MockTransformer;
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
	ActivityValidator activityValidator;

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

		// When
		PowerMockito.when(activity.getEmployeeTimePeriods())
				.thenReturn(periods);
		PowerMockito.when(period.getTimePeriods()).thenReturn(timePeriods);

		PowerMockito.when(
				dataAccess.getAllActivitiesBefore(period.getEmployee(),
						timePeriod)).thenThrow(
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

		// When
		PowerMockito.when(activity.getSchoolclasses())
				.thenReturn(schoolclasses);
		PowerMockito.when(dataAccess.getAllActivities(schoolclass, timePeriod))
				.thenThrow(new DatasetException("Exception"));

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

		List<Activity> beforeList = Arrays.asList(beforeActivity);
		List<Activity> afterList = Arrays.asList(afterActivity);
		List<Activity> fullList = new ArrayList<>();
		fullList.addAll(beforeList);
		fullList.addAll(afterList);

		// When
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

		// When
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

		// When
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
		TimePeriod timePeriod = Mockito.mock(TimePeriod.class);

		Schoolclass schoolclass = Mockito.mock(Schoolclass.class);
		List<Schoolclass> schoolclasses = new ArrayList<>();
		schoolclasses.add(schoolclass);

		// When
		PowerMockito.when(activity.getSchoolclasses())
				.thenReturn(schoolclasses);
		PowerMockito.when(dataAccess.getAllActivities(schoolclass, timePeriod))
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
						activityValidator.validateActivitySchoolclasses(
								activity, timePeriod) instanceof SuccessStatus);

	}

	@Test
	public void invalidActivitySchoolclassTest() throws DatasetException {
		// Given
		Activity activity = Mockito.mock(Activity.class);
		TimePeriod timePeriod = Mockito.mock(TimePeriod.class);

		Schoolclass schoolclass = Mockito.mock(Schoolclass.class);
		List<Schoolclass> schoolclasses = new ArrayList<>();
		schoolclasses.add(schoolclass);

		Activity activity2 = Mockito.mock(Activity.class);
		List<Activity> activities = new ArrayList<>();
		activities.add(activity2);

		// When
		PowerMockito.when(activity.getSchoolclasses())
				.thenReturn(schoolclasses);
		PowerMockito.when(dataAccess.getAllActivities(schoolclass, timePeriod))
				.thenReturn(activities);

		// Then
		AssertJUnit
				.assertTrue(
						"A FailureStatus is expected if the database returned matching activities",
						activityValidator
								.validateActivity(activity, timePeriod) instanceof FailureStatus);
		AssertJUnit
				.assertTrue(
						"A FailureStatus is expected if the database returned matching activities",
						activityValidator.validateActivitySchoolclasses(
								activity, timePeriod) instanceof FailureStatus);
	}

	@Test
	public void validActivitySchoolclassSameActivityTest()
			throws DatasetException {
		// Given
		Activity activity = Mockito.mock(Activity.class);
		TimePeriod timePeriod = Mockito.mock(TimePeriod.class);

		Schoolclass schoolclass = Mockito.mock(Schoolclass.class);
		List<Schoolclass> schoolclasses = new ArrayList<>();
		schoolclasses.add(schoolclass);

		List<Activity> activities = new ArrayList<>();
		activities.add(activity);

		// When
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
		AssertJUnit
				.assertTrue(
						"A SuccessStatus is expected if the database returned matching activities but the only matching activity is the given activity",
						activityValidator.validateActivitySchoolclasses(
								activity, timePeriod) instanceof SuccessStatus);
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

	@Test
	public void validExpandTimPeriodWhithTravelTimeWithNoExpansionTest()
			throws Exception {
		// Given
		Activity activity = Mockito.mock(Activity.class);
		Date date = Mockito.mock(Date.class);
		Location location = Mockito.mock(Location.class);

		Room room = Mockito.mock(Room.class);
		List<Room> rooms = new ArrayList<>();
		rooms.add(room);

		EmployeeTimePeriods period = Mockito.mock(EmployeeTimePeriods.class);
		List<EmployeeTimePeriods> periods = new ArrayList<>();
		periods.add(period);

		TimePeriod expectedTimePeriod = Mockito.mock(TimePeriod.class);

		List<Activity> beforeList = new ArrayList<>();
		List<Activity> afterList = new ArrayList<>();

		// When
		PowerMockito.when(activity.getEmployeeTimePeriods())
				.thenReturn(periods);
		PowerMockito.when(
				dataAccess.getAllActivitiesBefore(Mockito.any(Employee.class),
						Mockito.any(TimePeriod.class))).thenReturn(beforeList);
		PowerMockito.when(
				dataAccess.getAllActivitiesAfter(Mockito.any(Employee.class),
						Mockito.any(TimePeriod.class))).thenReturn(afterList);
		PowerMockito.when(expectedTimePeriod.getStartTime()).thenReturn(date);
		PowerMockito.when(expectedTimePeriod.getEndTime()).thenReturn(date);
		PowerMockito.when(activity.getRooms()).thenReturn(rooms);

		PowerMockito.when(dataAccess.getOneLocation(Mockito.any(Room.class)))
				.thenReturn(location);

		// Then
		activityValidator.expandTimPeriodWhithTravelTime(activity,
				expectedTimePeriod);

		Mockito.verify(dataAccess, Mockito.times(1)).getOneLocation(
				Mockito.any(Room.class));

	}

	@Test
	public void validExpandTimPeriodWhithTravelTimeWithNoEdgesTest()
			throws Exception {
		// Given
		Activity activity = Mockito.mock(Activity.class);
		Activity beforeActivity = Mockito.mock(Activity.class);
		Activity afterActivity = Mockito.mock(Activity.class);
		Date date = Mockito.mock(Date.class);
		Location location = Mockito.mock(Location.class);

		Room room = Mockito.mock(Room.class);
		List<Room> rooms = new ArrayList<>();
		rooms.add(room);

		EmployeeTimePeriods period = Mockito.mock(EmployeeTimePeriods.class);
		List<EmployeeTimePeriods> periods = new ArrayList<>();
		periods.add(period);

		TimePeriod expectedTimePeriod = Mockito.mock(TimePeriod.class);

		List<Activity> beforeList = Arrays.asList(beforeActivity);
		List<Activity> afterList = Arrays.asList(afterActivity);

		// When
		PowerMockito.when(activity.getEmployeeTimePeriods())
				.thenReturn(periods);
		PowerMockito.when(
				dataAccess.getAllActivitiesBefore(Mockito.any(Employee.class),
						Mockito.any(TimePeriod.class))).thenReturn(beforeList);
		PowerMockito.when(
				dataAccess.getAllActivitiesAfter(Mockito.any(Employee.class),
						Mockito.any(TimePeriod.class))).thenReturn(afterList);
		PowerMockito.when(expectedTimePeriod.getStartTime()).thenReturn(date);
		PowerMockito.when(expectedTimePeriod.getEndTime()).thenReturn(date);
		PowerMockito.when(activity.getRooms()).thenReturn(rooms);
		PowerMockito.when(beforeActivity.getRooms()).thenReturn(rooms);
		PowerMockito.when(afterActivity.getRooms()).thenReturn(rooms);
		PowerMockito.when(
				travelTimeList.getEdge(Mockito.any(Location.class),
						Mockito.any(Location.class))).thenReturn(null);

		PowerMockito.when(dataAccess.getOneLocation(Mockito.any(Room.class)))
				.thenReturn(location);

		// Then
		activityValidator.expandTimPeriodWhithTravelTime(activity,
				expectedTimePeriod);

		Mockito.verify(dataAccess, Mockito.times(3)).getOneLocation(
				Mockito.any(Room.class));
		Mockito.verify(travelTimeList, Mockito.times(2)).getEdge(
				Mockito.any(Location.class), Mockito.any(Location.class));
	}

	@Test
	public void validExpandTimPeriodWhithTravelTimeWithEdgesTest()
			throws Exception {
		// Given
		Activity activity = Mockito.mock(Activity.class);
		Activity beforeActivity = Mockito.mock(Activity.class);
		Activity afterActivity = Mockito.mock(Activity.class);
		Date date = Mockito.mock(Date.class);
		Location location = Mockito.mock(Location.class);
		Edge edge = Mockito.mock(Edge.class);

		Room room = Mockito.mock(Room.class);
		List<Room> rooms = new ArrayList<>();
		rooms.add(room);

		EmployeeTimePeriods period = Mockito.mock(EmployeeTimePeriods.class);
		List<EmployeeTimePeriods> periods = new ArrayList<>();
		periods.add(period);

		TimePeriod expectedTimePeriod = Mockito.mock(TimePeriod.class);

		List<Activity> beforeList = Arrays.asList(beforeActivity);
		List<Activity> afterList = Arrays.asList(afterActivity);

		// When
		PowerMockito.when(activity.getEmployeeTimePeriods())
				.thenReturn(periods);
		PowerMockito.when(
				dataAccess.getAllActivitiesBefore(Mockito.any(Employee.class),
						Mockito.any(TimePeriod.class))).thenReturn(beforeList);
		PowerMockito.when(
				dataAccess.getAllActivitiesAfter(Mockito.any(Employee.class),
						Mockito.any(TimePeriod.class))).thenReturn(afterList);
		PowerMockito.when(expectedTimePeriod.getStartTime()).thenReturn(date);
		PowerMockito.when(expectedTimePeriod.getEndTime()).thenReturn(date);
		PowerMockito.when(activity.getRooms()).thenReturn(rooms);
		PowerMockito.when(beforeActivity.getRooms()).thenReturn(rooms);
		PowerMockito.when(afterActivity.getRooms()).thenReturn(rooms);
		PowerMockito.when(
				travelTimeList.getEdge(Mockito.any(Location.class),
						Mockito.any(Location.class))).thenReturn(edge);
		PowerMockito.when(edge.getDistance()).thenReturn(0);

		PowerMockito.when(dataAccess.getOneLocation(Mockito.any(Room.class)))
				.thenReturn(location);

		// Then
		activityValidator.expandTimPeriodWhithTravelTime(activity,
				expectedTimePeriod);

		Mockito.verify(dataAccess, Mockito.times(3)).getOneLocation(
				Mockito.any(Room.class));
		Mockito.verify(travelTimeList, Mockito.times(2)).getEdge(
				Mockito.any(Location.class), Mockito.any(Location.class));
		Mockito.verify(edge, Mockito.times(2)).getDistance();

	}

}
