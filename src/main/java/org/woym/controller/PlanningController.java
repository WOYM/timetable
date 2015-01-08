package org.woym.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.woym.config.Config;
import org.woym.config.DefaultConfigEnum;
import org.woym.exceptions.DatasetException;
import org.woym.logic.util.ActivityParser;
import org.woym.objects.AcademicYear;
import org.woym.objects.Activity;
import org.woym.objects.CompoundLesson;
import org.woym.objects.Entity;
import org.woym.objects.Lesson;
import org.woym.objects.LessonType;
import org.woym.objects.Location;
import org.woym.objects.Meeting;
import org.woym.objects.PedagogicAssistant;
import org.woym.objects.Room;
import org.woym.objects.Schoolclass;
import org.woym.objects.Teacher;
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

	private Teacher teacher;
	private PedagogicAssistant pedagogicAssistant;
	private Schoolclass schoolclass;
	private AcademicYear academicYear;
	private Location location;
	private Room room;
	private Activity activity;

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

	public void onEventSelect(SelectEvent selectEvent) {
		ScheduleEvent event = (ScheduleEvent) selectEvent.getObject();

		setActivity((Activity) event.getData());
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

	public String getActivityDescriptionStartTime() {
		return getActivityDescriptionEndTime(activity);
	}

	public String getActivityDescriptionStartTime(Activity activity) {
		if(activity == null) {
			return "";
		}
		return getActivityDescriptionTime(activity.getTime().getStartTime(),
				activity);
	}

	public String getActivityDescriptionEndTime() {
		return getActivityDescriptionEndTime(activity);
	}

	public String getActivityDescriptionEndTime(Activity activity) {
		if(activity == null) {
			return "";
		}
		return getActivityDescriptionTime(activity.getTime().getEndTime(),
				activity);
	}

	public String getActivityDescriptionTime(Date date, Activity activity) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		String readableTime = activity.getTime().getDay().toString() + ", "
				+ calendar.get(Calendar.HOUR) + ":"
				+ calendar.get(Calendar.MINUTE);
		
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
