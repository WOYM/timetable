package org.woym.logic.command;

import java.util.LinkedList;
import java.util.List;

import org.woym.common.exceptions.DatasetException;
import org.woym.common.objects.AcademicYear;
import org.woym.common.objects.Activity;
import org.woym.common.objects.ActivityType;
import org.woym.common.objects.Classteam;
import org.woym.common.objects.CompoundLesson;
import org.woym.common.objects.Employee;
import org.woym.common.objects.EmployeeTimePeriods;
import org.woym.common.objects.Entity;
import org.woym.common.objects.Lesson;
import org.woym.common.objects.LessonType;
import org.woym.common.objects.Location;
import org.woym.common.objects.Room;
import org.woym.common.objects.Schoolclass;
import org.woym.common.objects.Teacher;
import org.woym.common.objects.TravelTimeList;
import org.woym.common.objects.TravelTimeList.Edge;
import org.woym.common.objects.spec.IActivityObject;
import org.woym.common.objects.spec.IMemento;
import org.woym.logic.spec.ICommand;
import org.woym.persistence.DataAccess;

/**
 * Diese Singleton-Klasse bietet eine Methode, die ein Delete-MacroCommand
 * erzeugt, so dass vor dem Löschen eines Objektes alle nötigen Abhängigkeiten
 * aufgelöst werden.
 * 
 * @author JurSch, Adrian
 *
 */
public class CommandCreator {

	private static final CommandCreator COMMAND_CREATOR = new CommandCreator();

	private CommandCreator() {

	}

	/**
	 * Gibt die Singleton-Instanz dieser Klasse zurück.
	 * 
	 * @return die Singleton-Instanz dieser Klasse
	 */
	public static CommandCreator getInstance() {
		return COMMAND_CREATOR;
	}

