package org.woym.persistence;

import java.util.Observer;

import org.woym.objects.Teacher;

/**
 * Diese Singleton-Klasse erweitert {@linkplain AbstractEmployeeDAO} und stellt
 * Methoden bereit, die Zusammenhang mit Datenbankanfragen stehen, die Lehrer
 * betreffen.
 * 
 * @author Adrian
 *
 */
public class TeacherDAO extends AbstractEmployeeDAO<Teacher> {

	/**
	 * Die Singleton-Instanz dieser Klasse.
	 */
	private static final TeacherDAO INSTANCE = new TeacherDAO();

	/**
	 * Der private Konstruktor. Registriert die Instanz bei
	 * {@linkplain DataBase} als {@linkplain Observer} und ruft
	 * {@linkplain AbstractGenericDAO#setClazz(Class)} mit {@linkplain Teacher}
	 * als Klasse auf.
	 */
	private TeacherDAO() {
		DataBase.getInstance().addObserver(this);
		setClazz(Teacher.class);
	}

	/**
	 * Gibt die Singleton-Instanz dieser Klasse zurück.
	 * 
	 * @return die Singleton-Instanz dieser Klasse.
	 */
	public static TeacherDAO getInstance() {
		return INSTANCE;
	}
}
