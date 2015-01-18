package org.woym.controller.manage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.woym.common.config.Config;
import org.woym.common.config.DefaultConfigEnum;
import org.woym.common.exceptions.DatasetException;
import org.woym.common.messages.GenericErrorMessage;
import org.woym.common.messages.MessageHelper;
import org.woym.common.objects.Classteam;
import org.woym.common.objects.Employee;
import org.woym.common.objects.PedagogicAssistant;
import org.woym.common.objects.Schoolclass;
import org.woym.common.objects.Teacher;
import org.woym.common.objects.spec.IMemento;
import org.woym.logic.CommandHandler;
import org.woym.logic.SuccessStatus;
import org.woym.logic.command.AddCommand;
import org.woym.logic.command.CommandCreator;
import org.woym.logic.command.MacroCommand;
import org.woym.logic.command.UpdateCommand;
import org.woym.logic.spec.IStatus;
import org.woym.persistence.DataAccess;

/**
 * Dieser Controller verwaltet die Klassenteams.
 * 
 * @author Adrian
 *
 */
@ViewScoped
@ManagedBean(name = "classteamController")
public class ClassteamController implements Serializable {

	private static final long serialVersionUID = 4989035440868809117L;

	/**
	 * Logger der Klasse.
	 */
	private static final Logger LOGGER = LogManager
			.getLogger(ClassteamController.class.getName());

	private DataAccess dataAccess = DataAccess.getInstance();

	/**
	 * Das in der Tabelle selektierte Klassenteam.
	 */
	private Classteam selectedClassteam;

	private IMemento classteamMemento;

	/**
	 * Die im Dialog selektierten Mitarbeiter.
	 */
	private List<Employee> selectedEmployees = new ArrayList<Employee>();
	/**
	 * Die im Dialog selektierten Schulklassen.
	 */
	private List<Schoolclass> selectedSchoolclasses = new ArrayList<Schoolclass>();

	private boolean hideDeletionDialog;
	private boolean hide;

	@PostConstruct
	public void init() {
		hideDeletionDialog = Config
				.getBooleanValue(DefaultConfigEnum.HIDE_CLASSTEAM_DELETION_DIALOG);
		hide = hideDeletionDialog;
	}

	/**
	 * Sofern mindestens ein Lehrer unter den selektierten Mitarbeitern ist,
	 * erzeugt diese Methode ein neues Klassenteam mit den selektierten Lehrern
	 * und Klassen.
	 */
	public void addClassteam() {
		if (!validate()) {
			return;
		}
		Classteam classteam = new Classteam();
		classteam.setEmployees(new ArrayList<Employee>(selectedEmployees));
		classteam.setSchoolclasses(new ArrayList<Schoolclass>(selectedSchoolclasses));
		IStatus status = CommandHandler.getInstance().execute(
				new AddCommand<Classteam>(classteam));

		if (status instanceof SuccessStatus) {
			selectedEmployees.clear();
			selectedSchoolclasses.clear();
		}

		FacesContext.getCurrentInstance().addMessage(null, status.report());
	}

	/**
	 * Erzeugt ein UpdateCommand für das selektierte Klassenteam mit den neu
	 * gewählten Mitarbeitern und Schulklassen und führt es aus.
	 */
	public void editClassteam() {
		if (!validate()) {
			return;
		}
		selectedClassteam.setEmployees(selectedEmployees);
		selectedClassteam.setSchoolclasses(selectedSchoolclasses);
		IStatus status = CommandHandler.getInstance().execute(
				new UpdateCommand<Classteam>(selectedClassteam,
						classteamMemento));
		if (status instanceof SuccessStatus) {
			selectedEmployees.clear();
			selectedSchoolclasses.clear();
		}

		FacesContext.getCurrentInstance().addMessage(null, status.report());
	}

	/**
	 * Erzeugt ein MacroCommand zum Löschen des selektieren Klassenteams und
	 * führt es aus.
	 */
	public void deleteClassteam() {
		if (hide != hideDeletionDialog) {
			Config.updateProperty(
					DefaultConfigEnum.HIDE_CLASSTEAM_DELETION_DIALOG
							.getPropKey(), String.valueOf(hideDeletionDialog));
		}
		MacroCommand macro = CommandCreator.getInstance().createDeleteCommand(
				selectedClassteam);
		IStatus status = CommandHandler.getInstance().execute(macro);
		selectedClassteam = null;

		FacesContext.getCurrentInstance().addMessage(null, status.report());

	}

	/**
	 * Führt die nötigen Schritte aus, die vor dem Bearbeiten eines Klassenteams
	 * notwendig sind.
	 */
	public void doBeforeEdit() {
		classteamMemento = selectedClassteam.createMemento();
		selectedEmployees = selectedClassteam.getEmployees();
		selectedSchoolclasses = selectedClassteam.getSchoolclasses();
	}

