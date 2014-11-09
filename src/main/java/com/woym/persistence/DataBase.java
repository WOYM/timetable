package com.woym.persistence;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ComponentSystemEvent;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

@ManagedBean
@SessionScoped
public class DataBase implements Serializable {
	
	private static final long serialVersionUID = 7717982039237763919L;
	
	private static EntityManager ENTITY_MANAGER;
	
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

	public static EntityManager getEntityManager() {
		return ENTITY_MANAGER;
	}
}
