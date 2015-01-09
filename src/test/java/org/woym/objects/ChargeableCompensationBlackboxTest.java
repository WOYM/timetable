package org.woym.objects;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Test(groups = "unit")
public class ChargeableCompensationBlackboxTest extends PowerMockTestCase {

	ChargeableCompensation c;

	@BeforeMethod
	public void init() {
		c = new ChargeableCompensation();
		c.setDescription("Test");
		c.setValue(10);
	}

	@Test
	public void equalsSameObject() {
		AssertJUnit.assertTrue(c.equals(c));
	}

	@Test
	public void equalsNull() {
		AssertJUnit.assertFalse(c.equals(null));
	}

	@Test
	public void equalsNotInstanceOfChargeableCompensation() {
		AssertJUnit.assertFalse(c.equals(PowerMockito.mock(Location.class)));
	}

	@Test
	public void equalsEquals() {
		ChargeableCompensation c1 = new ChargeableCompensation();
		c1.setDescription("Test");
		c1.setValue(10);
		AssertJUnit.assertTrue(c.equals(c1));
	}

	@Test
	public void equalsNotEqual() {
		ChargeableCompensation c1 = new ChargeableCompensation();
		c1.setDescription("Test 1");
		c1.setValue(10);
		AssertJUnit.assertFalse(c.equals(c1));
	}

	@Test
	public void equalsNotEqual2() {
		ChargeableCompensation c1 = new ChargeableCompensation();
		c1.setDescription("Test");
		c1.setValue(11);
		AssertJUnit.assertFalse(c.equals(c1));
	}

	@Test
	public void equalsNotEqual3() {
		ChargeableCompensation c1 = new ChargeableCompensation();
		c1.setDescription("Test 1");
		c1.setValue(11);
		AssertJUnit.assertFalse(c.equals(c1));
	}

}
