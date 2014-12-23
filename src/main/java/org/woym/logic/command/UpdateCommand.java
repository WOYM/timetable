package org.woym.logic.command;

import javax.faces.application.FacesMessage;

import org.woym.logic.FailureStatus;
import org.woym.logic.SuccessStatus;
import org.woym.messages.SpecificStatusMessage;
import org.woym.objects.Entity;
import org.woym.spec.logic.ICommand;
import org.woym.spec.logic.IStatus;
import org.woym.spec.objects.IMemento;

/**
 * 
 * @author JurSch
 *
 */
public class UpdateCommand<E extends Entity> implements ICommand {

	private final E entity;
	
	private final IMemento memento;

	public UpdateCommand(E entity) {
		if (entity == null) {
			throw new IllegalArgumentException("Entity was null");
		}
		this.entity = entity;
		memento = entity.createMemento();
	}

	@Override
	public IStatus execute() {
		IStatus status;

		try {
			entity.update();
			status = new SuccessStatus();
		} catch (Exception e) {
			status = new FailureStatus(
					SpecificStatusMessage.UPDATE_OBJECT_DATASET_EXCEPTION,
					entity.getClass(), FacesMessage.SEVERITY_ERROR);
		}
		return status;
	}

	@Override
	public IStatus undo() {
		entity.setMemento(memento);
		//TODO:
		return null;
	}

	@Override
	public IStatus redo() {
		// TODO Auto-generated method stub
		return null;
	}

}
