package org.woym.messages;

import javax.faces.application.FacesMessage;

import org.woym.objects.AcademicYear;
import org.woym.objects.Activity;
import org.woym.objects.Entity;
import org.woym.objects.LessonType;
import org.woym.objects.Location;
import org.woym.objects.MeetingType;
import org.woym.objects.PedagogicAssistant;
import org.woym.objects.ProjectType;
import org.woym.objects.Room;
import org.woym.objects.Schoolclass;
import org.woym.objects.Teacher;

/**
 * Diese Klasse dient der Generierung von Fehlernachrichten.
 * 
 * @author Adrian
 *
 */
public abstract class MessageHelper {

	/**
	 * Generiert aus der übergebenen {@linkplain SpecificStatusMessage}, der
	 * übergebenen Klasse und {@linkplain FacesMessage.Severity} eine
	 * {@linkplain FacesMessage} und gibt diese zurück.
	 * 
	 * @param message
	 *            - die Statusnachricht aus dem
	 *            {@linkplain SpecificStatusMessage}-Enum
	 * @param clazz
	 *            -die Klasse, welche betroffen ist
	 * @param severity
	 *            - die Art der Meldung (Info, Warnung, Fehler)
	 * @return die generierte {@linkplain FacesMessage}
	 */
	public static FacesMessage generateMessage(SpecificStatusMessage message,
			Class<? extends Entity> clazz, FacesMessage.Severity severity) {
		if (message == null || clazz == null || severity == null) {
			throw new IllegalArgumentException();
		}
		String toInsert = getInsertString(clazz);
		FacesMessage facesMessage = new FacesMessage();
		facesMessage.setSummary(message.getSummary());
		facesMessage.setDetail(String.format(message.getStatusMessage(),
				toInsert));
		facesMessage.setSeverity(severity);
		return facesMessage;
	}

	/**
	 * Generiert aus der übergebenen {@linkplain GenericStatusMessage} und der
	 * übergebenen {@linkplain FacesMessage.Severity} eine
	 * {@linkplain FacesMessage} und gibt diese zurück.
	 * 
	 * @param message
	 *            - die Statusnachricht aus dem
	 *            {@linkplain GenericStatusMessage}-Enum
	 * @param severity
	 *            - die Art der Meldung (Info, Warnung, Fehler)
	 * @return die generierte {@linkplain FacesMessage}
	 */
	public static FacesMessage generateMessage(GenericStatusMessage message,
			FacesMessage.Severity severity) {
		if (message == null || severity == null) {
			throw new IllegalArgumentException();
		}
		FacesMessage facesMessage = new FacesMessage();
		facesMessage.setSummary(message.getSummary());
		facesMessage.setDetail(message.getStatusMessage());
		facesMessage.setSeverity(severity);
		return facesMessage;
	}

	/**
	 * Gibt je nach übergebener Klasse einen passenden String zurück, der in die
	 * Statusnachricht eingefügt wird. Ist die Klasse unbekannt, wird
	 * {@code null} zurückgegeben.
	 * 
	 * @param clazz
	 *            - die Klasse, für welche der einzufügende Text zurückgegeben
	 *            werden soll
	 * @return der entsprechende Text für die übergebene Klasse oder
	 *         {@code null}, wenn die Klasse unbekannt ist
	 */
	private static String getInsertString(Class<? extends Entity> clazz) {
		if (clazz == AcademicYear.class) {
			return "des Jahrgangs";
		} else if (clazz == Activity.class
				|| clazz.getSuperclass() == Activity.class) {
			return "der Aktivität";
		} else if (clazz == LessonType.class) {
			return "des Unterrichtsinhalts";
		} else if (clazz == Location.class) {
			return "des Standortes";
		} else if (clazz == MeetingType.class) {
			return "der Sitzung";
		} else if (clazz == PedagogicAssistant.class) {
			return "des päd. Mitarbeiters";
		} else if (clazz == ProjectType.class) {
			return "des Projektes";
		} else if (clazz == Room.class) {
			return "des Raumes";
		} else if (clazz == Schoolclass.class) {
			return "der Schulklasse";
		} else if (clazz == Teacher.class) {
			return "der Lehrkraft";
		}
		return null;
	}
}
