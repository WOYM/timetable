package org.woym.objects;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.testng.PowerMockTestCase;

public class AcademicYearBlackboxTest extends PowerMockTestCase {

	@Test
	public void equalsSameObject() {
		AcademicYear a = new AcademicYear();
		a.setAcademicYear(1);
		AssertJUnit.assertTrue(a.equals(a));
	}

	@Test
	public void equalsNull() {
		AcademicYear a = new AcademicYear();
		a.setAcademicYear(1);
		AssertJUnit.assertFalse(a.equals(null));
	}

	@Test
	public void equalsNoAcademicYear() {
		AcademicYear a = new AcademicYear();
		a.setAcademicYear(1);
		AssertJUnit.assertFalse(a.equals(PowerMockito.mock(Location.class)));
	}

	@Test
	public void equalsNotEqual() {
		AcademicYear a = new AcademicYear();
		AcademicYear a1 = new AcademicYear();
		a.setAcademicYear(1);
		a1.setAcademicYear(2);
		AssertJUnit.assertFalse(a.equals(a1));
	}

	@Test
	public void equalsEqual() {
		AcademicYear a = new AcademicYear();
		AcademicYear a1 = new AcademicYear();
		a.setAcademicYear(1);
		a1.setAcademicYear(1);
		AssertJUnit.assertTrue(a.equals(a1));
	}

	@Test
	public void addSchoolclassSchoolclassNotExists() {
		AcademicYear a = new AcademicYear();
		Schoolclass s = PowerMockito.mock(Schoolclass.class);
		AssertJUnit.assertTrue(a.add(s));
		AssertJUnit.assertEquals(1, a.getSchoolclasses().size());
		AssertJUnit.assertEquals(s, a.getSchoolclasses().get(0));
	}

	@Test
	public void addSchoolclassSchoolclassExists() {
		AcademicYear a = new AcademicYear();
		Schoolclass s = PowerMockito.mock(Schoolclass.class);
		a.add(s);
		AssertJUnit.assertFalse(a.add(s));
	}

	@Test
	public void removeSchoolclassSchoolclassExists() {
		AcademicYear a = new AcademicYear();
		Schoolclass s = PowerMockito.mock(Schoolclass.class);
		a.add(s);
		AssertJUnit.assertTrue(a.remove(s));
		AssertJUnit.assertEquals(0, a.getSchoolclasses().size());
	}

	@Test
	public void removeSchoolclassSchoolclassNotExists() {
		AcademicYear a = new AcademicYear();
		Schoolclass s = PowerMockito.mock(Schoolclass.class);
		AssertJUnit.assertFalse(a.remove(s));
	}

	@Test
	public void containsSchoolclassSchoolclassExists() {
		AcademicYear a = new AcademicYear();
		Schoolclass s = PowerMockito.mock(Schoolclass.class);
		a.add(s);
		AssertJUnit.assertTrue(a.contains(s));
	}

	@Test
	public void containsSchoolclassSchoolclassNotExists() {
		AcademicYear a = new AcademicYear();
		Schoolclass s = PowerMockito.mock(Schoolclass.class);
		AssertJUnit.assertFalse(a.contains(s));
	}

	@Test
	public void addLessonTypeNotExists() {
		AcademicYear a = new AcademicYear();
		LessonType l = PowerMockito.mock(LessonType.class);
		AssertJUnit.assertTrue(a.add(l, 3));
		AssertJUnit.assertEquals(1, a.getLessonDemands().size());
		AssertJUnit.assertEquals(new Integer(3), a.getLessonDemands().get(l));
	}

	@Test
	public void addLessonTypeExists() {
		AcademicYear a = new AcademicYear();
		LessonType l = PowerMockito.mock(LessonType.class);
		a.add(l, 3);
		AssertJUnit.assertFalse(a.add(l, 3));
	}

	@Test
	public void replaceLessonTypeExists() {
		AcademicYear a = new AcademicYear();
		LessonType l = PowerMockito.mock(LessonType.class);
		a.add(l, 3);
		AssertJUnit.assertTrue(a.replace(l, 10));
		AssertJUnit.assertEquals(1, a.getLessonDemands().size());
		AssertJUnit.assertEquals(new Integer(10), a.getLessonDemands().get(l));
	}

	@Test
	public void replaceLessonTypeNotExists() {
		AcademicYear a = new AcademicYear();
		LessonType l = PowerMockito.mock(LessonType.class);
		AssertJUnit.assertFalse(a.replace(l, 10));
		AssertJUnit.assertEquals(0, a.getLessonDemands().size());
	}

	@Test
	public void removeLessonTypeExists() {
		AcademicYear a = new AcademicYear();
		LessonType l = PowerMockito.mock(LessonType.class);
		a.add(l, 3);
		AssertJUnit.assertTrue(a.remove(l));
		AssertJUnit.assertEquals(0, a.getLessonDemands().size());
	}

	@Test
	public void removeLessonTypeNotExists() {
		AcademicYear a = new AcademicYear();
		LessonType l = PowerMockito.mock(LessonType.class);
		AssertJUnit.assertFalse(a.remove(l));
	}

	@Test
	public void containsLessonTypeExists() {
		AcademicYear a = new AcademicYear();
		LessonType l = PowerMockito.mock(LessonType.class);
		a.add(l, 3);
		AssertJUnit.assertTrue(a.contains(l));
	}

	public void containsLessonTypeNotExists() {
		AcademicYear a = new AcademicYear();
		LessonType l = PowerMockito.mock(LessonType.class);
		AssertJUnit.assertFalse(a.contains(l));
	}

}
