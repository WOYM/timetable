package org.woym.persistence;

import org.woym.objects.Teacher;

public class TeacherDAO extends AbstractEmployeeDAO<Teacher> {

	private static final TeacherDAO INSTANCE = new TeacherDAO();

	private TeacherDAO() {
		DataBase.getInstance().addObserver(this);
	}

	public static TeacherDAO getInstance() {
		return INSTANCE;
	}
}
