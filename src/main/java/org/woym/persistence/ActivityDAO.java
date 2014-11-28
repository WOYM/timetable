package org.woym.persistence;

import org.woym.objects.Activity;
import org.woym.spec.persistence.IActivityDAO;

/**
 * 
 * Diese Singleton-Klasse erweitert {@linkplain AbstractGenericDAO} und
 * implementiert {@linkplain IActivityDAO}. Sie bietet Methoden, die in
 * Zusammenhang mit Datenbankanfragen bezüglich Aktivitäten stehen.
 * 
 * @author Adrian
 *
 */
public class ActivityDAO extends AbstractGenericDAO<Activity> implements
		IActivityDAO {

	/**
	 * Die Singleton-Instanz dieser Klasse.
	 */
	private static final ActivityDAO INSTANCE = new ActivityDAO();

	/**
	 * Der private Konstruktor.
	 */
	private ActivityDAO() {
		DataBase.getInstance().addObserver(this);
		setClazz(Activity.class);
	}

	public static ActivityDAO getInstance() {
		return INSTANCE;
	}

}