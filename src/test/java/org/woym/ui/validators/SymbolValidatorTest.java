package org.woym.ui.validators;

import java.util.ArrayList;
import java.util.List;

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
import org.woym.persistence.TeacherDAO;

@RunWith(PowerMockRunner.class)
public class SymbolValidatorTest {

	@Mock
	TeacherDAO teacherDAO;
	
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
		List<Teacher> teacherList = new ArrayList<>();
		teacherList.add(new Teacher());
		
		PowerMockito.when(teacherDAO.search(Mockito.anyString())).thenReturn(teacherList);
		
		symbolValidator.validate(facesContext, null, "testSymbol");
	}
	
	/**
	 * Test for a symbol that is not in use.
	 * 
	 * @throws DatasetException
	 */
	@Test
	public void testSymbolNotInuse() throws DatasetException {
		PowerMockito.when(teacherDAO.search(Mockito.anyString())).thenReturn(new ArrayList<Teacher>());
		
		symbolValidator.validate(facesContext, null, "testSymbol");
	}

}
