package com.woym.persistence;

import java.util.List;

import javax.persistence.Query;

import com.woym.exceptions.DatasetException;
import com.woym.objects.Teacher;

public class TeacherDataHandler extends DataHandler<Teacher> {


	@SuppressWarnings("unchecked")
	public List<Teacher> getTeachers() throws DatasetException {
		try {
			final Query query = entityManager
					.createQuery("SELECT t FROM Teacher t");
			List<Teacher> teachers =  query.getResultList();
			return teachers;
		} catch (Exception e) {
			throw new DatasetException("Error while getting all teachers: "
					+ e.getMessage());
		}
	}
}
