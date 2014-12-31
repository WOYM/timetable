package org.woym.ui.converters;

import static org.junit.Assert.*;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.woym.exceptions.DatasetException;
import org.woym.messages.GenericErrorMessage;
import org.woym.messages.MessageHelper;
import org.woym.objects.Location;
import org.woym.persistence.DataAccess;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest(MessageHelper.class)
public class LocationNameConverterTest {

	@Mock
	private DataAccess dataAccess;

	@Mock
	private MessageHelper messageHelper;

	@Mock
	private FacesContext facesContext;

	@Mock
	private UIComponent uiComponent;

	@InjectMocks
	private LocationNameConverter locationNameConverter;

	@Test
	public void getAsObjectSuccess() throws DatasetException {
		Location l = PowerMockito.mock(Location.class);
		PowerMockito.when(dataAccess.getOneLocation(Mockito.anyString()))
				.thenReturn(l);
		assertEquals(l, locationNameConverter.getAsObject(facesContext,
				uiComponent, "test"));
	}

	@Test(expected = ConverterException.class)
	public void getAsObjectException() throws DatasetException {
		PowerMockito.mockStatic(MessageHelper.class);
		Mockito.doThrow(DatasetException.class).when(dataAccess)
				.getOneLocation(Mockito.anyString());
		PowerMockito.when(
				MessageHelper.generateMessage(
						Mockito.any(GenericErrorMessage.class),
						Mockito.any(FacesMessage.Severity.class))).thenReturn(
				PowerMockito.mock(FacesMessage.class));
		locationNameConverter.getAsObject(facesContext, uiComponent, "test");
	}

	@Test
	public void getAsString() {
		Location l = PowerMockito.mock(Location.class);
		assertEquals(l.getName(),
				locationNameConverter.getAsString(facesContext, uiComponent, l));
	}

	@Test
	public void getAsStringNullValue() {
		assertNull(locationNameConverter.getAsString(facesContext, uiComponent,
				null));
	}

}
