package org.woym.objects;

import static org.junit.Assert.*;

import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

public class LocationBlackboxTest {

	@Test
	public void equalsSameObject() {
		Location l = new Location();
		assertTrue(l.equals(l));
	}

	@Test
	public void equalsNull() {
		Location l = new Location();
		assertFalse(l.equals(null));
	}

	@Test
	public void equalsNotInstanceOfLocation() {
		Location l = new Location();
		assertFalse(l.equals(PowerMockito.mock(Teacher.class)));
	}

	@Test
	public void equalsEqual() {
		Location l = new Location();
		Location l1 = new Location();
		l.setName("Hauptstandort");
		l1.setName("Hauptstandort");
		assertTrue(l.equals(l));
	}

	@Test
	public void equalsDifferentCase() {
		Location l = new Location();
		Location l1 = new Location();
		l.setName("Hauptstandort");
		l1.setName("HaUpTsTaNdOrT");
		assertTrue(l.equals(l));
	}

	@Test
	public void equalsNotEqual() {
		Location l = new Location();
		Location l1 = new Location();
		l.setName("Hauptstandort");
		l1.setName("Zweigstelle");
		assertFalse(l.equals(l1));
	}

	@Test
	public void addRoomNotExists() {
		Location l = new Location();
		Room r = PowerMockito.mock(Room.class);
		assertTrue(l.add(r));
		assertEquals(1, l.getRooms().size());
		assertEquals(r, l.getRooms().get(0));
	}

	@Test
	public void addRoomExists() {
		Location l = new Location();
		Room r = PowerMockito.mock(Room.class);
		l.add(r);
		assertFalse(l.add(r));
		assertEquals(1, l.getRooms().size());
	}

	@Test
	public void removeRoomExists() {
		Location l = new Location();
		Room r = PowerMockito.mock(Room.class);
		l.add(r);
		assertTrue(l.remove(r));
		assertEquals(0, l.getRooms().size());
	}

	@Test
	public void removeRoomNotExists() {
		Location l = new Location();
		Room r = PowerMockito.mock(Room.class);
		Room r1 = PowerMockito.mock(Room.class);
		l.add(r);
		assertFalse(l.remove(r1));
		assertEquals(1, l.getRooms().size());
	}

	@Test
	public void containsRoomExists() {
		Location l = new Location();
		Room r = PowerMockito.mock(Room.class);
		l.add(r);
		assertTrue(l.contains(r));
	}

	@Test
	public void containsRoomNotExists() {
		Location l = new Location();
		Room r = PowerMockito.mock(Room.class);
		assertFalse(l.contains(r));
	}

}
