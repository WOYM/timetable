package org.woym.common.objects;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;
import org.woym.common.objects.Employee;
import org.woym.common.objects.EmployeeTimePeriods;
import org.woym.common.objects.Location;
import org.woym.common.objects.Teacher;
import org.woym.common.objects.TimePeriod;

@Test(groups = "unit")
public class EmployeeTimePeriodsBlackboxTest extends PowerMockTestCase {

	@Test
	public void equalsSameObject() {
		EmployeeTimePeriods e = new EmployeeTimePeriods();
		AssertJUnit.assertTrue(e.equals(e));
	}

	@Test
	public void equalsNull() {
		EmployeeTimePeriods e = new EmployeeTimePeriods();
		AssertJUnit.assertFalse(e.equals(null));
	}

	@Test
	public void equalsNotInstanceOfEmployeeTimePeriods() {
		EmployeeTimePeriods e = new EmployeeTimePeriods();
		AssertJUnit.assertFalse(e.equals(PowerMockito.mock(Location.class)));
	}

	@Test
	public void equalsEqual() {
		EmployeeTimePeriods e = new EmployeeTimePeriods();
		EmployeeTimePeriods e1 = new EmployeeTimePeriods();
		Employee employee = PowerMockito.mock(Teacher.class);
		e.setEmployee(employee);
		e1.setEmployee(employee);
		AssertJUnit.assertTrue(e.equals(e1));
	}

	@Test
	public void equalsNotEqual() {
		EmployeeTimePeriods e = new EmployeeTimePeriods();
		EmployeeTimePeriods e1 = new EmployeeTimePeriods();
		e.setEmployee(PowerMockito.mock(Teacher.class));
		e1.setEmployee(PowerMockito.mock(Teacher.class));
		AssertJUnit.assertFalse(e.equals(e1));
	}

	@Test
	public void addTimePeriodNotExists() {
		EmployeeTimePeriods e = new EmployeeTimePeriods();
		TimePeriod t = PowerMockito.mock(TimePeriod.class);
		AssertJUnit.assertTrue(e.add(t));
		AssertJUnit.assertEquals(1, e.getTimePeriods().size());
		AssertJUnit.assertEquals(t, e.getTimePeriods().get(0));
	}

	@Test
	public void addTimePeriodExists() {
		EmployeeTimePeriods e = new EmployeeTimePeriods();
		TimePeriod t = PowerMockito.mock(TimePeriod.class);
		e.add(t);
		AssertJUnit.assertFalse(e.add(t));
		AssertJUnit.assertEquals(1, e.getTimePeriods().size());
	}

	@Test
	public void removeTimePeriodExists() {
		EmployeeTimePeriods e = new EmployeeTimePeriods();
		TimePeriod t = PowerMockito.mock(TimePeriod.class);
		e.add(t);
		AssertJUnit.assertTrue(e.remove(t));
		AssertJUnit.assertEquals(0, e.getTimePeriods().size());
	}

	@Test
	public void removeTimePeriodNotExists() {
		EmployeeTimePeriods e = new EmployeeTimePeriods();
		TimePeriod t = PowerMockito.mock(TimePeriod.class);
		TimePeriod t1 = PowerMockito.mock(TimePeriod.class);
		e.add(t);
		AssertJUnit.assertFalse(e.remove(t1));
		AssertJUnit.assertEquals(1, e.getTimePeriods().size());
	}

	@Test
	public void containsTimePeriodExists() {
		EmployeeTimePeriods e = new EmployeeTimePeriods();
		TimePeriod t = PowerMockito.mock(TimePeriod.class);
		e.add(t);
		AssertJUnit.assertTrue(e.contains(t));
	}

	@Test
	public void containsTimePeriodNotExists() {
		EmployeeTimePeriods e = new EmployeeTimePeriods();
		TimePeriod t = PowerMockito.mock(TimePeriod.class);
		AssertJUnit.assertFalse(e.contains(t));
	}
}
