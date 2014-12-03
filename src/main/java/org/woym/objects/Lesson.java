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

}
