package org.woym.messages;

import org.woym.exceptions.DatasetException;
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
 * Diese Klasse ist eine Hilfsklasse für {@linkplain StatusMessageEnum} um viele
 * doppelte Texte zu ersparen.
 * 
 * @author Adrian
 *
 */
public abstract class MessageHelper {

	/**
	 * Fehlerbezeichnung für einen Datenbankfehler.
	 */
	static final String DATABASE_ERROR = "Datenbankfehler";

	/**
	 * Text der Statusnachricht, wenn beim Hinzufügen eines Objektes eine
	 * {@linkplain DatasetException} auftritt.
	 */
	private static final String ADD_OBJECT_DATASET_EXCEPTION = "Beim Hinzufügen %s ist ein Datenbankfehler aufgetreten.";

	/**
	 * Text der Statusnachricht, wenn beim Aktualisieren eines Objektes eine
	 * {@linkplain DatasetException} auftritt.
	 */
	private static final String EDIT_OBJECT_DATASET_EXCEPTION = "Beim Aktualisieren %s ist ein Datenbankfehler aufgetreten.";

	/**
	 * Text der Statusnachricht, wenn beim Löschen eines Objektes eine
	 * {@linkplain DatasetException} auftritt.
	 */
	private static final String DELETE_OBJECT_DATASET_EXCEPTION = "Beim Löschen %s ist ein Datenbankfehler aufgetreten.";

	/**
	 * Gibt eine Statusnachricht für einen Datenbankfehler beim Hinzufügen der
	 * übergebenen Klasse zurück.
	 * 
	 * @param clazz
	 *            - die Klasse, bei welcher der Fehler auftritt
	 * @return die Statusnachricht für die übergebene Klasse, wenn die Klasse
	 *         unbekannt ist, enthält die Statusnachricht {@code null}
	 */
	static String addDatasetException(Class<? extends Entity> clazz) {
		String textToInsert = getInsertString(clazz);
		return String.format(ADD_OBJECT_DATASET_EXCEPTION, textToInsert);
	}

	/**
	 * Gibt eine Statusnachricht für einen Datenbankfehler beim Aktualisieren
	 * der übergebenen Klasse zurück.
	 * 
	 * @param clazz
	 *            - die Klasse, bei welcher der Fehler auftritt
	 * @return die Statusnachricht für die übergebene Klasse, wenn die Klasse
	 *         unbekannt ist, enthält die Statusnachricht {@code null}
	 */
	static String editDatasetException(Class<? extends Entity> clazz) {
		String textToInsert = getInsertString(clazz);
		return String.format(EDIT_OBJECT_DATASET_EXCEPTION, textToInsert);
	}

	/**
	 * Gibt eine Statusnachricht für einen Datenbankfehler beim Löschen der
	 * übergebenen Klasse zurück.
	 * 
	 * @param clazz
	 *            - die Klasse, bei welcher der Fehler auftritt
	 * @return die Statusnachricht für die übergebene Klasse, wenn die Klasse
	 *         unbekannt ist, enthält die Statusnachricht {@code null}
	 */
	static String deleteDatasetException(Class<? extends Entity> clazz) {
		String textToInsert = getInsertString(clazz);
		return String.format(DELETE_OBJECT_DATASET_EXCEPTION, textToInsert);
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
		if (clazz == Activity.class) {
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
			return "des Lehrers";
		}
		return null;
	}
}
