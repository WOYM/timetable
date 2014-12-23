package org.woym.spec.objects;

/**
 * Dieses Interface beschreibt Methoden, welche von Klassen implementiert werden
 * müssen, die eine Memento-Klasse besitzen.
 * 
 * @author adrian
 *
 */
public interface IMementoObject {

	/**
	 * Erzeugt ein neues Objekt der zugehörigen Mementoklasse, welche
	 * {@linkplain IMemento} implementiert und gibt es zurück.
	 * 
	 * @return ein Memento-Objekt der zugehörigen Memento-Klasse, welche
	 *         {@linkplain IMemento} implementiert
	 */
	public IMemento createMemento();

	/**
	 * Setzt den Status des Objektes auf den des übergebenen
	 * {@linkplain IMemento}-Objektes.
	 * 
	 * @param memento
	 *            - das {@linkplain IMemento}-Objekt, dessen Status das aktuelle
	 *            Objekt annehmen soll
	 */
	public void setMemento(IMemento memento);

}
