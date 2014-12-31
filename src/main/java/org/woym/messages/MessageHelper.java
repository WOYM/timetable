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
	 * Generiert aus der übergebenen {@linkplain SpecificErrorMessage}, der
	 * übergebenen Klasse und {@linkplain FacesMessage.Severity} eine
	 * {@linkplain FacesMessage} und gibt diese zurück.
	 * 
	 * @param message
	 *            - die Statusnachricht aus dem
	 *            {@linkplain SpecificErrorMessage}-Enum
	 * @param clazz
	 *            -die Klasse, welche betroffen ist
	 * @param severity
	 *            - die Art der Meldung (Info, Warnung, Fehler)
	 * @return die generierte {@linkplain FacesMessage}
	 */
	public static FacesMessage generateMessage(SpecificErrorMessage message,
			Class<? extends Entity> clazz, FacesMessage.Severity severity) {
		if (message == null || clazz == null || severity == null) {
			throw new IllegalArgumentException();
		}
		String toInsert = getInsertString(clazz, false);
		FacesMessage facesMessage = new FacesMessage();
		facesMessage.setSummary(message.getSummary());
		facesMessage.setDetail(String.format(message.getStatusMessage(),
				toInsert));
		facesMessage.setSeverity(severity);
		return facesMessage;
	}

	/**
	 * Generiert aus der übergebenen {@linkplain GenericErrorMessage} und der
	 * übergebenen {@linkplain FacesMessage.Severity} eine
	 * {@linkplain FacesMessage} und gibt diese zurück.
	 * 
	 * @param message
	 *            - die Statusnachricht aus dem {@linkplain GenericErrorMessage}
	 *            -Enum
	 * @param severity
	 *            - die Art der Meldung (Info, Warnung, Fehler)
	 * @return die generierte {@linkplain FacesMessage}
	 */
	public static FacesMessage generateMessage(GenericErrorMessage message,
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
	 * Generiert aus der übergebenen {@linkplain SpecificSuccessMessage} und dem
	 * übergebenen Objekt einer {@linkplain Entity} erweiternden Klasse eine
	 * {@linkplain FacesMessage} für einen Erfolgsfall. Wird für einen der
	 * Parameter {@code null} übergeben, wird eine
	 * {@linkplain IllegalArgumentException} geworfen.
	 * 
	 * @param message
	 *            - die Erfolgsmeldung
	 * @param entity
	 *            - das Objekt einer {@linkplain Entity} erweiternden Klasse,
	 *            welches die Meldung betrifft
	 * @return die generierte {@linkplain FacesMessage}
	 */
	public static FacesMessage generateMessage(SpecificSuccessMessage message,
			Entity entity) {
		if (message == null || entity == null) {
			throw new IllegalArgumentException();
		}
		String toInsert = getInsertString(entity.getClass(), true);
		String summary = String.format(message.getSummary(), toInsert);
		String msg = String.format(message.getMessage(), toInsert,
				entity.toString());
		FacesMessage facesMessage = new FacesMessage(
				FacesMessage.SEVERITY_INFO, summary, msg);
		return facesMessage;
	}

	/**
	 * Erzeugt aus der übergebenen {@linkplain GenericSuccessMessage} und der
	 * übergebenen {@linkplain FacesMessage.Severity} eine neue
	 * {@linkplain FacesMessage} und gibt diese zurück.
	 * 
	 * @param message
	 *            - die Erfolgsmeldung
	 * @param severity
	 *            - die Art der Meldung (Info, Warnung, Fehler)
	 * @return
	 */
	public static FacesMessage generateMessage(GenericSuccessMessage message) {
		if (message == null) {
			throw new IllegalArgumentException();
		}
		return new FacesMessage(FacesMessage.SEVERITY_INFO,
				message.getSummary(), message.getMessage());
	}

	/**
	 * Gibt je nach übergebener Klasse einen passenden String zurück, der in die
	 * Statusnachricht eingefügt wird. Ist die Klasse unbekannt, wird
	 * {@code null} zurückgegeben.
	 * 
	 * @param clazz
	 *            - die Klasse, für welche der einzufügende Text zurückgegeben
	 *            werden soll
	 * @param success
	 *            - {@code true}, wenn ein String für eine Erfolgsnachricht
	 *            benötigt wird, {@code false} für eine Fehlernachrichts
	 * @return der entsprechende Text für die übergebene Klasse oder
	 *         {@code null}, wenn die Klasse unbekannt ist
	 */
	private static String getInsertString(Class<? extends Entity> clazz,
			boolean success) {
		if (clazz == AcademicYear.class) {
			return !success ? "des Jahrgangs" : "Jahrgang";
		} else if (clazz == Activity.class
				|| clazz.getSuperclass() == Activity.class) {
			return !success ? "der Aktivität" : "Aktivität";
		} else if (clazz == LessonType.class) {
			return !success ? "des Unterrichtsinhalts" : "Unterrichtsinhalt";
		} else if (clazz == Location.class) {
			return !success ? "des Standortes" : "Standort";
		} else if (clazz == MeetingType.class) {
			return !success ? "der Team-Sitzung" : "Team-Sitzung";
		} else if (clazz == PedagogicAssistant.class) {
			return !success ? "des päd. Mitarbeiters" : "Päd. Mitarbeiter";
		} else if (clazz == ProjectType.class) {
			return !success ? "des Projektes" : "Projekt";
		} else if (clazz == Room.class) {
			return !success ? "des Raumes" : "Raum";
		} else if (clazz == Schoolclass.class) {
			return !success ? "der Schulklasse" : "Schulklasse";
		} else if (clazz == Teacher.class) {
			return !success ? "der Lehrkraft" : "Lehrkraft";
		}
		return null;
	}
}
