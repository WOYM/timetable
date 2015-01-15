package org.woym.common.objects;

import java.util.ArrayList;
import java.util.List;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;
import org.woym.common.objects.EmployeeTimePeriods;
import org.woym.common.objects.Pause;
import org.woym.common.objects.Room;
import org.woym.common.objects.Schoolclass;
import org.woym.common.objects.Teacher;
import org.woym.common.objects.spec.IActivityObject;

@Test(groups = "unit")
public class ActivityBlackboxTest extends PowerMockTestCase {

	@Test
	public void removeRoomRoomExists() {
		Pause p = new Pause();
		List<Room> rooms = new ArrayList<Room>();
		Room r = PowerMockito.mock(Room.class);
		Room r1 = PowerMockito.mock(Room.class);
		rooms.add(r);
		rooms.add(r1);
		p.setRooms(rooms);
		AssertJUnit.assertEquals(1, p.remove(r));
		AssertJUnit.assertEquals(1, p.getRooms().size());
		AssertJUnit.assertEquals(0, p.remove(r1));
		AssertJUnit.assertEquals(0, p.getRooms().size());
	}

	@Test
	public void removeRoomRoomNotExists() {
		Pause p = new Pause();
		List<Room> rooms = new ArrayList<Room>();
		Room r = PowerMockito.mock(Room.class);
		Room r1 = PowerMockito.mock(Room.class);
		rooms.add(r);
		p.setRooms(rooms);
		AssertJUnit.assertEquals(1, p.remove(r1));
		AssertJUnit.assertEquals(1, p.getRooms().size());
	}

	@Test
	public void addSchoolclassSchoolclassNotExists() {
		Pause p = new Pause();
		Schoolclass s = PowerMockito.mock(Schoolclass.class);
		AssertJUnit.assertTrue(p.add(s));
		AssertJUnit.assertEquals(1, p.getSchoolclasses().size());
		AssertJUnit.assertEquals(s, p.getSchoolclasses().get(0));
	}

	@Test
	public void addSchoolclassSchoolclassExists() {
		Pause p = new Pause();
		Schoolclass s = PowerMockito.mock(Schoolclass.class);
		AssertJUnit.assertTrue(p.add(s));
		AssertJUnit.assertFalse(p.add(s));
		AssertJUnit.assertEquals(1, p.getSchoolclasses().size());
	}

	@Test
	public void removeSchoolclassSchoolclassExists() {
		Pause p = new Pause();
		Schoolclass s = PowerMockito.mock(Schoolclass.class);
		Schoolclass s1 = PowerMockito.mock(Schoolclass.class);
		p.add(s);
		p.add(s1);
		AssertJUnit.assertEquals(1, p.remove(s));
		AssertJUnit.assertEquals(1, p.getSchoolclasses().size());
		AssertJUnit.assertEquals(0, p.remove(s1));
		AssertJUnit.assertEquals(0, p.getSchoolclasses().size());
	}

	@Test
	public void removeSchoolclassSchoolclassNotExists() {
		Pause p = new Pause();
		Schoolclass s = PowerMockito.mock(Schoolclass.class);
		Schoolclass s1 = PowerMockito.mock(Schoolclass.class);
		p.add(s);
		AssertJUnit.assertEquals(1, p.remove(s1));
		AssertJUnit.assertEquals(1, p.getSchoolclasses().size());
	}

	@Test
	public void containsSchoolclassSchoolclassExists() {
		Pause p = new Pause();
		Schoolclass s = PowerMockito.mock(Schoolclass.class);
		Schoolclass s1 = PowerMockito.mock(Schoolclass.class);
		p.add(s);
		p.add(s1);
		AssertJUnit.assertTrue(p.contains(s));
		AssertJUnit.assertTrue(p.contains(s1));
	}

