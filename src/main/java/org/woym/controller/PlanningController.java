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
import org.woym.config.Config;
import org.woym.config.DefaultConfigEnum;
import org.woym.exceptions.DatasetException;
import org.woym.objects.AcademicYear;
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

	private Teacher teacher;
	private Schoolclass schoolClass;
	private AcademicYear academicYear;

	private String searchTerm;

	/**
	 * Erzwingt die Erzeugung einer neuen User-Session vor dem Rendern des
	 * Views, sofern noch keine existiert. Wichtig für die Serialisierung aller
	 * Objekte.
	 */
	@PostConstruct
	public void init() {
		FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		
		searchTerm = "";
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
		calendar.set(1970, Calendar.JANUARY, 5, 0, 0, 0);

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
	 * @return Liste aller Lehrkräfte, deren Kürzel den Suchbegriff enthält.
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

	public Schoolclass getSchoolClass() {
		return schoolClass;
	}

	public void setSchoolClass(Schoolclass schoolClass) {
		this.schoolClass = schoolClass;
	}

	public AcademicYear getAcademicYear() {
		return academicYear;
	}

	public void setAcademicYear(AcademicYear academicYear) {
		this.academicYear = academicYear;
	}
}
