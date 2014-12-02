package org.woym.persistence;

import java.util.List;

import javax.persistence.Query;

import org.woym.exceptions.DatasetException;
import org.woym.objects.Location;
import org.woym.objects.Room;
import org.woym.spec.persistence.ILocationDAO;

public class LocationDAO extends AbstractGenericDAO<Location> implements
		ILocationDAO {

	private static final LocationDAO INSTANCE = new LocationDAO();

	private LocationDAO() {
		DataBase.getInstance().addObserver(this);
		setClazz(Location.class);
	}

	public static LocationDAO getInstance() {
		return INSTANCE;
	}

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

	@SuppressWarnings("unchecked")
	@Override
	public Room searchRoom(String locationName, String roomName)
			throws DatasetException {
		if (locationName == null || roomName == null) {
			throw new IllegalArgumentException();
		}
		try {
			final String select = "SELECT r FROM Room r, Location l WHERE r.name = ?1 AND l.name = ?2 AND r MEMBER OF l.rooms";
			final Query query = getEm().createQuery(select);
			query.setParameter(1, roomName);
			query.setParameter(2, locationName);
			List<Room> result = (List<Room>) query.getResultList();
			if (result.isEmpty()) {
				return null;
			}
			return result.get(0);
		} catch (Exception e) {
			LOGGER.error("Exception while checking if room with name "
					+ roomName + " exists in location " + locationName, e);
			throw new DatasetException(
					"Error while checking if room with name " + roomName
							+ " exists in location " + locationName + ": "
							+ e.getMessage());
		}
	}
}
