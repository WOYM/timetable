package org.woym.persistence;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.woym.exceptions.DatasetException;
import org.woym.objects.Employee;
import org.woym.objects.Employee_;
import org.woym.spec.persistence.IEmployeeDAO;

/**
 * Diese abstrakte Klasse erweitert {@linkplain AbstractGenericDAO} um Methoden
 * für {@linkplain Employee}-Objekte.
 * 
 * @author Adrian
 *
 * @param <E>
 *            die {@linkplain Employee} erweiterende Klasse
 */
public abstract class AbstractEmployeeDAO<E extends Employee> extends
		AbstractGenericDAO<E> implements IEmployeeDAO<E> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public E getOne(String symbol) throws DatasetException {
		if (symbol == null) {
			throw new IllegalArgumentException();
		}
		try {
			CriteriaBuilder cb = getEm().getCriteriaBuilder();
			CriteriaQuery<E> cq = cb.createQuery(getClazz());
			Root<E> root = cq.from(getClazz());
			cq.where(cb.equal(root.get(Employee_.symbol), symbol));
			TypedQuery<E> query = getEm().createQuery(cq);
			return query.getSingleResult();
		} catch (Exception e) {
			LOGGER.error(String.format(
					"Exception while getting %s with symbol %s.", getClazz()
							.getSimpleName(), symbol), e);
			throw new DatasetException(String.format(
					"Error while getting %s for symbol %s: ", getClazz()
							.getSimpleName(), symbol)
					+ e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<E> search(String searchSymbol) throws DatasetException {
		if (searchSymbol == null) {
			throw new IllegalArgumentException();
		}
		try {
			CriteriaBuilder cb = getEm().getCriteriaBuilder();
			CriteriaQuery<E> cq = cb.createQuery(getClazz());
			Root<E> root = cq.from(getClazz());
			cq.where(cb.like(cb.upper(root.get(Employee_.symbol)), "%" + searchSymbol.toUpperCase() + "%"));
			TypedQuery<E> query = getEm().createQuery(cq);
			return query.getResultList();
		} catch (Exception e) {
			LOGGER.error(
					String.format(
							"Exception while getting %s whose symbol contain %s.",
							getClazz().getSimpleName() + "s", searchSymbol), e);
			throw new DatasetException(String.format(
					"Error while getting %s for symbol %s: ", getClazz()
							.getSimpleName() + "s", searchSymbol)
					+ e.getMessage());
		}
	}

}
