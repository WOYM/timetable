package org.woym.ui.util;


import org.woym.common.objects.ActivityTO;
import org.woym.controller.PlanningController;
import org.woym.controller.planning.LessonController;

/**
 * <h1>ActivityHolder</h1>
 * <p>
 * Diese Klasse ist dafür zuständig, eine Aktivität zu halten. Da sie eine
 * statische Instanz zurückliefert, kann mit ihr die "Lücke" zwischen
 * Controllern überbrückt werden, da diese nicht direkt miteinander verbunden
 * werden sollten.
 * 
 * @see Activity
 * @see PlanningController
 * @see LessonController
 * @see MeetingController
 * @see CompoundLessonController
 * 
 * @author Tim Hansen (@tihansen)
 * @version 0.1.1
 * @since 0.1.1
 */
public class ActivityTOHolder {

	private static ActivityTOHolder INSTANCE = new ActivityTOHolder();

	private ActivityTO activityTO;

	private ActivityTOHolder() {
		activityTO = new ActivityTO();
	}

	/**
	 * Liefert die Instanz des {@link ActivityHolder}s zurück.
	 * 
	 * @return Die Instanz des {@link ActivityHolder}s
	 */
	public static ActivityTOHolder getInstance() {
		return INSTANCE;
	}

	/**
	 * Erzeugt ein neues leeres {@link ActivityTO}, dessen Start- und Endzeit auf der
	 * derzeitigen Uhrzeit des Benutzers steht und bereits im richtigen Zeitraum
	 * der Anzeige positioniert ist.
	 */
	public void plainActivityTO() {
		activityTO = new ActivityTO();
	}

	public ActivityTO getActivityTO() {
		return activityTO;
	}

	public void setActivityTO(ActivityTO activityTO) {
		this.activityTO = activityTO;
	}
}
