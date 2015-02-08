package org.woym.ui.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;
import org.woym.common.config.Config;
import org.woym.common.config.DefaultConfigEnum;
import org.woym.common.exceptions.DatasetException;
import org.woym.common.objects.Activity;
import org.woym.common.objects.CompoundLesson;
import org.woym.common.objects.Lesson;
import org.woym.common.objects.Meeting;
import org.woym.common.objects.Pause;
import org.woym.common.objects.PedagogicAssistant;
import org.woym.common.objects.Room;
import org.woym.common.objects.Schoolclass;
import org.woym.common.objects.Teacher;
import org.woym.controller.planning.PlanningController;
import org.woym.persistence.DataAccess;

/**
 * <h1>ActivityParser</h1>
 * <p>
 * Der {@link ActivityParser} erzeugt anhand gegebener Objekte und Parameter ein
 * {@link ScheduleModel}, das zur Anzeige im Stundenplan verwendet werden kann.
 * 
 * 
 * @author Tim Hansen (tihansen)
 * @since 0.0.8
 * @version 0.0.8
 */
public class ActivityParser {

	private static final ActivityParser INSTANCE = new ActivityParser();

	private static Logger LOGGER = LogManager.getLogger(ActivityParser.class);

	private DataAccess dataAccess = DataAccess.getInstance();

	private ActivityParser() {

	}

	/**
	 * Liefert eine Instanz der Klasse zurück.
	 * 
	 * @return Eine Instanz der Klasse
	 */
	public static ActivityParser getInstance() {
		return INSTANCE;
	}

	/**
	 * Liefert alle Aktivitäten einer Lehrkraft als {@link ScheduleModel}
	 * zurück.
	 * <p>
	 * Die Methode ist ausfallsicher, das heißt, dass bei einem Datenbankfehler
	 * ein leeres {@link ScheduleModel} zurückgeliefert wird.
	 * 
	 * @param teacher
	 *            Die Lehrkraft, für die ein {@link ScheduleModel} erzeugt
	 *            werden soll
	 * @return Alle Aktivitäten der Lehrkraft als {@link ScheduleModel}
	 */
	public ScheduleModel getActivityModel(Teacher teacher) {
		List<Activity> activities;

		try {
			activities = dataAccess.getAllActivities(teacher, true);
		} catch (DatasetException e) {
			LOGGER.error(e);
			activities = new ArrayList<>();
		}

		return getActivityModel(activities, true);
	}

	/**
	 * Liefert alle Aktivitäten einer Schulklasse als {@link ScheduleModel}
	 * zurück.
	 * <p>
	 * Die Methode ist ausfallsicher, das heißt, dass bei einem Datenbankfehler
	 * ein leeres {@link ScheduleModel} zurückgeliefert wird.
	 * 
	 * @param schoolclass
	 *            Die Schulklasse, für die ein {@link ScheduleModel} erzeugt
	 *            werden soll
	 * @return Alle Aktivitäten der Schulklasse als {@link ScheduleModel}
	 */
	public ScheduleModel getActivityModel(Schoolclass schoolclass) {
		List<Activity> activities;

		try {
			activities = dataAccess.getAllActivities(schoolclass, true);
		} catch (DatasetException e) {
			LOGGER.error(e);
			activities = new ArrayList<>();
		}

		return getActivityModel(activities, true);
	}

	/**
	 * Liefert alle Aktivitäten eines pädagogischen Mitarbeiters als
	 * {@link ScheduleModel} zurück.
	 * <p>
	 * Die Methode ist ausfallsicher, das heißt, dass bei einem Datenbankfehler
	 * ein leeres {@link ScheduleModel} zurückgeliefert wird.
	 * 
	 * @param pedagogicAssistant
	 *            Der pädagogische Mitarbeiter, für die ein
	 *            {@link ScheduleModel} erzeugt werden soll
	 * @return Alle Aktivitäten des pädagogischen Mitarbeiters als
	 *         {@link ScheduleModel}
	 */
	public ScheduleModel getActivityModel(PedagogicAssistant pedagogicAssistant) {
		List<Activity> activities;

		try {
			activities = dataAccess.getAllActivities(pedagogicAssistant, true);
		} catch (DatasetException e) {
			LOGGER.error(e);
			activities = new ArrayList<>();
		}

		return getActivityModel(activities, true);
	}

	/**
	 * Liefert alle Aktivitäten eines Raumes als {@link ScheduleModel} zurück.
	 * <p>
	 * Die Methode ist ausfallsicher, das heißt, dass bei einem Datenbankfehler
	 * ein leeres {@link ScheduleModel} zurückgeliefert wird.
	 * 
	 * @param room
	 *            Der Raum, für die ein {@link ScheduleModel} erzeugt werden
	 *            soll
	 * @return Alle Aktivitäten des Raumes als {@link ScheduleModel}
	 */
	public ScheduleModel getActivityModel(Room room) {
		List<Activity> activities;

		try {
			activities = dataAccess.getAllActivities(room, true);
		} catch (DatasetException e) {
			LOGGER.error(e);
			activities = new ArrayList<>();
		}

		return getActivityModel(activities, true);
	}

