/**
 * 
 */
package org.woym.logic.command;

import java.util.LinkedList;

import org.woym.logic.FailureStatus;
import org.woym.spec.logic.ICommand;
import org.woym.spec.logic.IStatus;

/**
 * @author JurSch
 *
 */
public class MacroCommand implements ICommand {

	private final LinkedList<ICommand> commands = new LinkedList<>();

	/**
	 * Fügt ein übergebenes Command dem Macro hinzu.
	 * 
	 * @param command
	 *            hinzuzufügende Command
	 */
	public void add(ICommand command) {
		commands.addLast(command);
	}

	@Override
	public IStatus execute() {
		LinkedList<ICommand> commandsToRevert = new LinkedList<>();
		for (ICommand c : commands) {
			// TODO zusammenfügen von Status_Objecten
			IStatus status = c.execute();

			if (status instanceof FailureStatus) {
				for (ICommand ctr : commandsToRevert) {
					ctr.undo();
				}
				return status;
			}
			commandsToRevert.addFirst(c);
		}
		//TODO ausgabe eines sinnvollen Status-Objects
		return null;
	}

	@Override
	public IStatus undo() {
		LinkedList<ICommand> commandsToRevert = new LinkedList<>();
		for (ICommand c : commands) {
			// TODO zusammenfügen von Status_Objecten
			IStatus status = c.undo();

			if (status instanceof FailureStatus) {
				for (ICommand ctr : commandsToRevert) {
					ctr.redo();
				}
				return status;
			}
			commandsToRevert.addFirst(c);
		}
		//TODO ausgabe eines sinnvollen Status-Objects
		return null;
	}

	@Override
	public IStatus redo() {
		LinkedList<ICommand> commandsToRevert = new LinkedList<>();
		for (ICommand c : commands) {
			// TODO zusammenfügen von Status_Objecten
			IStatus status = c.redo();

			if (status instanceof FailureStatus) {
				for (ICommand ctr : commandsToRevert) {
					ctr.undo();
				}
				return status;
			}
			commandsToRevert.addFirst(c);
		}
		//TODO ausgabe eines sinnvollen Status-Objects
		return null;
	}

}
