package org.woym.objects;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Diese Klasse repräsentiert einen Zeitraum.
 * @author Adrian
 *
 */
@Entity
public class TimePeriod implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2207686775603230048L;

	/**
	 * Die automatisch generierte ID ist der Primärschlüssel für die Datenbank.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	/**
	 * Der Startzeitpunkt des Zeitraumes.
	 */
	@Temporal(TemporalType.TIME)
	@Column(nullable = false)
	private Date start;
	
	/**
	 * Der Endzeitpunkt des Zeitraumes.
	 */
	@Temporal(TemporalType.TIME)
	@Column(nullable = false)
	private Date end;
	
	/**
	 * Die Dauer des Zeitraumes in Minuten.
	 */
	@Column(nullable = false)
	private int duration;
	
	/**
	 * Der Wochentag (s. {@linkplain Weekday}.
	 */
	@Column(nullable = false)
	private int day;
	
	/**
	 * Die Woche (s. {@linkplain Week}.
	 */
	@Column(nullable = false)
	private int week;
	

	public TimePeriod() {
	}
	
	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public Date getStart() {
		return start;
	}



	public void setStart(Date start) {
		this.start = start;
	}



	public Date getEnd() {
		return end;
	}



	public void setEnd(Date end) {
		this.end = end;
	}



	public int getDuration() {
		return duration;
	}



	public void setDuration(int duration) {
		this.duration = duration;
	}



	public int getDay() {
		return day;
	}



	public void setDay(int day) {
		this.day = day;
	}



	public int getWeek() {
		return week;
	}



	public void setWeek(int week) {
		this.week = week;
	}
}
