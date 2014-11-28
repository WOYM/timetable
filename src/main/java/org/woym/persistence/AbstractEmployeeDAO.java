package org.woym.persistence;

import java.util.List;

import javax.persistence.Query;

import org.woym.exceptions.DatasetException;
import org.woym.objects.Employee;
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
	@SuppressWarnings("unchecked")
	@Override
	public E getOne(String symbol) throws DatasetException {
		if (symbol == null) {
			throw new IllegalArgumentException();
		}
		try {
			final Query query = getEm().createQuery(
					"SELECT x FROM " + getClazz().getSimpleName()
							+ " x WHERE x.symbol = ?1");
			query.setParameter(1, symbol);
			List<E> employees = query.getResultList();
			if (employees.isEmpty()) {
				return null;
			}
			return employees.get(0);
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
	@SuppressWarnings("unchecked")
	@Override
	public List<E> search(String searchSymbol) throws DatasetException {
		if (searchSymbol == null) {
			throw new IllegalArgumentException();
		}
		try {
			final Query query = getEm().createQuery(
					"SELECT x FROM " + getClazz().getSimpleName()
							+ " x WHERE UPPER(x.symbol) LIKE '%?1%'");
			query.setParameter(1, searchSymbol.toUpperCase());
			return (List<E>) query.getResultList();
		} catch (Exception e) {
			LOGGER.error(
					String.format(
							"Exception while getting %s whose symbol contain %s.",
							getClazz().getSimpleName() + "s", searchSymbol)
							+ searchSymbol, e);
			throw new DatasetException(String.format(
					"Error while getting %s for symbol %s: ", getClazz()
							.getSimpleName() + "s", searchSymbol)
					+ e.getMessage());
		}
	}

}
