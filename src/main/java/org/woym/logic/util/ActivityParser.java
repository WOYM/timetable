package org.woym.logic.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;
import org.woym.controller.PlanningController;
import org.woym.exceptions.DatasetException;
import org.woym.objects.Activity;
import org.woym.objects.CompoundLesson;
import org.woym.objects.Lesson;
import org.woym.objects.Meeting;
import org.woym.objects.PedagogicAssistant;
import org.woym.objects.Room;
import org.woym.objects.Schoolclass;
import org.woym.objects.Teacher;
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
			activities = dataAccess.getAllActivities(teacher);
		} catch (DatasetException e) {
			LOGGER.error(e);
			activities = new ArrayList<>();
		}

		return getActivityModel(activities);
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
			activities = dataAccess.getAllActivities(schoolclass);
		} catch (DatasetException e) {
			LOGGER.error(e);
			activities = new ArrayList<>();
		}

		return getActivityModel(activities);
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
			activities = dataAccess.getAllActivities(pedagogicAssistant);
		} catch (DatasetException e) {
			LOGGER.error(e);
			activities = new ArrayList<>();
		}

		return getActivityModel(activities);
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
			activities = dataAccess.getAllActivities(room);
		} catch (DatasetException e) {
			LOGGER.error(e);
			activities = new ArrayList<>();
		}

		return getActivityModel(activities);
	}

	/**
	 * Lädt und erzeugt das ActivityModel für ein bestimmtes Objekt.
	 * 
	 * @param entity
	 *            Die Entity, deren ActivityModel gesetzt werden soll
	 * @throws IllegalArgumentException
	 *             Wenn eine ungültige Entity übergeben wird
	 */
	private ScheduleModel getActivityModel(List<Activity> activities) {

		ScheduleModel activityModel = new DefaultScheduleModel();

		for (Activity activity : activities) {
			Date startDate = getActivityStartDate(activity);
			Date endDate = getActivityEndDate(activity);

			// TODO Style-Class?
			DefaultScheduleEvent event = new DefaultScheduleEvent(
					activity.toString(), startDate, endDate);

			event.setData(activity);

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
			return getCompoundLessonEvent(event);
		} else {
			return event;
		}

	}

	private DefaultScheduleEvent getLessonEvent(DefaultScheduleEvent event,
			Lesson lesson) {
		event.setTitle(lesson.getLessonType().getName());
		return event;
	}

	private DefaultScheduleEvent getMeetingEvent(DefaultScheduleEvent event,
			Meeting meeting) {
		event.setTitle(meeting.getMeetingType().getName());
		return event;
	}
	
	private DefaultScheduleEvent getCompoundLessonEvent(DefaultScheduleEvent event) {
		event.setTitle(CompoundLesson.VALID_DISPLAY_NAME);
		return event;
	}
}
