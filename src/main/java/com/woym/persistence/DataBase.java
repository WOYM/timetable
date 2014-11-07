package com.woym.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

public abstract class DataBase {

	static EntityManager entityManager;

	static{
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
