package org.woym.persistence;

import java.util.List;

import javax.persistence.Query;

import org.woym.exceptions.DatasetException;
import org.woym.objects.PedagogicAssistant;
import org.woym.spec.persistence.IEmployeeDAO;

public class PedagogicAssistantDAO extends AbstractDAO<PedagogicAssistant>
		implements IEmployeeDAO<PedagogicAssistant> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3469006839428831985L;

	private final static String SELECT = "SELECT p FROM PedagogicAssistant p";
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<PedagogicAssistant> getBySymbol(String symbol)
			throws DatasetException {
		if (symbol == null) {
			throw new IllegalArgumentException();
		}
		try {
			final Query query = entityManager.createQuery(SELECT
					+ "WHERE p.symbol = ?1");
			query.setParameter(1, symbol);
			List<PedagogicAssistant> pedagogicAssistants = query.getResultList();
			return pedagogicAssistants;
		} catch (Exception e) {
			LOGGER.error("Exception while getting pedagogic assistant with symbol " + symbol, e);
			throw new DatasetException(
					"Error while getting pedagogic assistant for symbol " + symbol + ": "
							+ e.getMessage());
		}
	}

	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PedagogicAssistant> searchForEmployees(String searchSymbol)
			throws DatasetException {
		if (searchSymbol == null) {
			throw new IllegalArgumentException();
		}
		try {
			final Query query = entityManager.createQuery(SELECT
					+ "WHERE UPPER(p.symbol) LIKE ?1");
			query.setParameter(1, "%" + searchSymbol.toUpperCase() + "%");
			List<PedagogicAssistant> pedagogicAssistants = query.getResultList();
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

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PedagogicAssistant> getAll() throws DatasetException {
		try {
			final Query query = entityManager.createQuery(SELECT);
			List<PedagogicAssistant> pedagogicAssistants = query.getResultList();
			return pedagogicAssistants;
		} catch (Exception e) {
			LOGGER.error("Exception while getting all pedagogic assistants ", e);
			throw new DatasetException("Error while getting all pedagogic assistant: "
					+ e.getMessage());
		}
	}

	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PedagogicAssistant> getById(Long id) throws DatasetException {
		if (id == null) {
			throw new IllegalArgumentException();
		}
		try {
			final Query query = entityManager.createQuery(SELECT
					+ " WHERE p.id = ?1");
			query.setParameter(1, id);
			List<PedagogicAssistant> pedagogicAssistants = query.getResultList();
			return pedagogicAssistants;
		} catch (Exception e) {
			LOGGER.error("Exception while getting pedagogic assistant by id " + id, e);
			throw new DatasetException("Error while getting pedagogic assistant by id: "
					+ e.getMessage());
		}
	}

}