	/**
	 * Diese Methode erzeugt je nach übergebenem {@linkplain Entity}-Objekt ein
	 * entsprechendes {@linkplain MacroCommand}. Wird {@code null} übergeben,
	 * wird eine {@linkplain IllegalArgumentException} geworfen. <br>
	 * Der Methode bekannte Parameter sind Objekte vom Typ {@linkplain Activity}
	 * , {@linkplain Room}, {@linkplain Schoolclass}, {@linkplain Employee},
	 * {@linkplain ActivityType}, {@linkplain AcademicYear},
	 * {@linkplain Location} und {@linkplain Classteam}. Wird ein Objekt anderen Typs übergeben, wird eine
	 * {@linkplain UnsupportedOperationException} geworfen.<br>
	 * Für bekannte Objekte werden zunächst alle Referenzen bei anderen Objekten
	 * aufgelöst und entsprechende Commands dem {@linkplain MacroCommand}
	 * hinzugefügt. Als letztes wird dem {@linkplain MacroCommand} das
	 * tatsächliche {@linkplain DeleteCommand} für das übergebene
	 * {@linkplain Entity}-Objekt hinzugefügt und das {@linkplain MacroCommand}
	 * zurückgegeben. Sollte bei diesem Prozess eine
	 * {@linkplain DatasetException} auftreten, wird ein leeres
	 * {@linkplain MacroCommand} zurückgegeben.
	 * 
	 * @param entity
	 *            - {@linkplain Entity}-Objekt, welches gelöscht werden soll
	 * @return ein {@linkplain MacroCommand}, welches alle nötigen Commands zum
	 *         Löschen des {@linkplain Entity}-Objektes beinhaltet. Bei einem
	 *         Fehler bei der Erzeugung des MacroCommands, wird ein leeres
	 *         {@linkplain MacroCommand} zurückgegeben.
	 */
	public MacroCommand createDeleteCommand(final Entity entity) {
		if (entity == null) {
			throw new IllegalArgumentException("Entity was null");
		}

		MacroCommand macro = new MacroCommand();

		// Eine Aktivität kann direkt gelöscht werden
		if (entity instanceof Activity) {
			macro.add(new DeleteCommand<Entity>(entity));
		} else {
			// Liste der Commands, welche dem MacroCommand hinzugefügt werden
			// sollen
			LinkedList<ICommand> commands = new LinkedList<ICommand>();
			try {
				if (entity instanceof IActivityObject) {
					LinkedList<ICommand> activityCommands = relationActivity((IActivityObject) entity);
					LinkedList<ICommand> activityObjectsCommands = relationActivityObjects((IActivityObject) entity);

					commands.addAll(activityCommands);
					commands.addAll(activityObjectsCommands);

				} else if (entity instanceof AcademicYear) {
					// Beim Löschen eines Jahrgangs müssen alle Referenzen der
					// beinhalteten Schulklassen in anderen Objekten (außer im
					// Jahrgang selbst) aufgelöst werden

					List<Schoolclass> list = ((AcademicYear) entity)
							.getSchoolclasses();

					for (Schoolclass s : list) {
						commands.addAll(relationActivity(s));
						commands.addAll(relationSchoolClass(s, true));
					}
					commands.addLast(new DeleteCommand<Entity>(entity));

				} else if (entity instanceof Location) {
					// Beim Löschen eines Standortes müssen alle Referenzen der
					// beinhalteten Räume in anderen Objekten (außer im Standort
					// selbst) aufgelöst werden

					List<Room> rooms = ((Location) entity).getRooms();

					for (Room r : rooms) {
						commands.addAll(relationActivity(r));
						commands.addAll(relationRoom(r, true));
					}
					IMemento memento = TravelTimeList.getInstance()
							.createMemento();
					for (Edge e : TravelTimeList.getInstance().getTravelTimes(
							(Location) entity)) {
						TravelTimeList.getInstance().remove(e);
					}
					commands.addLast(new UpdateCommand<Entity>(TravelTimeList
							.getInstance(), memento));
					commands.addLast(new DeleteCommand<Entity>(entity));
				} else if (entity instanceof Classteam) {
					commands.add(new DeleteCommand<Entity>(entity));
				} else {
					throw new UnsupportedOperationException(
							"Entity not supported.");
				}
			} catch (DatasetException e) {
				// Leeres MacroCommand im Fehlerfall
				commands = new LinkedList<ICommand>();
			}
			macro = listToMacro(commands);
		}
		return macro;
	}

	/**
	 * Fügt die übergebene Liste von {@linkplain ICommand}-Objekten einem
	 * {@linkplain MacroCommand} hinzu und gibt dieses zurück.
	 * 
	 * @param list
	 *            - Liste der hinzuzufügenden {@linkplain ICommand}-Objekte
	 * @return ein {@linkplain MacroCommand}, gefüllt mit den übergebenen
	 *         Commands
	 */
	private MacroCommand listToMacro(List<ICommand> list) {
		MacroCommand macro = new MacroCommand();

		for (ICommand c : list) {
			macro.add(c);
		}
		return macro;
	}

	/**
	 * Gibt eine Liste von {@linkplain ICommand}-Objekten zurück, welche die
	 * Referenzen des übergebenen {@linkplain IActivityObject} bei allen
	 * Aktivitäten, wo das übergebene Objekt beteiltigt war, auflöst. Hat eine
	 * Aktivität nach dem Entfernen des übergebenen Objektes kein Objekt von
	 * diesem Typ mehr, wird der Liste ein {@linkplain DeleteCommand} für die
	 * Aktivität hinzugefügt, ansonsten lediglich ein {@linkplain UpdateCommand}
	 * .
	 * 
	 * @param entity
	 *            - {@linkplain IActivityObject}, für welches die Referenzen bei
	 *            seinen Aktivitäten aufgelöst werden sollen
	 * @return Liste von {@linkplain ICommand}-Objekten, welche in der gegebenen
	 *         Reihenfolge ausgeführt werden müssen, um die Referenzen
	 *         aufzuheben
	 * @throws DatasetException
	 */
	private LinkedList<ICommand> relationActivity(IActivityObject entity)
			throws DatasetException {
		LinkedList<ICommand> macro = new LinkedList<ICommand>();

		List<Activity> list = DataAccess.getInstance().getAllActivities(
				(IActivityObject) entity);

		for (Activity a : list) {
			IMemento memento = a.createMemento();
			int size = a.remove(entity);
			if (size == 0) {
				a.setMemento(memento);
				a.update();
				macro.add(new DeleteCommand<Entity>(a));
			} else if (size > 0) {
				macro.add(new UpdateCommand<Entity>(a, memento));
			}
		}

		return macro;
	}

