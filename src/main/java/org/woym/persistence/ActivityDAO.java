package org.woym.persistence;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.woym.exceptions.DatasetException;
import org.woym.objects.Activity;
import org.woym.spec.persistence.IActivityDAO;

/**
 * 
 * Diese Singleton-Klasse bietet Methoden, die in Zusammenhang mit
 * Datenbankanfragen bezüglich Aktivitäten stehen.
 * 
 * @author adrian
 *
 */
public class ActivityDAO extends AbstractGenericDAO<Activity> implements IActivityDAO {

	/**
	 * Die Singleton-Instanz dieser Klasse.
	 */
	private static final ActivityDAO INSTANCE = new ActivityDAO();

	/**
	 * Die Select-Abfrage, um alle Aktivitäten zu bekommen.
	 */
	private static final String SELECT = "SELECT a FROM Activity a";

	/**
	 * Der private Konstruktor.
	 */
	private ActivityDAO() {
	}

	public static ActivityDAO getInstance() {
		return INSTANCE;
	}

}