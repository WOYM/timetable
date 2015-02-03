package org.woym.logic.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.woym.common.exceptions.DatasetException;
import org.woym.common.objects.AcademicYear;
import org.woym.common.objects.Activity;
import org.woym.common.objects.ActivityType;
import org.woym.common.objects.Classteam;
import org.woym.common.objects.CompoundLesson;
import org.woym.common.objects.Employee;
import org.woym.common.objects.Entity;
import org.woym.common.objects.Lesson;
import org.woym.common.objects.LessonType;
import org.woym.common.objects.Location;
import org.woym.common.objects.PedagogicAssistant;
import org.woym.common.objects.Room;
import org.woym.common.objects.Schoolclass;
import org.woym.common.objects.Teacher;
import org.woym.common.objects.TravelTimeList;
import org.woym.common.objects.Location.Memento;
import org.woym.common.objects.TravelTimeList.Edge;
import org.woym.common.objects.spec.IActivityObject;
import org.woym.common.objects.spec.IMemento;
import org.woym.persistence.DataAccess;

/**
 * @author JurSch
 *
 */
@Test(groups = "unit")
@PowerMockIgnore("javax.management.*")
@PrepareForTest({ DataAccess.class, TravelTimeList.class })
public class TestCommandCreator extends PowerMockTestCase {

	@Mock
	private DataAccess dataAccess;

	@Mock
	private Activity activity;

	@Mock
	private Activity activity1;

	@Mock
	private org.woym.common.objects.Activity.Memento mementoActivity;

	@Mock
	private Activity activity2;

	@Mock
	private Location location;

	@Mock
	private Classteam team1;

	@Mock
	private Classteam team2;

	@Mock
	private Room room;

	@Mock
	private AcademicYear year;

	@Mock
	private Schoolclass schoolclass;

	@Mock
	private ActivityType type1;

	@Mock
	private PedagogicAssistant pedagogicAssistant;

	@Mock
	private Teacher teacher;

	@Mock
	private Employee employee;

	@Mock
	private TravelTimeList travelTimeList;

	@InjectMocks
	private CommandCreator creator;

	List<Activity> activities;

