package org.woym.common.objects;

import java.io.Serializable;

import org.woym.common.exceptions.DatasetException;
import org.woym.common.objects.spec.IMementoObject;
import org.woym.persistence.DataAccess;

/**
 * Diese abstrakte Klasse implementiert Methoden, welche auf Objekten einer
 * erweiternden Klasse aufgerufen werden können, um sie zu persistieren, löschen
 * oder zu aktualisieren.
 * 
 * @author adrian
 *
 */
public abstract class Entity implements Serializable, IMementoObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5127366857697076089L;

	/**
	 * Persistiert dieses Objekt in der Datenbank. Tritt dabei ein Fehler auf,
	 * wird eine {@linkplain DatasetException} geworfen.
	 * 
	 * @throws DatasetException
	 */
	public void persist() throws DatasetException {
		DataAccess.getInstance().persist(this);
	}

	/**
	 * Aktualisiert dieses Objekt in der Datenbank. Tritt dabei ein Fehler auf,
	 * wird eine {@linkplain DatasetException} geworfen.
	 * 
	 * @throws DatasetException
	 */
	public void update() throws DatasetException {
		DataAccess.getInstance().update(this);
	}

	/**
	 * Löscht dieses Objekt aus der Datenbank. Tritt dabei ein Fehler auf, wird
	 * eine {@linkplain DatasetException} geworfen.
	 * 
	 * @throws DatasetException
	 */
	public void delete() throws DatasetException {
		DataAccess.getInstance().delete(this);
	}

	/**
	 * Aktualisiert dieses Objekt auf den Zustand aus der Datenbank. Tritt dabei
	 * ein Fehler auf, wird eine {@linkplain DatasetException} geworfen.
	 * 
	 * @throws DatasetException
	 */
	public void refresh() throws DatasetException {
		DataAccess.getInstance().refresh(this);
	}

}
