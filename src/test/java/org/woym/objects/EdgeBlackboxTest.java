package org.woym.objects;

import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;
import org.woym.objects.TravelTimeList.Edge;

public class EdgeBlackboxTest extends PowerMockTestCase {

	@Mock
	private Location l1;

	@Mock
	private Location l2;

	// Test des Konstruktors bei Übergabe desselben Standortes für beide
	// Parameter. IllegalArgumentException wird erwartet
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void constructorEqualLocations() {
		new Edge(l1, l1, 10);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void constructorFirstParameterNull() {
		new Edge(null, l1, 10);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void constructorSecondParameterNull() {
		new Edge(l1, null, 10);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void constructorDistanceTooSmall() {
		new Edge(l1, l2, 0);
	}

	// Test der contains-Methode mit enthaltenen Standorten
	@Test
	public void containsLocationExists() {
		Edge e = new Edge(l1, l2, 10);
		AssertJUnit.assertTrue(e.contains(l1));
		AssertJUnit.assertTrue(e.contains(l2));
	}

	// Test der contains-Methode mit nicht enthaltenen Standorten
	@Test
	public void containsLocationNotExists() {
		Edge e = new Edge(l1, l2, 10);
		AssertJUnit.assertFalse(e.contains(PowerMockito.mock(Location.class)));
	}

	@Test
	public void equalsNullFalse() {
		Edge e = new Edge(l1, l2, 10);
		AssertJUnit.assertFalse(e.equals(null));
	}

	@Test
	public void equalsNoEdgeFalse() {
		Edge e = new Edge(l1, l2, 10);
		AssertJUnit.assertFalse(e.equals(PowerMockito.mock(Location.class)));
	}

	@Test
	public void equalsSameObjectTrue() {
		Edge e = new Edge(l1, l2, 10);
		AssertJUnit.assertTrue(e.equals(e));
	}

	// Equals mit zwei Kanten mit genau gleichen Parametern
	@Test
	public void equalsAllParametersEqualTrue() {
		Edge e1 = new Edge(l1, l2, 10);
		Edge e2 = new Edge(l1, l2, 10);
		AssertJUnit.assertTrue(e1.equals(e2));
	}

	// Equals mit zwei Kanten mit verschiedenen Distanzen, aber gleichen
	// Standorten
	@Test
	public void equalsDifferentDistanceTrue() {
		Edge e1 = new Edge(l1, l2, 10);
		Edge e2 = new Edge(l1, l2, 15);
		AssertJUnit.assertTrue(e1.equals(e2));
	}

	// Equals mit zwei Kanten mit selben Standorten aber in verschiedener
	// Reihenfolge
	@Test
	public void equalsLocationsSwappedTrue() {
		Edge e1 = new Edge(l1, l2, 10);
		Edge e2 = new Edge(l2, l1, 15);
		AssertJUnit.assertTrue(e1.equals(e2));
	}

	@Test
	public void equalsDifferentEdgesFalse() {
		Edge e1 = new Edge(l1, l2, 10);
		Edge e2 = new Edge(l2, PowerMockito.mock(Location.class), 15);
		AssertJUnit.assertFalse(e1.equals(e2));
	}

	// Test von getAdjacentLocation mit existierendem Standort
	@Test
	public void getAdjacentLocationLocationExists() {
		Edge e = new Edge(l1, l2, 10);
		AssertJUnit.assertEquals(l1, e.getAdjacentLocation(l2));
		AssertJUnit.assertEquals(l2, e.getAdjacentLocation(l1));
	}

	// Test von getAdjacentLocation mit nicht enthaltenem Standort
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void getAdjacentLocationLocationNotExists() {
		Edge e = new Edge(l1, l2, 10);
		e.getAdjacentLocation(PowerMockito.mock(Location.class));
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void getAdjacentLocationNull() {
		Edge e = new Edge(l1, l2, 10);
		e.getAdjacentLocation(null);
	}

}
