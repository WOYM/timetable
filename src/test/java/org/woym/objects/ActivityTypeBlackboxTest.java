package org.woym.objects;

import static org.junit.Assert.*;

import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

public class ActivityTypeBlackboxTest {

	@Test
	public void equalsSameObject() {
		LessonType l = new LessonType();
		l.setName("Mathe");
		assertTrue(l.equals(l));
	}

	@Test
	public void equalsNull() {
		LessonType l = new LessonType();
		l.setName("Mathe");
		assertFalse(l.equals(null));
	}

	@Test
	public void equalsNoActivityType() {
		LessonType l = new LessonType();
		l.setName("Mathe");
		assertFalse(l.equals(PowerMockito.mock(Location.class)));
	}

	@Test
	public void equalsEqual() {
		LessonType l = new LessonType();
		LessonType l1 = new LessonType();
		l.setName("Mathe");
		l1.setName("Mathe");
		assertTrue(l.equals(l1));
	}

	@Test
	public void equalsEqualDifferentCase() {
		LessonType l = new LessonType();
		LessonType l1 = new LessonType();
		l.setName("Mathe");
		l1.setName("MaThE");
		assertTrue(l.equals(l1));
	}

	@Test
	public void equalsNotEqual() {
		LessonType l = new LessonType();
		LessonType l1 = new LessonType();
		l.setName("Mathe");
		l1.setName("Englisch");
		assertFalse(l.equals(l1));
	}

	@Test
	public void addRoomNotExists() {
		LessonType l = new LessonType();
		l.setName("Mathe");
		Room r = PowerMockito.mock(Room.class);
		assertTrue(l.add(r));
		assertEquals(1, l.getRooms().size());
		assertEquals(r, l.getRooms().get(0));
	}

	@Test
	public void addRoomExists() {
		LessonType l = new LessonType();
		l.setName("Mathe");
		Room r = PowerMockito.mock(Room.class);
		l.add(r);
		assertFalse(l.add(r));
	}

	@Test
	public void removeRoomExists() {
		LessonType l = new LessonType();
		l.setName("Mathe");
		Room r = PowerMockito.mock(Room.class);
		l.add(r);
		assertTrue(l.remove(r));
		assertEquals(0, l.getRooms().size());
	}

	@Test
	public void removeRoomNotExists() {
		LessonType l = new LessonType();
		l.setName("Mathe");
		Room r = PowerMockito.mock(Room.class);
		assertFalse(l.remove(r));
	}

	@Test
	public void containsRoomExists() {
		LessonType l = new LessonType();
		l.setName("Mathe");
		Room r = PowerMockito.mock(Room.class);
		l.add(r);
		assertTrue(l.contains(r));
	}

	@Test
	public void containRoomNotExists() {
		LessonType l = new LessonType();
		l.setName("Mathe");
		Room r = PowerMockito.mock(Room.class);
		assertFalse(l.contains(r));
	}

	@Test
	public void getReadableDurationLessThanOneHour() {
		LessonType l = new LessonType();
		l.setName("Mathe");
		l.setTypicalDuration(45);
		assertEquals("45 Minuten", l.getReadableDuration());
	}

	@Test
	public void getReadableDurationOneHour() {
		LessonType l = new LessonType();
		l.setName("Mathe");
		l.setTypicalDuration(60);
		assertEquals("1 Stunde", l.getReadableDuration());
	}

	@Test
	public void getReadableDurationTwoHours() {
		LessonType l = new LessonType();
		l.setName("Mathe");
		l.setTypicalDuration(120);
		assertEquals("2 Stunden", l.getReadableDuration());
	}

	@Test
	public void getReadableDurationOneHourPlusFewMinutes() {
		LessonType l = new LessonType();
		l.setName("Mathe");
		l.setTypicalDuration(75);
		assertEquals("1 Stunde, 15 Minuten", l.getReadableDuration());
	}

}
