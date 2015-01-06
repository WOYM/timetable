package org.woym.logic.command;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;
import org.woym.exceptions.DatasetException;
import org.woym.logic.FailureStatus;
import org.woym.logic.SuccessStatus;
import org.woym.objects.Activity;

/**
 * @author JurSch
 *
 */
@Test(groups = "unit")
public class TestDeleteCommand extends PowerMockTestCase {

	@Mock
	Activity entity;

	@InjectMocks
	DeleteCommand<Activity> deleteCommand;

	@Test
	public void testNullConstruct() {
		try {
			new DeleteCommand<Activity>(null);
		} catch (IllegalArgumentException e) {
			AssertJUnit.assertEquals("Entity was null", e.getMessage());
		}
	}

	@Test
	public void testValidExecute() throws Exception {
		AssertJUnit
				.assertTrue(deleteCommand.execute() instanceof SuccessStatus);

		Mockito.verify(entity).delete();
	}

	@Test
	public void testNonValidExecute() throws Exception {
		Mockito.doThrow(DatasetException.class).when(entity).delete();

		AssertJUnit
				.assertTrue(deleteCommand.execute() instanceof FailureStatus);
		Mockito.verify(entity).delete();
	}

	@Test
	public void testValidUndo() throws Exception {
		AssertJUnit.assertTrue(deleteCommand.undo() instanceof SuccessStatus);

		Mockito.verify(entity).persist();
	}

	@Test
	public void testNonValidUndo() throws Exception {
		Mockito.doThrow(DatasetException.class).when(entity).persist();

		AssertJUnit.assertTrue(deleteCommand.undo() instanceof FailureStatus);
		Mockito.verify(entity).persist();
	}

	@Test
	public void testValidRedo() throws Exception {
		AssertJUnit.assertTrue(deleteCommand.redo() instanceof SuccessStatus);

		Mockito.verify(entity).delete();
	}

	@Test
	public void testNonValidRedo() throws Exception {
		Mockito.doThrow(DatasetException.class).when(entity).delete();

		AssertJUnit.assertTrue(deleteCommand.redo() instanceof FailureStatus);
		Mockito.verify(entity).delete();
	}

}
