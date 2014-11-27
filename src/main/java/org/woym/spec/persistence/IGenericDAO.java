package org.woym.spec.persistence;

import java.io.Serializable;
import java.util.List;

import org.woym.exceptions.DatasetException;

public interface IGenericDAO<E extends Serializable> {

	/**
	 * Persistiert das übergebene Objekt in der Datenbank oder aktualisiert es,
	 * falls es bereits in der Datenbank vorhanden ist. Diese Methode sollte nur
	 * zur Aktualisierung benutzt werden, wenn Beziehungen mit
	 * {@link CascadeType#ALL} oder {@link CascadeType#PERSIST} aktualisiert
	 * wurden. Tritt dabei ein Fehler auf, wird eine
	 * {@linkplain DatasetException} geworfen.
	 * 
	 * @param object
	 *            - das zu persistierende Objekt
	 */
	public void persist(E object) throws DatasetException;
	
	/**
	 * Aktualisiert das Objekt in der Datenbank, welches dem dem übergebenen
	 * entspricht. Tritt beim Merge ein Fehler auf, wird eine
	 * {@linkplain DatasetException} geworfen.
	 * 
	 * @param object
	 *            - das im System bereits aktualisierte, in der Datenbank zu
	 *            persistierende Objekt
	 * @throws DatasetException
	 */
	public void update(E object) throws DatasetException;
	
	
	/**
	 * Löscht das Objekt aus der Datenbank, das dem übergebenen entspricht.
	 * Tritt beim Löschen ein Fehler auf, wird eine
	 * {@linkplain DatasetException} geworfen.
	 * 
	 * @param object
	 *            - das zu löschende Objekt
	 * @throws DatasetException
	 */
	public void delete(E object) throws DatasetException;
	
	
	
	/**
	 * Gibt eine Liste aller in der Datenbank vorhandenen Objekte vom Typ E
	 * zurück. Tritt dabei ein Fehler auf, wird eine {@link DatasetException}
	 * geworfen.
	 * 
	 * @return Liste mit allen vorhanden Objekten vom Typ E oder leere Liste
	 * @throws DatasetException
	 */
	public List<E> getAll() throws DatasetException;

	/**
	 * Gibt ein Objekt vom Typ E mit der übergebenen ID zurück oder null, falls
	 * ein solches nicht existiert.
	 * 
	 * @param id
	 *            - ID des gesuchten Objektes
	 * @return Objekt vom Typ E, falls ein Objekt mit der übergebenen ID in der
	 *         Datenbank vorhanden ist, ansonsten null
	 * @throws DatasetException
	 */
	public E getById(Long id) throws DatasetException;
}
