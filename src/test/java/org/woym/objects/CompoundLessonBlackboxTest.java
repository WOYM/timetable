package org.woym.objects;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

@Test(groups = "unit")
public class CompoundLessonBlackboxTest extends PowerMockTestCase {

	@Test
	public void addLessonTypeNotExists() {
		CompoundLesson c = new CompoundLesson();
		LessonType l = PowerMockito.mock(LessonType.class);
		AssertJUnit.assertTrue(c.add(l));
		AssertJUnit.assertEquals(1, c.getLessonTypes().size());
		AssertJUnit.assertEquals(l, c.getLessonTypes().get(0));
	}

	@Test
	public void addLessonTypeExists() {
		CompoundLesson c = new CompoundLesson();
		LessonType l = PowerMockito.mock(LessonType.class);
		c.add(l);
		AssertJUnit.assertFalse(c.add(l));
		AssertJUnit.assertEquals(1, c.getLessonTypes().size());
	}

	@Test
	public void containsLessonTypeExists() {
		CompoundLesson c = new CompoundLesson();
		LessonType l = PowerMockito.mock(LessonType.class);
		c.add(l);
		AssertJUnit.assertTrue(c.contains(l));
	}

	@Test
	public void containsLessonTypeNotExists() {
		CompoundLesson c = new CompoundLesson();
		LessonType l = PowerMockito.mock(LessonType.class);
		AssertJUnit.assertFalse(c.contains(l));
	}

	@Test
	public void removeLessonTypeExists() {
		CompoundLesson c = new CompoundLesson();
		LessonType l = PowerMockito.mock(LessonType.class);
		LessonType l1 = PowerMockito.mock(LessonType.class);
		c.add(l);
		c.add(l1);
		AssertJUnit.assertEquals(1, c.remove(l));
		AssertJUnit.assertEquals(1, c.getLessonTypes().size());
		AssertJUnit.assertEquals(0, c.remove(l1));
		AssertJUnit.assertEquals(0, c.getLessonTypes().size());
	}

	@Test
	public void removeLessonTypeNotExists() {
		CompoundLesson c = new CompoundLesson();
		LessonType l = PowerMockito.mock(LessonType.class);
		LessonType l1 = PowerMockito.mock(LessonType.class);
		c.add(l);
		AssertJUnit.assertEquals(1, c.remove(l1));
		AssertJUnit.assertEquals(1, c.getLessonTypes().size());
	}
}
