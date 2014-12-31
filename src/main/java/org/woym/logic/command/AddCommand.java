/**
 * 
 */
package org.woym.logic.command;

import javax.faces.application.FacesMessage;

import org.woym.exceptions.DatasetException;
import org.woym.logic.FailureStatus;
import org.woym.logic.SuccessStatus;
import org.woym.messages.SpecificErrorMessage;
import org.woym.messages.SpecificSuccessMessage;
import org.woym.objects.Entity;
import org.woym.spec.logic.ICommand;
import org.woym.spec.logic.IStatus;

/**
 * @author JurSch
 *
 */
public class AddCommand<E extends Entity> implements ICommand {

	private E entity;

	public AddCommand(E entity) {
		if (entity == null) {
			throw new IllegalArgumentException("Entity was null");
		}
		this.entity = entity;
	}

	@Override
	public IStatus execute() {
		IStatus status;

		try {
			entity.persist();
			status = new SuccessStatus(SpecificSuccessMessage.ADD_OBJECT_SUCCESS,
					entity);
		} catch (DatasetException e) {
			status = new FailureStatus(
					SpecificErrorMessage.ADD_OBJECT_DATASET_EXCEPTION,
					entity.getClass(), FacesMessage.SEVERITY_ERROR);
		}
		return status;
	}

	@Override
	public IStatus undo() {
		IStatus status;

		try {
			entity.delete();
			status = new SuccessStatus(SpecificSuccessMessage.DELETE_OBJECT_SUCCESS,
					entity);
		} catch (DatasetException e) {
			status = new FailureStatus(
					SpecificErrorMessage.DELETE_OBJECT_DATASET_EXCEPTION,
					entity.getClass(), FacesMessage.SEVERITY_ERROR);
		}
		
		return status;
	}

	@Override
	public IStatus redo() {
		return execute();
	}

}
