package org.woym.spec.persistence;

import org.woym.exceptions.DatasetException;
import org.woym.objects.Room;

/**
 * Beschreibt Methoden für ein Room Data Access Object.
 * 
 * @author Adrian
 *
 */
public interface IRoomDAO extends IGenericDAO<Room> {

	/**
	 * Sucht nach einem Raum mit dem übergebenen Namen an einem Standort mit dem
	 * übergebenen Namen. Wird für einen der beiden Parameter {@code null}
	 * übergeben, wirde eine {@linkplain IllegalArgumentException} geworfen.
	 * Existiert der gesuchte Raum wird dieser zurückgegeben, ansonsten
	 * {@code null}. Tritt bei der Anfrage ein Fehler auf, wird eine
	 * {@linkplain DatasetException} geworfen.
	 * 
	 * @param locationName
	 *            - Name des Standortes, an welchem nach dem Raum gesucht werden
	 *            soll
	 * @param roomName
	 *            - Name des Raumes nach dem gesucht werden soll
	 * @return der gesuchte Raum oder {@code null}, wenn kein solcher existiert
	 * @throws DatasetException
	 */
	public Room getOne(String locationName, String roomName)
			throws DatasetException;
}
