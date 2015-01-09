package org.woym.objects;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

@Test(groups = "unit")
public class SchoolclassBlackboxTest extends PowerMockTestCase {

	@Test
	public void addLessonDemandNotExists() {
		Schoolclass s = new Schoolclass();
		LessonType l = PowerMockito.mock(LessonType.class);
		AssertJUnit.assertTrue(s.add(l, 4));
		AssertJUnit.assertEquals(1, s.getLessonDemands().size());
		AssertJUnit.assertEquals(new Integer(4), s.getLessonDemands().get(l));
	}

	@Test
	public void addLessonDemandExists() {
		Schoolclass s = new Schoolclass();
		LessonType l = PowerMockito.mock(LessonType.class);
		s.add(l, 4);
		AssertJUnit.assertFalse(s.add(l, 6));
		AssertJUnit.assertEquals(1, s.getLessonDemands().size());
	}

	@Test
	public void replaceLessonDemandExists() {
		Schoolclass s = new Schoolclass();
		LessonType l = PowerMockito.mock(LessonType.class);
		s.add(l, 4);
		AssertJUnit.assertTrue(s.replace(l, 6));
		AssertJUnit.assertEquals(new Integer(6), s.getLessonDemands().get(l));
	}

	@Test
	public void replaceLessonDemandNotExists() {
		Schoolclass s = new Schoolclass();
		LessonType l = PowerMockito.mock(LessonType.class);
		AssertJUnit.assertFalse(s.replace(l, 6));
		AssertJUnit.assertEquals(0, s.getLessonDemands().size());
	}

	@Test
	public void removeLessonDemandExists() {
		Schoolclass s = new Schoolclass();
		LessonType l = PowerMockito.mock(LessonType.class);
		s.add(l, 4);
		AssertJUnit.assertTrue(s.remove(l));
		AssertJUnit.assertEquals(0, s.getLessonDemands().size());
	}

	@Test
	public void removeLessonDemandNotExists() {
		Schoolclass s = new Schoolclass();
		LessonType l = PowerMockito.mock(LessonType.class);
		s.add(l, 4);
		AssertJUnit.assertFalse(s.remove(PowerMockito.mock(LessonType.class)));
		AssertJUnit.assertEquals(1, s.getLessonDemands().size());
	}

	@Test
	public void containsLessonDemandExists() {
		Schoolclass s = new Schoolclass();
		LessonType l = PowerMockito.mock(LessonType.class);
		s.add(l, 4);
		AssertJUnit.assertTrue(s.contains(l));
	}

	@Test
	public void containsLessonDemandNotExists() {
		Schoolclass s = new Schoolclass();
		LessonType l = PowerMockito.mock(LessonType.class);
		AssertJUnit.assertFalse(s.contains(l));
	}

}
