package org.woym.controller.planning;

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
import org.woym.common.exceptions.DatasetException;
import org.woym.common.objects.Activity;
import org.woym.common.objects.ActivityTO;
import org.woym.common.objects.Pause;
import org.woym.common.objects.Schoolclass;
import org.woym.logic.CommandHandler;
import org.woym.logic.SuccessStatus;
import org.woym.logic.command.AddCommand;
import org.woym.logic.command.CommandCreator;
import org.woym.logic.command.MacroCommand;
import org.woym.logic.spec.IStatus;
import org.woym.logic.util.ActivityValidator;
import org.woym.persistence.DataAccess;
import org.woym.ui.util.ActivityTOHolder;
import org.woym.ui.util.EntityHelper;
import org.woym.ui.util.ScheduleModelHolder;

/**
 * <h1>PauseController</h1>
 * <p>
 * Diese Controller ist dafür zuständig, {@link Activity}-Objekte des Types
 * {@link Pause} zu konfigurieren.
 * 
 * @author Tim Hansen (tihansen)
 * @version 0.2.0
 * @since 0.1.0
 *
 * @see PlanningController
 * @see ActivityValidator
 * @see EntityHelper
 * @see ScheduleModelHolder
 * @see ActivityTOHolder
 * @see ActivityTO
 */
@ViewScoped
@ManagedBean(name = "pauseController")
public class PauseController implements Serializable {

	private static final long serialVersionUID = 1258086895763280867L;

	private static Logger LOGGER = LogManager.getLogger(PauseController.class);

	private DataAccess dataAccess = DataAccess.getInstance();
	private final CommandCreator commandCreator = CommandCreator.getInstance();

	private ActivityValidator activityValidator = ActivityValidator
			.getInstance();
	private ScheduleModelHolder scheduleModelHolder = ScheduleModelHolder
			.getInstance();

	private ActivityTOHolder activityTOHolder = ActivityTOHolder.getInstance();
	private EntityHelper entityHelper = EntityHelper.getInstance();

	private Pause pause;
	
	private boolean allWeekdays;

	/**
	 * Diese Methode initialisiert die Bean und erzeugt eine neue {@link Pause},
	 * die danach von dieser Bean verwaltet wird.
	 * <p>
	 * Es wird anhand der Daten der {@link EntityHelper}-Instanz ein erster
	 * Datensatz für das Objekt erzeugt.
	 */
	@PostConstruct
	public void init() {
		pause = new Pause();
		pause.setTime(activityTOHolder.getActivityTO().getTimePeriod());

		if (entityHelper.getSchoolclass() != null) {
			List<Schoolclass> schoolclasses = new ArrayList<>();
			schoolclasses.add(entityHelper.getSchoolclass());
			pause.setSchoolclasses(schoolclasses);
		}
	}

	/**
	 * Diese Methode fügt mit Hilfe des {@link CommandHandler}s ein neues
	 * {@link Activity}-Objekt des Types {@link Pause} der Persistenz hinzu.
	 */
	public void addPause() {
		IStatus status = activityValidator.validateActivity(pause,
				pause.getTime());

		if (status instanceof SuccessStatus) {

			MacroCommand macro = commandCreator
					.createEmployeeUpdateAddWorkingHours(pause);
			macro.add(new AddCommand<Pause>(pause));
			status = CommandHandler.getInstance().execute(macro);

			if (status instanceof SuccessStatus) {
				init();
				RequestContext.getCurrentInstance().execute(
						"PF('wAddActivityDialog').hide()");
			}

			scheduleModelHolder.updateScheduleModel();
		}

		FacesMessage message = status.report();
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public List<Schoolclass> getAllSchoolclasses() {
		List<Schoolclass> schoolclasses = new ArrayList<>();

		try {
			schoolclasses = dataAccess.getAllSchoolclasses();
		} catch (DatasetException e) {
			LOGGER.error(e);
		}

		return schoolclasses;
	}

	public Pause getPause() {
		return pause;
	}

	public void setPause(Pause pause) {
		this.pause = pause;
	}

	public boolean isAllWeekdays() {
		return allWeekdays;
	}

	public void setAllWeekdays(boolean allWeekdays) {
		this.allWeekdays = allWeekdays;
	}
	
	
}
