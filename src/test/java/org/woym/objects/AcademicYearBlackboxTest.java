package org.woym.objects;

import static org.junit.Assert.*;

import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

public class AcademicYearBlackboxTest {

	@Test
	public void equalsSameObject() {
		AcademicYear a = new AcademicYear();
		a.setAcademicYear(1);
		assertTrue(a.equals(a));
	}

	@Test
	public void equalsNull() {
		AcademicYear a = new AcademicYear();
		a.setAcademicYear(1);
		assertFalse(a.equals(null));
	}

	@Test
	public void equalsNoAcademicYear() {
		AcademicYear a = new AcademicYear();
		a.setAcademicYear(1);
		assertFalse(a.equals(PowerMockito.mock(Location.class)));
	}

	@Test
	public void equalsNotEqual() {
		AcademicYear a = new AcademicYear();
		AcademicYear a1 = new AcademicYear();
		a.setAcademicYear(1);
		a1.setAcademicYear(2);
		assertFalse(a.equals(a1));
	}

	@Test
	public void equalsEqual() {
		AcademicYear a = new AcademicYear();
		AcademicYear a1 = new AcademicYear();
		a.setAcademicYear(1);
		a1.setAcademicYear(1);
		assertTrue(a.equals(a1));
	}

	@Test
	public void addSchoolclassSchoolclassNotExists() {
		AcademicYear a = new AcademicYear();
		Schoolclass s = PowerMockito.mock(Schoolclass.class);
		assertTrue(a.add(s));
		assertEquals(1, a.getSchoolclasses().size());
		assertEquals(s, a.getSchoolclasses().get(0));
	}

	@Test
	public void addSchoolclassSchoolclassExists() {
		AcademicYear a = new AcademicYear();
		Schoolclass s = PowerMockito.mock(Schoolclass.class);
		a.add(s);
		assertFalse(a.add(s));
	}

	@Test
	public void removeSchoolclassSchoolclassExists() {
		AcademicYear a = new AcademicYear();
		Schoolclass s = PowerMockito.mock(Schoolclass.class);
		a.add(s);
		assertTrue(a.remove(s));
		assertEquals(0, a.getSchoolclasses().size());
	}

	@Test
	public void removeSchoolclassSchoolclassNotExists() {
		AcademicYear a = new AcademicYear();
		Schoolclass s = PowerMockito.mock(Schoolclass.class);
		assertFalse(a.remove(s));
	}

	@Test
	public void containsSchoolclassSchoolclassExists() {
		AcademicYear a = new AcademicYear();
		Schoolclass s = PowerMockito.mock(Schoolclass.class);
		a.add(s);
		assertTrue(a.contains(s));
	}

	@Test
	public void containsSchoolclassSchoolclassNotExists() {
		AcademicYear a = new AcademicYear();
		Schoolclass s = PowerMockito.mock(Schoolclass.class);
		assertFalse(a.contains(s));
	}

	@Test
	public void addLessonTypeNotExists() {
		AcademicYear a = new AcademicYear();
		LessonType l = PowerMockito.mock(LessonType.class);
		assertTrue(a.add(l, 3));
		assertEquals(1, a.getLessonDemands().size());
		assertEquals(new Integer(3), a.getLessonDemands().get(l));
	}

	@Test
	public void addLessonTypeExists() {
		AcademicYear a = new AcademicYear();
		LessonType l = PowerMockito.mock(LessonType.class);
		a.add(l, 3);
		assertFalse(a.add(l, 3));
	}

	@Test
	public void replaceLessonTypeExists() {
		AcademicYear a = new AcademicYear();
		LessonType l = PowerMockito.mock(LessonType.class);
		a.add(l, 3);
		assertTrue(a.replace(l, 10));
		assertEquals(1, a.getLessonDemands().size());
		assertEquals(new Integer(10), a.getLessonDemands().get(l));
	}

	@Test
	public void replaceLessonTypeNotExists() {
		AcademicYear a = new AcademicYear();
		LessonType l = PowerMockito.mock(LessonType.class);
		assertFalse(a.replace(l, 10));
		assertEquals(0, a.getLessonDemands().size());
	}

	@Test
	public void removeLessonTypeExists() {
		AcademicYear a = new AcademicYear();
		LessonType l = PowerMockito.mock(LessonType.class);
		a.add(l, 3);
		assertTrue(a.remove(l));
		assertEquals(0, a.getLessonDemands().size());
	}

	@Test
	public void removeLessonTypeNotExists() {
		AcademicYear a = new AcademicYear();
		LessonType l = PowerMockito.mock(LessonType.class);
		assertFalse(a.remove(l));
	}

	@Test
	public void containsLessonTypeExists() {
		AcademicYear a = new AcademicYear();
		LessonType l = PowerMockito.mock(LessonType.class);
		a.add(l, 3);
		assertTrue(a.contains(l));
	}

	public void containsLessonTypeNotExists() {
		AcademicYear a = new AcademicYear();
		LessonType l = PowerMockito.mock(LessonType.class);
		assertFalse(a.contains(l));
	}

}
