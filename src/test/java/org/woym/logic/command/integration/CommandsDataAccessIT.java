package org.woym.logic.command.integration;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.woym.common.config.Config;
import org.woym.common.config.DefaultConfigEnum;
import org.woym.common.exceptions.DatasetException;
import org.woym.common.objects.AcademicYear;
import org.woym.common.objects.ActivityType;
import org.woym.common.objects.Classteam;
import org.woym.common.objects.CompoundLesson;
import org.woym.common.objects.Employee;
import org.woym.common.objects.Lesson;
import org.woym.common.objects.LessonType;
import org.woym.common.objects.Location;
import org.woym.common.objects.PedagogicAssistant;
import org.woym.common.objects.Room;
import org.woym.common.objects.Schoolclass;
import org.woym.common.objects.Teacher;
import org.woym.common.objects.TravelTimeList;
import org.woym.common.objects.spec.IMemento;
import org.woym.logic.SuccessStatus;
import org.woym.logic.command.AddCommand;
import org.woym.logic.command.CommandCreator;
import org.woym.logic.command.DeleteCommand;
import org.woym.logic.command.MacroCommand;
import org.woym.logic.command.UpdateCommand;
import org.woym.persistence.DataAccess;

@Test(groups = { "CommandsDataAccessIT", "integration" }, dependsOnGroups = {
		"DataAccessObjectsIT", "DataAccessObjectsIT2" })
public class CommandsDataAccessIT {

	private LessonType l;

	private AddCommand<LessonType> addCommand;

	private UpdateCommand<LessonType> updateCommand;

	private DeleteCommand<LessonType> deleteCommand;

	private DataAccess dataAccess = DataAccess.getInstance();

	@BeforeClass
	public void setUp() {
		Config.init();
		l = new LessonType();
		l.setName("Erdkunde");
		addCommand = new AddCommand<LessonType>(l);
	}

	@Test(groups = "AddCommand")
	public void addCommandExecuteSuccess() throws DatasetException {
		assertTrue(addCommand.execute() instanceof SuccessStatus);
		assertTrue(dataAccess.getAllLessonTypes().contains(l));
	}

	@Test(groups = "AddCommand", dependsOnMethods = "addCommandExecuteSuccess")
	public void addCommandUndoSuccess() throws Exception {
		assertTrue(addCommand.undo() instanceof SuccessStatus);
		assertTrue(!dataAccess.getAllLessonTypes().contains(l));
	}

	@Test(groups = "AddCommand", dependsOnMethods = "addCommandUndoSuccess")
	public void addCommandRedoSuccess() throws DatasetException {
		assertTrue(addCommand.redo() instanceof SuccessStatus);
		assertTrue(dataAccess.getAllLessonTypes().contains(l));
	}

	@Test(groups = "UpdateCommand", dependsOnGroups = "AddCommand")
	public void updateCommandExecuteSuccess() throws Exception {
		IMemento memento = l.createMemento();
		l.setName("Kunst");
		l.setTypicalDuration(45);
		l.add(dataAccess.getOneRoom("Hauptstandort", "Raum 100"));
		updateCommand = new UpdateCommand<LessonType>(l, memento);

		assertTrue(updateCommand.execute() instanceof SuccessStatus);
		assertTrue(dataAccess.getAllLessonTypes().contains(l));
		assertTrue(checkUpdateCommandSuccess());
	}

	@Test(groups = "UpdateCommand", dependsOnMethods = "updateCommandExecuteSuccess")
	public void updateCommandUndoSuccess() throws Exception {
		assertTrue(updateCommand.undo() instanceof SuccessStatus);
		boolean condition = false;
		for (LessonType l : dataAccess.getAllLessonTypes()) {
			if (l.getName().equals("Erdkunde") && l.getTypicalDuration() == 0
					&& l.getRooms().size() == 0) {
				condition = !condition;
			}
		}
		assertTrue(condition);
	}

	@Test(groups = "UpdateCommand", dependsOnMethods = "updateCommandUndoSuccess")
	public void updateCommandRedoSuccess() throws Exception {
		assertTrue(updateCommand.redo() instanceof SuccessStatus);
		assertTrue(checkUpdateCommandSuccess());
	}

	@Test(groups = "DeleteCommand", dependsOnGroups = "UpdateCommand")
	public void deleteCommandExecuteSuccess() throws Exception {
		deleteCommand = new DeleteCommand<LessonType>(l);
		assertTrue(deleteCommand.execute() instanceof SuccessStatus);
		assertFalse(dataAccess.getAllLessonTypes().contains(l));
	}

	@Test(groups = "DeleteCommand", dependsOnMethods = "deleteCommandExecuteSuccess")
	public void deleteCommandUndoSuccess() throws Exception {
		assertTrue(deleteCommand.undo() instanceof SuccessStatus);
		assertTrue(dataAccess.getAllLessonTypes().contains(l));
	}

