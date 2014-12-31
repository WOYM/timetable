/**
 * 
 */
package org.woym.logic.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.woym.logic.CommandHandler;
import org.woym.logic.FailureStatus;
import org.woym.logic.SuccessStatus;
import org.woym.logic.spec.ICommand;

/**
 * @author JurSch
 *
 */
@RunWith(PowerMockRunner.class)
public class TestCommandhandler {

	@Mock
	ICommand command1;

	@Mock
	ICommand command2;

	@Mock
	SuccessStatus successStatus;

	@Mock
	FailureStatus failureStatus;

	@InjectMocks
	CommandHandler handler = CommandHandler.getInstance();

	@Before
	public void setUp() {
		Mockito.when(command1.execute()).thenReturn(successStatus);
		Mockito.when(command1.undo()).thenReturn(successStatus);
		Mockito.when(command1.redo()).thenReturn(successStatus);
		Mockito.when(command2.execute()).thenReturn(failureStatus);

	}

	@Test
	public void testAll(){
		testEmptyUndoRedo();
		testExecuteNull();
		testExecuteValidCommand();
		testExecuteNonValidCommand();
		testValidUndoRedo();
		testNonValidUndoRedo();
	}
	
	public void testEmptyUndoRedo() {
		assertTrue(handler.undo() instanceof FailureStatus);
		assertTrue(handler.redo() instanceof FailureStatus);
	}

	public void testExecuteNull() {
		try {
			handler.execute(null);
		} catch (IllegalArgumentException e) {
			assertEquals("The Command was null", e.getMessage());
		}
	}

	public void testExecuteValidCommand() {
		assertTrue(handler.execute(command1) instanceof SuccessStatus);
		assertEquals((Integer)1, handler.getUndoSize());
		assertEquals((Integer)0, handler.getRedoSize());
		
		Mockito.verify(command1).execute();
	}
	
	public void testExecuteNonValidCommand() {
		assertTrue(handler.execute(command2) instanceof FailureStatus);
		//Vom vorhergehenden Test enthalten wegen Singleton 
		assertEquals((Integer)1, handler.getUndoSize());
		assertEquals((Integer)0, handler.getRedoSize());
		
		Mockito.verify(command2).execute();
	}
	
	public void testValidUndoRedo() {
		Mockito.when(command1.undo()).thenReturn(successStatus);
		Mockito.when(command1.redo()).thenReturn(successStatus);
		
		assertEquals((Integer)1, handler.getUndoSize());
		assertEquals((Integer)0, handler.getRedoSize());
		
		assertTrue(handler.undo() instanceof SuccessStatus);
		assertEquals((Integer)0, handler.getUndoSize());
		assertEquals((Integer)1, handler.getRedoSize());
		
		assertTrue(handler.redo() instanceof SuccessStatus);
		assertEquals((Integer)1, handler.getUndoSize());
		assertEquals((Integer)0, handler.getRedoSize());
	}
	
	public void testNonValidUndoRedo() {
		Mockito.when(command2.execute()).thenReturn(successStatus);
		Mockito.when(command2.undo()).thenReturn(failureStatus);
		Mockito.when(command2.redo()).thenReturn(failureStatus);
		
		handler.execute(command2);
		
		assertTrue(handler.undo() instanceof FailureStatus);
		assertEquals((Integer)0, handler.getUndoSize());
		assertEquals((Integer)0, handler.getRedoSize());
		
		Mockito.when(command2.undo()).thenReturn(successStatus);
		
		handler.execute(command2);
		handler.undo();
		
		assertTrue(handler.redo() instanceof FailureStatus);
		assertEquals((Integer)0, handler.getUndoSize());
		assertEquals((Integer)0, handler.getRedoSize());
	}

}
