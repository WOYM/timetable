package org.woym.persistence;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ComponentSystemEvent;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Diese Klasse initialisiert den EntityManager, welcher für alle
 * Datenbanktransaktionen verwendet wird.
 * 
 * @author Adrian
 *
 */
@ManagedBean
@SessionScoped
public class DataBase implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6237080407072299976L;

	/**
	 * Der Logger.
	 */
	private static Logger LOGGER = LogManager.getLogger("DataBase");
	
	/**
	 * Der EntityManager.
	 */
	private static EntityManager ENTITY_MANAGER;

	/**
	 * Initialisiert den EntityManager, sofern dieser noch nicht initialisiert
	 * wurde, also null ist.
	 * 
	 * @param event
	 *            - JSF Event
	 */
	public void setUp(ComponentSystemEvent event) {
		if (ENTITY_MANAGER == null) {
			LOGGER.info("Establishing database-connection...");
			try {
				EntityManagerFactory factory;
				factory = Persistence.createEntityManagerFactory("timetable");
				ENTITY_MANAGER = factory.createEntityManager();
				LOGGER.info("Connection established.");
			} catch (Exception e) {
				throw new PersistenceException(
						"Could not initialize persistence component: "
								+ e.getMessage());
			}
		}
	}

	/**
	 * Gibt die Instanz von {@linkplain EntityManager} zurück.
	 * @return {@linkplain EntityManager} - Instanz des EntityManager
	 */
	public static EntityManager getEntityManager() {
		return ENTITY_MANAGER;
	}
}
