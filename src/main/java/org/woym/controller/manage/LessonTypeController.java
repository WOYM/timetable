package org.woym.controller.manage;

import java.util.HashMap;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.woym.objects.LessonType;

/**
 * <h1>LocationController</h1>
 * <p>
 * This controller manages locations and rooms.
 * 
 * @author Tim Hansen (tihansen)
 *
 */
@SessionScoped
@ManagedBean(name = "activityTypeController")
public class LessonTypeController {
	private static final long serialVersionUID = -2341971622906815080L;

	private static Logger logger = LogManager.getLogger("activityTypeController");

	// private LocationDAO db = new LocationDAO();
	private LessonType selectedLessonType;
	private LessonType addLessonType;
	
	/**
	 * Opens a new dialog which enables the user to add a new location.
	 */
	public void addLocationDialog() {

		addLessonType = new LessonType();
		Map<String, Object> options = new HashMap<String, Object>();
		options.put("modal", true);
		options.put("draggable", false);
		options.put("resizable", false);
		options.put("contentHeight", 600);
		options.put("contentWidth", 800);

		RequestContext rc = RequestContext.getCurrentInstance();
		rc.openDialog("addLocationDialog", options, null);
	}

}
