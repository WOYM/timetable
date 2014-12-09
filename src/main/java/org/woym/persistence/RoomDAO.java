package org.woym.persistence;

import java.util.List;

import javax.persistence.Query;

import org.woym.exceptions.DatasetException;
import org.woym.objects.Room;
import org.woym.spec.persistence.IRoomDAO;

/**
 * Diese Singleton-Klasse implementiert Methoden, die im Zusammenhang mit
 * Datenbankanfragen bezüglich Räumen steshen.
 * 
 * @author Adrian
 *
 */
public class RoomDAO extends AbstractGenericDAO<Room> implements IRoomDAO {

	private static final RoomDAO INSTANCE = new RoomDAO();

	private RoomDAO() {
		DataBase.getInstance().addObserver(this);
		setClazz(Room.class);
	}

	public static RoomDAO getInstance() {
		return INSTANCE;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Room getOne(String locationName, String roomName)
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
