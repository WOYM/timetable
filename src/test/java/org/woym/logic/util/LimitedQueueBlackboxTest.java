package org.woym.logic.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;
import org.woym.logic.util.LimitedQueue;
import org.woym.spec.logic.ICommand;

@RunWith(PowerMockRunner.class)
public class LimitedQueueBlackboxTest {

	@Mock
	ICommand command1;

	@Mock
	ICommand command2;

	@InjectMocks
	LimitedQueue<ICommand> queue;

	@Before
	public void setUpBeforeClass() {
		queue = new LimitedQueue<>();
	}

	@Test
	public void testAddElemToEmptyQueue() {
		assertEquals((Integer) 0, queue.size());
		queue.add(command1);
		assertEquals((Integer) 1, queue.size());
	}

	@Test
	public void testAddElemToFullQueue() {
		queue.add(command1);
		queue.add(command2);
		queue.add(command1);
		queue.add(command2);
		queue.add(command1);
		queue.add(command2);
		queue.add(command1);
		queue.add(command2);
		queue.add(command1);
		queue.add(command2);
		assertEquals((Integer) 10, queue.size());
		queue.add(command1);
		assertEquals((Integer) 10, queue.size());
	}

	@Test
	public void testGetFirstElemOfQueue() {
		queue.add(command1);
		queue.add(command2);
		queue.add(command2);
		queue.add(command2);
		assertEquals(command1, queue.getFirst());
		assertEquals((Integer) 3, queue.size());
	}

	@Test
	public void testGetLastElemOfQueue() {
		queue.add(command1);
		queue.add(command1);
		queue.add(command1);
		queue.add(command2);
		assertEquals(command2, queue.getLast());
		assertEquals((Integer) 3, queue.size());
	}

	@Test
	public void testGetElemOfEmptyQueue() {
		assertEquals((Integer) 0, queue.size());
		assertNull(queue.getFirst());
		assertEquals((Integer) 0, queue.size());
		assertNull(queue.getLast());
		assertEquals((Integer) 0, queue.size());
	}

	@Test
	public void testClearQueue() {
		queue.add(command1);
		queue.add(command1);
		queue.add(command1);
		queue.add(command1);
		queue.add(command1);
		assertEquals((Integer) 5, queue.size());
		queue.clear();
		assertEquals((Integer) 0, queue.size());
	}

}
