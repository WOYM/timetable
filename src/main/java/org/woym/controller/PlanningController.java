package org.woym.controller;

import java.io.Serializable;
import java.util.ArrayList;
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
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;
import org.woym.config.Config;
import org.woym.config.DefaultConfigEnum;
import org.woym.exceptions.DatasetException;
import org.woym.messages.GenericErrorMessage;
import org.woym.messages.MessageHelper;
import org.woym.objects.AcademicYear;
import org.woym.objects.Activity;
import org.woym.objects.Entity;
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

	private DataAccess dataAccess = DataAccess.getInstance();
	private static Logger LOGGER = LogManager
			.getLogger(PlanningController.class);

	private static final int CALENDAR_YEAR = 1970;
	private static final int CALENDAR_MONTH = Calendar.JANUARY;
	private static final int CALENDAR_DAY = 5;

	private Teacher teacher;
	private Schoolclass schoolclass;
	private AcademicYear academicYear;

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
	 * Diese Methode liefert alle dem System bekannten Jahrgänge zurück.
	 * 
	 * @return Eine Liste aller Jahrgänge
	 */
	public List<AcademicYear> getAllAcademicYears() {
		List<AcademicYear> academicYears;

		try {
			academicYears = dataAccess.getAllAcademicYears();
		} catch (DatasetException e) {
			LOGGER.error(e);
			academicYears = new ArrayList<>();
		}

		return academicYears;
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
	 * Diese Methode gibt an, ob eine Lehrkraft oder eine Klasse ausgewählt
	 * wurde. So wird bestimmt, ob ein Stundenplan gerendert wird.
	 * 
	 * @return Wahrheitswert, ob ein Objekt gewählt wurde
	 */
	public Boolean getHasChosen() {
		if (teacher != null || schoolclass != null) {
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
		List<Teacher> teachers;
		try {
			teachers = dataAccess.getAllTeachers();

			if (teachers.size() > 0) {
				return true;
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
			setActivityModel(teacher);
		}
	}

	/**
	 * Lädt und setzt das ActivityModel des Controllers für ein bestimmtes
	 * Objekt.
	 * <p>
	 * Mögliche Objekte:
	 * <ul>
	 * <li>Teacher</li>
	 * <li>Schoolclass</li>
	 * </ul>
	 * 
	 * Bei einem Datenbankfehler wird eine Nachricht auf der GUI dargestellt und
	 * das ActivityModel nicht verändert.
	 * 
	 * @param entity
	 *            Die Entity, deren ActivityModel gesetzt werden soll
	 * @throws IllegalArgumentException
	 *             Wenn eine ungültige Entity übergeben wird
	 */
	private void setActivityModel(Entity entity) {
		try {

			List<Activity> activities = new ArrayList<>();

			// Validate
			if (entity instanceof Teacher) {
				activities = dataAccess.getAllActivities(teacher);
			} else if (entity instanceof Schoolclass) {
				activities = dataAccess.getAllActivities(schoolclass);
			} else {
				throw new IllegalArgumentException();
			}

			ScheduleModel activityModel = new DefaultScheduleModel();

			// Iteration
			for (Activity activity : activities) {
				Date startDate = getActivityStartDate(activity);
				Date endDate = getActivityEndDate(activity);

				// TODO Style-Class?
				DefaultScheduleEvent event = new DefaultScheduleEvent(
						activity.toString(), startDate, endDate);

				activityModel.addEvent(event);
			}

			// Set scheduleModel
			scheduleModel = activityModel;

		} catch (DatasetException e) {
			LOGGER.error(e);
			FacesMessage msg = MessageHelper.generateMessage(
					GenericErrorMessage.DATABASE_COMMUNICATION_ERROR,
					FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
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
	 * Erzeugt ein {@link Date}-Objekt, dass in der Stundenplandarstellung
	 * angezeigt werden kann.
	 * 
	 * @param activity
	 *            Die Aktivität
	 * @param time
	 *            Die Zeit in Millisekunden
	 * @return Das darstellbare Datum
	 */
	private Date getActivityDate(Activity activity, long time) {
		int day = CALENDAR_DAY + activity.getTime().getDay().getOrdinal();

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		calendar.set(CALENDAR_YEAR, CALENDAR_MONTH, day);

		return calendar.getTime();
	}

	// /////////////////////////////////////////////////////////////////////////
	// Getters & Setters
	// /////////////////////////////////////////////////////////////////////////

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
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
	}

	public AcademicYear getAcademicYear() {
		return academicYear;
	}

	public void setAcademicYear(AcademicYear academicYear) {
		this.academicYear = academicYear;
	}
	
	public ScheduleModel getScheduleModel() {
		return scheduleModel;
	}

	public void setScheduleModel(ScheduleModel scheduleModel) {
		this.scheduleModel = scheduleModel;
	}
}