	@Test(groups = "DeleteCommand", dependsOnMethods = "deleteCommandUndoSuccess")
	public void deleteCommandRedoSuccess() throws Exception {
		assertTrue(deleteCommand.redo() instanceof SuccessStatus);
		assertFalse(dataAccess.getAllLessonTypes().contains(l));
	}

	@Test(groups = "CommandCreator", dependsOnGroups = "DeleteCommand")
	public void commandCreatorDeleteActivitySuccess() throws Exception {
		Lesson lesson = dataAccess.getAllLessons(
				(LessonType) dataAccess.getOneActivityType("Mathe")).get(0);

		MacroCommand macro = CommandCreator.getInstance().createDeleteCommand(
				lesson);
		assertTrue(macro.execute() instanceof SuccessStatus);
		assertTrue(dataAccess.getAllLessons(
				(LessonType) dataAccess.getOneActivityType("Mathe")).isEmpty());
		BigDecimal allocatedHoursAfterDelete = dataAccess.getOneEmployee("MEY")
				.getAllocatedHours();
		BigDecimal expected = new BigDecimal(0)
				.subtract(new BigDecimal(lesson.getTime().getDuration()))
				.divide(new BigDecimal(
						Config.getSingleIntValue(DefaultConfigEnum.TEACHER_HOURLY_SETTLEMENT)))
				.setScale(Employee.SCALE, RoundingMode.HALF_UP);
		assertEquals(expected, allocatedHoursAfterDelete);

		assertTrue(macro.undo() instanceof SuccessStatus);
		assertTrue(dataAccess.getAllLessons(
				(LessonType) dataAccess.getOneActivityType("Mathe")).contains(
				lesson));

		assertTrue(macro.redo() instanceof SuccessStatus);
		assertTrue(dataAccess.getAllLessons(
				(LessonType) dataAccess.getOneActivityType("Mathe")).isEmpty());

		macro.undo();
	}

	@Test(groups = "CommandCreator", dependsOnGroups = "DeleteCommand")
	public void commandCreatorDeleteCompoundLessonSuccess() throws Exception {
		CompoundLesson compoundLesson = dataAccess.getAllCompoundLessons(
				(LessonType) dataAccess.getOneActivityType("Mathe")).get(0);

		MacroCommand macro = CommandCreator.getInstance().createDeleteCommand(
				compoundLesson);
		assertTrue(macro.execute() instanceof SuccessStatus);
		assertTrue(dataAccess.getAllCompoundLessons(
				(LessonType) dataAccess.getOneActivityType("Mathe")).isEmpty());
		BigDecimal allocatedHoursTeacher = dataAccess.getOneEmployee("MEY")
				.getAllocatedHours();
		BigDecimal allocatedHoursPA = dataAccess.getOneEmployee("MUS")
				.getAllocatedHours();

		BigDecimal expected = new BigDecimal(0)
				.subtract(new BigDecimal(30).divide(
						new BigDecimal(
								Config.getSingleIntValue(DefaultConfigEnum.TEACHER_HOURLY_SETTLEMENT)),
						Employee.SCALE, RoundingMode.HALF_UP));
		assertEquals(expected, allocatedHoursTeacher);

		expected = new BigDecimal(0)
				.subtract(new BigDecimal(45).divide(
						new BigDecimal(
								Config.getSingleIntValue(DefaultConfigEnum.PEDAGOGIC_ASSISTANT_HOURLY_SETTLEMENT)),
						Employee.SCALE, RoundingMode.HALF_UP));
		assertEquals(expected, allocatedHoursPA);

		assertTrue(macro.undo() instanceof SuccessStatus);
		assertTrue(dataAccess.getAllCompoundLessons(
				(LessonType) dataAccess.getOneActivityType("Mathe")).contains(
				compoundLesson));

		assertTrue(macro.redo() instanceof SuccessStatus);
		assertTrue(dataAccess.getAllCompoundLessons(
				(LessonType) dataAccess.getOneActivityType("Mathe")).isEmpty());

		macro.undo();
	}

