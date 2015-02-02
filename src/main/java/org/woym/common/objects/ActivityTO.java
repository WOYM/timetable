package org.woym.common.objects;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.woym.common.config.Config;
import org.woym.common.config.DefaultConfigEnum;
import org.woym.controller.planning.PlanningController;

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
		List<Weekday> weekdays = Config.getValidWeekdays();
		if (!weekdays.isEmpty()) {
			period.setDay(weekdays.get(0));
		}
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		Date startTime = calendar.getTime();
		Date endTime = calendar.getTime();
		try {
			startTime = sdf.parse(Config
					.getSingleStringValue(DefaultConfigEnum.WEEKDAY_STARTTIME));
			endTime.setTime(startTime.getTime()
					+ TimeUnit.MINUTES.toMillis(Config
							.getSingleIntValue(DefaultConfigEnum.TYPICAL_ACTIVITY_DURATION)));
		} catch (Exception e) {
			startTime = calendar.getTime();
			endTime.setTime(startTime.getTime() + TimeUnit.MINUTES.toMillis(45));
		}

		period.setStartTime(startTime);
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
