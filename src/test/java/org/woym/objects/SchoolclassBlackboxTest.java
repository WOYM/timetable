package org.woym.objects;

import static org.junit.Assert.*;

import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

public class SchoolclassBlackboxTest {

	@Test
	public void addLessonDemandNotExists() {
		Schoolclass s = new Schoolclass();
		LessonType l = PowerMockito.mock(LessonType.class);
		assertTrue(s.add(l, 4));
		assertEquals(1, s.getLessonDemands().size());
		assertEquals(new Integer(4), s.getLessonDemands().get(l));
	}

	@Test
	public void addLessonDemandExists() {
		Schoolclass s = new Schoolclass();
		LessonType l = PowerMockito.mock(LessonType.class);
		s.add(l, 4);
		assertFalse(s.add(l, 6));
		assertEquals(1, s.getLessonDemands().size());
	}

	@Test
	public void replaceLessonDemandExists() {
		Schoolclass s = new Schoolclass();
		LessonType l = PowerMockito.mock(LessonType.class);
		s.add(l, 4);
		assertTrue(s.replace(l, 6));
		assertEquals(new Integer(6), s.getLessonDemands().get(l));
	}

	@Test
	public void replaceLessonDemandNotExists() {
		Schoolclass s = new Schoolclass();
		LessonType l = PowerMockito.mock(LessonType.class);
		assertFalse(s.replace(l, 6));
		assertEquals(0, s.getLessonDemands().size());
	}

	@Test
	public void removeLessonDemandExists() {
		Schoolclass s = new Schoolclass();
		LessonType l = PowerMockito.mock(LessonType.class);
		s.add(l, 4);
		assertTrue(s.remove(l));
		assertEquals(0, s.getLessonDemands().size());
	}

	@Test
	public void removeLessonDemandNotExists() {
		Schoolclass s = new Schoolclass();
		LessonType l = PowerMockito.mock(LessonType.class);
		s.add(l, 4);
		assertFalse(s.remove(PowerMockito.mock(LessonType.class)));
		assertEquals(1, s.getLessonDemands().size());
	}

	@Test
	public void containsLessonDemandExists() {
		Schoolclass s = new Schoolclass();
		LessonType l = PowerMockito.mock(LessonType.class);
		s.add(l, 4);
		assertTrue(s.contains(l));
	}

	@Test
	public void containsLessonDemandNotExists() {
		Schoolclass s = new Schoolclass();
		LessonType l = PowerMockito.mock(LessonType.class);
		assertFalse(s.contains(l));
	}

}
