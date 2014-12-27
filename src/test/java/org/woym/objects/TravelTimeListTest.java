package org.woym.objects;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.woym.persistence.DataAccess;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({ DataAccess.class, TravelTimeList.class })
public class TravelTimeListTest {

	@Mock
	private DataAccess dataAccess;

	@InjectMocks
	private TravelTimeList list;

	@Before
	public void init() {
		PowerMockito.suppress(PowerMockito.constructor(DataAccess.class));
		PowerMockito.mockStatic(DataAccess.class);
		PowerMockito.when(DataAccess.getInstance()).thenReturn(dataAccess);
	}

	// Test der getInstance-Methode, wenn INSTANCE null ist und DataAccess auch
	// keine TravelTimeList zurückgibt
	@Test
	public void getInstanceInstanceNullDataAccessReturnNull() throws Exception {
		PowerMockito.when(dataAccess.getTravelTimeList()).thenReturn(null);
		assertNotNull(TravelTimeList.getInstance());
	}

	@Test
	public void getInstanceInstancceNullDataAccessReturnNotNull()
			throws Exception {
		PowerMockito.when(dataAccess.getTravelTimeList()).thenReturn(list);
		assertNotNull(TravelTimeList.getInstance());
	}

}
