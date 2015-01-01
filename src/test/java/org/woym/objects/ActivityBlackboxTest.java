package org.woym.objects;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.woym.objects.spec.IActivityObject;

public class ActivityBlackboxTest {

	@Test
	public void removeRoomRoomExists() {
		Pause p = new Pause();
		List<Room> rooms = new ArrayList<Room>();
		Room r = PowerMockito.mock(Room.class);
		Room r1 = PowerMockito.mock(Room.class);
		rooms.add(r);
		rooms.add(r1);
		p.setRooms(rooms);
		assertEquals(1, p.remove(r));
		assertEquals(1, p.getRooms().size());
		assertEquals(0, p.remove(r1));
		assertEquals(0, p.getRooms().size());
	}

	@Test
	public void removeRoomRoomNotExists() {
		Pause p = new Pause();
		List<Room> rooms = new ArrayList<Room>();
		Room r = PowerMockito.mock(Room.class);
		Room r1 = PowerMockito.mock(Room.class);
		rooms.add(r);
		p.setRooms(rooms);
		assertEquals(1, p.remove(r1));
		assertEquals(1, p.getRooms().size());
	}

	@Test
	public void addSchoolclassSchoolclassNotExists() {
		Pause p = new Pause();
		Schoolclass s = PowerMockito.mock(Schoolclass.class);
		assertTrue(p.add(s));
		assertEquals(1, p.getSchoolclasses().size());
		assertEquals(s, p.getSchoolclasses().get(0));
	}

	@Test
	public void addSchoolclassSchoolclassExists() {
		Pause p = new Pause();
		Schoolclass s = PowerMockito.mock(Schoolclass.class);
		assertTrue(p.add(s));
		assertFalse(p.add(s));
		assertEquals(1, p.getSchoolclasses().size());
	}

	@Test
	public void removeSchoolclassSchoolclassExists() {
		Pause p = new Pause();
		Schoolclass s = PowerMockito.mock(Schoolclass.class);
		Schoolclass s1 = PowerMockito.mock(Schoolclass.class);
		p.add(s);
		p.add(s1);
		assertEquals(1, p.remove(s));
		assertEquals(1, p.getSchoolclasses().size());
		assertEquals(0, p.remove(s1));
		assertEquals(0, p.getSchoolclasses().size());
	}

	@Test
	public void removeSchoolclassSchoolclassNotExists() {
		Pause p = new Pause();
		Schoolclass s = PowerMockito.mock(Schoolclass.class);
		Schoolclass s1 = PowerMockito.mock(Schoolclass.class);
		p.add(s);
		assertEquals(1, p.remove(s1));
		assertEquals(1, p.getSchoolclasses().size());
	}

	@Test
	public void containsSchoolclassSchoolclassExists() {
		Pause p = new Pause();
		Schoolclass s = PowerMockito.mock(Schoolclass.class);
		Schoolclass s1 = PowerMockito.mock(Schoolclass.class);
		p.add(s);
		p.add(s1);
		assertTrue(p.contains(s));
		assertTrue(p.contains(s1));
	}

	@Test
	public void containsSchoolclassSchoolclassNotExists() {
		Pause p = new Pause();
		Schoolclass s = PowerMockito.mock(Schoolclass.class);
		Schoolclass s1 = PowerMockito.mock(Schoolclass.class);
		p.add(s);
		assertFalse(p.contains(s1));
	}

	@Test
	public void addEmployeeTimePeriodsNotExists() {
		Pause p = new Pause();
		EmployeeTimePeriods e = PowerMockito.mock(EmployeeTimePeriods.class);
		assertTrue(p.add(e));
		assertEquals(1, p.getEmployeeTimePeriods().size());
		assertEquals(e, p.getEmployeeTimePeriods().get(0));
	}

	@Test
	public void addEmployeeTimePeriodsExists() {
		Pause p = new Pause();
		EmployeeTimePeriods e = PowerMockito.mock(EmployeeTimePeriods.class);
		p.add(e);
		assertFalse(p.add(e));
		assertEquals(1, p.getEmployeeTimePeriods().size());
	}

