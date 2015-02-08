package org.woym.controller.planning;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.woym.common.config.Config;
import org.woym.common.config.DefaultConfigEnum;
import org.woym.common.exceptions.DatasetException;
import org.woym.common.messages.GenericErrorMessage;
import org.woym.common.messages.MessageHelper;
import org.woym.common.objects.AcademicYear;
import org.woym.common.objects.Activity;
import org.woym.common.objects.ActivityTO;
import org.woym.common.objects.ActivityType;
import org.woym.common.objects.ActivityTypeEnum;
import org.woym.common.objects.CompoundLesson;
import org.woym.common.objects.EmployeeTimePeriods;
import org.woym.common.objects.Lesson;
import org.woym.common.objects.LessonType;
import org.woym.common.objects.Location;
import org.woym.common.objects.Meeting;
import org.woym.common.objects.Pause;
import org.woym.common.objects.PedagogicAssistant;
import org.woym.common.objects.Room;
import org.woym.common.objects.Schoolclass;
import org.woym.common.objects.Teacher;
import org.woym.common.objects.TimePeriod;
import org.woym.common.objects.Weekday;
import org.woym.common.objects.spec.IMemento;
import org.woym.logic.CommandHandler;
import org.woym.logic.SuccessStatus;
import org.woym.logic.command.CommandCreator;
import org.woym.logic.command.MacroCommand;
import org.woym.logic.command.UpdateCommand;
import org.woym.logic.spec.IStatus;
import org.woym.logic.util.ActivityValidator;
import org.woym.persistence.DataAccess;
import org.woym.ui.util.ActivityTOHolder;
import org.woym.ui.util.EmployeeDailyViewHelper;
import org.woym.ui.util.EntityHelper;
import org.woym.ui.util.PersonalPlanHelper;
import org.woym.ui.util.PersonalPlanRow;
import org.woym.ui.util.ScheduleModelHolder;

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
	private final CommandCreator commandCreator = CommandCreator.getInstance();

	private ActivityValidator activityValidator = ActivityValidator
			.getInstance();
	private CommandHandler commandHandler = CommandHandler.getInstance();
	private EntityHelper entityHelper = EntityHelper.getInstance();

	private Activity activity;

	private List<Weekday> weekdays = Arrays.asList(Weekday.values());

	private String searchTerm;

	private ScheduleModelHolder scheduleModelHolder = ScheduleModelHolder
			.getInstance();
	private ActivityTOHolder activityTOHolder = ActivityTOHolder.getInstance();

	private List<Weekday> validWeekdays;

	private Weekday dailyViewWeekday;

	@ManagedProperty(value = "#{lessonController}")
	private LessonController lessonController;

	@ManagedProperty(value = "#{meetingController}")
	private MeetingController meetingController;

	@ManagedProperty(value = "#{pauseController}")
	private PauseController pauseController;

	@ManagedProperty(value = "#{compoundLessonController}")
	private CompoundLessonController compoundLessonController;

	/**
	 * Erzwingt die Erzeugung einer neuen User-Session vor dem Rendern des
	 * Views, sofern noch keine existiert. Wichtig für die Serialisierung aller
	 * Objekte.
	 */
	@PostConstruct
	public void init() {
		FacesContext.getCurrentInstance().getExternalContext().getSession(true);

		searchTerm = "";
		validWeekdays = getValidWeekdays();
		dailyViewWeekday = Weekday.MONDAY;

		scheduleModelHolder.setScheduleModel(scheduleModelHolder
				.emptyScheduleModel());
		entityHelper.unsetAll();
	}

	/**
	 * Diese Methode initialisiert vor dem Öffnen des Dialoges die einzelnen
	 * Controller.
	 */
	public void dialogInit() {
		lessonController.init();
		meetingController.init();
		compoundLessonController.init();
		pauseController.init();
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
	 * Liefert den Minimalwert für Stunden zurück.
	 * 
	 * @return Der Minimalwert für Stunden als Ganzzahl
	 */
	public int getMinHour() {
		int hours = 0;

		String minTime = getMinTime();
		minTime = minTime.substring(0, 2);

		try {
			hours = Integer.parseInt(minTime);
		} catch (NumberFormatException e) {
			LOGGER.warn("Illegal input for hours. This is a config-problem.");
		}

		return hours;
	}

	/**
	 * Liefert den Maximalwert für Stunden zurück.
	 * 
	 * @return Der Maximalwert für Stunden als Ganzzahl
	 */
	public int getMaxHour() {
		int hours = 24;

		String maxTime = getMaxTime();
		maxTime = maxTime.substring(0, 2);

		try {
			hours = Integer.parseInt(maxTime);
		} catch (NumberFormatException e) {
			LOGGER.warn("Illegal input for hours. This is a config-problem.");
		}

		return hours;
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
	 * Diese Methode liefert einen Wahrheitswert zurück, der Aussage darüber
	 * macht, ob Samstag und Sonntag in der GUI gezeigt werden sollen.
	 * 
	 * @return Wahrheitswert, ob Samstag und Sonntag dargestellt werden sollen
	 */
	public Boolean getHasWeekend() {
		if (getValidWeekdays().contains(Weekday.SATURDAY)
				|| getValidWeekdays().contains(Weekday.SUNDAY)) {
			return true;
		}
		return false;
	}

	/**
	 * Diese Methode liefert mit Hilfe des {@link PersonalPlanHelper}s eine
	 * Liste von {@link PersonalPlanRow}-Objekten zurück.
	 * 
	 * @return Eine Liste von {@link PersonalPlanRow}-Objekten
	 */
	public List<PersonalPlanRow> getPersonalPlanRows() {
		PersonalPlanHelper personalPlanHelper = PersonalPlanHelper
				.getInstance();
		return personalPlanHelper.getPersonalPlanRows();
	}

	/**
	 * Diese Methode liefert mit Hilfe des {@link PersonalPlanHelper}s eine
	 * Liste von {@link EmployeeDailyViewHelper}-Objekten zurück.
	 * 
	 * @return Eine Liste von {@link EmployeeDailyViewHelper}-Objekten
	 */
	public List<EmployeeDailyViewHelper> getEmployeeDailyViewHelpers() {
		PersonalPlanHelper personalPlanHelper = PersonalPlanHelper
				.getInstance();

		return personalPlanHelper.getEmployeeDailyViews(dailyViewWeekday);
	}

	/**
	 * Liefert das {@link Date} für die Tagesanzeige zurück
	 * 
	 * @return Das {@link Date} für die Tagesanzeige
	 */
	public Date getDailyViewDate() {
		Date date = getInitalDate();

		date = changeDateByDelta(date, dailyViewWeekday.getOrdinal(), 0);

		return date;
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
	 * Diese Methode löscht die momentan in der Bean bekannte {@link Activity}
	 */
	public void deleteActivity() {
		if (activity == null) {
			return;
		}

		MacroCommand command = commandCreator.createDeleteCommand(activity);
		IStatus status = commandHandler.execute(command);

		if (status instanceof SuccessStatus) {
			activity = null;
		}

		scheduleModelHolder.updateScheduleModel();
		FacesMessage msg = status.report();
		FacesContext.getCurrentInstance().addMessage(null, msg);
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
		} else if (!validWeekdays.contains(Weekday.getByOrdinal(localDayDelta))) {
			msg = MessageHelper.generateMessage(
					GenericErrorMessage.INVALID_WEEKDAY,
					FacesMessage.SEVERITY_ERROR);
		} else if (isTimeInRange(startTime, endTime)) {
			msg = MessageHelper.generateMessage(
					GenericErrorMessage.TIME_OUTSIDE_LIMIT,
					FacesMessage.SEVERITY_ERROR);
		} else {
			IMemento activityMemento = activity.createMemento();

			// TODO Zeit Validieren
			TimePeriod time = new TimePeriod();
			time.setStartTime(startTime);
			time.setEndTime(endTime);
			time.setDay(Weekday.getByOrdinal(localDayDelta));

			IStatus status = activityValidator.validateActivity(activity, time);

			if (status instanceof SuccessStatus) {

				activity.setTime(time);

				for (EmployeeTimePeriods timePeriods : activity
						.getEmployeeTimePeriods()) {
					for (TimePeriod timePeriod : timePeriods.getTimePeriods()) {
						// TODO Would not work with multiple-teacher-periods
						timePeriod.setDay(time.getDay());
						timePeriod.setStartTime(startTime);
						timePeriod.setEndTime(endTime);
					}
				}
				UpdateCommand<Activity> command = new UpdateCommand<Activity>(
						activity, activityMemento);

				status = commandHandler.execute(command);
			}

			msg = status.report();
		}

		scheduleModelHolder.updateScheduleModel();
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	/**
	 * Bestimmt ob die übergebenen Zeiter außerhalb , der durch die
	 * Einstellungen festgelegten, Grenzen liegen.
	 * 
	 * @param startTime
	 *            Startzeit
	 * @param endTime
	 *            Endzeit
	 * @return Wahrheitswert ob sie außerhalb der Grenzen sind.
	 */
	@SuppressWarnings("deprecation")
	private boolean isTimeInRange(final Date startTime, final Date endTime) {
		SimpleDateFormat timeLimit = new SimpleDateFormat("HH:mm");
		Date startingTimeLimit = new Date();
		startingTimeLimit.setTime(startTime.getTime());
		Date endingTimeLimit = new Date();
		endingTimeLimit.setTime(endTime.getTime());
		try {
			Date localDate = timeLimit.parse(Config
					.getSingleStringValue(DefaultConfigEnum.WEEKDAY_STARTTIME));
			startingTimeLimit.setMinutes(localDate.getMinutes());
			startingTimeLimit.setHours(localDate.getHours());

			localDate = timeLimit.parse(Config
					.getSingleStringValue(DefaultConfigEnum.WEEKDAY_ENDTIME));
			endingTimeLimit.setMinutes(localDate.getMinutes());
			endingTimeLimit.setHours(localDate.getHours());
		} catch (ParseException e) {
			LOGGER.warn("Parse Error on: " + e.getMessage());
		}
		return startTime.before(startingTimeLimit)
				|| endTime.after(endingTimeLimit);

	}

	/**
	 * Wird aufgerufen, wenn in der Stundenplandarstellung auf einen leeren
	 * Zeitslot geklickt wird.
	 * <p>
	 * Erzeugt ein neues leeres {@link ActivityTO} und speicher es im
	 * {@link ActivityTOHolder}
	 * 
	 * @param selectEvent
	 *            Das Selektierungsevent
	 */
	public void onDateSelect(SelectEvent selectEvent) {
		Date date = (Date) selectEvent.getObject();
		activityTOHolder.plainActivityTO();

		if (!validWeekdays.contains(Weekday.getByDate(date))) {
			return;
		}

		TimePeriod timePeriod = activityTOHolder.getActivityTO()
				.getTimePeriod();
		timePeriod.setStartTime(date);
		timePeriod.setEndTime(date);
		timePeriod.setDay(Weekday.getByDate(date));

		activityTOHolder.getActivityTO().setTimePeriod(timePeriod);

		dialogInit();
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

		Date startTime = activity.getTime().getStartTime();
		Date endTime = changeDateByDelta(activity.getTime().getEndTime(),
				event.getDayDelta(), event.getMinuteDelta());

		// TODO Zeit Validieren
		TimePeriod time = new TimePeriod();
		time.setStartTime(startTime);
		time.setEndTime(endTime);
		time.setDay(activity.getTime().getDay());

		IStatus status = activityValidator.validateActivity(activity, time);
		if (isTimeInRange(startTime, endTime)) {
			msg = MessageHelper.generateMessage(
					GenericErrorMessage.TIME_OUTSIDE_LIMIT,
					FacesMessage.SEVERITY_ERROR);
		} else {

			if (status instanceof SuccessStatus) {

				MacroCommand macro = commandCreator
						.createEmployeeUpdateSubtractWorkingHours(activity);
				activity.setTime(time);
				for (EmployeeTimePeriods timePeriods : activity
						.getEmployeeTimePeriods()) {
					for (TimePeriod timePeriod : timePeriods.getTimePeriods()) {
						// TODO Would not work with multiple-teacher-periods
						timePeriod.setDay(time.getDay());
						timePeriod.setStartTime(activity.getTime()
								.getStartTime());
						timePeriod.setEndTime(endTime);
					}
				}
				macro.addAll(commandCreator
						.createEmployeeUpdateAddWorkingHours(activity));
				macro.add(new UpdateCommand<Activity>(activity, activityMemento));

				status = commandHandler.execute(macro);
			}
			msg = status.report();
		}
		scheduleModelHolder.updateScheduleModel();
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
	 * Gibt eine Liste aller zu verplanenden Wochentage zurück.
	 * 
	 * @return Liste aller zu verplanenden Wochentage
	 */
	public List<Weekday> getValidWeekdays() {
		if (validWeekdays == null) {
			validWeekdays = Config.getValidWeekdays();
		}
		return validWeekdays;
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
		List<Schoolclass> schoolclasses = new ArrayList<>();

		if (entityHelper.getAcademicYear() != null) {
			schoolclasses = entityHelper.getAcademicYear().getSchoolclasses();
		}

		return schoolclasses;
	}

	/**
	 * Diese Methode liefert alle Räume für einen bestimmten Standort zurück.
	 * Dies lässt die Auswahl über ein Dropdown-Menü zu.
	 * 
	 * @return Eine Liste aller Räume für einen Standort
	 */
	public List<Room> getRoomsForLocation() {
		List<Room> rooms = new ArrayList<>();

		if (entityHelper.getLocation() != null) {
			rooms = entityHelper.getLocation().getRooms();
		}

		return rooms;
	}

	/**
	 * Diese Methode wird vor dem Hinzufügen einer Aktivität aufgerufen.
	 */
	public void doBeforeAdd() {
		activityTOHolder.plainActivityTO();
		dialogInit();
	}

	/**
	 * Diese Methode gibt an, ob eine Lehrkraft oder eine Klasse ausgewählt
	 * wurde. So wird bestimmt, ob ein Stundenplan gerendert wird.
	 * 
	 * @return Wahrheitswert, ob ein Objekt gewählt wurde
	 */
	public Boolean getHasChosen() {
		return entityHelper.getHasEntity();
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

	public List<Weekday> getWeekdays() {
		return weekdays;
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
		if (entityHelper.getTeacher() != null) {
			scheduleModelHolder.setEntity(entityHelper.getTeacher());
			scheduleModelHolder.updateScheduleModel();
		}
	}

	/**
	 * Setzt das ActivityModel für einen pädagogischen Mitarbeiter.
	 */
	public void setPedagogicAssistantActivityModel() {
		if (entityHelper.getPedagogicAssistant() != null) {
			scheduleModelHolder.setEntity(entityHelper.getPedagogicAssistant());
			scheduleModelHolder.updateScheduleModel();
		}
	}

	/**
	 * Setzt das ActivityModel für eine Schulklasse.
	 */
	public void setSchoolclassActivityModel() {
		if (entityHelper.getSchoolclass() != null) {
			scheduleModelHolder.setEntity(entityHelper.getSchoolclass());
			scheduleModelHolder.updateScheduleModel();
		}
	}

	/**
	 * Setzt das ActivityModel für einen Raum.
	 */
	public void setRoomActivityModel() {
		if (entityHelper.getRoom() != null) {
			scheduleModelHolder.setEntity(entityHelper.getRoom());
			scheduleModelHolder.updateScheduleModel();
		}
	}

	/**
	 * /** Gibt einen sinnvollen Namen für die lokale Aktivität zurück.
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
		return activityTOHolder.getActivityTO().getActivityTypeEnum()
				.equals(ActivityTypeEnum.LESSON);
	}

	/**
	 * Diese Methode gibt an, ob es sich bei der derzeitigen {@link Activity} um
	 * ein {@link Meeting} handelt.
	 * 
	 * @return Wahrheitswert, ob es sich um eine Lesson handelt
	 */
	public Boolean getIsCurrentActivityMeeting() {
		return activityTOHolder.getActivityTO().getActivityTypeEnum()
				.equals(ActivityTypeEnum.MEETING);
	}

	/**
	 * Diese Methode gibt an, ob es sich bei der derzeitigen {@link Activity} um
	 * eine {@link Pause} handelt.
	 * 
	 * @return Wahrheitswert, ob es sich um eine Lesson handelt
	 */
	public Boolean getIsCurrentActivityPause() {
		return activityTOHolder.getActivityTO().getActivityTypeEnum()
				.equals(ActivityTypeEnum.PAUSE);
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
				+ calendar.get(Calendar.HOUR_OF_DAY) + ":" + readableMinutes;

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

	public Boolean getIsDialogActivityCompoundLesson() {
		return activity instanceof CompoundLesson;
	}

	/**
	 * Liefert einen Wahrheitswert, ob die momentane Aktivität des Controllers
	 * eine {@link CompoundLesson} ist.
	 * 
	 * @return Wahrheitswert, ob die Aktivität eine {@link CompoundLesson} ist
	 */
	public Boolean getIsCurrentActivityCompoundLesson() {
		return activityTOHolder.getActivityTO().getActivityTypeEnum()
				.equals(ActivityTypeEnum.COMPOUND_LESSON);
	}

	public ActivityTypeEnum getActivityType() {
		return activityTOHolder.getActivityTO().getActivityTypeEnum();
	}

	public void setActivityType(ActivityTypeEnum activityType) {
		activityTOHolder.getActivityTO().setActivityTypeEnum(activityType);
	}

	// /////////////////////////////////////////////////////////////////////////
	// Getters & Setters
	// /////////////////////////////////////////////////////////////////////////

	public Teacher getTeacher() {
		return entityHelper.getTeacher();
	}

	public void setTeacher(Teacher teacher) {
		entityHelper.setTeacher(teacher);

	}

	public String getSearchTerm() {
		return searchTerm;
	}

	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}

	public Schoolclass getSchoolclass() {
		return entityHelper.getSchoolclass();
	}

	public void setSchoolclass(Schoolclass schoolclass) {
		entityHelper.setSchoolclass(schoolclass);
	}

	public AcademicYear getAcademicYear() {
		return entityHelper.getAcademicYear();
	}

	public void setAcademicYear(AcademicYear academicYear) {
		entityHelper.setAcademicYear(academicYear);
	}

	public ScheduleModel getScheduleModel() {
		return scheduleModelHolder.getScheduleModel();
	}

	public void setScheduleModel(ScheduleModel scheduleModel) {
		this.scheduleModelHolder.setScheduleModel(scheduleModel);
	}

	public PedagogicAssistant getPedagogicAssistant() {
		return entityHelper.getPedagogicAssistant();
	}

	public void setPedagogicAssistant(PedagogicAssistant pedagogicAssistant) {
		entityHelper.setPedagogicAssistant(pedagogicAssistant);
	}

	public Location getLocation() {
		return entityHelper.getLocation();
	}

	public void setLocation(Location location) {
		entityHelper.setLocation(location);
	}

	public Room getRoom() {
		return entityHelper.getRoom();
	}

	public void setRoom(Room room) {
		entityHelper.setRoom(room);
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public LessonController getLessonController() {
		return lessonController;
	}

	public void setLessonController(LessonController lessonController) {
		this.lessonController = lessonController;
	}

	public MeetingController getMeetingController() {
		return meetingController;
	}

	public void setMeetingController(MeetingController meetingController) {
		this.meetingController = meetingController;
	}

	public PauseController getPauseController() {
		return pauseController;
	}

	public void setPauseController(PauseController pauseController) {
		this.pauseController = pauseController;
	}

	public CompoundLessonController getCompoundLessonController() {
		return compoundLessonController;
	}

	public void setCompoundLessonController(
			CompoundLessonController compoundLessonController) {
		this.compoundLessonController = compoundLessonController;
	}

	public Weekday getDailyViewWeekday() {
		return dailyViewWeekday;
	}

	public void setDailyViewWeekday(Weekday weekday) {
		this.dailyViewWeekday = weekday;
	}
}
