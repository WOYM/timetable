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
import org.woym.objects.Teacher;
import org.woym.persistence.DataAccess;

@RunWith(PowerMockRunner.class)
public class SymbolValidatorTest {

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
	SymbolValidator symbolValidator;

	/**
	 * Test for a symbol that is in use.
	 * 
	 * @throws DatasetException
	 */
	@Test(expected=ValidatorException.class)
	public void testSymbolInuse() throws DatasetException {
		
		Teacher t1 = Mockito.mock(Teacher.class);
		Teacher t2 = Mockito.mock(Teacher.class);
		
		PowerMockito.when(dataAccess.getOneEmployee(Mockito.anyString())).thenReturn(t1);
		PowerMockito.when(facesContext.getELContext()).thenReturn(elContext);
		PowerMockito.when(uiComponent.getValueExpression(Mockito.anyString())).thenReturn(valueExpression);
		PowerMockito.when(valueExpression.getValue(elContext)).thenReturn(t2);
		
		
		symbolValidator.validate(facesContext, uiComponent, "testSymbol");
	}

	/**
	 * Test for a symbol that is not in use.
	 * 
	 * @throws DatasetException
	 */
	@Test
	public void testSymbolNotInuse() throws DatasetException {
		
		Teacher t = Mockito.mock(Teacher.class);
		
		PowerMockito.when(dataAccess.getOneEmployee(Mockito.anyString())).thenReturn(null);
		PowerMockito.when(facesContext.getELContext()).thenReturn(elContext);
		PowerMockito.when(uiComponent.getValueExpression(Mockito.anyString())).thenReturn(valueExpression);
		PowerMockito.when(valueExpression.getValue(elContext)).thenReturn(t);
		
		symbolValidator.validate(facesContext, uiComponent, "testSymbol");
	}

}
