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
import org.woym.common.objects.Lesson;
import org.woym.common.objects.Schoolclass;
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
		Lesson lesson = Mockito.mock(Lesson.class);
		
		EmployeeTimePeriods period = Mockito.mock(EmployeeTimePeriods.class);
		List<EmployeeTimePeriods> periods = new ArrayList<>();
		periods.add(period);

		// When
		PowerMockito.when(lesson.getEmployeeTimePeriods()).thenReturn(periods);

		PowerMockito.when(
				dataAccess.getAllActivities(period.getEmployee(), lesson.getTime()))
				.thenThrow(new DatasetException("Exception"));

		// Then
		AssertJUnit
				.assertTrue(
						"A FailureStatus is expected if the database threw an exception",
						activityValidator.validateActivity(lesson) instanceof FailureStatus);
	}

	@Test
	public void datasetExceptionSchoolclassesTest() throws DatasetException {
		// Given
		Lesson lesson = Mockito.mock(Lesson.class);
		
		Schoolclass schoolclass = Mockito.mock(Schoolclass.class);
		List<Schoolclass> schoolclasses = new ArrayList<>();
		schoolclasses.add(schoolclass);

		// When
		PowerMockito.when(lesson.getSchoolclasses()).thenReturn(schoolclasses);
		PowerMockito.when(
				dataAccess.getAllActivities(schoolclass, lesson.getTime()))
				.thenThrow(new DatasetException("Exception"));

		// Then
		AssertJUnit
				.assertTrue(
						"A FailureStatus is expected if the database threw an exception",
						activityValidator.validateActivity(lesson) instanceof FailureStatus);
	}
	
	
	@Test
	public void validLessonEmployeeTest() throws DatasetException {
		// Given
		Lesson lesson = Mockito.mock(Lesson.class);
		
		EmployeeTimePeriods period = Mockito.mock(EmployeeTimePeriods.class);
		List<EmployeeTimePeriods> periods = new ArrayList<>();
		periods.add(period);

		// When
		PowerMockito.when(lesson.getEmployeeTimePeriods()).thenReturn(periods);
		PowerMockito.when(
				dataAccess.getAllActivities(period.getEmployee(), lesson.getTime()))
				.thenReturn(new ArrayList<Activity>());

		// Then
		AssertJUnit
				.assertTrue(
						"A SuccessStatus is expected if the database returned no matching activities",
						activityValidator.validateActivity(lesson) instanceof SuccessStatus);

	}
	
	@Test
	public void invalidLessonEmployeeTest() throws DatasetException {
		// Given
		Lesson lesson = Mockito.mock(Lesson.class);
		
		Employee employee = Mockito.mock(Employee.class);
		
		EmployeeTimePeriods period = Mockito.mock(EmployeeTimePeriods.class);
		List<EmployeeTimePeriods> periods = new ArrayList<>();
		periods.add(period);
		
		Lesson lesson2 = Mockito.mock(Lesson.class);
		List<Activity> activities = new ArrayList<>();
		activities.add(lesson2);

		// When
		PowerMockito.when(period.getEmployee()).thenReturn(employee);
		PowerMockito.when(lesson.getEmployeeTimePeriods()).thenReturn(periods);
		PowerMockito.when(
				dataAccess.getAllActivities(period.getEmployee(), lesson.getTime()))
				.thenReturn(activities);

		// Then
		AssertJUnit
				.assertTrue(
						"A FailureStatus is expected if the database returned matching activities",
						activityValidator.validateActivity(lesson) instanceof FailureStatus);
	}
	
	@Test
	public void validLessonEmployeeSameLessonTest() throws DatasetException {
		// Given
		Lesson lesson = Mockito.mock(Lesson.class);
		
		EmployeeTimePeriods period = Mockito.mock(EmployeeTimePeriods.class);
		List<EmployeeTimePeriods> periods = new ArrayList<>();
		periods.add(period);

		List<Activity> activities = new ArrayList<>();
		activities.add(lesson);

		// When
		PowerMockito.when(lesson.getEmployeeTimePeriods()).thenReturn(periods);
		PowerMockito.when(
				dataAccess.getAllActivities(period.getEmployee(), lesson.getTime()))
				.thenReturn(activities);

		// Then
		AssertJUnit
				.assertTrue(
						"A SuccessStatus is expected if the database returned matching activities but the only matching activity is the given activity",
						activityValidator.validateActivity(lesson) instanceof SuccessStatus);
	}
	
	@Test
	public void validLessonSchoolclassTest() throws DatasetException {
		// Given
		Lesson lesson = Mockito.mock(Lesson.class);
		
		Schoolclass schoolclass = Mockito.mock(Schoolclass.class);
		List<Schoolclass> schoolclasses = new ArrayList<>();
		schoolclasses.add(schoolclass);

		// When
		PowerMockito.when(lesson.getSchoolclasses()).thenReturn(schoolclasses);
		PowerMockito.when(
				dataAccess.getAllActivities(schoolclass, lesson.getTime()))
				.thenReturn(new ArrayList<Activity>());

		// Then
		AssertJUnit
				.assertTrue(
						"A SuccessStatus is expected if the database returned no matching activities",
						activityValidator.validateActivity(lesson) instanceof SuccessStatus);

	}

	@Test
	public void invalidLessonSchoolclassTest() throws DatasetException {
		// Given
		Lesson lesson = Mockito.mock(Lesson.class);
		
		Schoolclass schoolclass = Mockito.mock(Schoolclass.class);
		List<Schoolclass> schoolclasses = new ArrayList<>();
		schoolclasses.add(schoolclass);
		
		Lesson lesson2 = Mockito.mock(Lesson.class);
		List<Activity> activities = new ArrayList<>();
		activities.add(lesson2);

		// When
		PowerMockito.when(lesson.getSchoolclasses()).thenReturn(schoolclasses);
		PowerMockito.when(
				dataAccess.getAllActivities(schoolclass, lesson.getTime()))
				.thenReturn(activities);

		// Then
		AssertJUnit
				.assertTrue(
						"A FailureStatus is expected if the database returned matching activities",
						activityValidator.validateActivity(lesson) instanceof FailureStatus);
	}

	@Test
	public void validLessonSchoolclassSameLessonTest() throws DatasetException {
		// Given
		Lesson lesson = Mockito.mock(Lesson.class);
		
		Schoolclass schoolclass = Mockito.mock(Schoolclass.class);
		List<Schoolclass> schoolclasses = new ArrayList<>();
		schoolclasses.add(schoolclass);

		List<Activity> activities = new ArrayList<>();
		activities.add(lesson);

		// When
		PowerMockito.when(lesson.getSchoolclasses()).thenReturn(schoolclasses);
		PowerMockito.when(
				dataAccess.getAllActivities(schoolclass, lesson.getTime()))
				.thenReturn(activities);

		// Then
		AssertJUnit
				.assertTrue(
						"A SuccessStatus is expected if the database returned matching activities but the only matching activity is the given activity",
						activityValidator.validateActivity(lesson) instanceof SuccessStatus);
	}

}
