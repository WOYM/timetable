package com.woym.persistence;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ComponentSystemEvent;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

@ManagedBean
@SessionScoped
public class DataBase {

	static EntityManager entityManager;

	public void setUp(ComponentSystemEvent event) {
		if (entityManager == null) {
			try {
				EntityManagerFactory factory;
				factory = Persistence.createEntityManagerFactory("timetable");
				entityManager = factory.createEntityManager();
			} catch (Exception e) {
				throw new PersistenceException(
						"Could not initialize persistence component: "
								+ e.getMessage());
			}
		}
	}
}
