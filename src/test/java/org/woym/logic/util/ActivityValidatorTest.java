package org.woym.logic.util;

import java.util.ArrayList;
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
import org.woym.common.objects.Room;
import org.woym.common.objects.Schoolclass;
import org.woym.common.objects.TimePeriod;
import org.woym.logic.FailureStatus;
import org.woym.logic.SuccessStatus;
import org.woym.persistence.DataAccess;

@Test(groups = "unit")
public class ActivityValidatorTest extends PowerMockTestCase {

	@Mock
	DataAccess dataAccess;

	@InjectMocks
	ActivityValidator activityValidator;

	@Test
	public void getInstanceTest() {
		AssertJUnit.assertNotNull(ActivityValidator.getInstance());
	}

	@Test
	public void datasetExceptionEmployeesTest() throws DatasetException {
		// Given
		Activity activity = Mockito.mock(Activity.class);

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
				dataAccess.getAllActivities(period.getEmployee(), timePeriod))
				.thenThrow(new DatasetException("Exception"));

		// Then
		AssertJUnit
				.assertTrue(
						"A FailureStatus is expected if the database threw an exception",
						activityValidator.validateActivity(activity) instanceof FailureStatus);
		AssertJUnit
				.assertTrue(
						"A FailureStatus is expected if the database threw an exception",
						activityValidator.validateActivityEmployees(activity) instanceof FailureStatus);
	}

	@Test
	public void datasetExceptionSchoolclassesTest() throws DatasetException {
		// Given
		Activity activity = Mockito.mock(Activity.class);

		Schoolclass schoolclass = Mockito.mock(Schoolclass.class);
		List<Schoolclass> schoolclasses = new ArrayList<>();
		schoolclasses.add(schoolclass);

		// When
		PowerMockito.when(activity.getSchoolclasses())
				.thenReturn(schoolclasses);
		PowerMockito.when(
				dataAccess.getAllActivities(schoolclass, activity.getTime()))
				.thenThrow(new DatasetException("Exception"));

		// Then
		AssertJUnit
				.assertTrue(
						"A FailureStatus is expected if the database threw an exception",
						activityValidator.validateActivity(activity) instanceof FailureStatus);
		AssertJUnit
				.assertTrue(
						"A FailureStatus is expected if the database threw an exception",
						activityValidator
								.validateActivitySchoolclasses(activity) instanceof FailureStatus);
	}

	@Test
	public void datasetExceptionRoomsTest() throws DatasetException {
		// Given
		Activity activity = Mockito.mock(Activity.class);

		Room room = Mockito.mock(Room.class);
		List<Room> rooms = new ArrayList<>();
		rooms.add(room);

		// When
		PowerMockito.when(activity.getRooms()).thenReturn(rooms);
		PowerMockito
				.when(dataAccess.getAllActivities(room, activity.getTime()))
				.thenThrow(new DatasetException("Exception"));

		// Then
		AssertJUnit
				.assertTrue(
						"A FailureStatus is expected if the database threw an exception",
						activityValidator.validateActivity(activity) instanceof FailureStatus);
		AssertJUnit
				.assertTrue(
						"A FailureStatus is expected if the database threw an exception",
						activityValidator.validateActivityRooms(activity) instanceof FailureStatus);
	}

	@Test
	public void validActivityEmployeeTest() throws DatasetException {
		// Given
		Activity activity = Mockito.mock(Activity.class);

		EmployeeTimePeriods period = Mockito.mock(EmployeeTimePeriods.class);
		List<EmployeeTimePeriods> periods = new ArrayList<>();
		periods.add(period);

		// When
		PowerMockito.when(activity.getEmployeeTimePeriods())
				.thenReturn(periods);
		PowerMockito.when(
				dataAccess.getAllActivities(period.getEmployee(),
						activity.getTime())).thenReturn(
				new ArrayList<Activity>());

		// Then
		AssertJUnit
				.assertTrue(
						"A SuccessStatus is expected if the database returned no matching activities",
						activityValidator.validateActivity(activity) instanceof SuccessStatus);
		AssertJUnit
				.assertTrue(
						"A SuccessStatus is expected if the database returned no matching activities",
						activityValidator.validateActivityEmployees(activity) instanceof SuccessStatus);

	}

	@Test
	public void invalidActivityEmployeeTest() throws DatasetException {
		// Given
		Activity activity = Mockito.mock(Activity.class);

		Employee employee = Mockito.mock(Employee.class);

		EmployeeTimePeriods period = Mockito.mock(EmployeeTimePeriods.class);
		List<EmployeeTimePeriods> periods = new ArrayList<>();
		periods.add(period);

		List<TimePeriod> timePeriods = new ArrayList<TimePeriod>();
		TimePeriod timePeriod = Mockito.mock(TimePeriod.class);
		timePeriods.add(timePeriod);

		Activity activity2 = Mockito.mock(Activity.class);
		List<Activity> activities = new ArrayList<>();
		activities.add(activity2);

		// When
		PowerMockito.when(period.getEmployee()).thenReturn(employee);
		PowerMockito.when(period.getTimePeriods()).thenReturn(timePeriods);
		PowerMockito.when(activity.getEmployeeTimePeriods())
				.thenReturn(periods);
		PowerMockito.when(
				dataAccess.getAllActivities(period.getEmployee(), timePeriod))
				.thenReturn(activities);

		// Then
		AssertJUnit
				.assertTrue(
						"A FailureStatus is expected if the database returned matching activities",
						activityValidator.validateActivity(activity) instanceof FailureStatus);
		AssertJUnit
				.assertTrue(
						"A FailureStatus is expected if the database returned matching activities",
						activityValidator.validateActivityEmployees(activity) instanceof FailureStatus);
	}

	@Test
	public void validActivityEmployeeSameActivityTest() throws DatasetException {
		// Given
		Activity activity = Mockito.mock(Activity.class);

		EmployeeTimePeriods period = Mockito.mock(EmployeeTimePeriods.class);
		List<EmployeeTimePeriods> periods = new ArrayList<>();
		periods.add(period);

		List<Activity> activities = new ArrayList<>();
		activities.add(activity);

		// When
		PowerMockito.when(activity.getEmployeeTimePeriods())
				.thenReturn(periods);
		PowerMockito.when(
				dataAccess.getAllActivities(period.getEmployee(),
						activity.getTime())).thenReturn(activities);

		// Then
		AssertJUnit
				.assertTrue(
						"A SuccessStatus is expected if the database returned matching activities but the only matching activity is the given activity",
						activityValidator.validateActivity(activity) instanceof SuccessStatus);
		AssertJUnit
				.assertTrue(
						"A SuccessStatus is expected if the database returned matching activities but the only matching activity is the given activity",
						activityValidator.validateActivityEmployees(activity) instanceof SuccessStatus);
	}

	@Test
	public void validActivitySchoolclassTest() throws DatasetException {
		// Given
		Activity activity = Mockito.mock(Activity.class);

		Schoolclass schoolclass = Mockito.mock(Schoolclass.class);
		List<Schoolclass> schoolclasses = new ArrayList<>();
		schoolclasses.add(schoolclass);

		// When
		PowerMockito.when(activity.getSchoolclasses())
				.thenReturn(schoolclasses);
		PowerMockito.when(
				dataAccess.getAllActivities(schoolclass, activity.getTime()))
				.thenReturn(new ArrayList<Activity>());

		// Then
		AssertJUnit
				.assertTrue(
						"A SuccessStatus is expected if the database returned no matching activities",
						activityValidator.validateActivity(activity) instanceof SuccessStatus);
		AssertJUnit
				.assertTrue(
						"A SuccessStatus is expected if the database returned no matching activities",
						activityValidator
								.validateActivitySchoolclasses(activity) instanceof SuccessStatus);

	}

	@Test
	public void invalidActivitySchoolclassTest() throws DatasetException {
		// Given
		Activity activity = Mockito.mock(Activity.class);

		Schoolclass schoolclass = Mockito.mock(Schoolclass.class);
		List<Schoolclass> schoolclasses = new ArrayList<>();
		schoolclasses.add(schoolclass);

		Activity activity2 = Mockito.mock(Activity.class);
		List<Activity> activities = new ArrayList<>();
		activities.add(activity2);

		// When
		PowerMockito.when(activity.getSchoolclasses())
				.thenReturn(schoolclasses);
		PowerMockito.when(
				dataAccess.getAllActivities(schoolclass, activity.getTime()))
				.thenReturn(activities);

		// Then
		AssertJUnit
				.assertTrue(
						"A FailureStatus is expected if the database returned matching activities",
						activityValidator.validateActivity(activity) instanceof FailureStatus);
		AssertJUnit
				.assertTrue(
						"A FailureStatus is expected if the database returned matching activities",
						activityValidator
								.validateActivitySchoolclasses(activity) instanceof FailureStatus);
	}

	@Test
	public void validActivitySchoolclassSameActivityTest()
			throws DatasetException {
		// Given
		Activity activity = Mockito.mock(Activity.class);

		Schoolclass schoolclass = Mockito.mock(Schoolclass.class);
		List<Schoolclass> schoolclasses = new ArrayList<>();
		schoolclasses.add(schoolclass);

		List<Activity> activities = new ArrayList<>();
		activities.add(activity);

		// When
		PowerMockito.when(activity.getSchoolclasses())
				.thenReturn(schoolclasses);
		PowerMockito.when(
				dataAccess.getAllActivities(schoolclass, activity.getTime()))
				.thenReturn(activities);

		// Then
		AssertJUnit
				.assertTrue(
						"A SuccessStatus is expected if the database returned matching activities but the only matching activity is the given activity",
						activityValidator.validateActivity(activity) instanceof SuccessStatus);
		AssertJUnit
				.assertTrue(
						"A SuccessStatus is expected if the database returned matching activities but the only matching activity is the given activity",
						activityValidator
								.validateActivitySchoolclasses(activity) instanceof SuccessStatus);
	}

	@Test
	public void validActivityRoomTest() throws DatasetException {
		// Given
		Activity activity = Mockito.mock(Activity.class);

		Room room = Mockito.mock(Room.class);
		List<Room> rooms = new ArrayList<>();
		rooms.add(room);

		// When
		PowerMockito.when(activity.getRooms()).thenReturn(rooms);
		PowerMockito
				.when(dataAccess.getAllActivities(room, activity.getTime()))
				.thenReturn(new ArrayList<Activity>());

		// Then
		AssertJUnit
				.assertTrue(
						"A SuccessStatus is expected if the database returned no matching activities",
						activityValidator.validateActivity(activity) instanceof SuccessStatus);
		AssertJUnit
				.assertTrue(
						"A SuccessStatus is expected if the database returned no matching activities",
						activityValidator.validateActivityRooms(activity) instanceof SuccessStatus);

	}

	@Test
	public void invalidActivityRoomTest() throws DatasetException {
		// Given
		Activity activity = Mockito.mock(Activity.class);

		Room room = Mockito.mock(Room.class);
		List<Room> rooms = new ArrayList<>();
		rooms.add(room);

		Activity activity2 = Mockito.mock(Activity.class);
		List<Activity> activities = new ArrayList<>();
		activities.add(activity2);

		// When
		PowerMockito.when(activity.getRooms()).thenReturn(rooms);
		PowerMockito
				.when(dataAccess.getAllActivities(room, activity.getTime()))
				.thenReturn(activities);

		// Then
		AssertJUnit
				.assertTrue(
						"A FailureStatus is expected if the database returned matching activities",
						activityValidator.validateActivity(activity) instanceof FailureStatus);
		AssertJUnit
				.assertTrue(
						"A FailureStatus is expected if the database returned matching activities",
						activityValidator.validateActivityRooms(activity) instanceof FailureStatus);
	}

	@Test
	public void validActivityRoomSameActivityTest() throws DatasetException {
		// Given
		Activity activity = Mockito.mock(Activity.class);

		Room room = Mockito.mock(Room.class);
		List<Room> rooms = new ArrayList<>();
		rooms.add(room);

		List<Activity> activities = new ArrayList<>();
		activities.add(activity);

		// When
		PowerMockito.when(activity.getRooms()).thenReturn(rooms);
		PowerMockito
				.when(dataAccess.getAllActivities(room, activity.getTime()))
				.thenReturn(activities);

		// Then
		AssertJUnit
				.assertTrue(
						"A SuccessStatus is expected if the database returned matching activities but the only matching activity is the given activity",
						activityValidator.validateActivity(activity) instanceof SuccessStatus);
		AssertJUnit
				.assertTrue(
						"A SuccessStatus is expected if the database returned matching activities but the only matching activity is the given activity",
						activityValidator.validateActivityRooms(activity) instanceof SuccessStatus);
	}

}