	/**
	 * Prüft, ob die Liste der selektierten Mitarbeiter mindestens einen Lehrer
	 * enthält und ob keine der gewählten Klassen bereits einem Klassenteam
	 * zugeordnet ist, ist dies der Fall, wird {@code true} zurückgegeben,
	 * ansonsten wird eine entsprechende FacesMessage erstellt und {@code false}
	 * zurückgegeben.
	 * 
	 * @return {@code true}, wenn min. ein Lehrer selektiert und keine
	 *         selektierte Klasse einem Klassenteam angehört, ansonsten
	 *         {@code false}
	 */
	private boolean validate() {
		boolean valid = false;
		for (Employee e : selectedEmployees) {
			if (e instanceof Teacher) {
				valid = true;
				break;
			}
		}
		if (!valid) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Mindestens eine Lehrkraft benötigt.",
					"Einem Klassenteam muss mindestens eine Lehrkraft zugeordnet sein.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		try {
			boolean alreadyExists = false;
			for (Schoolclass s : selectedSchoolclasses) {
				Classteam classteam = dataAccess.getOneClassteam(s);
				if (classteam != null) {
					alreadyExists = true;
					valid = false;
					break;
				}
			}
			if (alreadyExists) {
				FacesMessage msg = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Ungültige Klassen gewählt.",
						"Für mindestens eine der gewählten Klassen existiert bereits ein Klassenteam.");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		} catch (DatasetException e) {
			LOGGER.error(e);
		}
		return valid;
	}

	/**
	 * Gibt eine Liste aller vorhandenen Klassenteams zurück.
	 * 
	 * @return Liste aller vorhandenen Klassenteams
	 */
	public List<Classteam> getClassteams() {
		try {
			return dataAccess.getAllClassteams();
		} catch (DatasetException e) {
			LOGGER.error(e);
			e.printStackTrace();
			FacesMessage msg = MessageHelper.generateMessage(
					GenericErrorMessage.DATABASE_COMMUNICATION_ERROR,
					FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, msg);

			return new ArrayList<Classteam>();
		}
	}

	/**
	 * Gibt eine Liste aller vorhandenen Lehrer zurück
	 * 
	 * @return Liste aller vorhandenen Lehrer
	 */
	public List<Teacher> getTeachers() {
		try {
			return dataAccess.getAllTeachers();
		} catch (DatasetException e) {
			LOGGER.error(e);
			e.printStackTrace();
			FacesMessage msg = MessageHelper.generateMessage(
					GenericErrorMessage.DATABASE_COMMUNICATION_ERROR,
					FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, msg);

			return new ArrayList<Teacher>();
		}
	}

	/**
	 * Gibt eine Liste aller vorhandnene pädagogischen Mitarbeiter zurück.
	 * 
	 * @return Liste aller vorhandenen pädagogischen Mitarbeiter
	 */
	public List<PedagogicAssistant> getPedagogicAssistants() {
		try {
			return dataAccess.getAllPAs();
		} catch (DatasetException e) {
			LOGGER.error(e);
			e.printStackTrace();
			FacesMessage msg = MessageHelper.generateMessage(
					GenericErrorMessage.DATABASE_COMMUNICATION_ERROR,
					FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, msg);

			return new ArrayList<PedagogicAssistant>();
		}
	}

	/**
	 * Gibt eine Liste aller Schulklassen ohne Klassenteam zurück.
	 * 
	 * @return Liste aller Schulklassen ohne Klassenteam
	 */
	public List<Schoolclass> getValidSchoolclasses() {
		try {
			return dataAccess.getAllSchoolclassesWithoutClassteam();
		} catch (DatasetException e) {
			LOGGER.error(e);
			e.printStackTrace();
			FacesMessage msg = MessageHelper.generateMessage(
					GenericErrorMessage.DATABASE_COMMUNICATION_ERROR,
					FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, msg);

			return new ArrayList<Schoolclass>();
		}
	}

	public SelectItem getDisabledTeacherSelectItem() {
		return new SelectItem(null, "----------- Lehrkräfte ------------",
				null, true, false, false);
	}

	public SelectItem getDisabledPedagogicAssistantSelectItem() {
		return new SelectItem(null, "-------- Päd. Mitarbeiter --------", null,
				true, false, false);
	}

	public Classteam getSelectedClassteam() {
		return selectedClassteam;
	}

	public void setSelectedClassteam(Classteam selectedClassteam) {
		this.selectedClassteam = selectedClassteam;
	}

	public List<Employee> getSelectedEmployees() {
		return selectedEmployees;
	}

	public void setSelectedEmployees(List<Employee> selectedEmployees) {
		this.selectedEmployees = selectedEmployees;
	}

	public List<Schoolclass> getSelectedSchoolclasses() {
		return selectedSchoolclasses;
	}

	public void setSelectedSchoolclasses(List<Schoolclass> selectedSchoolclasses) {
		this.selectedSchoolclasses = selectedSchoolclasses;
	}

	public boolean isHideDeletionDialog() {
		return hideDeletionDialog;
	}

	public void setHideDeletionDialog(boolean hideDeletionDialog) {
		this.hideDeletionDialog = hideDeletionDialog;
	}
}