	/**
	 * Gibt eine Liste von {@linkplain ICommand}-Objekten zurück, welche in der
	 * gegebenen Reihenfolge ausgeführt werden müssten, um die Refernzen des
	 * übergebenen Objektes bei anderen Objekten aufzulösen und das Objekt
	 * schließlich zu löschen. Im Fall von {@linkplain Room} und
	 * {@linkplain Schoolclass}, ist die Ausführungsreihenfolge von
	 * {@linkplain CommandCreator#relationRoomLocation(Room)} und
	 * {@linkplain CommandCreator#relationRoom(Room)} bzw.
	 * {@linkplain CommandCreator#relationSchoolClassAcademicYear(Schoolclass)}
	 * und {@linkplain CommandCreator#relationSchoolClass(Schoolclass)}
	 * einzuhalten.<br>
	 * <br>
	 * <b>HINWEIS:</b> Vor der Liste von Commands dieser Methode wird die Liste
	 * von {@linkplain CommandCreator#relationActivity(IActivityObject)}
	 * benötigt, um die Referenzen in Aktivitäten aufzulösen.
	 * 
	 * @param entity
	 *            - das {@linkplain IActivityObject}, welches gelöscht werden
	 *            soll
	 * @return Liste der benötigten Commands, um das übergebene
	 *         {@linkplain IActivityObject} zu löschen
	 * @throws DatasetException
	 */
	private LinkedList<ICommand> relationActivityObjects(IActivityObject entity)
			throws DatasetException {
		LinkedList<ICommand> macro = new LinkedList<ICommand>();
		if (entity instanceof Room) {
			macro.addAll(relationRoomLocation((Room) entity));
			macro.addAll(relationRoom((Room) entity, false));
		} else if (entity instanceof Schoolclass) {
			macro.addAll(relationSchoolClassAcademicYear((Schoolclass) entity));
			macro.addAll(relationSchoolClass((Schoolclass) entity, false));
		} else if (entity instanceof Employee) {
			macro.addAll(relationEmployee((Employee) entity));
		} else if (entity instanceof ActivityType) {
			macro.addAll(relationActivityType((ActivityType) entity));
		} else {
			throw new UnsupportedOperationException(
					"This IActivityObject implementing class is not supported.");
		}
		return macro;
	}

