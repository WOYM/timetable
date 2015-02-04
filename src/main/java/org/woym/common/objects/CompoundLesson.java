package org.woym.common.objects;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;

import org.woym.common.objects.spec.IMemento;

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
	private List<LessonType> lessonTypes = new ArrayList<LessonType>();
	
	public static final String VALID_DISPLAY_NAME = "Bandunterricht";

	public CompoundLesson() {
	}

	public List<LessonType> getLessonTypes() {
		return lessonTypes;
	}

	public void setLessonTypes(List<LessonType> lessonTypes) {
		this.lessonTypes = lessonTypes;
	}

	/**
	 * Fügt das übergebene {@linkplain LessonType}-Objekt der Liste hinzu,
	 * sofern es noch nicht vorhanden ist und gibt {@code true} zurück. Ist es
	 * bereits vorhanden, wird {@code false} zurückgegeben.
	 * 
	 * @param lessonType
	 *            - das hinzuzufügende {@linkplain LessonType}-Objekt
	 * @return {@code true}, wenn das übergebene {@linkplain LessonType}-Objekt
	 *         noch nicht vorhanden war und daher hinzugefügt wurde,
	 *         {@code false} ansonsten
	 */
	public boolean add(LessonType lessonType) {
		if (!lessonTypes.contains(lessonType)) {
			return lessonTypes.add(lessonType);
		}
		return false;
	}

	/**
	 * Gibt {@code true} zurück, wenn das übergebene {@linkplain LessonType}
	 * -Objekt in der entsprechenden Liste vorhanden ist, ansonsten
	 * {@code false}.
	 * 
	 * @param lessonType
	 *            - das zu prüfende {@linkplain LessonType}-Objekt
	 * @return {@code true}, wenn es vorhanden ist, ansonsten {@code false}
	 */
	public boolean contains(LessonType lessonType) {
		return lessonTypes.contains(lessonType);
	}

	/**
	 * Entfernt das übergebene {@linkplain LessonType}-Objekt aus der
	 * entsprechenden Liste, sofern es darin vorhanden ist und gibt die Größe
	 * der Liste nach dem Entfernen zurück. Ist es nicht vorhanden wird
	 * lediglich die Größe der Liste zurückgegeben.
	 * 
	 * @param lessonType
	 *            - das zu entferndene {@linkplain LessonType}-Objekt
	 * @return die Größe der entsprechenden Liste nach dem Entfernen
	 */
	public int remove(LessonType lessonType) {
		lessonTypes.remove(lessonType);
		return lessonTypes.size();
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
			lessonTypes = new ArrayList<LessonType>(actualMemento.lessonTypes);
		} else {
			throw new IllegalArgumentException(
					"Only " + Memento.class + " as parameter allowed.");
		}
	}
	
	@Override
	public String toString() {
		return "Bandunterricht, " + super.toString();
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
			lessonTypes = new ArrayList<LessonType>(originator.lessonTypes);
		}
	}

}