	@Test
	public void containsSchoolclassSchoolclassNotExists() {
		Pause p = new Pause();
		Schoolclass s = PowerMockito.mock(Schoolclass.class);
		Schoolclass s1 = PowerMockito.mock(Schoolclass.class);
		p.add(s);
		AssertJUnit.assertFalse(p.contains(s1));
	}

	@Test
	public void addEmployeeTimePeriodsNotExists() {
		Pause p = new Pause();
		EmployeeTimePeriods e = PowerMockito.mock(EmployeeTimePeriods.class);
		AssertJUnit.assertTrue(p.add(e));
		AssertJUnit.assertEquals(1, p.getEmployeeTimePeriods().size());
		AssertJUnit.assertEquals(e, p.getEmployeeTimePeriods().get(0));
	}

	@Test
	public void addEmployeeTimePeriodsExists() {
		Pause p = new Pause();
		EmployeeTimePeriods e = PowerMockito.mock(EmployeeTimePeriods.class);
		p.add(e);
		AssertJUnit.assertFalse(p.add(e));
		AssertJUnit.assertEquals(1, p.getEmployeeTimePeriods().size());
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
		AssertJUnit.assertEquals(1, p.remove(t));
		AssertJUnit.assertEquals(1, p.getEmployeeTimePeriods().size());
		AssertJUnit.assertEquals(0, p.remove(t1));
		AssertJUnit.assertEquals(0, p.getEmployeeTimePeriods().size());
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
		AssertJUnit.assertEquals(1, p.remove(t1));
		AssertJUnit.assertEquals(1, p.getEmployeeTimePeriods().size());
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
		AssertJUnit.assertTrue(p.contains(t));
		AssertJUnit.assertTrue(p.contains(t1));
	}

	@Test
	public void containsEmployeeNotExists() {
		Pause p = new Pause();
		EmployeeTimePeriods e = new EmployeeTimePeriods();
		Teacher t = PowerMockito.mock(Teacher.class);
		e.setEmployee(t);
		AssertJUnit.assertFalse(p.contains(t));
	}

	@Test
	public void removeIActivityObjectInstanceOfEmployee() {
		Pause p = new Pause();
		EmployeeTimePeriods e = new EmployeeTimePeriods();
		IActivityObject a = PowerMockito.mock(Teacher.class);
		e.setEmployee((Teacher) a);
		p.add(e);
		AssertJUnit.assertEquals(0, p.getSchoolclasses().size());
		AssertJUnit.assertEquals(0, p.getRooms().size());
		AssertJUnit.assertEquals(1, p.getEmployeeTimePeriods().size());
		AssertJUnit.assertEquals(0, p.remove(a));
		AssertJUnit.assertEquals(0, p.getEmployeeTimePeriods().size());
	}

	@Test
	public void removeIActivityObjectInstanceOfRoom() {
		Pause p = new Pause();
		IActivityObject a = PowerMockito.mock(Room.class);
		List<Room> rooms = new ArrayList<Room>();
		rooms.add((Room) a);
		p.setRooms(rooms);
		AssertJUnit.assertEquals(0, p.getSchoolclasses().size());
		AssertJUnit.assertEquals(1, p.getRooms().size());
		AssertJUnit.assertEquals(0, p.getEmployeeTimePeriods().size());
		AssertJUnit.assertEquals(0, p.remove(a));
		AssertJUnit.assertEquals(0, p.getRooms().size());
	}

	@Test
	public void removeIActivityObjectInstanceOfSchoolclass() {
		Pause p = new Pause();
		IActivityObject a = PowerMockito.mock(Schoolclass.class);
		p.add((Schoolclass) a);
		AssertJUnit.assertEquals(1, p.getSchoolclasses().size());
		AssertJUnit.assertEquals(0, p.getRooms().size());
		AssertJUnit.assertEquals(0, p.getEmployeeTimePeriods().size());
		AssertJUnit.assertEquals(0, p.remove(a));
		AssertJUnit.assertEquals(0, p.getSchoolclasses().size());
	}

}
