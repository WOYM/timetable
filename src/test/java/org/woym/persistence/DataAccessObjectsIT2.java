package org.woym.persistence;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import java.text.SimpleDateFormat;
import java.util.List;

import org.testng.annotations.Test;
import org.woym.exceptions.DatasetException;
import org.woym.objects.Activity;
import org.woym.objects.CompoundLesson;
import org.woym.objects.EmployeeTimePeriods;
import org.woym.objects.Lesson;
import org.woym.objects.LessonType;
import org.woym.objects.Meeting;
import org.woym.objects.MeetingType;
import org.woym.objects.TimePeriod;
import org.woym.objects.Weekday;
import org.woym.objects.spec.IActivityObject;

@Test(groups = { "DataAccessObjectsIT2", "integration" }, dependsOnGroups = "DataAccessObjectsIT")
public class DataAccessObjectsIT2 {

	private DataAccess dataAccess = DataAccess.getInstance();

	private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

	private Lesson l;
	private Meeting m;
	private CompoundLesson c;

	@Test(priority = 1, groups = "DataAcessActivity")
	public void getAllActivitiesSuccess() throws Exception {
		l = createLesson();
		m = createMeeting();
		c = createCompoundLesson();

		List<Activity> list = dataAccess.getAllActivities();
		assertEquals(3, list.size());
		assertTrue(list.contains(l));
		assertTrue(list.contains(c));
		assertTrue(list.contains(m));
	}

	@Test(priority = 1, groups = "DataAcessActivity", dependsOnMethods = "getAllActivitiesSuccess")
	public void getAllActivitiesEmployeeSuccess() throws Exception {

		List<Activity> list = dataAccess.getAllActivities(dataAccess
				.getOneEmployee("MEY"));
		assertEquals(3, list.size());
		assertTrue(list.contains(l));
		assertTrue(list.contains(m));
		assertTrue(list.contains(c));

		list = dataAccess.getAllActivities(dataAccess.getOneEmployee("MUS"));
		assertEquals(2, list.size());
		assertTrue(list.contains(m));
		assertTrue(list.contains(c));
	}

	@Test(priority = 2, groups = "DataAccessActivity")
	public void getAllActivitiesSchoolclassSuccess() throws DatasetException {
		List<Activity> list = dataAccess.getAllActivities(dataAccess
				.getOneSchoolclass(1, 'a'));
		assertEquals(2, list.size());
		assertTrue(list.contains(l));
		assertTrue(list.contains(c));

		list = dataAccess
				.getAllActivities(dataAccess.getOneSchoolclass(2, 'a'));
		assertEquals(1, list.size());
		assertTrue(list.contains(c));
	}

	@Test(priority = 2, groups = "DataAccessActivity")
	public void getAllActivitiesRoomSuccess() throws DatasetException {
		List<Activity> list = dataAccess.getAllActivities(dataAccess
				.getOneRoom("Hauptstandort", "Raum 100"));
		assertEquals(3, list.size());
		assertTrue(list.contains(l));
		assertTrue(list.contains(m));
		assertTrue(list.contains(c));

		list = dataAccess.getAllActivities(dataAccess.getOneRoom("Zweigstelle",
				"Raum 101"));
		assertEquals(1, list.size());
		assertTrue(list.contains(c));
	}

	@Test(priority = 2, groups = "DataAccessActivity")
	public void getAllActivitiesEmployeeWeekdaySuccess()
			throws DatasetException {
		List<Activity> list = dataAccess.getAllActivities(
				dataAccess.getOneEmployee("MEY"), Weekday.MONDAY);
		assertEquals(2, list.size());
		assertTrue(list.contains(l));
		assertTrue(list.contains(m));

		list = dataAccess.getAllActivities(dataAccess.getOneEmployee("MUS"),
				Weekday.TUESDAY);
		assertEquals(1, list.size());
		assertTrue(list.contains(c));
	}

	@Test(priority = 2, groups = "DataAccessActivity")
	public void getAllCompoundLessonsLessonTypeSuccess()
			throws DatasetException {
		List<CompoundLesson> list = dataAccess
				.getAllCompoundLessons((LessonType) dataAccess
						.getOneActivityType("Mathe"));
		assertEquals(1, list.size());
		assertTrue(list.contains(c));
	}

