package org.woym.logic.command;

import javax.faces.application.FacesMessage;

import org.woym.logic.FailureStatus;
import org.woym.logic.SuccessStatus;
import org.woym.logic.spec.ICommand;
import org.woym.logic.spec.IStatus;
import org.woym.messages.SpecificErrorMessage;
import org.woym.messages.SpecificSuccessMessage;
import org.woym.objects.Entity;
import org.woym.objects.spec.IMemento;

/**
 * 
 * @author JurSch
 *
 */
public class UpdateCommand<E extends Entity> implements ICommand {

	private final E entity;

	private IMemento memento;

	public UpdateCommand(E entity, IMemento memento) {
		if (entity == null) {
			throw new IllegalArgumentException("Entity was null");
		} else if (memento == null) {
			throw new IllegalArgumentException("Memento was null");
		}
		this.entity = entity;
		this.memento = memento;
	}

	@Override
	public IStatus execute() {
		IStatus status;

		try {
			entity.update();
			status = new SuccessStatus(
					SpecificSuccessMessage.UPDATE_OBJECT_SUCCESS, entity);
		} catch (Exception e) {
			status = new FailureStatus(
					SpecificErrorMessage.UPDATE_OBJECT_DATASET_EXCEPTION,
					entity.getClass(), FacesMessage.SEVERITY_ERROR);
		}
		return status;
	}

	@Override
	public IStatus undo() {
		IStatus status;
		IMemento placeholder = entity.createMemento();
		entity.setMemento(memento);

		try {
			entity.update();
			status = new SuccessStatus(
					SpecificSuccessMessage.UPDATE_OBJECT_SUCCESS, entity);
			memento = placeholder;
		} catch (Exception e) {
			status = new FailureStatus(
					SpecificErrorMessage.UPDATE_OBJECT_DATASET_EXCEPTION,
					entity.getClass(), FacesMessage.SEVERITY_ERROR);
			entity.setMemento(placeholder);
		}
		return status;
	}

	@Override
	public IStatus redo() {
		IStatus status;
		IMemento placeholder = entity.createMemento();
		entity.setMemento(memento);

		try {
			entity.update();
			status = new SuccessStatus(
					SpecificSuccessMessage.UPDATE_OBJECT_SUCCESS, entity);
			memento = placeholder;
		} catch (Exception e) {
			status = new FailureStatus(
					SpecificErrorMessage.UPDATE_OBJECT_DATASET_EXCEPTION,
					entity.getClass(), FacesMessage.SEVERITY_ERROR);
			entity.setMemento(placeholder);
		}
		return status;
	}

}
