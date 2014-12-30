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
import org.woym.objects.Teacher;
import org.woym.persistence.DataAccess;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest(MessageHelper.class)
public class TeacherNameConverterTest {

	@Mock
	private DataAccess dataAccess;

	@Mock
	private MessageHelper messageHelper;

	@Mock
	private FacesContext facesContext;

	@Mock
	private UIComponent uiComponent;

	@InjectMocks
	private TeacherNameConverter teacherNameConverter;

	@Test
	public void getAsObjectSuccess() throws DatasetException {
		Teacher t = PowerMockito.mock(Teacher.class);
		PowerMockito.when(dataAccess.getOneEmployee(Mockito.anyString()))
				.thenReturn(t);
		assertEquals(t, teacherNameConverter.getAsObject(facesContext,
				uiComponent, "test"));
	}

	@Test(expected = ConverterException.class)
	public void getAsObjectDatasetException() throws DatasetException {
		PowerMockito.mockStatic(MessageHelper.class);
		Mockito.doThrow(DatasetException.class).when(dataAccess)
				.getOneEmployee(Mockito.anyString());
		PowerMockito.when(
				MessageHelper.generateMessage(
						Mockito.any(GenericErrorMessage.class),
						Mockito.any(FacesMessage.Severity.class))).thenReturn(
				PowerMockito.mock(FacesMessage.class));
		teacherNameConverter.getAsObject(facesContext, uiComponent, "test");
	}

	@Test
	public void getAsStringNullValue() {
		assertEquals(null, teacherNameConverter.getAsString(facesContext,
				uiComponent, null));
	}

	@Test
	public void getAsStringSucess() {
		Teacher t = PowerMockito.mock(Teacher.class);
		assertEquals(t.getName(),
				teacherNameConverter.getAsString(facesContext, uiComponent, t));
	}
}
