package org.woym.objects;

import static org.junit.Assert.*;

import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

public class CompoundLessonBlackboxTest {

	@Test
	public void addLessonTypeNotExists() {
		CompoundLesson c = new CompoundLesson();
		LessonType l = PowerMockito.mock(LessonType.class);
		assertTrue(c.add(l));
		assertEquals(1, c.getLessonTypes().size());
		assertEquals(l, c.getLessonTypes().get(0));
	}

	@Test
	public void addLessonTypeExists() {
		CompoundLesson c = new CompoundLesson();
		LessonType l = PowerMockito.mock(LessonType.class);
		c.add(l);
		assertFalse(c.add(l));
		assertEquals(1, c.getLessonTypes().size());
	}

	@Test
	public void containsLessonTypeExists() {
		CompoundLesson c = new CompoundLesson();
		LessonType l = PowerMockito.mock(LessonType.class);
		c.add(l);
		assertTrue(c.contains(l));
	}

	@Test
	public void containsLessonTypeNotExists() {
		CompoundLesson c = new CompoundLesson();
		LessonType l = PowerMockito.mock(LessonType.class);
		assertFalse(c.contains(l));
	}

	@Test
	public void removeLessonTypeExists() {
		CompoundLesson c = new CompoundLesson();
		LessonType l = PowerMockito.mock(LessonType.class);
		LessonType l1 = PowerMockito.mock(LessonType.class);
		c.add(l);
		c.add(l1);
		assertEquals(1, c.remove(l));
		assertEquals(1, c.getLessonTypes().size());
		assertEquals(0, c.remove(l1));
		assertEquals(0, c.getLessonTypes().size());
	}
	
	@Test
	public void removeLessonTypeNotExists() {
		CompoundLesson c = new CompoundLesson();
		LessonType l = PowerMockito.mock(LessonType.class);
		LessonType l1 = PowerMockito.mock(LessonType.class);
		c.add(l);
		assertEquals(1, c.remove(l1));
		assertEquals(1, c.getLessonTypes().size());
	}
}