	/**
	 * Gibt eine Liste von {@linkplain ICommand}-Objekten zurück, welche
	 * zunächst Commands beinhaltet, die alle Referenzen auf das übergebene
	 * {@linkplain Employee}-Objekt in anderen Objekten (außer
	 * {@linkplain Activity} auflösen und schließlich das
	 * {@linkplain DeleteCommand}, mit welchem das übergebene
	 * {@linkplain Employee}-Objekt selbst gelöscht wird.<br>
	 * <br>
	 * <b>HINWEIS:</b> Vor der Liste von Commands dieser Methode wird die Liste
	 * von {@linkplain CommandCreator#relationActivity(IActivityObject)}
	 * benötigt, um die Referenzen in Aktivitäten aufzulösen.
	 * 
	 * @param employee
	 *            - das zu löschende {@linkplain Employee}-Objekt
	 * @return Liste der benötigten {@linkplain ICommand}-Objekte, um das
	 *         übergebene {@linkplain Employee} zu löschen (s. a. Hinweis oben)
	 * @throws DatasetException
	 */
	private LinkedList<ICommand> relationEmployee(Employee employee)
			throws DatasetException {
		LinkedList<ICommand> macro = new LinkedList<ICommand>();

		// Referenzen bei EmployeeTimePeriods-Objekten auflösen
		List<EmployeeTimePeriods> employeeTimePeriods = DataAccess
				.getInstance().getEmployeeTimePeriods(employee);
		for (EmployeeTimePeriods e : employeeTimePeriods) {
			macro.addLast(new DeleteCommand<Entity>(e));
		}

		// Referenzen bei den Klassenteams auflösen
		List<Classteam> classteams = DataAccess.getInstance().getAllClassteams(
				employee);
		for (Classteam c : classteams) {
			IMemento memento = c.createMemento();
			c.remove(employee);
			if (!c.teacherLeft()) {
				c.setMemento(memento);
				macro.addLast(new DeleteCommand<Entity>(c));
			} else {
				macro.addLast(new UpdateCommand<Entity>(c, memento));
			}
		}

		// Referenzen bei den Schulklassen auflösen, sofern der übergebene
		// Employee vom Typ Teacher ist
		if (employee instanceof Teacher) {
			List<Schoolclass> classes = DataAccess.getInstance()
					.getAllSchoolclasses((Teacher) employee);

			for (Schoolclass s : classes) {
				IMemento memento = s.createMemento();

				s.setTeacher(null);
				macro.addLast(new UpdateCommand<Entity>(s, memento));
			}
		}

		macro.addLast(new DeleteCommand<Entity>(employee));

		return macro;
	}

	/**
	 * Gibt eine Liste von {@linkplain ICommand}-Objekten zurück, welche
	 * zunächst Commands beinhaltet, die alle Referenzen auf das übergebene
	 * {@linkplain Schoolclass}-Objekt in {@linkplain Classteam}-Objekten
	 * auflösen und schließlich das {@linkplain DeleteCommand}, mit welchem das
	 * übergebene {@linkplain Schoolclass}-Objekt selbst gelöscht wird.<br>
	 * <br>
	 * <b>HINWEIS:</b> Vor der Liste von Commands dieser Methode wird zum
	 * Löschen einer Schulklasse die Liste von
	 * {@linkplain CommandCreator#relationSchoolClassAcademicYear(Schoolclass)}
	 * benötigt. Vor der Liste von Commands dieser Methode wiederum die Commands
	 * von {@linkplain CommandCreator#relationActivity(IActivityObject)}.
	 * 
	 * @param schoolclass
	 *            - die zu löschende Schulklasse
	 * @param deleteAcademicYear
	 *            - true, wenn diese Methode beim Löschen eines Jahrgangs
	 *            aufgerufen wird
	 * @return Liste von {@linkplain ICommand}-Objekten (s.a. Hinweis oben)
	 * @throws DatasetException
	 */
	private LinkedList<ICommand> relationSchoolClass(Schoolclass schoolclass,
			boolean deleteAcademicYear) throws DatasetException {
		LinkedList<ICommand> macro = new LinkedList<ICommand>();

		Classteam team = DataAccess.getInstance().getOneClassteam(schoolclass);
		if (team != null) {
			IMemento memento = team.createMemento();
			team.remove(schoolclass);

			macro.addLast(new UpdateCommand<Entity>(team, memento));
		}

		if (!deleteAcademicYear) {
			macro.addLast(new DeleteCommand<Entity>(schoolclass));
		}
		return macro;
	}