	@Test(priority = 2, groups = "DataAccessActivity")
	public void getAllCompoundLessonsLessonTypeEmptyList()
			throws DatasetException {
		LessonType l = new LessonType();
		l.setName("Deutsch");
		l.setTypicalDuration(45);
		l.persist();

		List<CompoundLesson> list = dataAccess
				.getAllCompoundLessons((LessonType) dataAccess
						.getOneActivityType("Deutsch"));
		assertTrue(list.isEmpty());
	}

	@Test(priority = 2, groups = "DataAccessActivity")
	public void getAllLessonsLessonTypeSuccess() throws DatasetException {
		List<Lesson> list = dataAccess.getAllLessons((LessonType) dataAccess
				.getOneActivityType("Mathe"));
		assertEquals(1, list.size());
		assertTrue(list.contains(l));
	}

	@Test(priority = 2, groups = "DataAccessActivity")
	public void getAllLessonsLessonTypeEmptyList() throws DatasetException {
		List<Lesson> list = dataAccess.getAllLessons((LessonType) dataAccess
				.getOneActivityType("Deutsch"));
		assertTrue(list.isEmpty());
	}

	@Test(priority = 2, groups = "DataAccessActivity")
	public void getAllMeetingsMeetingTypeSuccess() throws DatasetException {
		List<Meeting> list = dataAccess.getAllMeetings((MeetingType) dataAccess
				.getOneActivityType("Teamsitzung"));
		assertEquals(1, list.size());
		assertTrue(list.contains(m));
	}

	@Test(priority = 2, groups = "DataAccessActivity")
	public void getAllMeetingsMeetingTypeEmptyList() throws DatasetException {
		MeetingType meeting = new MeetingType();
		meeting.setName("Klassenteam-Sitzung");
		List<Meeting> list = dataAccess.getAllMeetings(meeting);
		assertTrue(list.isEmpty());
	}

	@Test(priority = 2, groups = "DataAccessActivity")
	public void getAllActivitiesEmployeeTimePeriodSuccess() throws Exception {
		TimePeriod t = new TimePeriod();
		t.setDay(Weekday.MONDAY);
		t.setStartTime(sdf.parse("10:00"));
		t.setEndTime(sdf.parse("10:30"));
		t.setDuration(30);

		List<Activity> list = dataAccess.getAllActivities(
				dataAccess.getOneEmployee("MEY"), t);
		assertEquals(1, list.size());
		assertTrue(list.contains(l));

		t.setStartTime(sdf.parse("10:00"));
		t.setEndTime(sdf.parse("10:45"));
		list = dataAccess.getAllActivities(dataAccess.getOneEmployee("MEY"), t);
		assertEquals(1, list.size());
		assertTrue(list.contains(l));

		t.setStartTime(sdf.parse("10:00"));
		t.setEndTime(sdf.parse("12:00"));
		list = dataAccess.getAllActivities(dataAccess.getOneEmployee("MEY"), t);
		assertEquals(2, list.size());
		assertTrue(list.contains(l));
		assertTrue(list.contains(m));

		t.setDay(Weekday.TUESDAY);
		t.setStartTime(sdf.parse("11:00"));
		t.setEndTime(sdf.parse("11:30"));
		list = dataAccess.getAllActivities(dataAccess.getOneEmployee("MEY"), t);
		assertEquals(1, list.size());
		assertTrue(list.contains(c));
	}

	@Test(priority = 2, groups = "DataAccessActivity")
	public void getAllActivitiesEmployeeTimePeriodEmptyList() throws Exception {
		TimePeriod t = new TimePeriod();
		t.setDay(Weekday.MONDAY);
		t.setStartTime(sdf.parse("09:00"));
		t.setEndTime(sdf.parse("10:00"));
		t.setDuration(30);

		List<Activity> list = dataAccess.getAllActivities(
				dataAccess.getOneEmployee("MEY"), t);
		assertTrue(list.isEmpty());

		t.setStartTime(sdf.parse("11:30"));
		t.setEndTime(sdf.parse("12:15"));
		list = dataAccess.getAllActivities(dataAccess.getOneEmployee("MEY"), t);
		assertTrue(list.isEmpty());

		t.setDay(Weekday.TUESDAY);
		t.setStartTime(sdf.parse("11:00"));
		t.setEndTime(sdf.parse("11:15"));
		list = dataAccess.getAllActivities(dataAccess.getOneEmployee("MEY"), t);
		assertTrue(list.isEmpty());
	}

