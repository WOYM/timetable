package org.woym.ui.util;

import java.io.Serializable;

import org.woym.common.config.DefaultConfigEnum;
import org.woym.common.objects.ActivityTO;
import org.woym.controller.planning.LessonController;
import org.woym.controller.planning.PlanningController;

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
public class ActivityTOHolder implements Serializable {

	private static final long serialVersionUID = 2766544452801934027L;

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
	 * Erzeugt ein neues leeres {@link ActivityTO}, dessen Startzeit auf
	 * Startzeit eines Wochentages liegt und dessen Endzeit um den Property-Wert von
	 * {@linkplain DefaultConfigEnum#TYPICAL_ACTIVITY_DURATION} nach der
	 * Startzeit liegt und bereits im richtigen Zeitraum der Anzeige
	 * positioniert ist.
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
