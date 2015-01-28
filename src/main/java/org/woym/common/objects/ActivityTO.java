package org.woym.common.objects;

import java.util.Calendar;

import org.woym.controller.PlanningController;

/**
 * <h1>ActivityTO</h1>
 * <p>
 * Diese Klasse repräsentiert ein Datentransferobjekt für eine {@link Activity},
 * um die Daten, die direkt durch den Nutzer gesetzt werden an die verschiedenen
 * Controller weitergeben zu können.
 * <p>
 * Hierbei wird das {@link ActivityTypeEnum} benutzt, um die verschiedenen
 * Aktivitätstypen übergeben zu können. <br>
 * Intial wird {@code ActivityTypeEnum.LESSON} und der {@link Weekday}
 * {@code Weekday.MONDAY} gesetzt.
 * 
 * @author Tim Hansen (tihansen)
 * @version 0.1.0
 * @since 0.1.0
 * 
 * @see Activity
 */
public class ActivityTO {

	private TimePeriod period;
	private ActivityTypeEnum activityTypeEnum;

	/**
	 * Erzeugt ein neues {@link ActivityTO} und setzt initial
	 * {@code ActivityTypeEnum.LESSON} und {@code Weekday.MONDAY} als Typ
	 */
	public ActivityTO() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, PlanningController.CALENDAR_YEAR);
		calendar.set(Calendar.MONTH, PlanningController.CALENDAR_MONTH);
		calendar.set(Calendar.DAY_OF_MONTH, PlanningController.CALENDAR_DAY);

		period = new TimePeriod();
		period.setDay(Weekday.MONDAY);
		period.setStartTime(calendar.getTime());
		period.setEndTime(calendar.getTime());

		activityTypeEnum = ActivityTypeEnum.LESSON;
	}

	public TimePeriod getTimePeriod() {
		return period;
	}

	public void setTimePeriod(TimePeriod timePeriod) {
		this.period = timePeriod;
	}

	public ActivityTypeEnum getActivityTypeEnum() {
		return activityTypeEnum;
	}

	public void setActivityTypeEnum(ActivityTypeEnum activityTypeEnum) {
		this.activityTypeEnum = activityTypeEnum;
	}
}
