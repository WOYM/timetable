package org.woym.objects;

import static org.junit.Assert.*;

import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

public class EmployeeBlackboxTest {

	@Test
	public void equalsSameObject() {
		Employee e = new Teacher();
		assertTrue(e.equals(e));
	}

	@Test
	public void equalsNull() {
		Employee e = new Teacher();
		assertFalse(e.equals(null));
	}

	@Test
	public void equalsNotInstanceOfEmployee() {
		Employee e = new Teacher();
		assertFalse(e.equals(PowerMockito.mock(Location.class)));
	}

	@Test
	public void equalsEqual() {
		Employee e = new Teacher();
		Employee e1 = new Teacher();
		e.setSymbol("MEY");
		e1.setSymbol("MEY");
		assertTrue(e.equals(e1));
	}

	@Test
	public void equalsDifferentCase() {
		Employee e = new Teacher();
		Employee e1 = new Teacher();
		e.setSymbol("MEY");
		e1.setSymbol("Mey");
		assertTrue(e.equals(e1));
	}

	@Test
	public void equalsNotEqual() {
		Employee e = new Teacher();
		Employee e1 = new Teacher();
		e.setSymbol("MEY");
		e1.setSymbol("MUS");
		assertFalse(e.equals(e1));
	}

	@Test
	public void addChargeableCompensationNotExists() {
		Employee e = new Teacher();
		ChargeableCompensation c = PowerMockito
				.mock(ChargeableCompensation.class);
		assertTrue(e.add(c));
		assertEquals(1, e.getCompensations().size());
		assertEquals(c, e.getCompensations().get(0));
	}

	@Test
	public void addChargeableCompensationExists() {
		Employee e = new Teacher();
		ChargeableCompensation c = PowerMockito
				.mock(ChargeableCompensation.class);
		e.add(c);
		assertFalse(e.add(c));
		assertEquals(1, e.getCompensations().size());
	}

	@Test
	public void removeChargeableCompensationExists() {
		Employee e = new Teacher();
		ChargeableCompensation c = PowerMockito
				.mock(ChargeableCompensation.class);
		e.add(c);
		assertTrue(e.remove(c));
		assertEquals(0, e.getCompensations().size());
	}

	@Test
	public void removeChargeableCompensationNotExists() {
		Employee e = new Teacher();
		ChargeableCompensation c = PowerMockito
				.mock(ChargeableCompensation.class);
		ChargeableCompensation c1 = PowerMockito
				.mock(ChargeableCompensation.class);
		e.add(c);
		assertFalse(e.remove(c1));
		assertEquals(1, e.getCompensations().size());
	}

	@Test
	public void containsChargeableCompensationExists() {
		Employee e = new Teacher();
		ChargeableCompensation c = PowerMockito
				.mock(ChargeableCompensation.class);
		e.add(c);
		assertTrue(e.contains(c));
	}

	@Test
	public void containsChargeableCompensationNotExists() {
		Employee e = new Teacher();
		ChargeableCompensation c = PowerMockito
				.mock(ChargeableCompensation.class);
		assertFalse(e.contains(c));
	}

	@Test
	public void addActivityTypeNotExists() {
		Employee e = new Teacher();
		ActivityType a = PowerMockito.mock(LessonType.class);
		assertTrue(e.add(a));
		assertEquals(1, e.getPossibleActivityTypes().size());
		assertEquals(a, e.getPossibleActivityTypes().get(0));
	}

	@Test
	public void addActivityTypeExists() {
		Employee e = new Teacher();
		ActivityType a = PowerMockito.mock(LessonType.class);
		e.add(a);
		assertFalse(e.add(a));
		assertEquals(1, e.getPossibleActivityTypes().size());
	}

	@Test
	public void removeActivityTypeExists() {
		Employee e = new Teacher();
		ActivityType a = PowerMockito.mock(LessonType.class);
		e.add(a);
		assertTrue(e.remove(a));
		assertEquals(0, e.getPossibleActivityTypes().size());
	}

	@Test
	public void removeActivityTypeNotExists() {
		Employee e = new Teacher();
		ActivityType a = PowerMockito.mock(LessonType.class);
		ActivityType a1 = PowerMockito.mock(LessonType.class);
		e.add(a);
		assertFalse(e.remove(a1));
		assertEquals(1, e.getPossibleActivityTypes().size());
	}

	@Test
	public void containsActivityTypeExists() {
		Employee e = new Teacher();
		ActivityType a = PowerMockito.mock(LessonType.class);
		e.add(a);
		assertTrue(e.contains(a));
	}

	@Test
	public void containsActivityTypeNotExists() {
		Employee e = new Teacher();
		ActivityType a = PowerMockito.mock(LessonType.class);
		assertFalse(e.contains(a));
	}

	@Test
	public void addTimePeriodNotExists() {
		Employee e = new Teacher();
		TimePeriod t = PowerMockito.mock(TimePeriod.class);
		assertTrue(e.add(t));
		assertEquals(1, e.getTimeWishes().size());
		assertEquals(t, e.getTimeWishes().get(0));
	}

	@Test
	public void addTimePeriodExists() {
		Employee e = new Teacher();
		TimePeriod t = PowerMockito.mock(TimePeriod.class);
		e.add(t);
		assertFalse(e.add(t));
		assertEquals(1, e.getTimeWishes().size());
	}

	@Test
	public void removeTimePeriodExists() {
		Employee e = new Teacher();
		TimePeriod t = PowerMockito.mock(TimePeriod.class);
		e.add(t);
		assertTrue(e.remove(t));
		assertEquals(0, e.getTimeWishes().size());
	}

	@Test
	public void removeTimePeriodNotExists() {
		Employee e = new Teacher();
		TimePeriod t = PowerMockito.mock(TimePeriod.class);
		TimePeriod t1 = PowerMockito.mock(TimePeriod.class);
		e.add(t);
		assertFalse(e.remove(t1));
		assertEquals(1, e.getTimeWishes().size());
	}
	
	@Test
	public void containsTimePeriodExists(){
		Employee e = new Teacher();
		TimePeriod t = PowerMockito.mock(TimePeriod.class);
		e.add(t);
		assertTrue(e.contains(t));
	}
	
	@Test
	public void containsTimePeriodNotExists(){
		Employee e = new Teacher();
		TimePeriod t = PowerMockito.mock(TimePeriod.class);
		assertFalse(e.contains(t));
	}

}
