package org.woym.logic.command;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.woym.logic.FailureStatus;
import org.woym.logic.SuccessStatus;
import org.woym.logic.spec.IStatus;
import org.woym.objects.Entity;

/**
 * @author JurSch
 *
 */
@Test(groups = "unit")
public class TestMacroCommand extends PowerMockTestCase {

	@Mock
	UpdateCommand<Entity> command1;

	@Mock
	UpdateCommand<Entity> command2;

	@Mock
	SuccessStatus successStatus;

	@Mock
	FailureStatus failureStatus;

	@InjectMocks
	MacroCommand macroCommand;

	@BeforeMethod
	public void setUp() throws Exception {
		Mockito.when(command1.execute()).thenReturn(successStatus);
		Mockito.when(command1.undo()).thenReturn(successStatus);
		Mockito.when(command1.redo()).thenReturn(successStatus);
		Mockito.when(command2.execute()).thenReturn(failureStatus);
		Mockito.when(command2.undo()).thenReturn(failureStatus);
		Mockito.when(command2.redo()).thenReturn(failureStatus);
		macroCommand = new MacroCommand();
	}

	@Test
	public void testExecuteSuccess() {
		for (int i = 0; i <= 5; i++) {
			macroCommand.add(command1);
		}

		IStatus status = macroCommand.execute();

		Mockito.verify(command1, Mockito.times(6)).execute();

		AssertJUnit.assertTrue(status instanceof SuccessStatus);

	}

	@Test
	public void testExecuteFailed() {
		for (int i = 0; i <= 2; i++) {
			macroCommand.add(command1);
		}

		macroCommand.add(command2);

		for (int i = 0; i <= 2; i++) {
			macroCommand.add(command1);
		}

		IStatus status = macroCommand.execute();

		Mockito.verify(command1, Mockito.times(3)).execute();
		Mockito.verify(command2).execute();
		Mockito.verify(command1, Mockito.times(3)).undo();

		AssertJUnit.assertTrue(status instanceof FailureStatus);

	}

	@Test
	public void testUndoSuccess() {
		for (int i = 0; i <= 5; i++) {
			macroCommand.add(command1);
		}

		IStatus status = macroCommand.undo();

		Mockito.verify(command1, Mockito.times(6)).undo();

		AssertJUnit.assertTrue(status instanceof SuccessStatus);

	}

	@Test
	public void testUndoFailed() {
		for (int i = 0; i <= 2; i++) {
			macroCommand.add(command1);
		}

		macroCommand.add(command2);

		for (int i = 0; i <= 2; i++) {
			macroCommand.add(command1);
		}

		IStatus status = macroCommand.undo();

		Mockito.verify(command1, Mockito.times(3)).undo();
		Mockito.verify(command2).undo();
		Mockito.verify(command1, Mockito.times(3)).redo();

		AssertJUnit.assertTrue(status instanceof FailureStatus);

	}

	@Test
	public void testRedoSuccess() {
		for (int i = 0; i <= 5; i++) {
			macroCommand.add(command1);
		}

		IStatus status = macroCommand.redo();

		Mockito.verify(command1, Mockito.times(6)).redo();

		AssertJUnit.assertTrue(status instanceof SuccessStatus);

	}

	@Test
	public void testRedoFailed() {
		for (int i = 0; i <= 2; i++) {
			macroCommand.add(command1);
		}

		macroCommand.add(command2);

		for (int i = 0; i <= 2; i++) {
			macroCommand.add(command1);
		}

		IStatus status = macroCommand.redo();

		Mockito.verify(command1, Mockito.times(3)).redo();
		Mockito.verify(command2).redo();
		Mockito.verify(command1, Mockito.times(3)).undo();

		AssertJUnit.assertTrue(status instanceof FailureStatus);

	}

}
