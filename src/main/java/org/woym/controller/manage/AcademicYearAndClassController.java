package org.woym.controller.manage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.woym.exceptions.DatasetException;
import org.woym.logic.CommandHandler;
import org.woym.logic.command.AddCommand;
import org.woym.logic.command.DeleteCommand;
import org.woym.messages.GenericErrorMessage;
import org.woym.messages.MessageHelper;
import org.woym.objects.AcademicYear;
import org.woym.objects.Location;
import org.woym.objects.Schoolclass;
import org.woym.persistence.DataAccess;
import org.woym.spec.logic.IStatus;

/**
 * <h1>AcademicYearAndClassController</h1>
 * <p>
 * Dieser Controller verwaltet die verschiedenen Jahrgänge und Klassen der
 * Jahrgänge.
 * 
 * @author Tim Hansen (tihansen)
 *
 */
@ViewScoped
@ManagedBean(name = "academicYearAndClassController")
public class AcademicYearAndClassController implements Serializable {

	private static final long serialVersionUID = 1L;

	private static Logger LOGGER = LogManager
			.getLogger(AcademicYearAndClassController.class);

	private DataAccess dataAccess = DataAccess.getInstance();

	private CommandHandler commandHandler = CommandHandler.getInstance();

	private AcademicYear academicYear;
	private Schoolclass schoolclass;
	private Location location;

	@PostConstruct
	public void init() {
		schoolclass = new Schoolclass();
	}
	
	/**
	 * Liefert eine Liste mit allen bekannten Jahrgängen zurück.
	 * 
	 * @return Liste mit Jahrgängen
	 */
	public List<AcademicYear> getAcademicYears() {
		try {
			return dataAccess.getAllAcademicYears();
		} catch (DatasetException e) {
			LOGGER.error(e);
			FacesMessage msg = MessageHelper.generateMessage(
					GenericErrorMessage.DATABASE_COMMUNICATION_ERROR,
					FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, msg);

			return new ArrayList<AcademicYear>();
		}
	}
	
	public List<Schoolclass> getSchoolclasses(AcademicYear academicYear) {
		return academicYear.getSchoolclasses();
	}

	/**
	 * Erzeugt einen neuen Jahrgang und persistiert diesen.
	 */
	public void addAcademicYear() {
		FacesMessage msg;

		try {
			// Produce new valid AcademicYear
			academicYear = new AcademicYear();
			int maxYear = 0;

			for (AcademicYear academicYear : dataAccess.getAllAcademicYears()) {
				if (maxYear < academicYear.getAcademicYear()) {
					maxYear = academicYear.getAcademicYear();
				}
			}
			maxYear += 1;
			academicYear.setAcademicYear(maxYear);

			AddCommand<AcademicYear> command = new AddCommand<>(academicYear);
			IStatus status = commandHandler.execute(command);
			msg = status.report();

		} catch (DatasetException e) {
			LOGGER.error(e);
			msg = MessageHelper.generateMessage(
					GenericErrorMessage.DATABASE_COMMUNICATION_ERROR,
					FacesMessage.SEVERITY_ERROR);
		}

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	/**
	 * Fügt eine neue Klasse dem Jahrgang hinzu.
	 */
	public void addSchoolclass() {
		// Check for correct data
		if(location != null && schoolclass.getRoom() != null && schoolclass.getTeacher() != null) {
			
		}
	}
	
	/**
	 * Löscht einen Jahrgang aus der Datennbank.
	 */
	public void deleteAcademicYear() {
		DeleteCommand<AcademicYear> command = new DeleteCommand<>(academicYear);
		IStatus status = commandHandler.execute(command);
		FacesMessage msg = status.report();

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public AcademicYear getAcademicYear() {
		return academicYear;
	}

	public void setAcademicYear(AcademicYear academicYear) {
		this.academicYear = academicYear;
	}

	public Schoolclass getSchoolclass() {
		return schoolclass;
	}

	public void setSchoolclass(Schoolclass schoolclass) {
		this.schoolclass = schoolclass;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
}
