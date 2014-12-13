package org.woym.messages;

import org.woym.objects.Activity;
import org.woym.objects.LessonType;
import org.woym.objects.Location;
import org.woym.objects.MeetingType;
import org.woym.objects.PedagogicAssistant;
import org.woym.objects.ProjectType;
import org.woym.objects.Room;
import org.woym.objects.Schoolclass;
import org.woym.objects.Teacher;

public enum StatusMessageEnum {

	DATABASE_COMMUNICATION_ERROR(0, MessageHelper.DATABASE_ERROR, "Bei der Kommunikation mit der Datenbank ist ein Fehler aufgetreten."),
	
	ADD_ACTIVITY_DATASET_EXCEPTION(1, MessageHelper.DATABASE_ERROR, MessageHelper.addDatasetException(Activity.class)),
	
	EDIT_ACTIVITY_DATASET_EXCEPTION(2, MessageHelper.DATABASE_ERROR, MessageHelper.editDatasetException(Activity.class)),
	
	DELETE_ACTIVITY_DATASET_EXCEPTION(3, MessageHelper.DATABASE_ERROR, MessageHelper.deleteDatasetException(Activity.class)),
	
	ADD_LESSONTYPE_DATASET_EXCEPTION(4, MessageHelper.DATABASE_ERROR, MessageHelper.addDatasetException(LessonType.class)),
	
	EDIT_LESSONTYPE_DATASET_EXCEPTION(5, MessageHelper.DATABASE_ERROR, MessageHelper.editDatasetException(LessonType.class)),
	
	DELETE_LESSONTYPE_DATASET_EXCEPTION(6, MessageHelper.DATABASE_ERROR, MessageHelper.deleteDatasetException(LessonType.class)),
	
	ADD_LOCATION_DATASET_EXCEPTION(7, MessageHelper.DATABASE_ERROR, MessageHelper.addDatasetException(Location.class)),
	
	EDIT_LOCATION_DATASET_EXCEPTION(8, MessageHelper.DATABASE_ERROR, MessageHelper.editDatasetException(Location.class)),
	
	DELETE_LOCATION_DATASET_EXCEPTION(9, MessageHelper.DATABASE_ERROR, MessageHelper.deleteDatasetException(Location.class)),
	
	ADD_MEETINGTYPE_DATASET_EXCEPTION(10, MessageHelper.DATABASE_ERROR, MessageHelper.addDatasetException(MeetingType.class)),
	
	EDIT_MEETINGTYPE_DATASET_EXCEPTION(11, MessageHelper.DATABASE_ERROR, MessageHelper.editDatasetException(MeetingType.class)),
	
	DELETE_MEETINGTYPE_DATASET_EXCEPTION(12, MessageHelper.DATABASE_ERROR, MessageHelper.deleteDatasetException(MeetingType.class)),
	
	ADD_PA_DATASET_EXCEPTION(13, MessageHelper.DATABASE_ERROR, MessageHelper.addDatasetException(PedagogicAssistant.class)),
	
	EDIT_PA_DATASET_EXCEPTION(14, MessageHelper.DATABASE_ERROR, MessageHelper.editDatasetException(PedagogicAssistant.class)),
	
	DELETE_PA_DATASET_EXCEPTION(15, MessageHelper.DATABASE_ERROR, MessageHelper.deleteDatasetException(PedagogicAssistant.class)),
	
	ADD_PROJECTTYPE_DATASET_EXCEPTION(16, MessageHelper.DATABASE_ERROR, MessageHelper.addDatasetException(ProjectType.class)),
	
	EDIT_PROJECTTYPE_DATASET_EXCEPTION(17, MessageHelper.DATABASE_ERROR, MessageHelper.editDatasetException(ProjectType.class)),
	
	DELETE_PROJECTTYPE_DATASET_EXCEPTION(18, MessageHelper.DATABASE_ERROR, MessageHelper.deleteDatasetException(ProjectType.class)),
	
	ADD_ROOM_DATASET_EXCEPTION(19, MessageHelper.DATABASE_ERROR, MessageHelper.addDatasetException(Room.class)),
	
	EDIT_ROOM_DATASET_EXCEPTION(20, MessageHelper.DATABASE_ERROR, MessageHelper.editDatasetException(Room.class)),
	
	DELETE_ROOM_DATASET_EXCEPTION(21, MessageHelper.DATABASE_ERROR, MessageHelper.deleteDatasetException(Room.class)),
	
	ADD_SCHOOLCLASS_DATASET_EXCEPTION(22, MessageHelper.DATABASE_ERROR, MessageHelper.addDatasetException(Schoolclass.class)),
	
	EDIT_SCHOOLCLASS_DATASET_EXCEPTION(23, MessageHelper.DATABASE_ERROR, MessageHelper.editDatasetException(Schoolclass.class)),
	
	DELETE_SCHOOLCLASS_DATASET_EXCEPTION(24, MessageHelper.DATABASE_ERROR, MessageHelper.deleteDatasetException(Schoolclass.class)),
	
	ADD_TEACHER_DATASET_EXCEPTION(25, MessageHelper.DATABASE_ERROR, MessageHelper.addDatasetException(Teacher.class)),
	
	EDIT_TEACHER_DATASET_EXCEPTION(26, MessageHelper.DATABASE_ERROR, MessageHelper.editDatasetException(Teacher.class)),
	
	DELETE_TEACHER_DATASET_EXCEPTION(27, MessageHelper.DATABASE_ERROR, MessageHelper.deleteDatasetException(Teacher.class)),
	
	INVALID_NAME(28, "Ungültiger Name.", "Name darf nicht leer sein."),
	
	NAME_ALREADY_EXISTS(29, "Ungültiger name.", "Dieser Name wird bereits verwendet."),
	
	INVALID_SYMBOL(30, "Ungültiges Kürzel.", "Kürzel darf nicht leer sein."),
	
	SYMBOL_ALREADY_EXISTS(31, "Ungültiges Kürzel.", "Dieses Kürzel wird bereits verwendet.")
	;
	
	/**
	 * Die Ordinalzahl.
	 */
	private final int ordinal;

	/**
	 * Zusammenfassung der Nachricht.
	 */
	private final String summary;
	
	/**
	 * Die Statusnachricht.
	 */
	private final String statusMessage;

	private StatusMessageEnum(int ordinal, String summary, String statusMessage) {
		this.ordinal = ordinal;
		this.summary = summary;
		this.statusMessage = statusMessage;
	}

	public int getOrdinal() {
		return ordinal;
	}

	public String getSummary(){
		return summary;
	}
	
	public String getStatusMessage() {
		return statusMessage;
	}
}
