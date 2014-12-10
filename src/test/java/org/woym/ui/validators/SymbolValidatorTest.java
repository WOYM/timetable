package org.woym.ui.validators;

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
	
	@InjectMocks
	SymbolValidator symbolValidator;
	
	/**
	 * Test for a symbol that is in use.
	 * 
	 * @throws DatasetException
	 */
	@Test(expected=ValidatorException.class)
	public void testSymbolInuse() throws DatasetException {
		
		Teacher t = PowerMockito.mock(Teacher.class);
		
		PowerMockito.when(dataAccess.getOneEmployee(Mockito.anyString())).thenReturn(t);
		
		symbolValidator.validate(facesContext, null, "testSymbol");
	}
	
	/**
	 * Test for a symbol that is not in use.
	 * 
	 * @throws DatasetException
	 */
	@Test
	public void testSymbolNotInuse() throws DatasetException {
		PowerMockito.when(dataAccess.getOneEmployee(Mockito.anyString())).thenReturn(null);
		
		symbolValidator.validate(facesContext, null, "testSymbol");
	}

}
