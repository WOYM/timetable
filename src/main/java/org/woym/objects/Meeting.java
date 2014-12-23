package org.woym.objects;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.woym.spec.objects.IMemento;

/**
 * Diese Klasse repräsentiert eine Sitzung des Personals.
 * 
 * @author Adrian
 *
 */
@Entity
@DiscriminatorValue("Meeting")
public class Meeting extends Activity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3347521812061561725L;

	private MeetingType meetingType;

	public Meeting() {
	}

	public MeetingType getMeetingType() {
		return meetingType;
	}

	public void setMeetingType(MeetingType meetingType) {
		this.meetingType = meetingType;
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
		if (memento instanceof Memento) {
			Memento actualMemento = (Memento) memento;
			meetingType = actualMemento.meetingType;
		} else {
			throw new IllegalArgumentException(
					"Only org.woym.objects.Meeting.Memento as parameter allowed.");
		}

	}

	/**
	 * Die Memento-Klasse zu {@linkplain Meeting}.
	 * 
	 * @author adrian
	 *
	 */
	public static class Memento extends Activity.Memento {

		private final MeetingType meetingType;

		Memento(Meeting originator) {
			super(originator);
			meetingType = originator.meetingType;
		}
	}
}
