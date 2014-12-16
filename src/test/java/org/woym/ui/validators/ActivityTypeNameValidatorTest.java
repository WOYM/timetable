package org.woym.ui.validators;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.woym.exceptions.DatasetException;
import org.woym.objects.ActivityType;
import org.woym.persistence.DataAccess;

@RunWith(PowerMockRunner.class)
public class ActivityTypeNameValidatorTest {

	@Mock
	DataAccess dataAccess;

	@Mock
	FacesContext facesContext;

	@Mock
	ELContext elContext;

	@Mock
	UIComponent uiComponent;

	@Mock
	ValueExpression valueExpression;

	@InjectMocks
	ActivityTypeNameValidator activityTypeNameValidator;

	/**
	 * Test for a name that is in use.
	 * 
	 * @throws DatasetException
	 */
	@Test(expected = ValidatorException.class)
	public void testSymbolInuse() throws DatasetException {

		ActivityType a1 = Mockito.mock(ActivityType.class);
		ActivityType a2 = Mockito.mock(ActivityType.class);

		PowerMockito.when(dataAccess.getOneActivityType(Mockito.anyString()))
				.thenReturn(a1);
		PowerMockito.when(facesContext.getELContext()).thenReturn(elContext);
		PowerMockito.when(uiComponent.getValueExpression(Mockito.anyString()))
				.thenReturn(valueExpression);
		PowerMockito.when(valueExpression.getValue(elContext)).thenReturn(a2);

		activityTypeNameValidator.validate(facesContext, uiComponent,
				"testName");
	}

	/**
	 * Test for a name that is not in use.
	 * 
	 * @throws DatasetException
	 */
	@Test
	public void testSymbolNotInuse() throws DatasetException {

		ActivityType a1 = Mockito.mock(ActivityType.class);

		PowerMockito.when(dataAccess.getOneActivityType(Mockito.anyString()))
				.thenReturn(null);
		PowerMockito.when(facesContext.getELContext()).thenReturn(elContext);
		PowerMockito.when(uiComponent.getValueExpression(Mockito.anyString()))
				.thenReturn(valueExpression);
		PowerMockito.when(valueExpression.getValue(elContext)).thenReturn(a1);

		activityTypeNameValidator.validate(facesContext, uiComponent,
				"testName");
	}

	/**
	 * Test for a name that is in use, but belongs to the current object that
	 * shall be edited or created.
	 * 
	 * @throws DatasetException
	 */
	@Test
	public void testSymbolOnSameObject() throws DatasetException {

		ActivityType a1 = Mockito.mock(ActivityType.class);

		PowerMockito.when(dataAccess.getOneActivityType(Mockito.anyString()))
				.thenReturn(a1);
		PowerMockito.when(facesContext.getELContext()).thenReturn(elContext);
		PowerMockito.when(uiComponent.getValueExpression(Mockito.anyString()))
				.thenReturn(valueExpression);
		PowerMockito.when(valueExpression.getValue(elContext)).thenReturn(a1);

		activityTypeNameValidator.validate(facesContext, uiComponent,
				"testName");
	}

}
