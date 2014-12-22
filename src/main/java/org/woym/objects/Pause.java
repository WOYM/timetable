package org.woym.objects;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Diese Klasse repräsentiert eine Pause.
 * 
 * @author Adrian
 *
 */
@Entity
@DiscriminatorValue("Pause")
public class Pause extends Activity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -917089197311092866L;

	public Pause() {
	}

	/**
	 * Erzeugt ein neues {@linkplain Memento} und gibt es zurück.
	 * 
	 * @return ein {@linkplain Memento} mit dem aktuellen Zustand des Objektes
	 */
	public Memento createMemento() {
		return new Memento(this);
	}

	/**
	 * Setzt den Status des {@linkplain Pause}-Objektes auf den Status des
	 * übergebenen {@linkplain Memento}-Objektes.
	 * 
	 * @param memento
	 *            - das Memento-Objekt, von welchem das {@linkplain Pause}
	 *            -Objekt den Status annehmen soll
	 */
	@Override
	public void setMemento(Memento memento) {
		super.setMemento(memento);
	}
}
