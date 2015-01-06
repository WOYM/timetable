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
public class TestAddCommand extends PowerMockTestCase {

	@Mock
	Activity entity;

	@InjectMocks
	AddCommand<Activity> addCommand;

	@Test
	public void testNullConstruct() {
		try {
			new AddCommand<Activity>(null);
		} catch (IllegalArgumentException e) {
			AssertJUnit.assertEquals("Entity was null", e.getMessage());
		}
	}

	@Test
	public void testValidExecute() throws Exception {
		AssertJUnit.assertTrue(addCommand.execute() instanceof SuccessStatus);

		Mockito.verify(entity).persist();
	}

	@Test
	public void testNonValidExecute() throws Exception {
		Mockito.doThrow(DatasetException.class).when(entity).persist();

		AssertJUnit.assertTrue(addCommand.execute() instanceof FailureStatus);
		Mockito.verify(entity).persist();
	}

	@Test
	public void testValidUndo() throws Exception {
		AssertJUnit.assertTrue(addCommand.undo() instanceof SuccessStatus);

		Mockito.verify(entity).delete();
	}

	@Test
	public void testNonValidUndo() throws Exception {
		Mockito.doThrow(DatasetException.class).when(entity).delete();

		AssertJUnit.assertTrue(addCommand.undo() instanceof FailureStatus);
		Mockito.verify(entity).delete();
	}

	@Test
	public void testValidRedo() throws Exception {
		AssertJUnit.assertTrue(addCommand.redo() instanceof SuccessStatus);

		Mockito.verify(entity).persist();
	}

	@Test
	public void testNonValidRedo() throws Exception {
		Mockito.doThrow(DatasetException.class).when(entity).persist();

		AssertJUnit.assertTrue(addCommand.redo() instanceof FailureStatus);
		Mockito.verify(entity).persist();
	}

}
