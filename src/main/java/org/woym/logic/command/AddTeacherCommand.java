/**
 * 
 */
package org.woym.logic.command;

import org.woym.logic.exception.NotSupportedException;
import org.woym.spec.logic.ICommand;
import org.woym.spec.logic.IStatus;

/**
 * @author JurSch
 *
 */
public class AddTeacherCommand implements ICommand {

	@Override
	public IStatus execute() {
		throw new NotSupportedException();
	}

	@Override
	public IStatus undo() {
		throw new NotSupportedException();
	}

	@Override
	public IStatus redo() {
		throw new NotSupportedException();
	}

}
