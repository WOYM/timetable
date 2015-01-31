package org.woym.controller.planning;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.woym.common.objects.Pause;
import org.woym.ui.util.ActivityTOHolder;

@ViewScoped
@ManagedBean(name = "pauseController")
public class PauseController implements Serializable {

	private static final long serialVersionUID = 1258086895763280867L;

	private ActivityTOHolder activityTOHolder = ActivityTOHolder.getInstance();
	
	private Pause pause;
	
	@PostConstruct
	public void init() {
		pause = new Pause();
	}
}
