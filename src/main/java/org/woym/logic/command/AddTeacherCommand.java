/**
 * 
 */
package org.woym.logic.command;

import org.woym.logic.FailureStatus;
import org.woym.logic.SuccessStatus;
import org.woym.objects.Teacher;
import org.woym.spec.logic.ICommand;
import org.woym.spec.logic.IStatus;

/**
 * @author JurSch
 *
 */
public class AddTeacherCommand implements ICommand {

	private Teacher teacher;

	public AddTeacherCommand(Teacher teacher) {
		if (teacher == null) {
			throw new IllegalArgumentException("Teacher was null");
		}
		this.teacher = teacher;
	}

	@Override
	public IStatus execute() {
		IStatus status;

		try {
			teacher.persist();
			status = new SuccessStatus();
		} catch (Exception e) {
			status = new FailureStatus();
			((FailureStatus) status).addException(e);
		}

		return status;
	}

	@Override
	public IStatus undo() {
		throw new UnsupportedOperationException();
	}

	@Override
	public IStatus redo() {
		throw new UnsupportedOperationException();
	}

}
