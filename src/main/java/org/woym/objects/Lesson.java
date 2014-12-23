package org.woym.objects;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.woym.spec.objects.IMemento;

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
			lessonType = actualMemento.lessonType;
		} else {
			throw new IllegalArgumentException(
					"Only org.woym.objects.Lesson.Memento as parameter allowed.");
		}

	}

	/**
	 * Die Memento-Klasse zu {@linkplain Lesson}.
	 * 
	 * @author adrian
	 *
	 */
	public class Memento extends Activity.Memento {

		private final LessonType lessonType;

		Memento(Lesson originator) {
			super(originator);
			lessonType = originator.lessonType;
		}
	}

}
