package org.woym.persistence.spec;

import java.util.List;

import org.woym.exceptions.DatasetException;
import org.woym.objects.Room;

/**
 * Dieses Interface beschreibt Methoden, die von einem {@linkplain Room}
 * Data-Access-Object zu implementieren wären.
 * 
 * @author adrian
 *
 */
public interface IRoomDAO {

	/**
	 * Gibt eine Liste aller Räume zurück, die den übergebenen String als
	 * Funktion besitzen. Groß- und Kleinschreibung wird ignoriert.
	 * 
	 * @param purpose
	 *            - Funktion, für welche alle Räume gesucht sind
	 * @return eine Liste aller Räume, die den übergebenen String als Funktion
	 *         besitzen
	 * @throws DatasetException
	 */
	public List<Room> getAllRooms(String purpose) throws DatasetException;

	/**
	 * Sucht nach einem Raum mit dem übergebenen Namen an einem Standort mit dem
	 * übergebenen Namen. Wird für einen der beiden Parameter {@code null}
	 * übergeben, wirde eine {@linkplain IllegalArgumentException} geworfen.
	 * Existiert der gesuchte Raum wird dieser zurückgegeben, ansonsten
	 * {@code null}. Tritt bei der Anfrage ein Fehler auf, wird eine
	 * {@linkplain DatasetException} geworfen. Groß- und Kleinschreibung wird
	 * ignoriert.
	 * 
	 * @param locationName
	 *            - Name des Standortes, an welchem nach dem Raum gesucht werden
	 *            soll
	 * @param roomName
	 *            - Name des Raumes nach dem gesucht werden soll
	 * @return der gesuchte Raum oder {@code null}, wenn kein solcher existiert
	 * @throws DatasetException
	 */
	public Room getOneRoom(String locationName, String roomName)
			throws DatasetException;

	/**
	 * Gibt eine Liste aller disjunkten für Räume angegebene Funktionen zurück.
	 * Tritt dabei ein Fehler auf, wird eine {@linkplain DatasetException}
	 * geworfen.
	 * 
	 * @return Liste von Strings aller für Räume angegebenen Funktionen
	 * @throws DatasetException
	 */
	public List<String> getRoomPurposes() throws DatasetException;
}
