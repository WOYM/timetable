package org.woym.objects;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;

/**
 * Eine "zusammengesetzte" Unterrichtsstunde. Repräsentiert den Bandunterricht
 * der Schule, bei dem mehrere Schulklassen und Lehrer auf mehrere Räume und
 * Schulfächer verteilt werden.
 * 
 * @author Adrian
 *
 */
@Entity
@DiscriminatorValue("CompoundLesson")
public class CompoundLesson extends Activity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5513204957417530421L;

	@ManyToMany
	@OrderBy("name")
	private List<LessonType> lessonTypes;

	public CompoundLesson() {
	}

	public List<LessonType> getLessonTypes() {
		return lessonTypes;
	}

	public void setLessonTypes(List<LessonType> lessonTypes) {
		this.lessonTypes = lessonTypes;
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
	 * Setzt den Status des {@linkplain CompoundLesson}-Objektes auf den Status
	 * des übergebenen {@linkplain Memento}-Objektes.
	 * 
	 * @param memento
	 *            - das Memento-Objekt, von welchem das
	 *            {@linkplain CompoundLesson}-Objekt den Status annehmen soll
	 */
	public void setMemento(Memento memento) {
		super.setMemento(memento);
		lessonTypes = memento.lessonTypes;
	}

	/**
	 * Die Memento-Klasse von {@linkplain CompoundLesson}. Erweitert die
	 * abstrakte Memento-Klasse {@linkplain Activity.Memento}.
	 * 
	 * @author adrian
	 *
	 */
	public class Memento extends Activity.Memento {

		private final List<LessonType> lessonTypes;

		public Memento(CompoundLesson originator) {
			super(originator);
			lessonTypes = originator.lessonTypes;
		}
	}
}
