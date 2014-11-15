package org.woym.spec.persistence;

import java.util.List;

import org.woym.exceptions.DatasetException;

/**
 * Eine Schnittstelle, die Methoden für ein konkretes DataAccessObject
 * bereitstellt.
 * 
 * @author Adrian
 *
 * @param <E>
 *            - generische Klasse
 */
public interface IConcreteDAO<E> {

	/**
	 * Gibt eine Liste aller in der Datenbank vorhandenen Objekte vom Typ E
	 * zurück. Tritt dabei ein Fehler auf, wird eine
	 * {@linkplain DatasetException} geworfen.
	 * 
	 * @return List&lt;E&gt; - Liste aller Objekte vom Typ E, kann auch leer
	 *         sein
	 * 
	 * @throws DatasetException
	 */
	public List<E> getAll() throws DatasetException;

	/**
	 * Gibt das Objekt vom Typ E mit der übergebenen ID zurück oder null, wenn
	 * es nicht vorhanden ist. Tritt bei der Datenbankanfrage ein Fehler auf,
	 * wird eine {@linkplain DatasetException} geworfen.
	 * 
	 * @param pId
	 *            - ID des gesuchten Elementes
	 * @return E, falls ein Objekt mit der übergebenen ID vorhanden ist<br>
	 *         {@code null}, falls keins vorhanden ist
	 * @throws DatasetException
	 */
	public E getById(final Long pId) throws DatasetException;
}
