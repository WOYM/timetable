package org.woym.objects;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

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
	 * Erzeugt ein neues {@linkplain Memento} und gibt es zurück.
	 * 
	 * @return ein {@linkplain Memento} mit dem aktuellen Zustand des Objektes
	 */
	public Memento createMemento() {
		return new Memento(this);
	}

	/**
	 * Setzt den Status des {@linkplain Meeting}-Objektes auf den Status des
	 * übergebenen {@linkplain Memento}-Objektes.
	 * 
	 * @param memento
	 *            - das Memento-Objekt, von welchem das {@linkplain Meeting}
	 *            -Objekt den Status annehmen soll
	 */
	public void setMemento(Memento memento) {
		super.setMemento(memento);
		meetingType = memento.meetingType;
	}

	/**
	 * Die Memento-Klasse zu {@linkplain Meeting}.
	 * 
	 * @author adrian
	 *
	 */
	public static class Memento extends Activity.Memento {

		private final MeetingType meetingType;

		public Memento(Meeting originator) {
			super(originator);
			meetingType = originator.meetingType;
		}
	}
}