	/**
	 * Lädt und erzeugt das ActivityModel für ein bestimmtes Objekt.
	 * 
	 * @param entity
	 *            Die Entity, deren ActivityModel gesetzt werden soll
	 * @throws IllegalArgumentException
	 *             Wenn eine ungültige Entity übergeben wird
	 */
	public ScheduleModel getActivityModel(List<Activity> activities,
			Boolean editable) {

		ScheduleModel activityModel = new DefaultScheduleModel();

		for (Activity activity : activities) {
			Date startDate = getActivityStartDate(activity);
			Date endDate = getActivityEndDate(activity);

			DefaultScheduleEvent event = new DefaultScheduleEvent(
					activity.toString(), startDate, endDate);

			event.setData(activity);

			event.setEditable(editable);

			event = getEvent(event, activity);

			activityModel.addEvent(event);
		}

		return activityModel;
	}

	private Date getActivityStartDate(Activity activity) {
		long time = activity.getTime().getStartTime().getTime();

		return getActivityDate(activity, time);
	}

	private Date getActivityEndDate(Activity activity) {
		long time = activity.getTime().getEndTime().getTime();

		return getActivityDate(activity, time);
	}

	/**
	 * Erzeugt ein {@link Date}-Objekt, das in der Stundenplandarstellung
	 * angezeigt werden kann. Das heißt, dass die Aktivität auf ein
	 * entsprechendes Datum nach dem 05.01.1970 umgerechnet wird.
	 * 
	 * @param activity
	 *            Die Aktivität
	 * @param time
	 *            Die Zeit in Millisekunden
	 * @return Das darstellbare Datum
	 */
	private Date getActivityDate(Activity activity, long time) {
		int day = PlanningController.CALENDAR_DAY
				+ activity.getTime().getDay().getOrdinal();

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		calendar.set(PlanningController.CALENDAR_YEAR,
				PlanningController.CALENDAR_MONTH, day);

		return calendar.getTime();
	}

	private DefaultScheduleEvent getEvent(DefaultScheduleEvent event,
			Activity activity) {

		if (activity instanceof Lesson) {
			return getLessonEvent(event, (Lesson) activity);
		} else if (activity instanceof Meeting) {
			return getMeetingEvent(event, (Meeting) activity);
		} else if (activity instanceof CompoundLesson) {
			return getCompoundLessonEvent(event, (CompoundLesson) activity);
		} else if (activity instanceof Pause) {
			return getPauseEvent(event, (Pause) activity);
		} else {
			return event;
		}

	}

	private DefaultScheduleEvent getLessonEvent(DefaultScheduleEvent event,
			Lesson lesson) {
		String classname = lesson.getSchoolclasses().get(0).getName();
		String room = lesson.getRooms().get(0).getName();
		String location = lesson.getRooms().get(0).getLocationName();
		event.setTitle(lesson.getLessonType().getName() + ", " + classname
				+ "\n" + "Raum: " + room + " (" + location + ")");
		event.setStyleClass(lesson.getLessonType().getColor().getStyleClass());
		return event;
	}

	private DefaultScheduleEvent getMeetingEvent(DefaultScheduleEvent event,
			Meeting meeting) {
		String room = meeting.getRooms().get(0).getName();
		String location = meeting.getRooms().get(0).getLocationName();
		event.setTitle(meeting.getMeetingType().getName() + "\n" + "Raum: "
				+ room + " (" + location + ")");
		event.setStyleClass(Config
				.getSingleStringValue(DefaultConfigEnum.MEETING_COLOR));
		return event;
	}

	private DefaultScheduleEvent getCompoundLessonEvent(
			DefaultScheduleEvent event, CompoundLesson compoundLesson) {
		String location = compoundLesson.getRooms().get(0).getLocationName();
		event.setTitle(CompoundLesson.VALID_DISPLAY_NAME + "\n" + "Standort: "
				+ location);
		event.setStyleClass(Config
				.getSingleStringValue(DefaultConfigEnum.COMPOUND_LESSON_COLOR));
		return event;
	}

	private DefaultScheduleEvent getPauseEvent(DefaultScheduleEvent event,
			Pause pause) {
		event.setTitle(Pause.VALID_DISPLAY_NAME);
		event.setStyleClass(Config
				.getSingleStringValue(DefaultConfigEnum.PAUSE_COLOR));
		return event;
	}
}