	@Test(priority = 2, groups = "DataAccessActivity")
	public void getAllActivitiesRoomTimePeriodSuccess() throws Exception {
		TimePeriod t = new TimePeriod();
		t.setDay(Weekday.MONDAY);
		t.setStartTime(sdf.parse("10:00"));
		t.setEndTime(sdf.parse("10:30"));
		t.setDuration(30);

		List<Activity> list = dataAccess.getAllActivities(
				dataAccess.getOneRoom("Hauptstandort", "Raum 100"), t);
		assertEquals(1, list.size());
		assertTrue(list.contains(l));

		t.setStartTime(sdf.parse("09:00"));
		t.setEndTime(sdf.parse("12:00"));
		list = dataAccess.getAllActivities(
				dataAccess.getOneRoom("Hauptstandort", "Raum 100"), t);
		assertEquals(2, list.size());
		assertTrue(list.contains(l));
		assertTrue(list.contains(m));
	}

	@Test(priority = 2, groups = "DataAccessActivity")
	public void getAllActivitiesRoomTimePeriodEmptyList() throws Exception {
		TimePeriod t = new TimePeriod();
		t.setDay(Weekday.MONDAY);
		t.setStartTime(sdf.parse("11:30"));
		t.setEndTime(sdf.parse("12:15"));
		t.setDuration(30);

		List<Activity> list = dataAccess.getAllActivities(
				dataAccess.getOneRoom("Hauptstandort", "Raum 100"), t);
		assertTrue(list.isEmpty());

		t.setStartTime(sdf.parse("09:15"));
		t.setEndTime(sdf.parse("10:00"));
		list = dataAccess.getAllActivities(
				dataAccess.getOneRoom("Hauptstandort", "Raum 100"), t);
		assertTrue(list.isEmpty());
	}

	@Test(priority = 2, groups = "DataAccessActivity")
	public void getAllActivitiesSchoolclassTimePeriodSuccess() throws Exception {
		TimePeriod t = new TimePeriod();
		t.setDay(Weekday.TUESDAY);
		t.setStartTime(sdf.parse("10:45"));
		t.setEndTime(sdf.parse("11:00"));
		t.setDuration(15);

		List<Activity> list = dataAccess.getAllActivities(
				dataAccess.getOneSchoolclass(1, 'a'), t);
		assertEquals(1, list.size());
		assertTrue(list.contains(c));

		t.setStartTime(sdf.parse("10:30"));
		t.setEndTime(sdf.parse("11:45"));
		list = dataAccess.getAllActivities(
				dataAccess.getOneSchoolclass(1, 'a'), t);
		assertEquals(1, list.size());
		assertTrue(list.contains(c));

		t.setStartTime(sdf.parse("10:30"));
		t.setEndTime(sdf.parse("11:00"));
		list = dataAccess.getAllActivities(
				dataAccess.getOneSchoolclass(1, 'a'), t);
		assertEquals(1, list.size());
		assertTrue(list.contains(c));
	}

	@Test(priority = 2, groups = "DataAccessActivity")
	public void getAllActivitiesSchoolclassTimePeriodEmptyList()
			throws Exception {
		TimePeriod t = new TimePeriod();
		t.setDay(Weekday.TUESDAY);
		t.setStartTime(sdf.parse("11:30"));
		t.setEndTime(sdf.parse("12:00"));
		t.setDuration(30);

		List<Activity> list = dataAccess.getAllActivities(
				dataAccess.getOneSchoolclass(1, 'a'), t);
		assertTrue(list.isEmpty());

		t.setStartTime(sdf.parse("10:00"));
		t.setEndTime(sdf.parse("10:45"));
		list = dataAccess.getAllActivities(
				dataAccess.getOneSchoolclass(1, 'a'), t);
		assertTrue(list.isEmpty());
	}

