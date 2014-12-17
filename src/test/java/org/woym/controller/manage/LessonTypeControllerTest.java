package org.woym.controller.manage;
import org.junit.Test;
import org.mockito.Mockito;
import org.woym.objects.LessonType;

//package org.woym.controller.manage;
//
//import javax.faces.context.FacesContext;
//
//import org.junit.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.powermock.api.mockito.PowerMockito;
//import org.woym.exceptions.DatasetException;
//import org.woym.objects.LessonType;
//import org.woym.persistence.DataAccess;
//
//public class LessonTypeControllerTest {
//
//	@Mock
//	DataAccess dataAccess;
//
//	@Mock
//	LessonType lessonType;
//
//	@InjectMocks
////	LessonTypeController lessonTypeController;
//
////	@Test(expected = IllegalArgumentException.class)
//	public void testAddLessonTypeFromDialogLessonTypeNull() {
//
//		lessonTypeController.addLessonTypeFromDialog();
//
//	}
//	
//	@Test
//	public void testAddLessonTypeFromDialogLessonTypeValid() {
//		
//		LessonTypeController lessonTypeController = new LessonTypeController();
//		
//		lessonTypeController.addLessonTypeDialog();
//		
//		
//
//		lessonTypeController.addLessonTypeFromDialog();
//
//	}
//
//	public void testEditLessonType() throws DatasetException {
//
//		LessonType lessonType = Mockito.mock(LessonType.class);
//
//		PowerMockito.when(lessonType.getName()).thenReturn("lesson");
//
//		lessonTypeController.editLessonType();
//
//	}
//
//}

public class LessonTypeControllerTest {

	@Test
	public void testAddLessonTypeFromDialog() {
		
		LessonType lessonType = Mockito.mock(LessonType.class);

		LessonTypeController lessonTypeController = new LessonTypeController();
		lessonTypeController.setLessonType(lessonType);
		lessonTypeController.addLessonTypeFromDialog();
		
		
	}

}
