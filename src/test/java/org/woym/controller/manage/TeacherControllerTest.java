package org.woym.controller.manage;

import javax.faces.context.FacesContext;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.woym.objects.Teacher;
import org.woym.persistence.DataAccess;

public class TeacherControllerTest {
	
	
	@Mock
	DataAccess dataAccess;
	
	@Mock
	FacesContext facesContext;
	
	@InjectMocks
	TeacherController teacherController;
	
	@Test
	public void testAddTeacher() {
		
		Teacher teacher = Mockito.mock(Teacher.class);
		
		PowerMockito.when(teacher.getName()).thenReturn("Hans");
		PowerMockito.when(teacher.getSymbol()).thenReturn("Ha");
		
		teacherController.addTeacherFromDialog();
		
		
		
	}

}