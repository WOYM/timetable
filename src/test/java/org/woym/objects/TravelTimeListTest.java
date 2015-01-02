package org.woym.objects;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.lang.reflect.Field;
import java.util.List;

import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.woym.exceptions.DatasetException;
import org.woym.objects.TravelTimeList.Edge;
import org.woym.persistence.DataAccess;

@PowerMockIgnore("javax.management.*")
@PrepareForTest({ DataAccess.class, TravelTimeList.class })
public class TravelTimeListTest extends PowerMockTestCase {

	@Mock
	private DataAccess dataAccess;

	@Mock
	private TravelTimeList list;

	@BeforeMethod
	public void init() throws Exception {
		PowerMockito.mockStatic(DataAccess.class);
		PowerMockito.when(DataAccess.getInstance()).thenReturn(dataAccess);
		Field field = PowerMockito.field(TravelTimeList.class, "INSTANCE");
		field.set(TravelTimeList.class, null);
		PowerMockito.when(dataAccess.getTravelTimeList()).thenReturn(null);
	}

	// Test der getInstance-Methode, wenn INSTANCE null ist und DataAccess auch
	// keine TravelTimeList zurückgibt
	@Test
	public void getInstanceInstanceNullDataAccessReturnNull() {
		assertNotNull(TravelTimeList.getInstance());
	}

	@Test
	public void getInstanceInstanceNullDataAccessReturnNotNull()
			throws DatasetException {
		PowerMockito.when(dataAccess.getTravelTimeList()).thenReturn(list);
		assertEquals(list, TravelTimeList.getInstance());
	}

	@Test
	public void addEdgeNotExists() {
		Edge e = PowerMockito.mock(Edge.class);
		Edge e1 = PowerMockito.mock(Edge.class);

		assertTrue(TravelTimeList.getInstance().add(e));
		assertEquals(1, TravelTimeList.getInstance().getEdges().size());
		assertTrue(TravelTimeList.getInstance().add(e1));
		assertEquals(2, TravelTimeList.getInstance().getEdges().size());
		assertEquals(e, TravelTimeList.getInstance().getEdges().get(0));
		assertEquals(e1, TravelTimeList.getInstance().getEdges().get(1));
	}

	@Test
	public void addEdgeAlreadyExists() {
		Edge e = PowerMockito.mock(Edge.class);
		TravelTimeList.getInstance().add(e);
		assertFalse(TravelTimeList.getInstance().add(e));
		assertEquals(1, TravelTimeList.getInstance().getEdges().size());
	}

	@Test
	public void removeEdgeExists() {
		Edge e = PowerMockito.mock(Edge.class);
		TravelTimeList.getInstance().add(e);
		assertTrue(TravelTimeList.getInstance().remove(e));
		assertTrue(TravelTimeList.getInstance().getEdges().isEmpty());
	}

	@Test
	public void removeEdgeNotExists() {
		Edge e = PowerMockito.mock(Edge.class);
		Edge e1 = PowerMockito.mock(Edge.class);
		TravelTimeList.getInstance().add(e);
		assertFalse(TravelTimeList.getInstance().remove(e1));
		assertEquals(1, TravelTimeList.getInstance().getEdges().size());
	}

	/**
	 * {@linkplain IllegalArgumentException} bei ungültigen Werten durch
	 * {@linkplain EdgeBlackboxTest} abgedeckt
	 */
	@Test
	public void addValidParameters() {
		Location l = PowerMockito.mock(Location.class);
		Location l1 = PowerMockito.mock(Location.class);
		assertTrue(TravelTimeList.getInstance().add(l, l1, 1));
		assertEquals(1, TravelTimeList.getInstance().getEdges().size());
	}

	@Test
	public void removeValidParameters() {
		Location l = PowerMockito.mock(Location.class);
		Location l1 = PowerMockito.mock(Location.class);
		TravelTimeList.getInstance().add(l, l1, 1);
		assertTrue(TravelTimeList.getInstance().remove(l, l1));
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void getTravelTimesLocationNull() {
		TravelTimeList.getInstance().getTravelTimes(null);
	}

	@Test
	public void getTravelTimesSuccess() {
		Location l = PowerMockito.mock(Location.class);
		Location l1 = PowerMockito.mock(Location.class);
		Location l2 = PowerMockito.mock(Location.class);
		Location l3 = PowerMockito.mock(Location.class);
		TravelTimeList tList = TravelTimeList.getInstance();
		tList.add(l, l1, 10);
		tList.add(l, l2, 20);
		tList.add(l1, l2, 15);
		tList.add(l2, l3, 30);
		List<Edge> result = tList.getTravelTimes(l);
		assertNotNull(result);
		assertEquals(2, result.size());
		assertTrue(result.get(0).contains(l));
		assertTrue(result.get(1).contains(l));
	}

}
