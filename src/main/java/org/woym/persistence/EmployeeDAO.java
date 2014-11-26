package org.woym.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.woym.exceptions.DatasetException;
import org.woym.objects.Employee;
import org.woym.objects.PedagogicAssistant;
import org.woym.objects.Teacher;
import org.woym.spec.persistence.IEmployeeDAO;

public class EmployeeDAO implements IEmployeeDAO {

	private static final EmployeeDAO INSTANCE = new EmployeeDAO();

	private static final Logger LOGGER = LogManager.getLogger("Persistence");

	private final static String SELECT = "SELECT e FROM Employee e";

	private final static String SELECT_PA = "SELECT p FROM PedagogicAssistant p";

	private final static String SELECT_TEACHER = "SELECT t FROM Teacher t";

	private EmployeeDAO() {
	}

	public static EmployeeDAO getInstance() {
		return INSTANCE;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Employee> getAll() throws DatasetException {
		try {
			final EntityManager em = DataBase.getEntityManager();
			final Query query = em.createQuery(SELECT);
			List<Employee> employees = query.getResultList();
			return employees;
		} catch (Exception e) {
			LOGGER.error("Exception while getting all employees ", e);
			throw new DatasetException("Error while getting all employees: "
					+ e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Employee getById(Long id) throws DatasetException {
		if (id == null) {
			throw new IllegalArgumentException();
		}
		try {
			final EntityManager em = DataBase.getEntityManager();
			return em.find(Employee.class, id);
		} catch (Exception e) {
			LOGGER.error("Exception while getting employee by id " + id, e);
			throw new DatasetException("Error while getting employee by id: "
					+ e.getMessage());
		}
	}

	@SuppressWarnings("unchecked") 
	public List<Teacher> getAllTeachers() throws DatasetException {
		try {
			final EntityManager em = DataBase.getEntityManager();
			final Query query = em.createQuery(SELECT_TEACHER);
			List<Teacher> teachers = query.getResultList();
			return teachers;
		} catch (Exception e) {
			LOGGER.error("Exception while getting all teachers ", e);
			throw new DatasetException("Error while getting all teachers: "
					+ e.getMessage());
		}
	}

	@Override
	public List<PedagogicAssistant> getAllPAs() throws DatasetException {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Teacher getTeacher(String symbol) throws DatasetException {
		if (symbol == null) {
			throw new IllegalArgumentException();
		}
		try {
			final EntityManager em = DataBase.getEntityManager();
			final Query query = em.createQuery(SELECT_TEACHER
					+ " WHERE t.symbol = ?1");
			query.setParameter(1, symbol);
			List<Teacher> teachers = query.getResultList();
			if (teachers.isEmpty()) {
				return null;
			}
			return teachers.get(0);
		} catch (Exception e) {
			LOGGER.error("Exception while getting teacher with symbol "
					+ symbol, e);
			throw new DatasetException(
					"Error while getting teacher for symbol " + symbol + ": "
							+ e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public PedagogicAssistant getPedagogicAssistant(String symbol)
			throws DatasetException {
		if (symbol == null) {
			throw new IllegalArgumentException();
		}
		try {
			final EntityManager em = DataBase.getEntityManager();
			final Query query = em.createQuery(SELECT_PA
					+ " WHERE p.symbol = ?1");
			query.setParameter(1, symbol);
			List<PedagogicAssistant> pedagogicAssistants = query
					.getResultList();
			if (pedagogicAssistants.isEmpty()) {
				return null;
			}
			return pedagogicAssistants.get(0);
		} catch (Exception e) {
			LOGGER.error(
					"Exception while getting pedagogic assistant with symbol "
							+ symbol, e);
			throw new DatasetException(
					"Error while getting pedagogic assistant for symbol "
							+ symbol + ": " + e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Teacher> searchForTeachers(String searchSymbol)
			throws DatasetException {
		if (searchSymbol == null) {
			throw new IllegalArgumentException();
		}
		try {
			final EntityManager em = DataBase.getEntityManager();
			final Query query = em.createQuery(SELECT_TEACHER
					+ " WHERE UPPER(t.symbol) LIKE ?1");
			query.setParameter(1, "%" + searchSymbol.toUpperCase() + "%");
			List<Teacher> teachers = query.getResultList();
			return teachers;
		} catch (Exception e) {
			LOGGER.error(
					"Exception while searching for teachers whose symbol contain "
							+ searchSymbol, e);
			throw new DatasetException(
					"Error while getting teachers whose symbol contain "
							+ searchSymbol + ": " + e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PedagogicAssistant> searchForPAs(String searchSymbol)
			throws DatasetException {
		if (searchSymbol == null) {
			throw new IllegalArgumentException();
		}
		try {
			final EntityManager em = DataBase.getEntityManager();
			final Query query = em.createQuery(SELECT_PA
					+ " WHERE UPPER(p.symbol) LIKE ?1");
			query.setParameter(1, "%" + searchSymbol.toUpperCase() + "%");
			List<PedagogicAssistant> pedagogicAssistants = query
					.getResultList();
			return pedagogicAssistants;
		} catch (Exception e) {
			LOGGER.error(
					"Exception while searching for pedagogic assistants whose symbol contain "
							+ searchSymbol, e);
			throw new DatasetException(
					"Error while getting pedagogic assistants whose symbol contain "
							+ searchSymbol + ": " + e.getMessage());
		}
	}

}
