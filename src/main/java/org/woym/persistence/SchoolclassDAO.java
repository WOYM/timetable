package org.woym.persistence;

import java.util.Observer;

import org.woym.objects.Schoolclass;
import org.woym.spec.persistence.ISchoolclassDAO;

/**
 * Diese Singleton-Klasse erweitert {@linkplain AbstractGenericDAO} und
 * implementiert {@linkplain ISchoolclassDAO}. Sie stellt Methoden bereit, die
 * im Zusammenhang mit Datenbankanfragen, die Schulklassen betreffen, stehen.
 * 
 * @author Adrian
 *
 */
public class SchoolclassDAO extends AbstractGenericDAO<Schoolclass> implements
		ISchoolclassDAO {

	/**
	 * Die Singleton-Instanz dieser Klasse.
	 */
	private static final SchoolclassDAO INSTANCE = new SchoolclassDAO();

	/**
	 * Der private Konstruktor. Registriert die Instanz bei
	 * {@linkplain DataBase} als {@linkplain Observer} und ruft
	 * {@linkplain AbstractGenericDAO#setClazz(Class)} mit
	 * {@linkplain Schoolclass} als Klasse auf.
	 */
	private SchoolclassDAO() {
		DataBase.getInstance().addObserver(this);
		setClazz(Schoolclass.class);
	}

	/**
	 * Gibt die Singleton-Instanz dieser Klasse zurück.
	 * 
	 * @return die Singleton-Instanz dieser Klasse
	 */
	public static SchoolclassDAO getInstance() {
		return INSTANCE;
	}

}
