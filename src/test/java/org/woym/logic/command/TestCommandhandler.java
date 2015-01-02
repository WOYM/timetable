package org.woym.logic.command;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.woym.logic.CommandHandler;
import org.woym.logic.FailureStatus;
import org.woym.logic.SuccessStatus;
import org.woym.logic.spec.ICommand;

/**
 * @author JurSch
 *
 */
public class TestCommandhandler extends PowerMockTestCase {

	@Mock
	private ICommand command1;

	@Mock
	private ICommand command2;

	@Mock
	private SuccessStatus successStatus;

	@Mock
	private FailureStatus failureStatus;

	@InjectMocks
	private CommandHandler handler = CommandHandler.getInstance();

	@BeforeMethod
	public void setUp() {
		Mockito.when(command1.execute()).thenReturn(successStatus);
		Mockito.when(command1.undo()).thenReturn(successStatus);
		Mockito.when(command1.redo()).thenReturn(successStatus);
		Mockito.when(command2.execute()).thenReturn(failureStatus);

	}

	@Test(priority = 1)
	public void testEmptyUndoRedo() {
		assertTrue(handler.undo() instanceof FailureStatus);
		assertTrue(handler.redo() instanceof FailureStatus);
	}

	@Test(priority = 2)
	public void testExecuteNull() {
		try {
			handler.execute(null);
		} catch (IllegalArgumentException e) {
			assertEquals("The Command was null", e.getMessage());
		}
	}

	@Test(priority = 3)
	public void testExecuteValidCommand() {
		assertTrue(handler.execute(command1) instanceof SuccessStatus);
		assertEquals((Integer) 1, handler.getUndoSize());
		assertEquals((Integer) 0, handler.getRedoSize());

		Mockito.verify(command1).execute();
	}

	@Test(priority = 4)
	public void testExecuteNonValidCommand() {
		assertTrue(handler.execute(command2) instanceof FailureStatus);
		// Vom vorhergehenden Test enthalten wegen Singleton
		assertEquals((Integer) 1, handler.getUndoSize());
		assertEquals((Integer) 0, handler.getRedoSize());

		Mockito.verify(command2).execute();
	}

	@Test(priority = 5)
	public void testValidUndoRedo() {
		Mockito.when(command1.undo()).thenReturn(successStatus);
		Mockito.when(command1.redo()).thenReturn(successStatus);

		assertEquals((Integer) 1, handler.getUndoSize());
		assertEquals((Integer) 0, handler.getRedoSize());

		assertTrue(handler.undo() instanceof SuccessStatus);
		assertEquals((Integer) 0, handler.getUndoSize());
		assertEquals((Integer) 1, handler.getRedoSize());

		assertTrue(handler.redo() instanceof SuccessStatus);
		assertEquals((Integer) 1, handler.getUndoSize());
		assertEquals((Integer) 0, handler.getRedoSize());
	}

	@Test(priority = 6)
	public void testNonValidUndoRedo() {
		Mockito.when(command2.execute()).thenReturn(successStatus);
		Mockito.when(command2.undo()).thenReturn(failureStatus);
		Mockito.when(command2.redo()).thenReturn(failureStatus);

		handler.execute(command2);

		assertTrue(handler.undo() instanceof FailureStatus);
		assertEquals((Integer) 0, handler.getUndoSize());
		assertEquals((Integer) 0, handler.getRedoSize());

		Mockito.when(command2.undo()).thenReturn(successStatus);

		handler.execute(command2);
		handler.undo();

		assertTrue(handler.redo() instanceof FailureStatus);
		assertEquals((Integer) 0, handler.getUndoSize());
		assertEquals((Integer) 0, handler.getRedoSize());
	}

}
