package org.woym.spec.persistence;

import org.woym.exceptions.DatasetException;
import org.woym.objects.ActivityType;

public interface IActivityTypeDAO extends IGenericDAO<ActivityType> {

	/**
	 * Sucht nach einem {@linkplain ActivityType} mit dem übergebenen Namen,
	 * wird ein solcher gefunden, wird dieser zurückgegeben, ansonsten
	 * {@code null}.
	 * 
	 * @param name
	 *            - der Name des gesuchten Aktivitätstyps
	 * @return den gesuchten Aktivitätstypen oder {@code nulls}
	 * @throws DatasetException
	 */
	public ActivityType getOne(String name) throws DatasetException;
}
