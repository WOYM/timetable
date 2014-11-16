package org.woym.persistence;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ComponentSystemEvent;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

/**
 * Diese Klasse initialisiert den EntityManager, welcher für alle
 * Datenbanktransaktionen verwendet wird.
 * 
 * @author Adrian
 *
 */
@ManagedBean
@SessionScoped
public class DataBase {

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
			try {
				EntityManagerFactory factory;
				factory = Persistence.createEntityManagerFactory("timetable");
				ENTITY_MANAGER = factory.createEntityManager();
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