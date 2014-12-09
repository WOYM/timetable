package org.woym.logic;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;
import org.woym.spec.logic.ICommand;

@RunWith(PowerMockRunner.class)
public class FiFoQueueTest {

	@Mock
	ICommand command;

	@InjectMocks
	FiFoQueue<ICommand> queue;

	@Before
	public void setUpBeforeClass() {
		queue = new FiFoQueue<>();
	}

	@Test
	public void testAddElemToEmptyQueue() {
		assertEquals((Integer) 0, queue.size());
		queue.add(command);
		assertEquals((Integer) 1, queue.size());
	}

	@Test
	public void testAddElemToFullQueue() {
		queue.add(command);
		queue.add(command);
		queue.add(command);
		queue.add(command);
		queue.add(command);
		assertEquals((Integer) 5, queue.size());
		queue.add(command);
		assertEquals((Integer) 5, queue.size());
	}

	@Test
	public void testGetElemOfQueue() {
		queue.add(command);
		assertEquals(command, queue.get());
		assertEquals((Integer) 0, queue.size());
	}

	@Test
	public void testGetElemOfEmptyQueue() {
		assertEquals((Integer) 0, queue.size());
		assertNull(queue.get());
		assertEquals((Integer) 0, queue.size());
	}

}
