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
}
