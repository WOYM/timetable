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
public class TestAddCommand {

	@Mock
	Activity entity;

	@InjectMocks
	AddCommand<Activity> addCommand;

	@Test
	public void testNullConstruct() {
		try {
			new AddCommand<Activity>(null);
		} catch (IllegalArgumentException e) {
			assertEquals("Entity was null", e.getMessage());
		}
	}

	@Test
	public void testValidExecute() throws Exception {
		assertTrue(addCommand.execute() instanceof SuccessStatus);

		Mockito.verify(entity).persist();
	}

	@Test
	public void testNonValidExecute() throws Exception {
		Mockito.doThrow(DatasetException.class).when(entity).persist();

		assertTrue(addCommand.execute() instanceof FailureStatus);
		Mockito.verify(entity).persist();
	}

	@Test
	public void testValidUndo() throws Exception {
		assertTrue(addCommand.undo() instanceof SuccessStatus);

		Mockito.verify(entity).delete();
	}

	@Test
	public void testNonValidUndo() throws Exception {
		Mockito.doThrow(DatasetException.class).when(entity).delete();

		assertTrue(addCommand.undo() instanceof FailureStatus);
		Mockito.verify(entity).delete();
	}

	@Test
	public void testValidRedo() throws Exception {
		assertTrue(addCommand.redo() instanceof SuccessStatus);

		Mockito.verify(entity).persist();
	}

	@Test
	public void testNonValidRedo() throws Exception {
		Mockito.doThrow(DatasetException.class).when(entity).persist();

		assertTrue(addCommand.redo() instanceof FailureStatus);
		Mockito.verify(entity).persist();
	}

}
