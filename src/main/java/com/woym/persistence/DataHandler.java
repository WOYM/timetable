package com.woym.persistence;

import javax.persistence.EntityManager;

import com.woym.exceptions.DatasetException;

public abstract class DataHandler<E> {

	 EntityManager entityManager = DataBase.entityManager;

	public void persistObject(final E pObject) throws DatasetException {
		if (pObject == null) {
			throw new IllegalArgumentException("Parameter is null.");
		}
		try {
			entityManager.getTransaction().begin();
			entityManager.persist(pObject);
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			throw new DatasetException("Error while persisting object: "
					+ e.getMessage());
		}
	}

	public void updateObject(final E pObject) throws DatasetException {
		if (pObject == null) {
			throw new IllegalArgumentException("Parameter is null.");
		}
		try {
			entityManager.getTransaction().begin();
			entityManager.merge(pObject);
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			throw new DatasetException("Error while updating object: "
					+ e.getMessage());
		}
	}

	public void deleteObject(final E pObject) throws DatasetException {
		if (pObject == null) {
			throw new IllegalArgumentException("Parameter is null.");
		}
		try {
			entityManager.getTransaction().begin();
			entityManager.remove(pObject);
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			throw new DatasetException("Error while updating objects: "
					+ e.getMessage());
		}
	}
}
