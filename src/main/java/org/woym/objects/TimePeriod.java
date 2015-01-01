package org.woym.objects;

import java.io.Serializable;
import java.util.Date;

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

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
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
}
