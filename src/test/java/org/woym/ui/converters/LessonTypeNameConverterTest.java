package org.woym.ui.converters;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;
import org.woym.common.exceptions.DatasetException;
import org.woym.common.messages.GenericErrorMessage;
import org.woym.common.messages.MessageHelper;
import org.woym.common.objects.ActivityType;
import org.woym.common.objects.LessonType;
import org.woym.persistence.DataAccess;

@Test(groups = "unit")
@PowerMockIgnore("javax.management.*")
@PrepareForTest(MessageHelper.class)
public class LessonTypeNameConverterTest extends PowerMockTestCase {

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
		AssertJUnit.assertEquals(a, lessonTypeNameConverter.getAsObject(
				facesContext, uiComponent, "testString"));
	}

	@Test
	public void getAsObjectException() throws DatasetException {
		PowerMockito.mockStatic(MessageHelper.class);
		Mockito.doThrow(DatasetException.class).when(dataAccess)
				.getOneActivityType(Mockito.anyString());
		PowerMockito.when(
				MessageHelper.generateMessage(
						Mockito.any(GenericErrorMessage.class),
						Mockito.any(FacesMessage.Severity.class))).thenReturn(
				PowerMockito.mock(FacesMessage.class));
		try {
			lessonTypeNameConverter.getAsObject(facesContext, uiComponent,
					"test");
		} catch (ConverterException e) {
			return;
		}
		Assert.fail();
	}

	@Test
	public void getAsString() {
		ActivityType a = PowerMockito.mock(LessonType.class);
		AssertJUnit.assertEquals(a.getName(), lessonTypeNameConverter
				.getAsString(facesContext, uiComponent, a));
	}

}
