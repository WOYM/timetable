package org.woym.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;
import org.woym.common.config.Config;
import org.woym.common.config.DefaultConfigEnum;
import org.woym.common.exceptions.DatasetException;
import org.woym.common.messages.GenericErrorMessage;
import org.woym.common.messages.MessageHelper;
import org.woym.common.objects.AcademicYear;
import org.woym.common.objects.Activity;
import org.woym.common.objects.ActivityType;
import org.woym.common.objects.ActivityTypeEnum;
import org.woym.common.objects.CompoundLesson;
import org.woym.common.objects.Entity;
import org.woym.common.objects.Lesson;
import org.woym.common.objects.LessonType;
import org.woym.common.objects.Location;
import org.woym.common.objects.Meeting;
import org.woym.common.objects.PedagogicAssistant;
import org.woym.common.objects.Room;
import org.woym.common.objects.Schoolclass;
import org.woym.common.objects.Teacher;
import org.woym.common.objects.TimePeriod;
import org.woym.common.objects.Weekday;
import org.woym.common.objects.spec.IMemento;
import org.woym.logic.CommandHandler;
import org.woym.logic.SuccessStatus;
import org.woym.logic.command.UpdateCommand;
import org.woym.logic.spec.IStatus;
import org.woym.logic.util.ActivityParser;
import org.woym.logic.util.ActivityValidator;
import org.woym.persistence.DataAccess;

/**
 * <h1>PlanningController</h1>
 * <p>
 * Dieser Controller ist für die Steuerung der Stundenplandarstellung zuständig
 * und stellt das Aktivitätsmodell für die Anzeige, sowie die Werte für die
 * Anzeige selbst zur Verfügung.
 * 
 * @author Tim Hansen (tihansen)
 */
@ViewScoped
@ManagedBean(name = "planningController")
public class PlanningController implements Serializable {

	private static final long serialVersionUID = 3334120004119771842L;

	private static Logger LOGGER = LogManager
			.getLogger(PlanningController.class);

	public static final int CALENDAR_YEAR = 1970;
	public static final int CALENDAR_MONTH = Calendar.JANUARY;
	public static final int CALENDAR_DAY = 5;

	private DataAccess dataAccess = DataAccess.getInstance();
	private ActivityParser activityParser = ActivityParser.getInstance();
	private ActivityValidator activityValidator = ActivityValidator
			.getInstance();
	private CommandHandler commandHandler = CommandHandler.getInstance();

	private Teacher teacher;
	private PedagogicAssistant pedagogicAssistant;
	private Schoolclass schoolclass;
	private AcademicYear academicYear;
	private Location location;
	private Room room;
	private Activity activity;
	private ActivityTypeEnum activityType;

	private String searchTerm;

	private ScheduleModel scheduleModel;

	/**
	 * Erzwingt die Erzeugung einer neuen User-Session vor dem Rendern des
	 * Views, sofern noch keine existiert. Wichtig für die Serialisierung aller
	 * Objekte.
	 */
	@PostConstruct
	public void init() {
		FacesContext.getCurrentInstance().getExternalContext().getSession(true);

		searchTerm = "";
		scheduleModel = new DefaultScheduleModel();
	}

	/**
	 * Liefert das Zeitraster des Stundenplans zurück.
	 * 
	 * @return Minutenwert, wie groß ein 'Slot' ist
	 */
	public int getSlotMinutes() {
		return Config.getSingleIntValue(DefaultConfigEnum.TIMETABLE_GRID);
	}

	/**
	 * Liefert die Startzeit der Anzeige zurück.
	 * 
	 * @return Startzeit
	 */
	public String getMinTime() {
		return Config.getSingleStringValue(DefaultConfigEnum.WEEKDAY_STARTTIME);
	}

	/**
	 * Liefert die Endzeit der Anzeige zurück.
	 * 
	 * @return Startzeit
	 */
	public String getMaxTime() {
		return Config.getSingleStringValue(DefaultConfigEnum.WEEKDAY_ENDTIME);
	}

	/**
	 * Setzt das initiale Darstellungsdatum des Kalenders auf den ersten Montag
	 * nach der Unix-Zeit (05.01.1970).
	 * 
	 * Dies gewährleistet, dass dieses Datum niemals erreicht werden kann. So
	 * wird die Makierung des momentanen Wochentages unterdrückt.
	 * 
	 * @return Datumsobjekt, das den Montag nach der Unix-Zeit repräsentiert
	 *         (05.01.1970)
	 */
	public Date getInitalDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(CALENDAR_YEAR, CALENDAR_MONTH, CALENDAR_DAY, 0, 0, 0);

