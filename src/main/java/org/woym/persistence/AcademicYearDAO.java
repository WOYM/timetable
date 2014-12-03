package org.woym.persistence;

import java.util.Observer;

import javax.persistence.Query;

import org.woym.exceptions.DatasetException;
import org.woym.objects.AcademicYear;
import org.woym.spec.persistence.IAcademicYearDAO;

/**
 * Diese Singleton-Klasse erweitert {@linkplain AbstractGenericDAO} und
 * implementiert {@linkplain IAcademicYearDAO}. Sie realisiert Methoden, die in
 * Zusammenhang mit Datenbankanfragen stehen, die Jahrgänge betreffen.
 * 
 * @author Adrian
 *
 */
public class AcademicYearDAO extends AbstractGenericDAO<AcademicYear> implements
		IAcademicYearDAO {

	/**
	 * Die Singleton-Instanz dieser Klasse.
	 */
	private static final AcademicYearDAO INSTANCE = new AcademicYearDAO();

	/**
	 * Der private Konstruktor. Fügt die Instanz {@linkplain DataBase} als
	 * {@linkplain Observer} hinzu und ruft
	 * {@linkplain AbstractGenericDAO#setClazz(Class)} mit
	 * {@linkplain AcademicYear} als Klasse auf.
	 */
	private AcademicYearDAO() {
		DataBase.getInstance().addObserver(this);
		setClazz(AcademicYear.class);
	}

	/**
	 * Gibt die Singleton-Instanz dieser Klasse zurück.
	 * 
	 * @return die Singleton-Instanz dieser Klasse
	 */
	public static AcademicYearDAO getInstance() {
		return INSTANCE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AcademicYear getByYear(int year) throws DatasetException {
		if (year < 1) {
			throw new IllegalArgumentException();
		}
		try {
			final Query query = getEm().createQuery(
					"SELECT x FROM AcademicYear WHERE x.academicYear = ?1");
			query.setParameter(1, year);
			return (AcademicYear) query.getSingleResult();
		} catch (Exception e) {
			LOGGER.error("Exception while getting AcademicYear for year "
					+ year, e);
			throw new DatasetException(
					"Error while getting AcademicYear for year " + year + ": "
							+ e.getMessage());
		}
	}
}