	/**
	 * Gibt eine Liste von {@linkplain ICommand}-Objekten zurück, die die
	 * Referenzen von der übergebenen {@linkplain Schoolclass} zu ihrem Jahrgang
	 * auflösen. <br>
	 * <br>
	 * 
	 * <b>HINWEIS:</b> Wird nur beim Löschen einer einzelnen Schulklasse, nicht
	 * beim Löschen eines Jahrgangs benötigt. Muss dann aber vor
	 * {@linkplain CommandCreator#relationSchoolClass(Schoolclass)} ausgeführt
	 * werden.
	 * 
	 * @param schoolclass
	 *            - zu löschende Schulklasse
	 * @return Liste von {@linkplain ICommand}-Objekten, die die Referenzen von
	 *         der übergebenen {@linkplain Schoolclass} zu ihrem Jahrgang
	 *         auflösen
	 * @throws DatasetException
	 */
	private LinkedList<ICommand> relationSchoolClassAcademicYear(
			Schoolclass schoolclass) throws DatasetException {

		LinkedList<ICommand> macro = new LinkedList<ICommand>();

		AcademicYear year = DataAccess.getInstance().getOneAcademicYear(
				schoolclass);
		if (year != null) {
			IMemento yearMemento = year.createMemento();
			year.remove(schoolclass);

			macro.addLast(new UpdateCommand<Entity>(year, yearMemento));
		}
		return macro;
	}

	/**
	 * Gibt eine Liste von {@linkplain ICommand}-Objekten zurück, welche
	 * zunächst Commands beinhaltet, die alle Referenzen auf das übergebene
	 * {@linkplain Room}-Objekt in {@linkplain Schoolclass}- und
	 * {@linkplain ActivityType}-Objekten auflösen und schließlich das
	 * {@linkplain DeleteCommand}, mit welchem das übergebene {@linkplain Room}
	 * -Objekt selbst gelöscht wird.<br>
	 * <br>
	 * <b>HINWEIS:</b> Vor der Liste von Commands dieser Methode wird zum
	 * Löschen eines Raumes die Liste von
	 * {@linkplain CommandCreator#relationRoomLocation(Room)} benötigt. Vor der
	 * Liste von Commands dieser Methode wiederum die Commands von
	 * {@linkplain CommandCreator#relationActivity(IActivityObject)}.
	 * 
	 * @param room
	 *            - der zu löschende Raum
	 * @param - {@code true}, wenn diese Methode bei der Löschung eines
	 *        Jahrgangs aufgerufen wird, ansonsten {@code false}
	 * @return Liste von {@linkplain ICommand}-Objekten, die in der Lage sind,
	 *         Referenzen in {@linkplain Schoolclass} und
	 *         {@linkplain ActivityType}-Objekten für den übergebenen Raum
	 *         aufzulösen
	 * @throws DatasetException
	 */
	private LinkedList<ICommand> relationRoom(Room room, boolean deleteLocation)
			throws DatasetException {
		LinkedList<ICommand> macro = new LinkedList<ICommand>();

		Schoolclass schoolclass = DataAccess.getInstance().getOneSchoolclass(
				room);
		if (schoolclass != null) {
			IMemento secondMemento = schoolclass.createMemento();
			schoolclass.setRoom(null);

			macro.addLast(new UpdateCommand<Entity>((Entity) schoolclass,
					secondMemento));
		}

		List<ActivityType> activityTypes = DataAccess.getInstance()
				.getAllActivityTypes(room);
		for (ActivityType a : activityTypes) {
			IMemento thirdMemento = a.createMemento();
			a.remove(room);
			macro.addLast(new UpdateCommand<Entity>(a, thirdMemento));
		}

		if (!deleteLocation) {
			macro.addLast(new DeleteCommand<Entity>(room));
		}

		return macro;
	}

