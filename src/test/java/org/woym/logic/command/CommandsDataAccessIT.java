package org.woym.logic.command;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.assertTrue;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.woym.exceptions.DatasetException;
import org.woym.logic.SuccessStatus;
import org.woym.objects.AcademicYear;
import org.woym.objects.ActivityType;
import org.woym.objects.Lesson;
import org.woym.objects.LessonType;
import org.woym.objects.Location;
import org.woym.objects.PedagogicAssistant;
import org.woym.objects.Room;
import org.woym.objects.Schoolclass;
import org.woym.objects.Teacher;
import org.woym.objects.spec.IMemento;
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

		assertTrue(macro.undo() instanceof SuccessStatus);
		assertTrue(dataAccess.getAllLessons(
				(LessonType) dataAccess.getOneActivityType("Mathe")).contains(
				lesson));

		assertTrue(macro.redo() instanceof SuccessStatus);
		assertTrue(dataAccess.getAllLessons(
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
		assertTrue(dataAccess.getAllActivities(room).isEmpty());
		assertEquals(1, dataAccess.getAllActivities().size());

		assertTrue(macro.undo() instanceof SuccessStatus);
		assertEquals(room, dataAccess.getOneRoom("Hauptstandort", "Raum 100"));
		assertEquals(3, dataAccess.getAllActivities(room).size());

		assertTrue(macro.redo() instanceof SuccessStatus);
		assertNull(dataAccess.getOneRoom("Hauptstandort", "Raum 100"));
		assertTrue(dataAccess.getAllActivities(room).isEmpty());

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
		assertEquals(3, dataAccess.getAllActivities(teacher).size());
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
		assertEquals(2, dataAccess.getAllActivities(pa).size());
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
		assertEquals(2, dataAccess.getAllActivities(dataAccess
				.getOneSchoolclass(1, 'a')).size());

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

		assertTrue(macro.undo() instanceof SuccessStatus);
		assertEquals(3, dataAccess.getAllActivities().size());
		assertEquals(location, dataAccess.getOneLocation("Hauptstandort"));
		assertEquals(2, dataAccess.getOneLocation("Hauptstandort").getRooms()
				.size());

		assertTrue(macro.redo() instanceof SuccessStatus);
		assertEquals(1, dataAccess.getAllActivities().size());

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
