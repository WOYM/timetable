package org.woym.objects;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Diese Klasse repräsentiert eine reguläre Unterrichtsstunde.
 * 
 * @author Adrian
 *
 */
@Entity
@DiscriminatorValue("Lesson")
public class Lesson extends Activity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2876347057241150863L;

	private LessonType lessonType;

	public Lesson() {
	}

	public LessonType getLessonType() {
		return lessonType;
	}

	public void setLessonType(LessonType lessonType) {
		this.lessonType = lessonType;
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
	 * Setzt den Status des {@linkplain Lesson}-Objektes auf den Status des
	 * übergebenen {@linkplain Memento}-Objektes.
	 * 
	 * @param memento
	 *            - das Memento-Objekt, von welchem das {@linkplain Lesson}
	 *            -Objekt den Status annehmen soll
	 */
	public void setMemento(Memento memento) {
		super.setMemento(memento);
		lessonType = memento.lessonType;
	}

	/**
	 * Die Memento-Klasse zu {@linkplain Lesson}.
	 * 
	 * @author adrian
	 *
	 */
	public class Memento extends Activity.Memento {

		private final LessonType lessonType;

		public Memento(Lesson originator) {
			super(originator);
			lessonType = originator.lessonType;
		}
	}

}
