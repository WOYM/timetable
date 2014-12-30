package org.woym.ui.converters;

import static org.junit.Assert.assertEquals;

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
import org.woym.objects.ActivityType;
import org.woym.objects.LessonType;
import org.woym.persistence.DataAccess;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest(MessageHelper.class)
public class LessonTypeNameConverterTest {

	@Mock
	private DataAccess dataAccess;

	@Mock
	private MessageHelper messageHelper;

	@Mock
	private FacesContext facesContext;

	@Mock
	private UIComponent uiComponent;

	@InjectMocks
	private LessonTypeNameConverter lessonTypeNameConverter;

	@Test
	public void getAsObjectSuccess() throws DatasetException {
		ActivityType a = PowerMockito.mock(LessonType.class);
		PowerMockito.when(dataAccess.getOneActivityType(Mockito.anyString()))
				.thenReturn(a);
		assertEquals(a, lessonTypeNameConverter.getAsObject(facesContext,
				uiComponent, "testString"));
	}

	@Test(expected = ConverterException.class)
	public void getAsObjectException() throws DatasetException {
		PowerMockito.mockStatic(MessageHelper.class);
		Mockito.doThrow(DatasetException.class).when(dataAccess)
				.getOneActivityType(Mockito.anyString());
		PowerMockito.when(
				MessageHelper.generateMessage(
						Mockito.any(GenericErrorMessage.class),
						Mockito.any(FacesMessage.Severity.class))).thenReturn(
				PowerMockito.mock(FacesMessage.class));
		lessonTypeNameConverter.getAsObject(facesContext, uiComponent, "test");
	}
	
	@Test
	public void getAsString(){
		ActivityType a = PowerMockito.mock(LessonType.class);
		assertEquals(a.getName(), lessonTypeNameConverter.getAsString(facesContext, uiComponent, a));
	}

}
