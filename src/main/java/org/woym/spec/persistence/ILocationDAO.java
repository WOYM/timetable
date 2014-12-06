package org.woym.spec.persistence;

import org.woym.exceptions.DatasetException;
import org.woym.objects.Location;

public interface ILocationDAO extends IGenericDAO<Location> {

	/**
	 * Sucht in der Datenbank nach einem Standort mit dem übergebenen Namen.
	 * Existiert kein Standort mit dem übergebenen Namen wird {@code null}
	 * zurückgegeben. Ansonsten der gefundene Standort.
	 * 
	 * @param name
	 *            - Name des gesuchten Standortes
	 * @return den Standort mit dem übergebenen Namen oder {@code null}, wenn
	 *         kein solcher existiert
	 */
	public Location getOne(String name) throws DatasetException;

}
