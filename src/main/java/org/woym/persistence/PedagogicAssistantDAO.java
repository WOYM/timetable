package org.woym.persistence;

import java.util.Observer;

import org.woym.objects.PedagogicAssistant;

/**
 * Diese Singleton-Klasse erweitert {@link AbstractEmployeeDAO} und bietet
 * Methoden, die in Zusammenhang mit Datenbankanfragen stehen, die päd.
 * Mitarbeiter betreffen.
 * 
 * @author Adrian
 *
 */
public class PedagogicAssistantDAO extends
		AbstractEmployeeDAO<PedagogicAssistant> {

	/**
	 * Die Singleton-Instanz dieser Klasse.
	 */
	private static final PedagogicAssistantDAO INSTANCE = new PedagogicAssistantDAO();

	/**
	 * Der private Konstruktor dieser Klasse. Registriert die Instanz bei
	 * {@linkplain DataBase} als {@linkplain Observer} und ruft
	 * {@linkplain AbstractGenericDAO#setClazz(Class)} mit
	 * {@linkplain PedagogicAssistant} als Klasse auf.
	 */
	private PedagogicAssistantDAO() {
		DataBase.getInstance().addObserver(this);
		setClazz(PedagogicAssistant.class);
	}

	/**
	 * Gibt die Singleton-Instanz dieser Klasse zurück.
	 * 
	 * @return die Singleton-Instanz dieser Klasse.
	 */
	public static PedagogicAssistantDAO getInstance() {
		return INSTANCE;
	}
}
