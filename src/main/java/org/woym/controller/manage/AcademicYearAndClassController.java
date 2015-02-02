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
import org.primefaces.context.RequestContext;
import org.woym.common.config.Config;
import org.woym.common.config.DefaultConfigEnum;
import org.woym.common.exceptions.DatasetException;
import org.woym.common.messages.GenericErrorMessage;
import org.woym.common.messages.MessageHelper;
import org.woym.common.objects.AcademicYear;
import org.woym.common.objects.Location;
import org.woym.common.objects.Schoolclass;
import org.woym.common.objects.spec.IMemento;
import org.woym.logic.CommandHandler;
import org.woym.logic.SuccessStatus;
import org.woym.logic.command.AddCommand;
import org.woym.logic.command.CommandCreator;
import org.woym.logic.command.MacroCommand;
import org.woym.logic.command.UpdateCommand;
import org.woym.logic.spec.IStatus;
import org.woym.logic.util.SchoolclassIdentifierUtil;
import org.woym.persistence.DataAccess;

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

	private static final long serialVersionUID = -720596768882714799L;

	private static Logger LOGGER = LogManager
			.getLogger(AcademicYearAndClassController.class.getName());

	private static int INITIAL_GENERATOR_SIZE = 1;
	private static int MAX_GENERATOR_SIZE = 20;

	private DataAccess dataAccess = DataAccess.getInstance();

	private CommandHandler commandHandler = CommandHandler.getInstance();
	private CommandCreator commandCreator = CommandCreator.getInstance();

	private AcademicYear academicYear;
	private Schoolclass schoolclass;
	private Location location;
	private IMemento academicYearMemento;
	private IMemento schoolclassMemento;

	private int generatorSize;

	private boolean hideDeletionDialog;
	private boolean hide;

	@PostConstruct
	public void init() {
		schoolclass = new Schoolclass();
		generatorSize = INITIAL_GENERATOR_SIZE;
		hideDeletionDialog = Config
				.getBooleanValue(DefaultConfigEnum.HIDE_SCHOOLCLASS_DELETION_DIALOG);
		hide = hideDeletionDialog;
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

	/**
	 * Wird vor dem Hinzufügen einer Schulklasse ausgeführt.
	 */
	public void doBeforeAdd() {
		location = null;
		schoolclass = new Schoolclass();
		academicYearMemento = academicYear.createMemento();

		schoolclass.setLessonDemands(academicYear.getLessonDemands());
	}

	public Boolean getExistAcademicYears() {
		if (getAcademicYears().size() == 0) {
			return false;
		}
		return true;
	}

	public List<Schoolclass> getSchoolclasses() {
		return academicYear.getSchoolclasses();
	}

	/**
	 * Diese Methode generiert automatisch Jahrgänge.
	 * 
	 * Es wird eine FacesMessage zurückgeliefert, die dem Nutzer Auskunft über
	 * Erfolg und Misserfolg gibt.
	 */
	public void generateAcademicYears() {
		FacesMessage msg;

		MacroCommand macroCommand = new MacroCommand();

		// For safety, should never happen
		if (generatorSize < INITIAL_GENERATOR_SIZE) {
			generatorSize = INITIAL_GENERATOR_SIZE;
		}

		if (generatorSize > MAX_GENERATOR_SIZE) {
			generatorSize = MAX_GENERATOR_SIZE;
		}

		try {
			int maxYear = getMaxYear();

			for (int i = 0; i < generatorSize; i++) {
				AcademicYear academicYear = new AcademicYear();
				academicYear.setAcademicYear(maxYear);

				AddCommand<AcademicYear> addCommand = new AddCommand<>(
						academicYear);
				macroCommand.add(addCommand);

				maxYear++;
			}

			IStatus status = commandHandler.execute(macroCommand);
			msg = status.report();

			generatorSize = INITIAL_GENERATOR_SIZE;

		} catch (DatasetException e) {
			LOGGER.error(e);
			msg = MessageHelper.generateMessage(
					GenericErrorMessage.DATABASE_COMMUNICATION_ERROR,
					FacesMessage.SEVERITY_ERROR);
		}

		FacesContext.getCurrentInstance().addMessage(null, msg);

	}

	/**
	 * Erzeugt einen neuen Jahrgang und persistiert diesen.
	 * 
	 * @deprecated Abgelöst durch
	 *             {@linkplain AcademicYearAndClassController#generateAcademicYears()}
	 */
	public void addAcademicYear() {
		FacesMessage msg;

		try {
			// Produce new valid AcademicYear
			academicYear = new AcademicYear();
			academicYear.setAcademicYear(getMaxYear());

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

	private int getMaxYear() throws DatasetException {
		int maxYear = 0;

		for (AcademicYear academicYear : dataAccess.getAllAcademicYears()) {
			if (maxYear < academicYear.getAcademicYear()) {
				maxYear = academicYear.getAcademicYear();
			}
		}
		maxYear += 1;

		return maxYear;
	}

	/**
	 * Gibt eine Liste mit gültigen Bezeichnern für diesen Jahrgang zurück.
	 * 
	 * @return Liste mit Bezeichnern
	 */
	public List<Character> getValidIdentifiers() {

		List<Character> validIdentifiers = new ArrayList<>();

		try {

			return SchoolclassIdentifierUtil
					.getAvailableCharacters(academicYear);

		} catch (DatasetException e) {
			LOGGER.error(e);
			FacesMessage msg = MessageHelper.generateMessage(
					GenericErrorMessage.DATABASE_COMMUNICATION_ERROR,
					FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

		return validIdentifiers;
	}

	/**
	 * Fügt eine neue Klasse dem Jahrgang hinzu.
	 */
	public void addSchoolclass() {
		academicYear.add(schoolclass);

		UpdateCommand<AcademicYear> command = new UpdateCommand<AcademicYear>(
				academicYear, academicYearMemento);
		IStatus status = commandHandler.execute(command);
		FacesMessage msg = status.report();

		if (status instanceof SuccessStatus) {
			location = null;
			schoolclass = new Schoolclass();
			academicYearMemento = academicYear.createMemento();
		}

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	/**
	 * Führt die benötigten Operationen vor dem Editieren einer Schulklasse aus.
	 */
	public void doBeforeEdit() {
		schoolclassMemento = schoolclass.createMemento();

		if (schoolclass.getRoom() != null) {
			try {
				location = dataAccess.getOneLocation(schoolclass.getRoom());
			} catch (DatasetException e) {
				LOGGER.error(e);

				FacesMessage msg = MessageHelper.generateMessage(
						GenericErrorMessage.DATABASE_COMMUNICATION_ERROR,
						FacesMessage.SEVERITY_ERROR);
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		}
	}

	/**
	 * Aktualisiert den Zustand der gewählten Schulklasse in der Datenbank.
	 */
	public void editSchoolclass() {
		UpdateCommand<Schoolclass> command = new UpdateCommand<Schoolclass>(
				schoolclass, schoolclassMemento);
		IStatus status = commandHandler.execute(command);
		FacesMessage msg = status.report();

		if (status instanceof SuccessStatus) {
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('wEditSchoolclassDialog').hide();");
		}

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	/**
	 * Löscht eine Klasse aus der Datennbank.
	 */
	public void deleteSchoolclass() {
		if (hide != hideDeletionDialog) {
			Config.updateProperty(
					DefaultConfigEnum.HIDE_SCHOOLCLASS_DELETION_DIALOG
							.getPropKey(), String.valueOf(hideDeletionDialog));
		}
		MacroCommand macroCommand = commandCreator
				.createDeleteCommand(schoolclass);
		IStatus status = commandHandler.execute(macroCommand);
		FacesMessage msg = status.report();

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public int getMaxGeneratorSize() {
		return MAX_GENERATOR_SIZE;
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
		if (schoolclass != null) {
			this.schoolclass = schoolclass;
		}
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public int getGeneratorSize() {
		return generatorSize;
	}

	public void setGeneratorSize(int generatorSize) {
		this.generatorSize = generatorSize;
	}

	public boolean isHideDeletionDialog() {
		return hideDeletionDialog;
	}

	public void setHideDeletionDialog(boolean hideDeletionDialog) {
		this.hideDeletionDialog = hideDeletionDialog;
	}

}
