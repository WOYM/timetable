package org.woym.logic.command;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.woym.common.exceptions.DatasetException;
import org.woym.common.objects.Activity;
import org.woym.common.objects.Activity.Memento;
import org.woym.logic.FailureStatus;
import org.woym.logic.SuccessStatus;

/**
 * @author JurSch
 *
 */
@Test(groups = "unit")
public class TestUpdateCommand extends PowerMockTestCase {

	@Mock
	Activity entity;

	@Mock
	Memento memento;

	@Mock
	Memento oldMemento;

	@InjectMocks
	UpdateCommand<Activity> update;

	@BeforeMethod
	public void setUp() {
		update = new UpdateCommand<Activity>(entity, memento);
	}

	@Test
	public void testNullConstruktor() {
		try {
			new UpdateCommand<Activity>(null, memento);
		} catch (IllegalArgumentException e) {
			AssertJUnit.assertEquals("Entity was null", e.getMessage());
		}
		try {
			new UpdateCommand<Activity>(entity, null);
		} catch (IllegalArgumentException e) {
			AssertJUnit.assertEquals("Memento was null", e.getMessage());
		}
	}

	@Test
	public void testValidExecute() throws Exception {
		AssertJUnit.assertTrue(update.execute() instanceof SuccessStatus);

		Mockito.verify(entity).update();

	}

	@Test
	public void testNonValidExecute() throws Exception {
		Mockito.doThrow(DatasetException.class).when(entity).update();

		AssertJUnit.assertTrue(update.execute() instanceof FailureStatus);

		Mockito.verify(entity).update();

	}

	@Test
	public void testValidUndo() throws Exception {
		Mockito.when(entity.createMemento()).thenReturn(oldMemento);

		AssertJUnit.assertTrue(update.undo() instanceof SuccessStatus);

		Mockito.verify(entity).createMemento();
		Mockito.verify(entity).setMemento(memento);
		Mockito.verify(entity).update();

	}

	@Test
	public void testNonValidUndo() throws Exception {
		Mockito.when(entity.createMemento()).thenReturn(oldMemento);
		Mockito.doThrow(DatasetException.class).when(entity).update();

		AssertJUnit.assertTrue(update.undo() instanceof FailureStatus);

		Mockito.verify(entity).createMemento();
		Mockito.verify(entity).setMemento(memento);
		Mockito.verify(entity).update();
		Mockito.verify(entity).setMemento(oldMemento);

	}

	@Test
	public void testValidRedo() throws Exception {
		Mockito.when(entity.createMemento()).thenReturn(oldMemento);

		AssertJUnit.assertTrue(update.redo() instanceof SuccessStatus);

		Mockito.verify(entity).createMemento();
		Mockito.verify(entity).setMemento(memento);
		Mockito.verify(entity).update();

	}

	@Test
	public void testNonValidRedo() throws Exception {
		Mockito.when(entity.createMemento()).thenReturn(oldMemento);
		Mockito.doThrow(DatasetException.class).when(entity).update();

		AssertJUnit.assertTrue(update.redo() instanceof FailureStatus);

		Mockito.verify(entity).createMemento();
		Mockito.verify(entity).setMemento(memento);
		Mockito.verify(entity).update();
		Mockito.verify(entity).setMemento(oldMemento);

	}

}
