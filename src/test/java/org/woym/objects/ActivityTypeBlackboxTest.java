package org.woym.objects;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

@Test(groups = "unit")
public class ActivityTypeBlackboxTest extends PowerMockTestCase {

	@Test
	public void equalsSameObject() {
		LessonType l = new LessonType();
		l.setName("Mathe");
		AssertJUnit.assertTrue(l.equals(l));
	}

	@Test
	public void equalsNull() {
		LessonType l = new LessonType();
		l.setName("Mathe");
		AssertJUnit.assertFalse(l.equals(null));
	}

	@Test
	public void equalsNoActivityType() {
		LessonType l = new LessonType();
		l.setName("Mathe");
		AssertJUnit.assertFalse(l.equals(PowerMockito.mock(Location.class)));
	}

	@Test
	public void equalsEqual() {
		LessonType l = new LessonType();
		LessonType l1 = new LessonType();
		l.setName("Mathe");
		l1.setName("Mathe");
		AssertJUnit.assertTrue(l.equals(l1));
	}

	@Test
	public void equalsEqualDifferentCase() {
		LessonType l = new LessonType();
		LessonType l1 = new LessonType();
		l.setName("Mathe");
		l1.setName("MaThE");
		AssertJUnit.assertTrue(l.equals(l1));
	}

	@Test
	public void equalsNotEqual() {
		LessonType l = new LessonType();
		LessonType l1 = new LessonType();
		l.setName("Mathe");
		l1.setName("Englisch");
		AssertJUnit.assertFalse(l.equals(l1));
	}

	@Test
	public void addRoomNotExists() {
		LessonType l = new LessonType();
		l.setName("Mathe");
		Room r = PowerMockito.mock(Room.class);
		AssertJUnit.assertTrue(l.add(r));
		AssertJUnit.assertEquals(1, l.getRooms().size());
		AssertJUnit.assertEquals(r, l.getRooms().get(0));
	}

	@Test
	public void addRoomExists() {
		LessonType l = new LessonType();
		l.setName("Mathe");
		Room r = PowerMockito.mock(Room.class);
		l.add(r);
		AssertJUnit.assertFalse(l.add(r));
	}

	@Test
	public void removeRoomExists() {
		LessonType l = new LessonType();
		l.setName("Mathe");
		Room r = PowerMockito.mock(Room.class);
		l.add(r);
		AssertJUnit.assertTrue(l.remove(r));
		AssertJUnit.assertEquals(0, l.getRooms().size());
	}

	@Test
	public void removeRoomNotExists() {
		LessonType l = new LessonType();
		l.setName("Mathe");
		Room r = PowerMockito.mock(Room.class);
		AssertJUnit.assertFalse(l.remove(r));
	}

	@Test
	public void containsRoomExists() {
		LessonType l = new LessonType();
		l.setName("Mathe");
		Room r = PowerMockito.mock(Room.class);
		l.add(r);
		AssertJUnit.assertTrue(l.contains(r));
	}

	@Test
	public void containRoomNotExists() {
		LessonType l = new LessonType();
		l.setName("Mathe");
		Room r = PowerMockito.mock(Room.class);
		AssertJUnit.assertFalse(l.contains(r));
	}

	@Test
	public void getReadableDurationLessThanOneHour() {
		LessonType l = new LessonType();
		l.setName("Mathe");
		l.setTypicalDuration(45);
		AssertJUnit.assertEquals("45 Minuten", l.getReadableDuration());
	}

	@Test
	public void getReadableDurationOneHour() {
		LessonType l = new LessonType();
		l.setName("Mathe");
		l.setTypicalDuration(60);
		AssertJUnit.assertEquals("1 Stunde", l.getReadableDuration());
	}

	@Test
	public void getReadableDurationTwoHours() {
		LessonType l = new LessonType();
		l.setName("Mathe");
		l.setTypicalDuration(120);
		AssertJUnit.assertEquals("2 Stunden", l.getReadableDuration());
	}

	@Test
	public void getReadableDurationOneHourPlusFewMinutes() {
		LessonType l = new LessonType();
		l.setName("Mathe");
		l.setTypicalDuration(75);
		AssertJUnit.assertEquals("1 Stunde, 15 Minuten",
				l.getReadableDuration());
	}

}
