package org.woym.common.objects;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Diese Klasse repräsentiert einen Zeitraum.
 * 
 * @author Adrian
 *
 */
@Embeddable
public class TimePeriod implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2207686775603230048L;

	/**
	 * Der Startzeitpunkt des Zeitraumes.
	 */
	@Temporal(TemporalType.TIME)
	@Column(nullable = false)
	private Date startTime;

	/**
	 * Der Endzeitpunkt des Zeitraumes.
	 */
	@Temporal(TemporalType.TIME)
	@Column(nullable = false)
	private Date endTime;

	/**
	 * Die Dauer des Zeitraumes in Minuten.
	 */
	@Column(nullable = false)
	private int duration;

	/**
	 * Der Wochentag (s. {@linkplain Weekday}.
	 */
	@Column(nullable = false)
	private Weekday day;

	public TimePeriod() {
	}

	public Date getStartTime() {
		return startTime;
	}

	/**
	 * Setzt die Startzeit. Ist die Endzeit nicht {@code null}, wird auch die
	 * Dauer gesetzt.
	 * 
	 * @param startTime
	 *            - die Startzeit
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
		if (endTime != null) {
			duration = (int) computeDuration();
		}
	}

	public Date getEndTime() {
		return endTime;
	}

	/**
	 * Setzt die Endzeit. Ist die Startzeit nicht {@code null}, wird auch die
	 * Dauer gesetzt.
	 * 
	 * @param endTime
	 *            - Endzeit
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
		if (startTime != null) {
			duration = (int) computeDuration();
		}
	}

	public int getDuration() {
		return duration;
	}

	public Weekday getDay() {
		return day;
	}

	public void setDay(Weekday day) {
		this.day = day;
	}

	@Override
	public String toString() {
		return day + ", " + startTime + "-" + endTime;
	}

	/**
	 * Berechnet die Dauer dieses Zeitraums in Minuten und gibt sie zurück.
	 * 
	 * @return die Dauer dieses Zeitraums in Minuten als long
	 */
	private long computeDuration() {
		return TimeUnit.MILLISECONDS.toMinutes(Math.abs(startTime.getTime()
				- endTime.getTime()));
	}
}
