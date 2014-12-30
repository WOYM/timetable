package org.woym.objects;

import static org.junit.Assert.*;

import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

public class EmployeeTimePeriodsBlackboxTest {

	@Test
	public void equalsSameObject() {
		EmployeeTimePeriods e = new EmployeeTimePeriods();
		assertTrue(e.equals(e));
	}

	@Test
	public void equalsNull() {
		EmployeeTimePeriods e = new EmployeeTimePeriods();
		assertFalse(e.equals(null));
	}

	@Test
	public void equalsNotInstanceOfEmployeeTimePeriods() {
		EmployeeTimePeriods e = new EmployeeTimePeriods();
		assertFalse(e.equals(PowerMockito.mock(Location.class)));
	}

	@Test
	public void equalsEqual() {
		EmployeeTimePeriods e = new EmployeeTimePeriods();
		EmployeeTimePeriods e1 = new EmployeeTimePeriods();
		Employee employee = PowerMockito.mock(Teacher.class);
		e.setEmployee(employee);
		e1.setEmployee(employee);
		assertTrue(e.equals(e1));
	}

	@Test
	public void equalsNotEqual() {
		EmployeeTimePeriods e = new EmployeeTimePeriods();
		EmployeeTimePeriods e1 = new EmployeeTimePeriods();
		e.setEmployee(PowerMockito.mock(Teacher.class));
		e1.setEmployee(PowerMockito.mock(Teacher.class));
		assertFalse(e.equals(e1));
	}

	@Test
	public void addTimePeriodNotExists() {
		EmployeeTimePeriods e = new EmployeeTimePeriods();
		TimePeriod t = PowerMockito.mock(TimePeriod.class);
		assertTrue(e.add(t));
		assertEquals(1, e.getTimePeriods().size());
		assertEquals(t, e.getTimePeriods().get(0));
	}

	@Test
	public void addTimePeriodExists() {
		EmployeeTimePeriods e = new EmployeeTimePeriods();
		TimePeriod t = PowerMockito.mock(TimePeriod.class);
		e.add(t);
		assertFalse(e.add(t));
		assertEquals(1, e.getTimePeriods().size());
	}

	@Test
	public void removeTimePeriodExists() {
		EmployeeTimePeriods e = new EmployeeTimePeriods();
		TimePeriod t = PowerMockito.mock(TimePeriod.class);
		e.add(t);
		assertTrue(e.remove(t));
		assertEquals(0, e.getTimePeriods().size());
	}

	@Test
	public void removeTimePeriodNotExists() {
		EmployeeTimePeriods e = new EmployeeTimePeriods();
		TimePeriod t = PowerMockito.mock(TimePeriod.class);
		TimePeriod t1 = PowerMockito.mock(TimePeriod.class);
		e.add(t);
		assertFalse(e.remove(t1));
		assertEquals(1, e.getTimePeriods().size());
	}

	@Test
	public void containsTimePeriodExists() {
		EmployeeTimePeriods e = new EmployeeTimePeriods();
		TimePeriod t = PowerMockito.mock(TimePeriod.class);
		e.add(t);
		assertTrue(e.contains(t));
	}

	@Test
	public void containsTimePeriodNotExists() {
		EmployeeTimePeriods e = new EmployeeTimePeriods();
		TimePeriod t = PowerMockito.mock(TimePeriod.class);
		assertFalse(e.contains(t));
	}
}
