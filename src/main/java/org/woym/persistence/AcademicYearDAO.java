package org.woym.persistence;

import java.util.Observer;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.woym.exceptions.DatasetException;
import org.woym.objects.AcademicYear;
import org.woym.objects.AcademicYear_;
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
		try {
			CriteriaBuilder cb = getEm().getCriteriaBuilder();
			CriteriaQuery<AcademicYear> cq = cb.createQuery(AcademicYear.class);
			Root<AcademicYear> root = cq.from(AcademicYear.class);
			cq.where(cb.equal(root.get(AcademicYear_.academicYear), year));
			TypedQuery<AcademicYear> query = getEm().createQuery(cq);
			return query.getSingleResult();
		} catch (Exception e) {
			LOGGER.error("Exception while getting AcademicYear for year "
					+ year, e);
			throw new DatasetException(
					"Error while getting AcademicYear for year " + year + ": "
							+ e.getMessage());
		}
	}
}