	@Test(groups = "CommandCreator", dependsOnGroups = "DeleteCommand", dependsOnMethods = "commandCreatorDeleteActivitySuccess")
	public void commandCreatorDeleteRoomSuccess() throws Exception {
		Room room = dataAccess.getOneRoom("Hauptstandort", "Raum 100");

		MacroCommand macro = CommandCreator.getInstance().createDeleteCommand(
				room);
		assertTrue(macro.execute() instanceof SuccessStatus);
		assertNull(dataAccess.getOneRoom("Hauptstandort", "Raum 100"));
		assertTrue(dataAccess.getAllActivities(room, false).isEmpty());
		assertEquals(1, dataAccess.getAllActivities().size());

		assertTrue(macro.undo() instanceof SuccessStatus);
		assertEquals(room, dataAccess.getOneRoom("Hauptstandort", "Raum 100"));
		assertEquals(3, dataAccess.getAllActivities(room, false).size());

		assertTrue(macro.redo() instanceof SuccessStatus);
		assertNull(dataAccess.getOneRoom("Hauptstandort", "Raum 100"));
		assertTrue(dataAccess.getAllActivities(room, false).isEmpty());

		macro.undo();
	}

	@Test(groups = "CommandCreator", dependsOnGroups = "DeleteCommand", dependsOnMethods = "commandCreatorDeleteRoomSuccess")
	public void commandCreatorDeleteSchoolclassSuccess() throws Exception {
		Schoolclass schoolclass = dataAccess.getOneSchoolclass(1, 'a');

		MacroCommand macro = CommandCreator.getInstance().createDeleteCommand(
				schoolclass);
		assertTrue(macro.execute() instanceof SuccessStatus);
		assertNull(dataAccess.getOneSchoolclass(1, 'a'));
		assertEquals(2, dataAccess.getAllActivities().size());

		assertTrue(macro.undo() instanceof SuccessStatus);
		assertEquals(schoolclass, dataAccess.getOneSchoolclass(1, 'a'));
		assertEquals(3, dataAccess.getAllActivities().size());

		assertTrue(macro.redo() instanceof SuccessStatus);
		assertNull(dataAccess.getOneSchoolclass(1, 'a'));
		assertEquals(2, dataAccess.getAllActivities().size());

		macro.undo();
	}

	@Test(groups = "CommandCreator", dependsOnGroups = "DeleteCommand", dependsOnMethods = "commandCreatorDeleteSchoolclassSuccess")
	public void commandCreatorDeleteTeacherSuccess() throws Exception {
		Teacher teacher = (Teacher) dataAccess.getOneEmployee("MEY");

		MacroCommand macro = CommandCreator.getInstance().createDeleteCommand(
				teacher);
		assertTrue(macro.execute() instanceof SuccessStatus);
		checkDeleteTeacher();

		assertTrue(macro.undo() instanceof SuccessStatus);
		assertEquals(teacher, dataAccess.getOneEmployee("MEY"));
		assertEquals(3, dataAccess.getAllActivities().size());
		for (Schoolclass s : dataAccess.getAllSchoolclasses()) {
			assertEquals(teacher, s.getTeacher());
		}
		assertEquals(3, dataAccess.getAllActivities(teacher, false).size());
		assertEquals(2, dataAccess.getAllClassteams().size());

		assertTrue(macro.redo() instanceof SuccessStatus);
		checkDeleteTeacher();

		macro.undo();
	}

	@Test(groups = "CommandCreator", dependsOnMethods = "commandCreatorDeleteTeacherSuccess")
	public void commandCreatorDeletePASuccess() throws Exception {
		PedagogicAssistant pa = dataAccess.getAllPAs().get(0);

		MacroCommand macro = CommandCreator.getInstance().createDeleteCommand(
				pa);
		assertTrue(macro.execute() instanceof SuccessStatus);
		checkDeletePA();

		assertTrue(macro.undo() instanceof SuccessStatus);
		assertEquals(pa, dataAccess.getOneEmployee("MUS"));
		assertEquals(2, dataAccess.getAllActivities(pa, false).size());
		assertEquals(1, dataAccess.getAllClassteams(pa).size());

		assertTrue(macro.redo() instanceof SuccessStatus);
		checkDeletePA();

		macro.undo();
	}

	@Test(groups = "CommandCreator", dependsOnMethods = "commandCreatorDeletePASuccess")
	public void commandCreatorDeleteLessonTypeSuccess() throws Exception {
		ActivityType a = dataAccess.getOneActivityType("Mathe");

		MacroCommand macro = CommandCreator.getInstance()
				.createDeleteCommand(a);
		assertTrue(macro.execute() instanceof SuccessStatus);
		assertEquals(2, dataAccess.getAllActivities().size());
		assertEquals(1, dataAccess.getAllTeachers().size());
		assertEquals(1, dataAccess.getAllPAs().size());

		assertTrue(macro.undo() instanceof SuccessStatus);
		assertEquals(3, dataAccess.getAllActivities().size());
		assertEquals(1, dataAccess.getAllLessons((LessonType) a).size());
		assertEquals(1, dataAccess.getAllCompoundLessons((LessonType) a).size());
		assertEquals(2, dataAccess.getAllEmployees(a).size());

		assertTrue(macro.redo() instanceof SuccessStatus);
		assertEquals(2, dataAccess.getAllActivities().size());

		macro.undo();
	}