	/**
	 * Gibt eine Liste von {@linkplain ICommand}-Objekten zurück, die die
	 * Referenzen vom übergebenen {@linkplain Room} zu seinem Standort auflösen. <br>
	 * <br>
	 * 
	 * <b>HINWEIS:</b> Wird nur beim Löschen eines einzelnen Raumes, nicht beim
	 * Löschen eines Standortes benötigt. Muss dann aber vor
	 * {@linkplain CommandCreator#relationRoom(Room)} ausgeführt werden.
	 * 
	 * @param room
	 *            - der zu löschende Raum
	 * @return Liste von {@linkplain ICommand}-Objekten, die die Referenzen von
	 *         dem übergebenen {@linkplain Room} zu seinem Standort auflösen
	 * @throws DatasetException
	 */
	private LinkedList<ICommand> relationRoomLocation(Room room)
			throws DatasetException {
		LinkedList<ICommand> macro = new LinkedList<ICommand>();

		Location location = DataAccess.getInstance().getOneLocation(room);
		if (location != null) {
			IMemento firstMemento = location.createMemento();
			location.remove(room);

			macro.addLast(new UpdateCommand<Entity>((Entity) location,
					firstMemento));
		}
		return macro;
	}

	/**
	 * Gibt eine Liste von {@linkplain ICommand}-Objekten zurück, die bei
	 * Ausführung in der gegebenen Reihenfolge zunächst alle Referenzen auf das
	 * übergebene {@linkplain ActivityType}-Objekt in anderen Objekten auflösen
	 * und anschließend das {@linkplain ActivityType}-Objekt selbst löschen
	 * 
	 * @param activityType
	 *            - das zu löschende {@linkplain ActivityType}-Objekt
	 * @return Liste von {@linkplain ICommand}-Objekten, die in der gegebenen
	 *         Reihenfolge in der Lage sind, das übergebene
	 *         {@linkplain ActivityType}-Objekt zu löschen
	 * @throws DatasetException
	 */
	private LinkedList<ICommand> relationActivityType(ActivityType activityType)
			throws DatasetException {
		LinkedList<ICommand> macro = new LinkedList<ICommand>();
		if (activityType instanceof LessonType) {
			// Referenzen bei Lesson-Objekten
			List<Lesson> lessons = DataAccess.getInstance().getAllLessons(
					(LessonType) activityType);
			for (Lesson l : lessons) {
				macro.addLast(new DeleteCommand<Entity>(l));
			}

			// Referenzen bei CompoundLesson-Objekten
			List<CompoundLesson> compoundLessons = DataAccess.getInstance()
					.getAllCompoundLessons((LessonType) activityType);
			for (CompoundLesson c : compoundLessons) {
				IMemento memento = c.createMemento();
				int size = c.remove((LessonType) activityType);
				if (size == 0) {
					c.setMemento(memento);
					macro.addLast(new DeleteCommand<Entity>(c));
				} else {
					macro.addLast(new UpdateCommand<Entity>(c, memento));
				}
			}

			// Referenzen bei Schulklassen
			List<Schoolclass> schoolclasses = DataAccess.getInstance()
					.getAllSchoolclasses();
			for (Schoolclass s : schoolclasses) {
				IMemento memento = s.createMemento();
				s.remove((LessonType) activityType);
				macro.addLast(new UpdateCommand<Entity>(s, memento));
			}

			// Referenzen bei Jahrgängen
			List<AcademicYear> years = DataAccess.getInstance()
					.getAllAcademicYears();
			for (AcademicYear a : years) {
				IMemento memento = a.createMemento();
				a.remove((LessonType) activityType);
				macro.addLast(new UpdateCommand<Entity>(a, memento));
			}
		}

		// TODO: später ggf. ProjectType und MeetingType

		// Referenzen bei Mitarbeitern
		List<Employee> employees = DataAccess.getInstance().getAllEmployees(
				activityType);

		for (Employee e : employees) {
			IMemento memento = e.createMemento();
			e.remove(activityType);
			macro.addLast(new UpdateCommand<Entity>(e, memento));
		}

		macro.addLast(new DeleteCommand<Entity>(activityType));

		return macro;
	}
}
