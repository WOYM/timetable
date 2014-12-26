/**
 * 
 */
package org.woym.logic.command;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.woym.exceptions.DatasetException;
import org.woym.logic.FailureStatus;
import org.woym.logic.SuccessStatus;
import org.woym.objects.Activity;

/**
 * @author JurSch
 *
 */
@RunWith(PowerMockRunner.class)
public class TestDeleteCommand {
	
	@Mock
	Activity entity;

	@InjectMocks
	DeleteCommand<Activity> deleteCommand;

	@Test
	public void testNullConstruct() {
		try {
			new DeleteCommand<Activity>(null);
		} catch (IllegalArgumentException e) {
			assertEquals("Entity was null", e.getMessage());
		}
	}

	@Test
	public void testValidExecute() throws Exception {
		assertTrue(deleteCommand.execute() instanceof SuccessStatus);
		
		Mockito.verify(entity).delete();
	}
	
	@Test
	public void testNonValidExecute() throws Exception {
		Mockito.doThrow(DatasetException.class).when(entity).delete();

		assertTrue(deleteCommand.execute() instanceof FailureStatus);
		Mockito.verify(entity).delete();
	}
	
	@Test
	public void testValidUndo() throws Exception {
		assertTrue(deleteCommand.undo() instanceof SuccessStatus);
		
		Mockito.verify(entity).persist();
	}
	
	@Test
	public void testNonValidUndo() throws Exception {
		Mockito.doThrow(DatasetException.class).when(entity).persist();

		assertTrue(deleteCommand.undo() instanceof FailureStatus);
		Mockito.verify(entity).persist();
	}
	
	@Test
	public void testValidRedo() throws Exception {
		assertTrue(deleteCommand.redo() instanceof SuccessStatus);
		
		Mockito.verify(entity).delete();
	}
	
	@Test
	public void testNonValidRedo() throws Exception {
		Mockito.doThrow(DatasetException.class).when(entity).delete();

		assertTrue(deleteCommand.redo() instanceof FailureStatus);
		Mockito.verify(entity).delete();
	}

}
