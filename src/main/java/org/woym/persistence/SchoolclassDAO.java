package org.woym.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.woym.exceptions.DatasetException;
import org.woym.objects.Schoolclass;
import org.woym.spec.persistence.ISchoolclassDAO;

public class SchoolclassDAO extends AbstractGenericDAO<Schoolclass> implements ISchoolclassDAO {
	
	private static final SchoolclassDAO INSTANCE = new SchoolclassDAO();

	private static final String SELECT = "SELECT s FROM Schoolclass s";

	private SchoolclassDAO(){
	}
	
	public static SchoolclassDAO getInstance(){
		return INSTANCE;
	}

}
