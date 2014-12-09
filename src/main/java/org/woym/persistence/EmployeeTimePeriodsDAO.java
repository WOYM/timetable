package org.woym.persistence;

import java.util.Observer;

import org.woym.objects.EmployeeTimePeriods;
import org.woym.spec.persistence.IEmployeeTimePeriodsDAO;

/**
 * Diese Klasse implementiert Methoden, die in Zusammenhang mit
 * Datenbankanfragen bezüglich {@linkplain EmployeeTimePeriods}-Objekten stehen.
 * 
 * @author Adrian
 *
 */
public class EmployeeTimePeriodsDAO extends
		AbstractGenericDAO<EmployeeTimePeriods> implements
		IEmployeeTimePeriodsDAO {

	/**
	 * Die Singleton-Instanz dieser Klasse.
	 */
	private static final EmployeeTimePeriodsDAO INSTANCE = new EmployeeTimePeriodsDAO();

	/**
	 * Der private Konstruktor dieser Klasse. Registriert die Instanz bei
	 * {@linkplain DataBase} als {@linkplain Observer} und ruft
	 * {@linkplain AbstractGenericDAO#setClazz(Class)} mit
	 * {@linkplain EmployeeTimePeriods} als Klasse auf.
	 */
	private EmployeeTimePeriodsDAO() {
		DataBase.getInstance().addObserver(this);
		setClazz(EmployeeTimePeriods.class);
	}

	/**
	 * Gibt die Singleton-Instanz dieser Klasse zurück.
	 * 
	 * @return die Singleton-Instanz dieser Klasse.
	 */
	public static EmployeeTimePeriodsDAO getInstance() {
		return INSTANCE;
	}
}
