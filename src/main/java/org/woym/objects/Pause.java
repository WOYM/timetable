package org.woym.objects;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.woym.spec.objects.IMemento;

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
	 * {@inheritDoc}
	 */
	@Override
	public Memento createMemento() {
		return new Memento(this);
	}

	/**
	 * Bei Übergabe von {@code null} oder einem Parameter, der nicht vom Typ
	 * {@linkplain Memento} ist, wird eine {@linkplain IllegalArgumentException}
	 * geworfen. Ansonsten wird der Status des Objektes auf den des übergebenen
	 * Memento-Objektes gesetzt.
	 * 
	 * @param memento
	 *            - das {@linkplain Memento}-Objekt, von welchem dieses Objekt
	 *            den Status annehmen soll
	 */
	@Override
	public void setMemento(IMemento memento) {
		super.setMemento(memento);
	}
}
