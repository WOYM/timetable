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
import org.woym.common.exceptions.DatasetException;
import org.woym.common.objects.Pause;
import org.woym.common.objects.Schoolclass;
import org.woym.logic.CommandHandler;
import org.woym.logic.SuccessStatus;
import org.woym.logic.command.AddCommand;
import org.woym.logic.spec.IStatus;
import org.woym.logic.util.ActivityValidator;
import org.woym.persistence.DataAccess;
import org.woym.ui.util.ActivityTOHolder;
import org.woym.ui.util.EntityHelper;
import org.woym.ui.util.ScheduleModelHolder;

@ViewScoped
@ManagedBean(name = "pauseController")
public class PauseController implements Serializable {

	private static final long serialVersionUID = 1258086895763280867L;

	private static Logger LOGGER = LogManager.getLogger(PauseController.class);

	private DataAccess dataAccess = DataAccess.getInstance();
	private ActivityValidator activityValidator = ActivityValidator
			.getInstance();
	private ScheduleModelHolder scheduleModelHolder = ScheduleModelHolder
			.getInstance();

	private ActivityTOHolder activityTOHolder = ActivityTOHolder.getInstance();
	private EntityHelper entityHelper = EntityHelper.getInstance();

	private Pause pause;

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

	public void addPause() {
		IStatus status = activityValidator.validateActivity(pause,
				pause.getTime());

		if (status instanceof SuccessStatus) {
			AddCommand<Pause> command = new AddCommand<Pause>(pause);
			status = CommandHandler.getInstance().execute(command);

			if (status instanceof SuccessStatus) {
				init();
			}
		}

		scheduleModelHolder.updateScheduleModel();

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
}
