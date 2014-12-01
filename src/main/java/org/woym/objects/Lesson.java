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

	private LessonType subject;
	
	public Lesson() {
	}

	public LessonType getSubject() {
		return subject;
	}

	public void setSubject(LessonType subject) {
		this.subject = subject;
	}
}
