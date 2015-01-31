package org.woym.logic.command;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.faces.application.FacesMessage;

import org.woym.common.messages.GenericErrorMessage;
import org.woym.logic.FailureStatus;
import org.woym.logic.spec.ICommand;
import org.woym.logic.spec.IStatus;

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

	public List<ICommand> getCommands() {
		return new LinkedList<ICommand>(commands);
	}
	
	public void addAll(MacroCommand macro){
		commands.addAll(macro.getCommands());
	}

	@Override
	public IStatus execute() {
		LinkedList<ICommand> commandsToRevert = new LinkedList<>();
		IStatus status = new FailureStatus(
				GenericErrorMessage.DATABASE_COMMUNICATION_ERROR,
				FacesMessage.SEVERITY_INFO);
		for (ICommand c : commands) {
			status = c.execute();

			if (status instanceof FailureStatus) {
				for (ICommand ctr : commandsToRevert) {
					ctr.undo();
				}
				return status;
			}
			commandsToRevert.addFirst(c);
		}
		return status;
	}

	@Override
	public IStatus undo() {
		LinkedList<ICommand> commandsToRevert = new LinkedList<>();
		IStatus status = new FailureStatus(
				GenericErrorMessage.DATABASE_COMMUNICATION_ERROR,
				FacesMessage.SEVERITY_ERROR);

		for (Iterator<ICommand> it = commands.descendingIterator(); it
				.hasNext();) {

			ICommand command = it.next();
			status = command.undo();
			if (status instanceof FailureStatus) {
				for (ICommand ctr : commandsToRevert) {
					ctr.redo();
				}
				return status;
			}
			commandsToRevert.addFirst(command);
		}
		return status;
	}

	@Override
	public IStatus redo() {
		LinkedList<ICommand> commandsToRevert = new LinkedList<>();
		IStatus status = new FailureStatus(
				GenericErrorMessage.DATABASE_COMMUNICATION_ERROR,
				FacesMessage.SEVERITY_INFO);
		for (ICommand c : commands) {
			status = c.redo();

			if (status instanceof FailureStatus) {
				for (ICommand ctr : commandsToRevert) {
					ctr.undo();
				}
				return status;
			}
			commandsToRevert.addFirst(c);
		}
		return status;
	}

}