	@Test
	public void removeEmployeeExists() {
		Pause p = new Pause();
		EmployeeTimePeriods e = new EmployeeTimePeriods();
		EmployeeTimePeriods e1 = new EmployeeTimePeriods();
		Teacher t = PowerMockito.mock(Teacher.class);
		Teacher t1 = PowerMockito.mock(Teacher.class);
		e.setEmployee(t);
		e1.setEmployee(t1);
		p.add(e);
		p.add(e1);
		assertEquals(1, p.remove(t));
		assertEquals(1, p.getEmployeeTimePeriods().size());
		assertEquals(0, p.remove(t1));
		assertEquals(0, p.getEmployeeTimePeriods().size());
	}

	@Test
	public void removeEmployeeNotExists() {
		Pause p = new Pause();
		EmployeeTimePeriods e = new EmployeeTimePeriods();
		EmployeeTimePeriods e1 = new EmployeeTimePeriods();
		Teacher t = PowerMockito.mock(Teacher.class);
		Teacher t1 = PowerMockito.mock(Teacher.class);
		e.setEmployee(t);
		e1.setEmployee(t1);
		p.add(e);
		assertEquals(1, p.remove(t1));
		assertEquals(1, p.getEmployeeTimePeriods().size());
	}

	@Test
	public void containsEmployeeExits() {
		Pause p = new Pause();
		EmployeeTimePeriods e = new EmployeeTimePeriods();
		EmployeeTimePeriods e1 = new EmployeeTimePeriods();
		Teacher t = PowerMockito.mock(Teacher.class);
		Teacher t1 = PowerMockito.mock(Teacher.class);
		e.setEmployee(t);
		e1.setEmployee(t1);
		p.add(e);
		p.add(e1);
		assertTrue(p.contains(t));
		assertTrue(p.contains(t1));
	}

	@Test
	public void containsEmployeeNotExists() {
		Pause p = new Pause();
		EmployeeTimePeriods e = new EmployeeTimePeriods();
		Teacher t = PowerMockito.mock(Teacher.class);
		e.setEmployee(t);
		assertFalse(p.contains(t));
	}

	@Test
	public void removeIActivityObjectInstanceOfEmployee() {
		Pause p = new Pause();
		EmployeeTimePeriods e = new EmployeeTimePeriods();
		IActivityObject a = PowerMockito.mock(Teacher.class);
		e.setEmployee((Teacher) a);
		p.add(e);
		assertEquals(0, p.getSchoolclasses().size());
		assertEquals(0, p.getRooms().size());
		assertEquals(1, p.getEmployeeTimePeriods().size());
		assertEquals(0, p.remove(a));
		assertEquals(0, p.getEmployeeTimePeriods().size());
	}

	@Test
	public void removeIActivityObjectInstanceOfRoom() {
		Pause p = new Pause();
		IActivityObject a = PowerMockito.mock(Room.class);
		List<Room> rooms = new ArrayList<Room>();
		rooms.add((Room) a);
		p.setRooms(rooms);
		assertEquals(0, p.getSchoolclasses().size());
		assertEquals(1, p.getRooms().size());
		assertEquals(0, p.getEmployeeTimePeriods().size());
		assertEquals(0, p.remove(a));
		assertEquals(0, p.getRooms().size());
	}
	
	@Test
	public void removeIActivityObjectInstanceOfSchoolclass() {
		Pause p = new Pause();
		IActivityObject a = PowerMockito.mock(Schoolclass.class);
		p.add((Schoolclass)a);
		assertEquals(1, p.getSchoolclasses().size());
		assertEquals(0, p.getRooms().size());
		assertEquals(0, p.getEmployeeTimePeriods().size());
		assertEquals(0, p.remove(a));
		assertEquals(0, p.getSchoolclasses().size());
	}

}
