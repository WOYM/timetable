package org.woym.objects;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

public class ChargeableCompensationBlackboxTest {

	ChargeableCompensation c;

	@Before
	public void init() {
		c = new ChargeableCompensation();
		c.setDescription("Test");
		c.setValue(10);
	}

	@Test
	public void equalsSameObject() {
		assertTrue(c.equals(c));
	}

	@Test
	public void equalsNull() {
		assertFalse(c.equals(null));
	}

	@Test
	public void equalsNotInstanceOfChargeableCompensation() {
		assertFalse(c.equals(PowerMockito.mock(Location.class)));
	}

	@Test
	public void equalsEquals() {
		ChargeableCompensation c1 = new ChargeableCompensation();
		c1.setDescription("Test");
		c1.setValue(10);
		assertTrue(c.equals(c1));
	}

	@Test
	public void equalsNotEqual() {
		ChargeableCompensation c1 = new ChargeableCompensation();
		c1.setDescription("Test 1");
		c1.setValue(10);
		assertFalse(c.equals(c1));
	}

	@Test
	public void equalsNotEqual2() {
		ChargeableCompensation c1 = new ChargeableCompensation();
		c1.setDescription("Test");
		c1.setValue(11);
		assertFalse(c.equals(c1));
	}

	@Test
	public void equalsNotEqual3() {
		ChargeableCompensation c1 = new ChargeableCompensation();
		c1.setDescription("Test 1");
		c1.setValue(11);
		assertFalse(c.equals(c1));
	}

}
