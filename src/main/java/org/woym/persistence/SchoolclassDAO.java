package org.woym.persistence;

import org.woym.objects.Schoolclass;
import org.woym.spec.persistence.ISchoolclassDAO;

public class SchoolclassDAO extends AbstractGenericDAO<Schoolclass> implements ISchoolclassDAO {
	
	private static final SchoolclassDAO INSTANCE = new SchoolclassDAO();

	private static final String SELECT = "SELECT s FROM Schoolclass s";

	private SchoolclassDAO(){
		DataBase.getInstance().addObserver(this);
		setClazz(Schoolclass.class);
	}
	
	public static SchoolclassDAO getInstance(){
		return INSTANCE;
	}

}