	@BeforeMethod
	public void init() throws DatasetException {
		PowerMockito.mockStatic(DataAccess.class);
		PowerMockito.when(DataAccess.getInstance()).thenReturn(dataAccess);
		PowerMockito.mockStatic(TravelTimeList.class);
		PowerMockito.when(TravelTimeList.getInstance()).thenReturn(
				travelTimeList);

		Mockito.when(activity1.remove(Mockito.any(IActivityObject.class)))
				.thenReturn(1);
		Mockito.when(activity2.remove(Mockito.any(IActivityObject.class)))
				.thenReturn(0);
		Mockito.when(activity1.createMemento()).thenReturn(mementoActivity);

		activities = new ArrayList<>(Arrays.asList(activity1, activity2));

		Mockito.when(
				DataAccess.getInstance().getAllActivities(
						Mockito.any(IActivityObject.class), Mockito.anyBoolean())).thenReturn(
				activities);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testNullEntity() {
		CommandCreator.getInstance().createDeleteCommand(null);
	}

	@Test
	public void testActvity() {
		MacroCommand macro = CommandCreator.getInstance().createDeleteCommand(
				activity);

		AssertJUnit.assertEquals(1, macro.getCommands().size());
		AssertJUnit
				.assertTrue(macro.getCommands().get(0) instanceof DeleteCommand);
	}

	@Test
	public void testAcademicYearWhithoutClasses() {
		MacroCommand macro = CommandCreator.getInstance().createDeleteCommand(
				year);

		AssertJUnit.assertEquals(1, macro.getCommands().size());
		AssertJUnit
				.assertTrue(macro.getCommands().get(0) instanceof DeleteCommand);
	}

	@Test
	public void testSchoolclass() throws Exception {

		IMemento memento = Mockito
				.mock(org.woym.common.objects.Classteam.Memento.class);

		Mockito.when(DataAccess.getInstance().getOneClassteam(schoolclass))
				.thenReturn(team1);
		Mockito.when(team1.createMemento()).thenReturn(memento);

		Mockito.when(DataAccess.getInstance().getOneAcademicYear(schoolclass))
				.thenReturn(year);
		IMemento yearMemento = Mockito
				.mock(org.woym.common.objects.AcademicYear.Memento.class);
		Mockito.when(year.createMemento()).thenReturn(
				(org.woym.common.objects.AcademicYear.Memento) yearMemento);

		MacroCommand macro = CommandCreator.getInstance().createDeleteCommand(
				schoolclass);

		AssertJUnit.assertEquals(5, macro.getCommands().size());

		AssertJUnit
				.assertTrue(macro.getCommands().get(0) instanceof UpdateCommand);
		AssertJUnit
				.assertTrue(macro.getCommands().get(1) instanceof DeleteCommand);

		AssertJUnit
				.assertTrue(macro.getCommands().get(2) instanceof UpdateCommand);
		AssertJUnit
				.assertTrue(macro.getCommands().get(3) instanceof UpdateCommand);
		AssertJUnit
				.assertTrue(macro.getCommands().get(4) instanceof DeleteCommand);

	}

	@Test
	public void testRoom() throws Exception {
		IMemento mementoLocation = Mockito
				.mock(org.woym.common.objects.Location.Memento.class);
		IMemento menentoSchoolclass = Mockito
				.mock(org.woym.common.objects.Schoolclass.Memento.class);
		IMemento mementoActivityType = Mockito
				.mock(org.woym.common.objects.ActivityType.Memento.class);
		List<ActivityType> activityTypes = Arrays.asList(type1, type1);

		Mockito.when(DataAccess.getInstance().getOneLocation(room)).thenReturn(
				location);
		Mockito.when(location.createMemento()).thenReturn(
				(Memento) mementoLocation);

		Mockito.when(DataAccess.getInstance().getOneSchoolclass(room))
				.thenReturn(schoolclass);
		Mockito.when(schoolclass.createMemento())
				.thenReturn(
						(org.woym.common.objects.Schoolclass.Memento) menentoSchoolclass);

		Mockito.when(DataAccess.getInstance().getAllActivityTypes(room))
				.thenReturn(activityTypes);
		Mockito.when(type1.createMemento())
				.thenReturn(
						(org.woym.common.objects.ActivityType.Memento) mementoActivityType);

		MacroCommand macro = CommandCreator.getInstance().createDeleteCommand(
				room);

		AssertJUnit.assertEquals(7, macro.getCommands().size());

		AssertJUnit
				.assertTrue(macro.getCommands().get(0) instanceof UpdateCommand);
		AssertJUnit
				.assertTrue(macro.getCommands().get(1) instanceof DeleteCommand);

		AssertJUnit
				.assertTrue(macro.getCommands().get(2) instanceof UpdateCommand);
		AssertJUnit
				.assertTrue(macro.getCommands().get(3) instanceof UpdateCommand);
		AssertJUnit
				.assertTrue(macro.getCommands().get(4) instanceof UpdateCommand);
		AssertJUnit
				.assertTrue(macro.getCommands().get(5) instanceof UpdateCommand);
		AssertJUnit
				.assertTrue(macro.getCommands().get(6) instanceof DeleteCommand);
	}

	@Test
	public void testLocation() throws Exception {
		List<Room> rooms = Arrays.asList(room, room);
		IMemento menentoSchoolclass = Mockito
				.mock(org.woym.common.objects.Schoolclass.Memento.class);
		IMemento mementoActivityType = Mockito
				.mock(org.woym.common.objects.ActivityType.Memento.class);
		IMemento mementoRoom = Mockito
				.mock(org.woym.common.objects.Room.Memento.class);
		IMemento mementoTravelTimeList = Mockito
				.mock(TravelTimeList.Memento.class);
		Edge edge = Mockito.mock(Edge.class);

		List<Edge> edgeList = Arrays.asList(edge);
		List<ActivityType> activityTypes = Arrays.asList(type1, type1);

		Mockito.when(location.getRooms()).thenReturn(rooms);
		Mockito.when(room.createMemento()).thenReturn(
				(org.woym.common.objects.Room.Memento) mementoRoom);

		Mockito.when(DataAccess.getInstance().getOneSchoolclass(room))
				.thenReturn(schoolclass);
		Mockito.when(schoolclass.createMemento())
				.thenReturn(
						(org.woym.common.objects.Schoolclass.Memento) menentoSchoolclass);

		Mockito.when(DataAccess.getInstance().getAllActivityTypes(room))
				.thenReturn(activityTypes);
		Mockito.when(type1.createMemento())
				.thenReturn(
						(org.woym.common.objects.ActivityType.Memento) mementoActivityType);
		Mockito.when(travelTimeList.createMemento())
				.thenReturn(
						(org.woym.common.objects.TravelTimeList.Memento) mementoTravelTimeList);
		Mockito.when(travelTimeList.getTravelTimes(location)).thenReturn(
				edgeList);

		MacroCommand macro = CommandCreator.getInstance().createDeleteCommand(
				location);

		AssertJUnit.assertEquals(12, macro.getCommands().size());

		AssertJUnit
				.assertTrue(macro.getCommands().get(11) instanceof DeleteCommand);
		Mockito.verify(travelTimeList).remove(edge);

	}

	@Test
	public void testPedagogicAssistant() throws Exception {
		IMemento teamMemento = Mockito
				.mock(org.woym.common.objects.Classteam.Memento.class);
		List<Classteam> classteams = Arrays.asList(team1, team1);

		Mockito.when(
				DataAccess.getInstance().getAllClassteams(pedagogicAssistant))
				.thenReturn(classteams);
		Mockito.when(team1.createMemento()).thenReturn(
				(org.woym.common.objects.Classteam.Memento) teamMemento);

		List<Employee> employees = new ArrayList<Employee>();
		employees.add(teacher);
		employees.add(pedagogicAssistant);

		Mockito.when(team1.getEmployees()).thenReturn(employees);
		Mockito.when(team1.teacherLeft()).thenReturn(true);

		MacroCommand macro = CommandCreator.getInstance().createDeleteCommand(
				pedagogicAssistant);

		AssertJUnit.assertEquals(5, macro.getCommands().size());

		AssertJUnit
				.assertTrue(macro.getCommands().get(0) instanceof UpdateCommand);
		AssertJUnit
				.assertTrue(macro.getCommands().get(1) instanceof DeleteCommand);

		AssertJUnit
				.assertTrue(macro.getCommands().get(2) instanceof UpdateCommand);
		AssertJUnit
				.assertTrue(macro.getCommands().get(3) instanceof UpdateCommand);
		AssertJUnit
				.assertTrue(macro.getCommands().get(4) instanceof DeleteCommand);

	}

	@Test
	public void testTeacher() throws Exception {
		IMemento teamMemento = Mockito
				.mock(org.woym.common.objects.Employee.Memento.class);
		IMemento menentoSchoolclass = Mockito
				.mock(org.woym.common.objects.Schoolclass.Memento.class);
		List<Classteam> classteams = Arrays.asList(team1, team2);
		List<Schoolclass> schoolclasses = Arrays.asList(schoolclass,
				schoolclass);
		Teacher otherTeatcher = Mockito.mock(Teacher.class);

		List<Employee> teacher1 = new ArrayList<Employee>();
		teacher1.add(teacher);

		List<Employee> teacher2 = new ArrayList<Employee>();
		teacher2.add(otherTeatcher);
		teacher2.add(teacher);

		Mockito.when(DataAccess.getInstance().getAllClassteams(teacher))
				.thenReturn(classteams);
		Mockito.when(team1.createMemento()).thenReturn(
				(org.woym.common.objects.Employee.Memento) teamMemento);
		Mockito.when(team2.createMemento()).thenReturn(
				(org.woym.common.objects.Employee.Memento) teamMemento);
		Mockito.when(team1.getEmployees()).thenReturn(teacher2);
		Mockito.when(team2.getEmployees()).thenReturn(teacher1);
		Mockito.when(team1.teacherLeft()).thenReturn(true);
		Mockito.when(team2.teacherLeft()).thenReturn(false);

		Mockito.when(DataAccess.getInstance().getAllSchoolclasses(teacher))
				.thenReturn(schoolclasses);
		Mockito.when(schoolclass.createMemento())
				.thenReturn(
						(org.woym.common.objects.Schoolclass.Memento) menentoSchoolclass);

		MacroCommand macro = CommandCreator.getInstance().createDeleteCommand(
				teacher);

		Mockito.verify(schoolclass, Mockito.times(2)).setTeacher(null);

		AssertJUnit.assertEquals(7, macro.getCommands().size());

		AssertJUnit
				.assertTrue(macro.getCommands().get(0) instanceof UpdateCommand);
		AssertJUnit
				.assertTrue(macro.getCommands().get(1) instanceof DeleteCommand);

		AssertJUnit
				.assertTrue(macro.getCommands().get(2) instanceof UpdateCommand);
		AssertJUnit
				.assertTrue(macro.getCommands().get(3) instanceof DeleteCommand);
		AssertJUnit
				.assertTrue(macro.getCommands().get(4) instanceof UpdateCommand);
		AssertJUnit
				.assertTrue(macro.getCommands().get(5) instanceof UpdateCommand);
		AssertJUnit
				.assertTrue(macro.getCommands().get(6) instanceof DeleteCommand);
	}

	@Test
	public void testAcademicYearWhithClasses() throws Exception {
		IMemento memento = Mockito
				.mock(org.woym.common.objects.Classteam.Memento.class);
		List<Schoolclass> schoolclasses = Arrays.asList(schoolclass,
				schoolclass);

		Mockito.when(year.getSchoolclasses()).thenReturn(schoolclasses);

		Mockito.when(DataAccess.getInstance().getOneClassteam(schoolclass))
				.thenReturn(team1);
		Mockito.when(team1.createMemento()).thenReturn(memento);

		MacroCommand macro = CommandCreator.getInstance().createDeleteCommand(
				year);

		AssertJUnit.assertEquals(7, macro.getCommands().size());

		AssertJUnit
				.assertTrue(macro.getCommands().get(0) instanceof UpdateCommand);
		AssertJUnit
				.assertTrue(macro.getCommands().get(1) instanceof DeleteCommand);
		AssertJUnit
				.assertTrue(macro.getCommands().get(2) instanceof UpdateCommand);
		AssertJUnit
				.assertTrue(macro.getCommands().get(3) instanceof UpdateCommand);
		AssertJUnit
				.assertTrue(macro.getCommands().get(4) instanceof DeleteCommand);
		AssertJUnit
				.assertTrue(macro.getCommands().get(5) instanceof UpdateCommand);
		AssertJUnit
				.assertTrue(macro.getCommands().get(6) instanceof DeleteCommand);
	}

	@Test(expectedExceptions = DatasetException.class)
	public void testDatasetException() throws Exception {
		IMemento memento = Mockito
				.mock(org.woym.common.objects.Classteam.Memento.class);
		List<Schoolclass> schoolclasses = Arrays.asList(schoolclass,
				schoolclass);

		Mockito.when(year.getSchoolclasses()).thenReturn(schoolclasses);
		Mockito.when(dataAccess.getOneClassteam(Mockito.any(Schoolclass.class)))
				.thenThrow(new DatasetException("test"));

		Mockito.when(DataAccess.getInstance().getOneClassteam(schoolclass))
				.thenReturn(team1);
		Mockito.when(team1.createMemento()).thenReturn(memento);
		CommandCreator.getInstance().createDeleteCommand(year);
	}

	@Test
	public void testActivityTypeLessondType() throws Exception {
		LessonType lessonType = Mockito.mock(LessonType.class);
		CompoundLesson compoundLesson1 = Mockito.mock(CompoundLesson.class);
		CompoundLesson compoundLesson2 = Mockito.mock(CompoundLesson.class);
		Lesson lesson = Mockito.mock(Lesson.class);

		IMemento mementoEmployee = Mockito
				.mock(org.woym.common.objects.Employee.Memento.class);
		IMemento mementoCompoundLesson = Mockito
				.mock(org.woym.common.objects.CompoundLesson.Memento.class);
		IMemento mementoSchoolClass = Mockito
				.mock(org.woym.common.objects.Schoolclass.Memento.class);
		IMemento mementoYear = Mockito
				.mock(org.woym.common.objects.AcademicYear.Memento.class);

		List<Employee> employees = Arrays.asList(employee, employee);
		List<Lesson> lessons = Arrays.asList(lesson, lesson);
		List<CompoundLesson> compoundLessons = Arrays.asList(compoundLesson1,
				compoundLesson2);
		List<Schoolclass> schoolClasses = Arrays.asList(schoolclass,
				schoolclass);
		List<AcademicYear> years = Arrays.asList(year, year);

		Mockito.when(DataAccess.getInstance().getAllLessons(lessonType))
				.thenReturn(lessons);

		Mockito.when(DataAccess.getInstance().getAllCompoundLessons(lessonType))
				.thenReturn(compoundLessons);
		Mockito.when(compoundLesson1.createMemento())
				.thenReturn(
						(org.woym.common.objects.CompoundLesson.Memento) mementoCompoundLesson);
		Mockito.when(compoundLesson2.createMemento())
				.thenReturn(
						(org.woym.common.objects.CompoundLesson.Memento) mementoCompoundLesson);
		Mockito.when(compoundLesson1.remove(Mockito.any(LessonType.class)))
				.thenReturn(0);
		Mockito.when(compoundLesson2.remove(Mockito.any(LessonType.class)))
				.thenReturn(1);

		Mockito.when(DataAccess.getInstance().getAllSchoolclasses())
				.thenReturn(schoolClasses);
		Mockito.when(schoolclass.createMemento())
				.thenReturn(
						(org.woym.common.objects.Schoolclass.Memento) mementoSchoolClass);

		Mockito.when(DataAccess.getInstance().getAllAcademicYears())
				.thenReturn(years);
		Mockito.when(year.createMemento()).thenReturn(
				(org.woym.common.objects.AcademicYear.Memento) mementoYear);

		Mockito.when(DataAccess.getInstance().getAllEmployees(lessonType))
				.thenReturn(employees);
		Mockito.when(employee.createMemento()).thenReturn(
				(org.woym.common.objects.Employee.Memento) mementoEmployee);

		MacroCommand macro = CommandCreator.getInstance().createDeleteCommand(
				lessonType);

		AssertJUnit.assertEquals(13, macro.getCommands().size());

		AssertJUnit
				.assertTrue(macro.getCommands().get(0) instanceof UpdateCommand);
		AssertJUnit
				.assertTrue(macro.getCommands().get(1) instanceof DeleteCommand);

		AssertJUnit
				.assertTrue(macro.getCommands().get(2) instanceof DeleteCommand);
		AssertJUnit
				.assertTrue(macro.getCommands().get(3) instanceof DeleteCommand);

		Mockito.verify(compoundLesson1).setMemento(mementoCompoundLesson);

		AssertJUnit
				.assertTrue(macro.getCommands().get(4) instanceof DeleteCommand);
		AssertJUnit
				.assertTrue(macro.getCommands().get(5) instanceof UpdateCommand);

		AssertJUnit
				.assertTrue(macro.getCommands().get(6) instanceof UpdateCommand);
		AssertJUnit
				.assertTrue(macro.getCommands().get(7) instanceof UpdateCommand);

		AssertJUnit
				.assertTrue(macro.getCommands().get(8) instanceof UpdateCommand);
		AssertJUnit
				.assertTrue(macro.getCommands().get(9) instanceof UpdateCommand);

		AssertJUnit
				.assertTrue(macro.getCommands().get(10) instanceof UpdateCommand);
		AssertJUnit
				.assertTrue(macro.getCommands().get(11) instanceof UpdateCommand);

		AssertJUnit
				.assertTrue(macro.getCommands().get(12) instanceof DeleteCommand);

	}

	@Test
	public void testClassTeam() throws Exception {
		Classteam classteam = Mockito.mock(Classteam.class);

		MacroCommand macro = CommandCreator.getInstance().createDeleteCommand(
				classteam);

		AssertJUnit.assertTrue(macro.getCommands().size() == 1);

		AssertJUnit
				.assertTrue(macro.getCommands().get(0) instanceof DeleteCommand);
	}

	@Test(expectedExceptions = UnsupportedOperationException.class)
	public void testEntity() throws Exception {
		Entity entity = Mockito.mock(Entity.class);

		MacroCommand macro = CommandCreator.getInstance().createDeleteCommand(
				entity);

	}

}
