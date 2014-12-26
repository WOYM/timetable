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
import org.woym.objects.Activity.Memento;

/**
 * @author JurSch
 *
 */
@RunWith(PowerMockRunner.class)
public class TestUpdateCommand {

	@Mock
	Activity entity;

	@Mock
	Memento memento;
	
	@Mock
	Memento oldMemento;

	@InjectMocks
	UpdateCommand<Activity> update;

	@Test
	public void testNullKonstruktor() {
		try {
			new UpdateCommand<Activity>(null, memento);
		} catch (IllegalArgumentException e) {
			assertEquals("Entity was null", e.getMessage());
		}
		try {
			new UpdateCommand<Activity>(entity, null);
		} catch (IllegalArgumentException e) {
			assertEquals("Memento was null", e.getMessage());
		}
	}


	@Test
	public void testValidExecute() throws Exception {
		assertTrue(update.execute() instanceof SuccessStatus);
		
		Mockito.verify(entity).update();

	}

	@Test
	public void testNonValidExecute() throws Exception {
		Mockito.doThrow(DatasetException.class).when(entity).update();

		assertTrue(update.execute() instanceof FailureStatus);
		
		Mockito.verify(entity).update();

	}
	
	@Test
	public void testValidUndo() throws Exception {
		update = new UpdateCommand<Activity>(entity, memento);
		
		Mockito.when(entity.createMemento()).thenReturn(oldMemento);
		
		assertTrue(update.undo() instanceof SuccessStatus);
		
		Mockito.verify(entity).createMemento();
		Mockito.verify(entity).setMemento(memento);
		Mockito.verify(entity).update();

	}

	@Test
	public void testNonValidUndo() throws Exception {
		update = new UpdateCommand<Activity>(entity, memento);
		
		Mockito.when(entity.createMemento()).thenReturn(oldMemento);
		Mockito.doThrow(DatasetException.class).when(entity).update();
		
		assertTrue(update.undo() instanceof FailureStatus);
		
		Mockito.verify(entity).createMemento();
		Mockito.verify(entity).setMemento(memento);
		Mockito.verify(entity).update();
		Mockito.verify(entity).setMemento(oldMemento);

	}
	
	@Test
	public void testValidRedo() throws Exception {
		update = new UpdateCommand<Activity>(entity, memento);
		
		Mockito.when(entity.createMemento()).thenReturn(oldMemento);
		
		assertTrue(update.redo() instanceof SuccessStatus);
		
		Mockito.verify(entity).createMemento();
		Mockito.verify(entity).setMemento(memento);
		Mockito.verify(entity).update();

	}

	@Test
	public void testNonValidRedo() throws Exception {
		update = new UpdateCommand<Activity>(entity, memento);
		
		Mockito.when(entity.createMemento()).thenReturn(oldMemento);
		Mockito.doThrow(DatasetException.class).when(entity).update();
		
		assertTrue(update.redo() instanceof FailureStatus);
		
		Mockito.verify(entity).createMemento();
		Mockito.verify(entity).setMemento(memento);
		Mockito.verify(entity).update();
		Mockito.verify(entity).setMemento(oldMemento);

	}

}
