package org.woym.messages;

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

public abstract class MessageHelper {

	static final String DATABASE_ERROR = "Datenbankfehler";

	private static final String ADD_OBJECT_DATASET_EXCEPTION = "Beim Hinzufügen %s ist ein Datenbankfehler aufgetreten.";
	
	private static final String EDIT_OBJECT_DATASET_EXCEPTION = "Beim Aktualisieren %s ist ein Datenbankfehler aufgetreten.";
	
	private static final String DELETE_OBJECT_DATASET_EXCEPTION = "Beim Löschen %s ist ein Datenbankfehler aufgetreten.";

	static String addDatasetException(Class<? extends Entity> clazz) {
		String textToInsert = getInsertString(clazz);
		return String.format(ADD_OBJECT_DATASET_EXCEPTION, textToInsert);
	}
	
	static String editDatasetException(Class<? extends Entity> clazz) {
		String textToInsert = getInsertString(clazz);
		return String.format(EDIT_OBJECT_DATASET_EXCEPTION, textToInsert);
	}
	
	static String deleteDatasetException(Class<? extends Entity> clazz) {
		String textToInsert = getInsertString(clazz);
		return String.format(DELETE_OBJECT_DATASET_EXCEPTION, textToInsert);
	}
	
	private static String getInsertString(Class<? extends Entity> clazz){
		if(clazz == Activity.class){
			return "der Aktivität";
		}else if(clazz == LessonType.class){
			return"des Unterrichtsinhalts";
		}else if(clazz == Location.class){
			return "des Standortes";
		} else if (clazz == MeetingType.class){
			return "der Sitzung";
		} else if (clazz == PedagogicAssistant.class){
			return "des päd. Mitarbeiters";
		} else if (clazz == ProjectType.class){
			return "des Projektes";
		} else if (clazz == Room.class){
			return "des Raumes";
		} else if (clazz == Schoolclass.class){
			return"der Schulklasse";
		} else if(clazz == Teacher.class){
			return "des Lehrers";
		}
		return null;
	}
}
