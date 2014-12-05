package org.woym.persistence;

import java.util.List;
import java.util.Observer;

import javax.persistence.Query;

import org.woym.exceptions.DatasetException;
import org.woym.objects.Location;
import org.woym.spec.persistence.ILocationDAO;

/**
 * Diese Klasse implementiert Methoden die in Zusammenhang mit Datenbankanfragen
 * bezüglich {@linkplain Location}-Objekten stehen.
 * 
 * @author Adrian
 *
 */
public class LocationDAO extends AbstractGenericDAO<Location> implements
		ILocationDAO {

	/**
	 * Die Singleton-Instanz dieser Klasse.
	 */
	private static final LocationDAO INSTANCE = new LocationDAO();

	/**
	 * Der private Konstruktor dieser Klasse. Registriert die Instanz bei
	 * {@linkplain DataBase} als {@linkplain Observer} und ruft
	 * {@linkplain AbstractGenericDAO#setClazz(Class)} mit {@linkplain Location}
	 * als Klasse auf.
	 */
	private LocationDAO() {
		DataBase.getInstance().addObserver(this);
		setClazz(Location.class);
	}

	/**
	 * Gibt die Singleton-Instanz dieser Klasse zurück.
	 * 
	 * @return die Singleton-Instanz dieser Klasse
	 */
	public static LocationDAO getInstance() {
		return INSTANCE;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Location getOne(String name) throws DatasetException {
		if (name == null) {
			throw new IllegalArgumentException();
		}
		try {
			final Query query = getEm().createQuery(
					"SELECT x FROM Location x WHERE x.name = ?1");
			query.setParameter(1, name);
			List<Location> result = (List<Location>) query.getResultList();
			if (result.isEmpty()) {
				return null;
			}
			return result.get(0);
		} catch (Exception e) {
			LOGGER.error("Exception while getting location with name " + name,
					e);
			throw new DatasetException(
					"Error while getting location with name " + name + ": "
							+ e.getMessage());
		}
	}
}
