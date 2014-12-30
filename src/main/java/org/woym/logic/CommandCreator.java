/**
 * 
 */
package org.woym.logic;

import java.util.LinkedList;
import java.util.List;

import org.woym.exceptions.DatasetException;
import org.woym.logic.command.DeleteCommand;
import org.woym.logic.command.MacroCommand;
import org.woym.logic.command.UpdateCommand;
import org.woym.objects.AcademicYear;
import org.woym.objects.Activity;
import org.woym.objects.Classteam;
import org.woym.objects.Employee;
import org.woym.objects.Entity;
import org.woym.objects.Room;
import org.woym.objects.Schoolclass;
import org.woym.objects.Teacher;
import org.woym.persistence.DataAccess;
import org.woym.spec.logic.ICommand;
import org.woym.spec.objects.IActivityObject;
import org.woym.spec.objects.IMemento;

/**
 * @author JurSch
 *
 */
public class CommandCreator {

	private static final CommandCreator COMMAND_CREATOR = new CommandCreator();

	private CommandCreator() {

	}

	public static CommandCreator getInstance() {
		return COMMAND_CREATOR;
	}

	public MacroCommand createDeleteCommand(final Entity entity) {
		if (entity == null) {
			throw new IllegalArgumentException("Entit was null");
		}

		MacroCommand macro = new MacroCommand();

		if (entity instanceof Activity || entity instanceof AcademicYear) {
			macro.add(new DeleteCommand<Entity>(entity));
		} else {
			if (entity instanceof IActivityObject) {
				LinkedList<ICommand> commands = new LinkedList<ICommand>();
				LinkedList<ICommand> activityCommands = relationActivity((IActivityObject) entity);
				LinkedList<ICommand> activityObejctsCommands = relationActivityObjects((IActivityObject) entity);

				if (activityCommands.isEmpty()
						|| activityObejctsCommands.isEmpty()) {
					return new MacroCommand();
				} else {
					commands.addAll(activityCommands);
					commands.addAll(activityObejctsCommands);
					macro = listToMacro(commands);
				}

			} else {
				throw new UnsupportedOperationException("Not supportet Entity");
			}
		}
		return macro;
	}

	private MacroCommand listToMacro(List<ICommand> list) {
		MacroCommand macro = new MacroCommand();

		for (ICommand c : list) {
			macro.add(c);
		}
		return macro;
	}

	private LinkedList<ICommand> relationActivity(IActivityObject entity) {
		LinkedList<ICommand> macro = new LinkedList<ICommand>();

		try {
			List<Activity> list = DataAccess.getInstance().getAllActivities(
					(IActivityObject) entity);

			for (Activity a : list) {
				IMemento memento = a.createMemento();
				int size = a.remove(entity);
				if (size == 0) {
					a.setMemento(memento);
					macro.add(new DeleteCommand<Entity>(a));
				} else if (size > 0) {
					macro.add(new UpdateCommand<Entity>(a, memento));
				}
			}
		} catch (DatasetException e) {
			macro = new LinkedList<ICommand>();
		}
		return macro;
	}

	private LinkedList<ICommand> relationActivityObjects(IActivityObject entity) {
		LinkedList<ICommand> macro = new LinkedList<ICommand>();

		if (entity instanceof Room) {
			macro.addAll(relationRoom((Room) entity));
		} else if (entity instanceof Schoolclass) {
			macro.addAll(relationSchoolClass((Schoolclass) entity));
		} else if (entity instanceof Employee) {
			macro.addAll(relationEmployee((Employee) entity));
		} else {
			return new LinkedList<ICommand>();
		}

		return macro;
	}

	private LinkedList<ICommand> relationEmployee(Employee employee) {
		LinkedList<ICommand> macro = new LinkedList<ICommand>();
		try {
			List<Classteam> classteams = DataAccess.getInstance()
					.getAllClassteams(employee);

			for (Classteam c : classteams) {
				IMemento memento = c.createMemento();

				if (employee instanceof Teacher) {
					if (c.getTeacher().equals(employee)) {
						macro.addLast(new DeleteCommand<Entity>(c));
						continue;
					}
				}
				c.remove(employee);
				macro.addLast(new UpdateCommand<Entity>(c, memento));
			}
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

		} catch (DatasetException e) {
			macro = new LinkedList<ICommand>();
		}

		return macro;
	}

	private LinkedList<ICommand> relationSchoolClass(Schoolclass schoolclass) {
		LinkedList<ICommand> macro = new LinkedList<ICommand>();

		try {
			Classteam team = DataAccess.getInstance().getOneClassteam(
					schoolclass);
			IMemento memento = team.createMemento();
			team.remove(schoolclass);

			macro.addLast(new UpdateCommand<Entity>(team, memento));
			macro.addLast(new DeleteCommand<Entity>(schoolclass));
		} catch (DatasetException e) {
			macro = new LinkedList<ICommand>();
		}
		return macro;
	}

	private LinkedList<ICommand> relationRoom(Room room) {
		LinkedList<ICommand> macro = new LinkedList<ICommand>();

		org.woym.objects.Location location;
		try {
			location = DataAccess.getInstance().getOneLocation(room);
			IMemento firstMemento = location.createMemento();
			location.remove(room);

			macro.addLast(new UpdateCommand<Entity>((Entity) location,
					firstMemento));

			Schoolclass schoolclass = DataAccess.getInstance()
					.getOneSchoolclass(room);
			IMemento secondMemento = schoolclass.createMemento();
			schoolclass.setRoom(null);

			macro.addLast(new UpdateCommand<Entity>((Entity) schoolclass,
					secondMemento));

			macro.addLast(new DeleteCommand<Entity>(room));

		} catch (DatasetException e) {
			macro = new LinkedList<ICommand>();
		}
		return macro;
	}

}
