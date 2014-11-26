package org.woym.spec.persistence;

import javax.persistence.CascadeType;

import org.woym.exceptions.DatasetException;

public interface IGenericDAO {

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
	public void persist(final Object object) throws DatasetException;

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
	public void merge(final Object object) throws DatasetException;

	/**
	 * Löscht das Objekt aus der Datenbank, das dem übergebenen entspricht.
	 * Tritt beim Löschen ein Fehler auf, wird eine
	 * {@linkplain DatasetException} geworfen.
	 * 
	 * @param object
	 *            - das zu löschende Objekt
	 * @throws DatasetException
	 */
	public void delete(final Object object) throws DatasetException;
}
