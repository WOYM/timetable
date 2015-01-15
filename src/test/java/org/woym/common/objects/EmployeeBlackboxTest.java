package org.woym.common.objects;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;
import org.woym.common.objects.ActivityType;
import org.woym.common.objects.ChargeableCompensation;
import org.woym.common.objects.Employee;
import org.woym.common.objects.LessonType;
import org.woym.common.objects.Location;
import org.woym.common.objects.Teacher;
import org.woym.common.objects.TimePeriod;

@Test(groups = "unit")
public class EmployeeBlackboxTest extends PowerMockTestCase {

	@Test
	public void equalsSameObject() {
		Employee e = new Teacher();
		AssertJUnit.assertTrue(e.equals(e));
	}

	@Test
	public void equalsNull() {
		Employee e = new Teacher();
		AssertJUnit.assertFalse(e.equals(null));
	}

	@Test
	public void equalsNotInstanceOfEmployee() {
		Employee e = new Teacher();
		AssertJUnit.assertFalse(e.equals(PowerMockito.mock(Location.class)));
	}

	@Test
	public void equalsEqual() {
		Employee e = new Teacher();
		Employee e1 = new Teacher();
		e.setSymbol("MEY");
		e1.setSymbol("MEY");
		AssertJUnit.assertTrue(e.equals(e1));
	}

	@Test
	public void equalsDifferentCase() {
		Employee e = new Teacher();
		Employee e1 = new Teacher();
		e.setSymbol("MEY");
		e1.setSymbol("Mey");
		AssertJUnit.assertTrue(e.equals(e1));
	}

	@Test
	public void equalsNotEqual() {
		Employee e = new Teacher();
		Employee e1 = new Teacher();
		e.setSymbol("MEY");
		e1.setSymbol("MUS");
		AssertJUnit.assertFalse(e.equals(e1));
	}

	@Test
	public void addChargeableCompensationNotExists() {
		Employee e = new Teacher();
		ChargeableCompensation c = PowerMockito
				.mock(ChargeableCompensation.class);
		AssertJUnit.assertTrue(e.add(c));
		AssertJUnit.assertEquals(1, e.getCompensations().size());
		AssertJUnit.assertEquals(c, e.getCompensations().get(0));
	}

	@Test
	public void addChargeableCompensationExists() {
		Employee e = new Teacher();
		ChargeableCompensation c = PowerMockito
				.mock(ChargeableCompensation.class);
		e.add(c);
		AssertJUnit.assertFalse(e.add(c));
		AssertJUnit.assertEquals(1, e.getCompensations().size());
	}

	@Test
	public void removeChargeableCompensationExists() {
		Employee e = new Teacher();
		ChargeableCompensation c = PowerMockito
				.mock(ChargeableCompensation.class);
		e.add(c);
		AssertJUnit.assertTrue(e.remove(c));
		AssertJUnit.assertEquals(0, e.getCompensations().size());
	}

	@Test
	public void removeChargeableCompensationNotExists() {
		Employee e = new Teacher();
		ChargeableCompensation c = PowerMockito
				.mock(ChargeableCompensation.class);
		ChargeableCompensation c1 = PowerMockito
				.mock(ChargeableCompensation.class);
		e.add(c);
		AssertJUnit.assertFalse(e.remove(c1));
		AssertJUnit.assertEquals(1, e.getCompensations().size());
	}

	@Test
	public void containsChargeableCompensationExists() {
		Employee e = new Teacher();
		ChargeableCompensation c = PowerMockito
				.mock(ChargeableCompensation.class);
		e.add(c);
		AssertJUnit.assertTrue(e.contains(c));
	}

	@Test
	public void containsChargeableCompensationNotExists() {
		Employee e = new Teacher();
		ChargeableCompensation c = PowerMockito
				.mock(ChargeableCompensation.class);
		AssertJUnit.assertFalse(e.contains(c));
	}

	@Test
	public void addActivityTypeNotExists() {
		Employee e = new Teacher();
		ActivityType a = PowerMockito.mock(LessonType.class);
		AssertJUnit.assertTrue(e.add(a));
		AssertJUnit.assertEquals(1, e.getPossibleActivityTypes().size());
		AssertJUnit.assertEquals(a, e.getPossibleActivityTypes().get(0));
	}

	@Test
	public void addActivityTypeExists() {
		Employee e = new Teacher();
		ActivityType a = PowerMockito.mock(LessonType.class);
		e.add(a);
		AssertJUnit.assertFalse(e.add(a));
		AssertJUnit.assertEquals(1, e.getPossibleActivityTypes().size());
	}

	@Test
	public void removeActivityTypeExists() {
		Employee e = new Teacher();
		ActivityType a = PowerMockito.mock(LessonType.class);
		e.add(a);
		AssertJUnit.assertTrue(e.remove(a));
		AssertJUnit.assertEquals(0, e.getPossibleActivityTypes().size());
	}

	@Test
	public void removeActivityTypeNotExists() {
		Employee e = new Teacher();
		ActivityType a = PowerMockito.mock(LessonType.class);
		ActivityType a1 = PowerMockito.mock(LessonType.class);
		e.add(a);
		AssertJUnit.assertFalse(e.remove(a1));
		AssertJUnit.assertEquals(1, e.getPossibleActivityTypes().size());
	}

	@Test
	public void containsActivityTypeExists() {
		Employee e = new Teacher();
		ActivityType a = PowerMockito.mock(LessonType.class);
		e.add(a);
		AssertJUnit.assertTrue(e.contains(a));
	}

	@Test
	public void containsActivityTypeNotExists() {
		Employee e = new Teacher();
		ActivityType a = PowerMockito.mock(LessonType.class);
		AssertJUnit.assertFalse(e.contains(a));
	}

	@Test
	public void addTimePeriodNotExists() {
		Employee e = new Teacher();
		TimePeriod t = PowerMockito.mock(TimePeriod.class);
		AssertJUnit.assertTrue(e.add(t));
		AssertJUnit.assertEquals(1, e.getTimeWishes().size());
		AssertJUnit.assertEquals(t, e.getTimeWishes().get(0));
	}

	@Test
	public void addTimePeriodExists() {
		Employee e = new Teacher();
		TimePeriod t = PowerMockito.mock(TimePeriod.class);
		e.add(t);
		AssertJUnit.assertFalse(e.add(t));
		AssertJUnit.assertEquals(1, e.getTimeWishes().size());
	}

	@Test
	public void removeTimePeriodExists() {
		Employee e = new Teacher();
		TimePeriod t = PowerMockito.mock(TimePeriod.class);
		e.add(t);
		AssertJUnit.assertTrue(e.remove(t));
		AssertJUnit.assertEquals(0, e.getTimeWishes().size());
	}

	@Test
	public void removeTimePeriodNotExists() {
		Employee e = new Teacher();
		TimePeriod t = PowerMockito.mock(TimePeriod.class);
		TimePeriod t1 = PowerMockito.mock(TimePeriod.class);
		e.add(t);
		AssertJUnit.assertFalse(e.remove(t1));
		AssertJUnit.assertEquals(1, e.getTimeWishes().size());
	}

	@Test
	public void containsTimePeriodExists() {
		Employee e = new Teacher();
		TimePeriod t = PowerMockito.mock(TimePeriod.class);
		e.add(t);
		AssertJUnit.assertTrue(e.contains(t));
	}

	@Test
	public void containsTimePeriodNotExists() {
		Employee e = new Teacher();
		TimePeriod t = PowerMockito.mock(TimePeriod.class);
		AssertJUnit.assertFalse(e.contains(t));
	}

}
