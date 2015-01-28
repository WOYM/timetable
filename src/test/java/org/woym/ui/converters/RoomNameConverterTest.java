package org.woym.ui.converters;

import static org.testng.AssertJUnit.assertEquals;

import java.util.ArrayList;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.annotations.Test;
import org.woym.common.exceptions.DatasetException;
import org.woym.common.objects.Location;
import org.woym.common.objects.Room;
import org.woym.persistence.DataAccess;

@Test(groups = "unit")
public class RoomNameConverterTest extends PowerMockTestCase {

	@Mock
	private DataAccess dataAccess;

	@Mock
	private FacesContext facesContext;

	@Mock
	private UIComponent uiComponent;

	@InjectMocks
	private RoomNameConverter roomNameConverter;

	@Test
	public void getAsObjectSuccess() throws DatasetException {
		Room r = PowerMockito.mock(Room.class);
		PowerMockito.when(
				dataAccess.getById(Mockito.eq(Room.class), Mockito.anyLong()))
				.thenReturn(r);
		assertEquals(r,
				roomNameConverter.getAsObject(facesContext, uiComponent, "1"));
	}

	@Test
	public void getAsObjectNumberFormatException() {
		assertEquals(null, roomNameConverter.getAsObject(facesContext,
				uiComponent, "test"));
	}

	@Test
	public void getAsObjectDatasetException() throws DatasetException {
		Mockito.doThrow(DatasetException.class).when(dataAccess)
				.getById(Mockito.eq(Room.class), Mockito.anyLong());
		assertEquals(null,
				roomNameConverter.getAsObject(facesContext, uiComponent, "1"));
	}

	@Test
	public void getAsStringSuccess() throws DatasetException {
		Room r = PowerMockito.mock(Room.class);
		ArrayList<Room> rooms = new ArrayList<Room>();
		rooms.add(r);

		Location l = PowerMockito.mock(Location.class);
		Location l1 = PowerMockito.mock(Location.class);
		ArrayList<Location> locations = new ArrayList<Location>();
		locations.add(l);
		locations.add(l1);

		PowerMockito.when(l1.getRooms()).thenReturn(rooms);
		PowerMockito.when(dataAccess.getAllLocations()).thenReturn(locations);
		assertEquals(r.getId().toString(),
				roomNameConverter.getAsString(facesContext, uiComponent, r));
	}

	@Test
	public void getAsStringDatasetException() throws DatasetException {
		Room r = PowerMockito.mock(Room.class);
		Mockito.doThrow(DatasetException.class).when(dataAccess)
				.getAllLocations();
		assertEquals("",
				roomNameConverter.getAsString(facesContext, uiComponent, r));
	}

	@Test
	public void getAsStringRoomNotMatching() throws DatasetException {
		Room r = PowerMockito.mock(Room.class);
		Room r1 = PowerMockito.mock(Room.class);
		ArrayList<Room> rooms = new ArrayList<Room>();
		rooms.add(r);

		Location l = PowerMockito.mock(Location.class);
		Location l1 = PowerMockito.mock(Location.class);
		ArrayList<Location> locations = new ArrayList<Location>();
		locations.add(l);
		locations.add(l1);

		PowerMockito.when(l1.getRooms()).thenReturn(rooms);
		PowerMockito.when(dataAccess.getAllLocations()).thenReturn(locations);
		assertEquals("",
				roomNameConverter.getAsString(facesContext, uiComponent, r1));
	}
}
