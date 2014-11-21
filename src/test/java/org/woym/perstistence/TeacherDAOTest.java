package org.woym.perstistence;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;
import org.woym.exceptions.DatasetException;
import org.woym.objects.Activity;
import org.woym.objects.Teacher;
import org.woym.objects.TimePeriod;
import org.woym.objects.Weekday;
import org.woym.persistence.DataBase;
import org.woym.persistence.TeacherDAO;

public class TeacherDAOTest {

	@Test
	public void test() {
		new DataBase().setUp();
		TeacherDAO db = new TeacherDAO();
		Teacher teacher = new Teacher();
		teacher.setFirstName("Julia");
		teacher.setLastName("Meyer");
		teacher.setSymbol("MEY");
		teacher.setHoursPerWeek(new BigDecimal("20"));
		try {
			db.persistObject(teacher);
			List<Teacher> teachers = db.getAll();
			assertEquals(1, teachers.size());
			System.out.println(teachers.get(0).toString());
		} catch (DatasetException e) {
			fail();
		}
		teacher.setFirstName("Hans");
		teacher.setLastName("Schmidt");
		teacher.setSymbol("SCH");
		try{
			db.persistObject(teacher);
			List<Teacher> teachers = db.getAll();
			assertEquals(1, teachers.size());
			System.out.println(teachers.get(0).toString());
		} catch (DatasetException e){
			fail();
			System.out.println("Test succeeded");
		}		
	}
}