	@Test(priority = 2, groups = "DataAccessActivity")
	public void getAllActivitiesIActivityObjectSuccess()
			throws DatasetException {
		IActivityObject a = dataAccess.getOneEmployee("MEY");

		List<Activity> list = dataAccess.getAllActivities(a);
		assertEquals(3, list.size());
		assertTrue(list.contains(l));
		assertTrue(list.contains(m));
		assertTrue(list.contains(c));

		a = dataAccess.getOneSchoolclass(1, 'a');
		list = dataAccess.getAllActivities(a);
		assertEquals(2, list.size());
		assertTrue(list.contains(l));
		assertTrue(list.contains(c));

		a = dataAccess.getOneRoom("Hauptstandort", "Raum 100");
		list = dataAccess.getAllActivities(a);
		assertEquals(3, list.size());
		assertTrue(list.contains(l));
		assertTrue(list.contains(m));
		assertTrue(list.contains(c));

		a = new LessonType();
		list = dataAccess.getAllActivities(a);
		assertTrue(list.isEmpty());
	}

	private Lesson createLesson() throws Exception {
		Lesson l = new Lesson();
		l.add(dataAccess.getOneSchoolclass(1, 'a'));

		TimePeriod t = new TimePeriod();
		t.setDay(Weekday.MONDAY);
		t.setStartTime(sdf.parse("10:00"));
		t.setEndTime(sdf.parse("10:45"));
		t.setDuration(45);
		l.setTime(t);

		EmployeeTimePeriods e = new EmployeeTimePeriods();
		e.setEmployee(dataAccess.getOneEmployee("MEY"));
		e.add(t);
		l.add(e);

		l.setLessonType((LessonType) dataAccess.getOneActivityType("Mathe"));

		l.add(dataAccess.getOneRoom("Hauptstandort", "Raum 100"));

		l.persist();
		return l;
	}

	private Meeting createMeeting() throws Exception {
		Meeting m = new Meeting();

		TimePeriod t = new TimePeriod();
		t.setDay(Weekday.MONDAY);
		t.setStartTime(sdf.parse("10:45"));
		t.setEndTime(sdf.parse("11:30"));
		t.setDuration(45);

		m.setTime(t);

		EmployeeTimePeriods e = new EmployeeTimePeriods();
		e.setEmployee(dataAccess.getOneEmployee("MEY"));
		e.add(t);
		m.add(e);

		e = new EmployeeTimePeriods();
		e.setEmployee(dataAccess.getOneEmployee("MUS"));
		e.add(t);
		m.add(e);

		m.setMeetingType((MeetingType) dataAccess
				.getOneActivityType("Teamsitzung"));

		m.add(dataAccess.getOneRoom("Hauptstandort", "Raum 100"));

		m.persist();
		return m;
	}

	private CompoundLesson createCompoundLesson() throws Exception {
		CompoundLesson c = new CompoundLesson();

		TimePeriod t = new TimePeriod();
		t.setDay(Weekday.TUESDAY);
		t.setStartTime(sdf.parse("10:45"));
		t.setEndTime(sdf.parse("11:30"));
		t.setDuration(45);

		c.setTime(t);
		c.add((LessonType) dataAccess.getOneActivityType("Musik"));
		c.add((LessonType) dataAccess.getOneActivityType("Mathe"));

		EmployeeTimePeriods e = new EmployeeTimePeriods();
		e.setEmployee(dataAccess.getOneEmployee("MUS"));
		e.add(t);
		c.add(e);

		e = new EmployeeTimePeriods();
		e.setEmployee(dataAccess.getOneEmployee("MEY"));

		t = new TimePeriod();
		t.setDay(Weekday.TUESDAY);
		t.setStartTime(sdf.parse("10:45"));
		t.setEndTime(sdf.parse("11:00"));
		t.setDuration(15);
		e.add(t);

		t = new TimePeriod();
		t.setDay(Weekday.TUESDAY);
		t.setStartTime(sdf.parse("11:15"));
		t.setEndTime(sdf.parse("11:30"));
		t.setDuration(15);
		e.add(t);

		c.add(e);

		c.add(dataAccess.getOneSchoolclass(1, 'a'));
		c.add(dataAccess.getOneSchoolclass(2, 'a'));

		c.add(dataAccess.getOneRoom("Hauptstandort", "Raum 100"));
		c.add(dataAccess.getOneRoom("Hauptstandort", "Raum 102"));
		c.add(dataAccess.getOneRoom("Zweigstelle", "Raum 101"));

		c.persist();

		return c;
	}
}
