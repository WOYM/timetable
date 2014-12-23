package org.woym.objects;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;

import org.woym.spec.objects.IMemento;

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
			lessonTypes = actualMemento.lessonTypes;
		} else {
			throw new IllegalArgumentException(
					"Only org.woym.objects.CompoundLesson.Memento as parameter allowed.");
		}
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

		Memento(CompoundLesson originator) {
			super(originator);
			lessonTypes = originator.lessonTypes;
		}
	}

}
