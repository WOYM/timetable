package org.woym.persistence;

import java.util.List;
import java.util.Observer;

import javax.persistence.Query;

import org.woym.exceptions.DatasetException;
import org.woym.objects.Schoolclass;
import org.woym.spec.persistence.ISchoolclassDAO;

/**
 * Diese Singleton-Klasse erweitert {@linkplain AbstractGenericDAO} und
 * implementiert {@linkplain ISchoolclassDAO}. Sie stellt Methoden bereit, die
 * im Zusammenhang mit Datenbankanfragen, die Schulklassen betreffen, stehen.
 * 
 * @author Adrian
 *
 */
public class SchoolclassDAO extends AbstractGenericDAO<Schoolclass> implements
		ISchoolclassDAO {

	/**
	 * Die Singleton-Instanz dieser Klasse.
	 */
	private static final SchoolclassDAO INSTANCE = new SchoolclassDAO();

	/**
	 * Der private Konstruktor. Registriert die Instanz bei
	 * {@linkplain DataBase} als {@linkplain Observer} und ruft
	 * {@linkplain AbstractGenericDAO#setClazz(Class)} mit
	 * {@linkplain Schoolclass} als Klasse auf.
	 */
	private SchoolclassDAO() {
		DataBase.getInstance().addObserver(this);
		setClazz(Schoolclass.class);
	}

	/**
	 * Gibt die Singleton-Instanz dieser Klasse zurück.
	 * 
	 * @return die Singleton-Instanz dieser Klasse
	 */
	public static SchoolclassDAO getInstance() {
		return INSTANCE;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Schoolclass getOne(int academicYear, char identifier)
			throws DatasetException {
		try {
			final Query query = getEm()
					.createQuery(
							"SELECT s FROM Schoolclass s, AcademicYear a WHERE a.academicYear = ?1 "
									+ "AND s.identifier = ?2 AND s MEMBER OF a.schoolclasses");
			query.setParameter(1, academicYear);
			query.setParameter(2, identifier);
			List<Schoolclass> result = query.getResultList();
			if (result.isEmpty()) {
				return null;
			}
			return result.get(0);
		} catch (Exception e) {
			LOGGER.error("Exception while getting schoolclass with identifier "
					+ identifier + " from academic year " + academicYear, e);
			throw new DatasetException(String.format(
					"Exception while getting schoolclass with identifier %s"
							+ " from academic year %s: " + e.getMessage(),
					identifier, academicYear));
		}
	}
}