		return calendar.getTime();
	}

	/**
	 * Wird aufgerufen, wenn in der Darstellung eine Aktivität selektiert wird.
	 * <p>
	 * Setzt die lokale Aktivität entsprechend der Angeklickten.
	 * 
	 * @param selectEvent
	 *            Das Event
	 */
	public void onEventSelect(SelectEvent selectEvent) {
		DefaultScheduleEvent event = (DefaultScheduleEvent) selectEvent
				.getObject();

		setActivity((Activity) event.getData());
	}

	/**
	 * Diese Methode wird aufgerufen, wenn ein Element in der Darstellung bewegt
	 * wird.
	 * <p>
	 * Die {@link Activity} im Event wird dabei so angepasst, dass ihre
	 * Anfangszeit entsprechend des Event-Deltas gesetzt wird, wobei auch die
	 * Endzeit entsprechend angepasst wird. Hierbei wird die Standarddauer der
	 * Aktivität nicht beachtet, um Veränderungen an der Aktivität durch
	 * Resizing nicht zu verfälschen.
	 * <p>
	 * Sollte es der Fall sein, dass die Aktivität sich in Überschneidung mit
	 * anderen Aktivitäten befindet, so wird auf der GUI eine entsprechende
	 * Nachricht dargestellt und keine Aktualisierung der Aktivität vorgenommen.
	 * 
	 * @param event
	 *            Das Event
	 */
	public void onEventMove(ScheduleEntryMoveEvent event) {

		FacesMessage msg;

		Activity activity = (Activity) event.getScheduleEvent().getData();
		TimePeriod oldTime = activity.getTime();

		IMemento activityMemento = activity.createMemento();

		Date startTime = changeDateByDelta(activity.getTime().getStartTime(),
				event.getDayDelta(), event.getMinuteDelta());
		Date endTime = changeDateByDelta(activity.getTime().getEndTime(),
				event.getDayDelta(), event.getMinuteDelta());

		int localDayDelta = activity.getTime().getDay().getOrdinal()
				+ event.getDayDelta();

		if (Weekday.getByOrdinal(localDayDelta) == null) {
			msg = MessageHelper.generateMessage(
					GenericErrorMessage.INVALID_WEEKDAY_IN_DISPLAY,
					FacesMessage.SEVERITY_ERROR);
		} else {

			TimePeriod time = new TimePeriod();
			time.setStartTime(startTime);
			time.setEndTime(endTime);
			time.setDay(Weekday.getByOrdinal(localDayDelta));

			activity.setTime(time);

			IStatus status = activityValidator.validateActivity(activity);

			if (status instanceof SuccessStatus) {

				UpdateCommand<Activity> command = new UpdateCommand<Activity>(
						activity, activityMemento);

				status = commandHandler.execute(command);

				msg = status.report();

				// Update event
				DefaultScheduleEvent defaultScheduleEvent = (DefaultScheduleEvent) event
						.getScheduleEvent();
				defaultScheduleEvent.setData(activity);
				scheduleModel.updateEvent(defaultScheduleEvent);
				FacesContext.getCurrentInstance().addMessage(null, msg);
				return;
			} else {
				msg = status.report();
			}
		}

		// Fallback
		DefaultScheduleEvent defaultScheduleEvent = (DefaultScheduleEvent) event
				.getScheduleEvent();
		defaultScheduleEvent.setStartDate(oldTime.getStartTime());
		defaultScheduleEvent.setEndDate(oldTime.getEndTime());
		scheduleModel.updateEvent(defaultScheduleEvent);

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	/**
	 * Diese Methode wird aufgerufen, wenn die Größe einer Aktivität in der
	 * Stundenplandarstellung der GUI verändert wird.
	 * <p>
	 * Durch den Aufbau der {@code Schedule}-Komponente kann ein Event nur in
	 * seiner Endzeit verändert werden.
	 * <p>
	 * Da in der Darstellung die Wochenansicht erzwungen wird, wird der
	 * Wochentag nicht überschrieben. Dieses passiert beim Bewegen von
	 * Aktivitäten.
	 * 
	 * @param event
	 *            Das Event
	 */
	public void onEventResize(ScheduleEntryResizeEvent event) {

		FacesMessage msg;

		Activity activity = (Activity) event.getScheduleEvent().getData();

		IMemento activityMemento = activity.createMemento();

		Date endTime = changeDateByDelta(activity.getTime().getEndTime(),
				event.getDayDelta(), event.getMinuteDelta());

		TimePeriod time = new TimePeriod();
		time.setStartTime(activity.getTime().getStartTime());
		time.setEndTime(endTime);
		time.setDay(activity.getTime().getDay());

		activity.setTime(time);

		IStatus status = activityValidator.validateActivity(activity);

		if (status instanceof SuccessStatus) {

			UpdateCommand<Activity> command = new UpdateCommand<Activity>(
					activity, activityMemento);

			status = commandHandler.execute(command);

			msg = status.report();

			// Update event
			scheduleModel.updateEvent(event.getScheduleEvent());
		} else {
			msg = status.report();
		}

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	/**
	 * Diese Methode passt ein übergebenes {@link Date} um ein übergebenes
	 * Tages- und Minutendelta an.
	 * <p>
	 * Wir ein Wert für Minuten angegeben, der den Umfang eines Tages
	 * überschreitet, so wird dies entsprechend umgerechnet.
	 * 
	 * @param date
	 *            Das {@link Date}, das um die Delta verändert werden soll
	 * @param dayDelta
	 *            Das Tagesdelta
	 * @param minuteDelta
	 *            Das Minutendelta
	 * @return Das angepasste {@link Date}
	 */
	private Date changeDateByDelta(Date date, int dayDelta, int minuteDelta) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		calendar.set(Calendar.DATE, (calendar.get(Calendar.DATE) + dayDelta));
		calendar.set(Calendar.MINUTE,
				(calendar.get(Calendar.MINUTE) + minuteDelta));

		return calendar.getTime();
	}

	/**
	 * Gibt alle Lehrkräfte für einen bestimmten Suchbegriff zurück.
	 * 
	 * Gesucht wird anhand des Kürzels einer Lehrkraft.
	 * 
	 * Die Methode ist ausfallsicher, das heißt, dass im Falle eines
	 * Datenbankfehlers eine leere Liste zurückgeliefert wird.
	 * 
	 * @return Liste aller Lehrkräfte, deren Kürzel den Suchbegriff enthält
	 */
	public List<Teacher> getTeachersForSearchTerm() {

		List<Teacher> teachers;

		try {
			teachers = dataAccess.searchTeachers(searchTerm);
		} catch (DatasetException e) {
			LOGGER.error(e);
			teachers = new ArrayList<>();
		}

		return teachers;
	}

	/**
	 * Gibt alle pädagogischen Mitarbeiter für einen bestimmten Suchbegriff
	 * zurück.
	 * 
	 * Gesucht wird anhand des Kürzels eines pädagogischen Mitarbeiters.
	 * 
	 * Die Methode ist ausfallsicher, das heißt, dass im Falle eines
	 * Datenbankfehlers eine leere Liste zurückgeliefert wird.
	 * 
	 * @return Liste aller pädagogischen Mitarbeiter, deren Kürzel den
	 *         Suchbegriff enthält
	 */
	public List<PedagogicAssistant> getPedagogicAssistantsForSearchTerm() {

		List<PedagogicAssistant> pedagogicAssistants;

		try {
			pedagogicAssistants = dataAccess.searchPAs(searchTerm);
		} catch (DatasetException e) {
			LOGGER.error(e);
			pedagogicAssistants = new ArrayList<>();
		}

		return pedagogicAssistants;
	}

	/**
	 * Diese Methode liefert alle Schulklassen für einen bestimmten Jahrgang
	 * zurück. Dies lässt die Auswahl über ein Dropdown-Menü zu.
	 * 
	 * @return Eine Liste aller Schulklassen für einen Jahrgang
	 */
	public List<Schoolclass> getSchoolclassesForAcademicYear() {
		return academicYear.getSchoolclasses();
	}

	/**
	 * Diese Methode liefert alle Räume für einen bestimmten Standort zurück.
	 * Dies lässt die Auswahl über ein Dropdown-Menü zu.
	 * 
	 * @return Eine Liste aller Räume für einen Standort
	 */
	public List<Room> getRoomsForLocation() {
		return location.getRooms();
	}

	public void doBeforeAdd() {
		activity = null;
		activityType = null;
	}

	/**
	 * Diese Methode gibt an, ob eine Lehrkraft oder eine Klasse ausgewählt
	 * wurde. So wird bestimmt, ob ein Stundenplan gerendert wird.
	 * 
	 * @return Wahrheitswert, ob ein Objekt gewählt wurde
	 */
	public Boolean getHasChosen() {
		if (teacher != null || pedagogicAssistant != null
				|| schoolclass != null || room != null) {
			return true;
		}

		return false;
	}

	/**
	 * Diese Methode gibt an, ob dem System Lehrkräfte bekannt sind.
	 * 
	 * @return Wahrheitswert, ob es im System Lehrkräfte gibt
	 */
	public Boolean getExistTeachers() {
		try {
			List<Teacher> teachers = dataAccess.getAllTeachers();

			if (teachers.size() > 0) {
				return true;
			}

		} catch (DatasetException e) {
			LOGGER.error(e);
		}

		return false;
	}

	/**
	 * Diese Methode gibt an, ob dem System Schulklassen bekannt sind.
	 * 
	 * @return Wahrheitswert, ob es im System Schulklassen gibt
	 */
	public Boolean getExistSchoolclasses() {
		try {
			List<Schoolclass> schoolclasses = dataAccess.getAllSchoolclasses();

			if (schoolclasses.size() > 0) {
				return true;
			}

		} catch (DatasetException e) {
			LOGGER.error(e);
		}

		return false;
	}

	/**
	 * Diese Methode gibt an, ob dem System pädagogische Mitarbeiter bekannt
	 * sind.
	 * 
	 * @return Wahrheitswert, ob es im System pädagogische Mitarbeiter gibt
	 */
	public Boolean getExistPedagogicAssistants() {
		try {
			List<PedagogicAssistant> pedagogicAssistants = dataAccess
					.getAllPAs();

			if (pedagogicAssistants.size() > 0) {
				return true;
			}

		} catch (DatasetException e) {
			LOGGER.error(e);
		}

		return false;
	}

	/**
	 * Diese Methode gibt an, ob dem System pädagogische Mitarbeiter bekannt
	 * sind.
	 * 
	 * @return Wahrheitswert, ob es im System pädagogische Mitarbeiter gibt
	 */
	public Boolean getExistRooms() {
		try {
			List<Location> locations = dataAccess.getAllLocations();

			for (Location location : locations) {
				if (location.getRooms().size() > 0) {
					return true;
				}
			}

		} catch (DatasetException e) {
			LOGGER.error(e);
		}

		return false;
	}

	/**
	 * Diese Methode gibt alle bekannten {@link ActivityType}s zurück, die eine
	 * {@link Activity} haben kann.
	 * <p>
	 * Damit sind die hartkodierten Typen (z.B. {@link Lesson}, {@link Meeting}
	 * und {@link CompoundLesson}) gemeint.
	 * 
	 * @return Eine {@link ArrayList} mit {@link ActivityType}s
	 */
	public List<ActivityTypeEnum> getMainActivityTypes() {
		return Arrays.asList(ActivityTypeEnum.values());
	}

	/**
	 * Setzt das ActivityModel für eine Lehrkraft.
	 */
	public void setTeacherActivityModel() {
		if (teacher != null) {
			scheduleModel = activityParser.getActivityModel(teacher);
		}
	}

	/**
	 * Setzt das ActivityModel für einen pädagogischen Mitarbeiter.
	 */
	public void setPedagogicAssistantActivityModel() {
		if (pedagogicAssistant != null) {
			scheduleModel = activityParser.getActivityModel(pedagogicAssistant);
		}
	}

	/**
	 * Setzt das ActivityModel für eine Schulklasse.
	 */
	public void setSchoolclassActivityModel() {
		if (schoolclass != null) {
			scheduleModel = activityParser.getActivityModel(schoolclass);
		}
	}

	/**
	 * Setzt das ActivityModel für einen Raum.
	 */
	public void setRoomActivityModel() {
		if (room != null) {
			scheduleModel = activityParser.getActivityModel(room);
		}
	}

	/**
	 * Setzt alle Objekte außer dem Übergebenen {@code null}.
	 * <p>
	 * Bei einer Klasse oder einem Raum wird das Elternobjekt, also Jahrgang
	 * oder Standort nicht zurückgesetzt.
	 * 
	 * @param entity
	 *            Die Entity, die nicht null gesetzt werden soll.
	 */
	private void unsetAllExcept(Entity entity) {
		if (!(entity instanceof Teacher)) {
			teacher = null;
		}
		if (!(entity instanceof PedagogicAssistant)) {
			pedagogicAssistant = null;
		}
		if (!(entity instanceof AcademicYear)
				&& !(entity instanceof Schoolclass)) {
			academicYear = null;
		}
		if (!(entity instanceof Schoolclass)) {
			schoolclass = null;
		}
		if (!(entity instanceof Location) && !(entity instanceof Room)) {
			location = null;
		}
		if (!(entity instanceof Room)) {
			room = null;
		}
	}

	/**
	 * Gibt einen sinnvollen Namen für die lokale Aktivität zurück.
	 * 
	 * @return Ein sinnvoller Name
	 */
	public String getActivityDescriptionName() {
		return getActivityDescriptionName(activity);
	}

	/**
	 * Gibt einen sinnvollen Namen für eine übergebene Aktivität zurück.
	 * 
	 * @param activity
	 *            Die Aktivität, für die ein sinnvoller Name benötigt wird
	 * @return Ein sinnvoller Name
	 */
	public String getActivityDescriptionName(Activity activity) {

		String title = "";

		if (activity instanceof Lesson) {
			title += ((Lesson) activity).getLessonType().getName();
		} else if (activity instanceof Meeting) {
			title += ((Meeting) activity).getMeetingType().getName();
		} else if (activity instanceof CompoundLesson) {
			title += CompoundLesson.VALID_DISPLAY_NAME;
		}

		return title;
	}

	/**
	 * Diese Methode gibt an, ob es sich bei der derzeitigen {@link Activity} um
	 * eine {@link Lesson} handelt.
	 * 
	 * @return Wahrheitswert, ob es sich um eine Lesson handelt
	 */
	public Boolean getIsCurrentActivityLesson() {
		return activity instanceof Lesson;
	}

	/**
	 * Liefert die Startzeit der lokalen {@link Activity} in einem lesbaren
	 * Format zurück.
	 * 
	 * @return Die lesbare Zeit
	 */
	public String getActivityDescriptionStartTime() {
		return getActivityDescriptionStartTime(activity);
	}

	/**
	 * Liefert die Startzeit einer {@link Activity} in einem lesbaren Format
	 * zurück.
	 * 
	 * @param activity
	 *            Die {@link Activity}
	 * @return Die lesbare Zeit
	 */
	public String getActivityDescriptionStartTime(Activity activity) {
		if (activity == null) {
			return "";
		}
		return getActivityDescriptionTime(activity.getTime().getStartTime(),
				activity);
	}

	/**
	 * Liefert die Endzeit der lokalen {@link Activity} in einem lesbaren Format
	 * zurück.
	 * 
	 * @return Die lesbare Zeit
	 */
	public String getActivityDescriptionEndTime() {
		return getActivityDescriptionEndTime(activity);
	}

	/**
	 * Liefert die Endzeit einer {@link Activity} in einem lesbaren Format
	 * zurück.
	 * 
	 * @param activity
	 *            Die {@link Activity}
	 * @return Die lesbare Zeit
	 */
	public String getActivityDescriptionEndTime(Activity activity) {
		if (activity == null) {
			return "";
		}
		return getActivityDescriptionTime(activity.getTime().getEndTime(),
				activity);
	}

	/**
	 * Liefert die Uhrzeit der {@link Activity} in einem lesbaren Format zurück.
	 * 
	 * @param date
	 *            Das {@link Date}, dessen Zeit dargestellt werden soll
	 * @param activity
	 *            Die {@link Activity}, um deren Datum es geht {@link Weekday}
	 * @return Eine lesbare Zeit
	 */
	public String getActivityDescriptionTime(Date date, Activity activity) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		String readableMinutes;

		if (calendar.get(Calendar.MINUTE) > 0) {
			readableMinutes = String.valueOf(calendar.get(Calendar.MINUTE));
		} else {
			readableMinutes = "00";
		}

		String readableTime = activity.getTime().getDay().toString() + ", "
				+ calendar.get(Calendar.HOUR) + ":" + readableMinutes;

		return readableTime;
	}

	/**
	 * Gibt eine Liste mit Namen von Unterrichtsinhalten für die lokal
	 * gespeicherte {@link CompoundLesson} zurück.
	 * <p>
	 * Diese Methode sollte nur aufgerufen werden, wenn es sich bei der
	 * {@link Activity} wirklich um eine {@link CompoundLesson} handelt, da
	 * sonst eine leere Liste zurückgeliefert wird.
	 * 
	 * @return Eine Liste mit darstellbaren Namen.
	 */
	public List<String> getCompoundLessonDescriptionNames() {
		return getCompoundLessonDescriptionNames(activity);
	}

	/**
	 * Gibt die Namen der einzelnen Unterrichtsinhalte einer
	 * {@link CompoundLesson} zurück.
	 * <p>
	 * Diese Methode sollte nur aufgerufen werden, wenn es sich bei der
	 * {@link Activity} wirklich um eine {@link CompoundLesson} handelt, da
	 * sonst eine leere Liste zurückgeliefert wird.
	 * 
	 * @param activity
	 *            Die {@link CompoundLesson}, deren Unterrichtsinhalte benötigt
	 *            werden
	 * @return Eine Liste mit darstellbaren Namen
	 */
	public List<String> getCompoundLessonDescriptionNames(Activity activity) {
		List<String> names = new ArrayList<String>();

		if (activity instanceof CompoundLesson) {
			for (LessonType lessonType : ((CompoundLesson) activity)
					.getLessonTypes()) {
				String name = "";
				name += lessonType.getName();
				names.add(name);
			}
		}

		return names;
	}

	/**
	 * Liefert einen Wahrheitswert, ob die momentane Aktivität des Controllers
	 * eine {@link CompoundLesson} ist.
	 * 
	 * @return Wahrheitswert, ob die Aktivität eine {@link CompoundLesson} ist
	 */
	public Boolean getIsCurrentActivityCompoundLesson() {
		if (activity instanceof CompoundLesson) {
			return true;
		}

		return false;
	}

	/**
	 * Diese Methode gibt alle bekannten {@link LessonTypes} zurück
	 * 
	 * @return Liste mit allen bekannten {@link LessonTypes}
	 */
	public List<LessonType> getAllLessonTypes() {
		List<LessonType> lessonTypes = new ArrayList<>();

		try {
			lessonTypes = dataAccess.getAllLessonTypes();
		} catch (DatasetException e) {
			LOGGER.error(e);
		}

		return lessonTypes;
	}

	public void setLessonType(LessonType lessonType) {
		if (lessonType != null || activity instanceof Lesson) {
			((Lesson) activity).setLessonType(lessonType);
		}
	}
	
	public LessonType getLessonType() {
		if (activity instanceof Lesson) {
			return ((Lesson) activity).getLessonType();
		}
		
		return null;
	}
	
	public ActivityTypeEnum getActivityType() {
		return activityType;
	}

	public void setActivityType(ActivityTypeEnum activityType) {	
		this.activityType = activityType;
		activity = null;

		if (activityType.equals(ActivityTypeEnum.LESSON)) {
			activity = new Lesson();
			LessonType lessonType = new LessonType();
			((Lesson) activity).setLessonType(lessonType);
		}
	}

	// /////////////////////////////////////////////////////////////////////////
	// Getters & Setters
	// /////////////////////////////////////////////////////////////////////////

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
		unsetAllExcept(teacher);

	}

	public String getSearchTerm() {
		return searchTerm;
	}

	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}

	public Schoolclass getSchoolclass() {
		return schoolclass;
	}

	public void setSchoolclass(Schoolclass schoolclass) {
		this.schoolclass = schoolclass;
		unsetAllExcept(schoolclass);
	}

	public AcademicYear getAcademicYear() {
		return academicYear;
	}

	public void setAcademicYear(AcademicYear academicYear) {
		this.academicYear = academicYear;
		unsetAllExcept(academicYear);
	}

	public ScheduleModel getScheduleModel() {
		return scheduleModel;
	}

	public void setScheduleModel(ScheduleModel scheduleModel) {
		this.scheduleModel = scheduleModel;
	}

	public PedagogicAssistant getPedagogicAssistant() {
		return pedagogicAssistant;
	}

	public void setPedagogicAssistant(PedagogicAssistant pedagogicAssistant) {
		this.pedagogicAssistant = pedagogicAssistant;
		unsetAllExcept(pedagogicAssistant);
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
		unsetAllExcept(location);
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
		unsetAllExcept(room);
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}
}
