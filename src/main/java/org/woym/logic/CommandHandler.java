/**
 * 
 */
package org.woym.logic;

import org.woym.logic.util.LimitedQueue;
import org.woym.spec.logic.ICommand;
import org.woym.spec.logic.ICommandHandler;
import org.woym.spec.logic.ILimitedQueue;
import org.woym.spec.logic.IStatus;

/**
 * Klasse zur umsetzung des CommandHandlers nach dem Singleton Muster.
 * 
 * @author JurSch
 *
 */
public class CommandHandler implements ICommandHandler {

	static final private CommandHandler COMMAND_HANDLER = new CommandHandler();

	final private ILimitedQueue<ICommand> undo = new LimitedQueue<ICommand>();

	final private ILimitedQueue<ICommand> redo = new LimitedQueue<ICommand>();

	/**
	 * Privater Konstruktor
	 */
	private CommandHandler() {

	}

	/**
	 * Ermöglich den zugrif von außen durch eine Getter auf die Instance.
	 * 
	 * @return Instanz des Singleton-CommandHandler
	 */
	static public CommandHandler getInstance() {
		return COMMAND_HANDLER;
	}

	@Override
	public IStatus execute(ICommand command) {
		if (command == null) {
			throw new IllegalArgumentException("The Command was null");
		}

		IStatus status = command.execute();

		if (status instanceof SuccessStatus) {
			undo.add(command);
			redo.clear();
		}

		return status;
	}

	@Override
	public IStatus undo() {
		ICommand command = undo.getLast();
		IStatus status = command.execute();

		if (status instanceof SuccessStatus) {
			redo.add(command);
		} else {
			redo.clear();
			undo.clear();
		}

		return status;
	}

	@Override
	public IStatus redo() {
		ICommand command = redo.getLast();
		IStatus status = command.execute();

		if (status instanceof SuccessStatus) {
			undo.add(command);
		}  else {
			redo.clear();
			undo.clear();
		}

		return status;
	}

}
