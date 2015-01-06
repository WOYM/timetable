package org.woym.objects;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.testng.PowerMockTestCase;

@Test(groups = "unit")
public class LocationBlackboxTest extends PowerMockTestCase {

	@Test
	public void equalsSameObject() {
		Location l = new Location();
		AssertJUnit.assertTrue(l.equals(l));
	}

	@Test
	public void equalsNull() {
		Location l = new Location();
		AssertJUnit.assertFalse(l.equals(null));
	}

	@Test
	public void equalsNotInstanceOfLocation() {
		Location l = new Location();
		AssertJUnit.assertFalse(l.equals(PowerMockito.mock(Teacher.class)));
	}

	@Test
	public void equalsEqual() {
		Location l = new Location();
		Location l1 = new Location();
		l.setName("Hauptstandort");
		l1.setName("Hauptstandort");
		AssertJUnit.assertTrue(l.equals(l));
	}

	@Test
	public void equalsDifferentCase() {
		Location l = new Location();
		Location l1 = new Location();
		l.setName("Hauptstandort");
		l1.setName("HaUpTsTaNdOrT");
		AssertJUnit.assertTrue(l.equals(l));
	}

	@Test
	public void equalsNotEqual() {
		Location l = new Location();
		Location l1 = new Location();
		l.setName("Hauptstandort");
		l1.setName("Zweigstelle");
		AssertJUnit.assertFalse(l.equals(l1));
	}

	@Test
	public void addRoomNotExists() {
		Location l = new Location();
		Room r = PowerMockito.mock(Room.class);
		AssertJUnit.assertTrue(l.add(r));
		AssertJUnit.assertEquals(1, l.getRooms().size());
		AssertJUnit.assertEquals(r, l.getRooms().get(0));
	}

	@Test
	public void addRoomExists() {
		Location l = new Location();
		Room r = PowerMockito.mock(Room.class);
		l.add(r);
		AssertJUnit.assertFalse(l.add(r));
		AssertJUnit.assertEquals(1, l.getRooms().size());
	}

	@Test
	public void removeRoomExists() {
		Location l = new Location();
		Room r = PowerMockito.mock(Room.class);
		l.add(r);
		AssertJUnit.assertTrue(l.remove(r));
		AssertJUnit.assertEquals(0, l.getRooms().size());
	}

	@Test
	public void removeRoomNotExists() {
		Location l = new Location();
		Room r = PowerMockito.mock(Room.class);
		Room r1 = PowerMockito.mock(Room.class);
		l.add(r);
		AssertJUnit.assertFalse(l.remove(r1));
		AssertJUnit.assertEquals(1, l.getRooms().size());
	}

	@Test
	public void containsRoomExists() {
		Location l = new Location();
		Room r = PowerMockito.mock(Room.class);
		l.add(r);
		AssertJUnit.assertTrue(l.contains(r));
	}

	@Test
	public void containsRoomNotExists() {
		Location l = new Location();
		Room r = PowerMockito.mock(Room.class);
		AssertJUnit.assertFalse(l.contains(r));
	}

}
