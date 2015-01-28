package org.woym.logic.util;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.woym.logic.spec.ICommand;

@Test(groups = "unit")
public class LimitedQueueBlackboxTest extends PowerMockTestCase {

	@Mock
	ICommand command1;

	@Mock
	ICommand command2;

	@InjectMocks
	LimitedQueue<ICommand> queue;

	@BeforeMethod
	public void setUpBeforeClass() {
		queue = new LimitedQueue<>();
	}

	@Test
	public void testAddElemToEmptyQueue() {
		AssertJUnit.assertEquals((Integer) 0, queue.size());
		queue.add(command1);
		AssertJUnit.assertEquals((Integer) 1, queue.size());
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
		AssertJUnit.assertEquals((Integer) 10, queue.size());
		queue.add(command1);
		AssertJUnit.assertEquals((Integer) 10, queue.size());
	}

	@Test
	public void testGetFirstElemOfQueue() {
		queue.add(command1);
		queue.add(command2);
		queue.add(command2);
		queue.add(command2);
		AssertJUnit.assertEquals(command1, queue.getFirst());
		AssertJUnit.assertEquals((Integer) 3, queue.size());
	}

	@Test
	public void testGetLastElemOfQueue() {
		queue.add(command1);
		queue.add(command1);
		queue.add(command1);
		queue.add(command2);
		AssertJUnit.assertEquals(command2, queue.getLast());
		AssertJUnit.assertEquals((Integer) 3, queue.size());
	}

	@Test
	public void testGetElemOfEmptyQueue() {
		AssertJUnit.assertEquals((Integer) 0, queue.size());
		AssertJUnit.assertNull(queue.getFirst());
		AssertJUnit.assertEquals((Integer) 0, queue.size());
		AssertJUnit.assertNull(queue.getLast());
		AssertJUnit.assertEquals((Integer) 0, queue.size());
	}

	@Test
	public void testClearQueue() {
		queue.add(command1);
		queue.add(command1);
		queue.add(command1);
		queue.add(command1);
		queue.add(command1);
		AssertJUnit.assertEquals((Integer) 5, queue.size());
		queue.clear();
		AssertJUnit.assertEquals((Integer) 0, queue.size());
	}

}
