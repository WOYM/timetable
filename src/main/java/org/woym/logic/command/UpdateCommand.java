package org.woym.logic.command;

import org.woym.logic.FailureStatus;
import org.woym.logic.SuccessStatus;
import org.woym.objects.Entity;
import org.woym.spec.logic.ICommand;
import org.woym.spec.logic.IStatus;

/**
 * 
 * @author JurSch
 *
 */
public class UpdateCommand<E extends Entity> implements ICommand {

	private E entity;

	public UpdateCommand(E entity) {
		if (entity == null) {
			throw new IllegalArgumentException("Entity was null");
		}
		this.entity = entity;
	}

	@Override
	public IStatus execute() {
		IStatus status;

		try {
			entity.update();
			status = new SuccessStatus();
		} catch (Exception e) {
			status = new FailureStatus();
			((FailureStatus) status).addException(e);
		}
		return status;
	}

	@Override
	public IStatus undo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IStatus redo() {
		// TODO Auto-generated method stub
		return null;
	}

}