	@Test(groups = "CommandCreator", dependsOnMethods = "commandCreatorDeleteLessonTypeSuccess")
	public void commandCreatorDeleteAcademicYearSuccess() throws Exception {
		AcademicYear a = dataAccess.getOneAcademicYear(1);

		MacroCommand macro = CommandCreator.getInstance()
				.createDeleteCommand(a);
		assertTrue(macro.execute() instanceof SuccessStatus);
		assertNull(dataAccess.getOneSchoolclass(1, 'a'));
		assertEquals(2, dataAccess.getAllActivities().size());

		assertTrue(macro.undo() instanceof SuccessStatus);
		assertEquals(a, dataAccess.getOneAcademicYear(1));
		assertNotNull(dataAccess.getOneSchoolclass(1, 'a'));
		assertEquals(3, dataAccess.getAllActivities().size());
		assertEquals(
				2,
				dataAccess.getAllActivities(
						dataAccess.getOneSchoolclass(1, 'a'), false).size());

		assertTrue(macro.redo() instanceof SuccessStatus);
		assertNull(dataAccess.getOneSchoolclass(1, 'a'));
		assertEquals(2, dataAccess.getAllActivities().size());

		macro.undo();
	}

	@Test(groups = "CommandCreator", dependsOnMethods = "commandCreatorDeleteAcademicYearSuccess")
	public void commandCreatorDeleteLocationSuccess() throws Exception {
		Location location = dataAccess.getOneLocation("Hauptstandort");

		MacroCommand macro = CommandCreator.getInstance().createDeleteCommand(
				location);
		assertTrue(macro.execute() instanceof SuccessStatus);
		assertNull(dataAccess.getOneLocation("Hauptstandort"));
		assertEquals(1, dataAccess.getAllActivities().size());
		assertEquals(2, dataAccess.getAllSchoolclasses().size());
		assertTrue(TravelTimeList.getInstance().getEdges().isEmpty());

		assertTrue(macro.undo() instanceof SuccessStatus);
		assertEquals(3, dataAccess.getAllActivities().size());
		assertEquals(location, dataAccess.getOneLocation("Hauptstandort"));
		assertEquals(2, dataAccess.getOneLocation("Hauptstandort").getRooms()
				.size());
		assertEquals(1, TravelTimeList.getInstance().getEdges().size());

		assertTrue(macro.redo() instanceof SuccessStatus);
		assertEquals(1, dataAccess.getAllActivities().size());

		macro.undo();
	}

	@Test(groups = "CommandCreator", dependsOnMethods = "commandCreatorDeleteLocationSuccess")
	public void commandCreatorDeleteClassteamSuccess() throws Exception {
		Classteam classteam = dataAccess.getOneClassteam(dataAccess
				.getOneSchoolclass(1, 'a'));

		MacroCommand macro = CommandCreator.getInstance().createDeleteCommand(
				classteam);
		assertTrue(macro.execute() instanceof SuccessStatus);
		assertNull(dataAccess.getOneClassteam(dataAccess.getOneSchoolclass(1,
				'a')));

		assertTrue(macro.undo() instanceof SuccessStatus);
		assertEquals(classteam, dataAccess.getOneClassteam(dataAccess
				.getOneSchoolclass(1, 'a')));

		assertTrue(macro.redo() instanceof SuccessStatus);
		assertNull(dataAccess.getOneClassteam(dataAccess.getOneSchoolclass(1,
				'a')));

		macro.undo();
	}

	private boolean checkUpdateCommandSuccess() throws Exception {
		boolean condition = false;
		for (LessonType l : dataAccess.getAllLessonTypes()) {
			if (l.getName().equals("Kunst")
					&& l.getTypicalDuration() == 45
					&& l.getRooms()
							.get(0)
							.equals(dataAccess.getOneRoom("Hauptstandort",
									"Raum 100"))) {
				condition = !condition;
			}
		}
		return condition;
	}

	private void checkDeleteTeacher() throws Exception {
		assertNull(dataAccess.getOneEmployee("MEY"));
		// TODO: Prüfen, ob Verhalten bezüglich Beibehaltung von CompoundLesson
		// ohne Lehrer so gewollt
		assertEquals(2, dataAccess.getAllActivities().size());
		for (Schoolclass s : dataAccess.getAllSchoolclasses()) {
			assertNull(s.getTeacher());
		}
		assertTrue(dataAccess.getAllClassteams().isEmpty());
	}

	private void checkDeletePA() throws Exception {
		assertNull(dataAccess.getOneEmployee("MUS"));
		assertEquals(3, dataAccess.getAllActivities().size());
		assertEquals(2, dataAccess.getAllClassteams().size());
	}
}
