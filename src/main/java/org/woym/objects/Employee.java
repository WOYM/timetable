package org.woym.objects;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.woym.spec.logic.ActivityObject;

/**
 * Superklasse für alle Personen des Personals.
 * 
 * @author Adrian
 *
 */
@Entity
@Inheritance
@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.STRING, length = 20)
public abstract class Employee extends org.woym.objects.Entity implements ActivityObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9008279410416213060L;

	/**
	 * Die automatisch generierte ID ist der Primärschlüssel für die Datenbank.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	/**
	 * Der Name dieses Lehrers.
	 */
	@Column(nullable = false)
	private String name;

	/**
	 * Das Kürzel. Darf in der Datenbank nicht null sein und ist einzigartig.
	 */
	@Column(unique = true, nullable = false)
	private String symbol;

	/**
	 * Anzahl der Wochenstunden. Darf nicht null sein.
	 */
	@Column(nullable = false)
	private BigDecimal hoursPerWeek;

	/**
	 * Anzahl der Stunden, die die Person des Personals auf Aktivitäten verteilt
	 * ist. Bei Erzeugung sind dies null Stunden.
	 */
	@Column(nullable = false)
	private BigDecimal allocatedHours = new BigDecimal("0");

	/**
	 * Die anrechenbaren Ersatzleistungen des Lehrers.
	 */
	@ElementCollection
	@OneToMany
	private List<ChargeableCompensation> compensations = new ArrayList<ChargeableCompensation>();

	/**
	 * Die von dieser Person des Personals betreuten Jahrgänge.
	 */
	@ManyToMany
	@OrderBy("academicYear")
	private List<AcademicYear> guidedAcademicYears = new ArrayList<AcademicYear>();

	/**
	 * Die von dieser Person des Personals betreuten Schulklassen.
	 */
	@ManyToMany
	private List<Schoolclass> guidedSchoolclasses = new ArrayList<Schoolclass>();

	/**
	 * Die möglichen Stundeninhalte dieser Person des Personals.
	 */
	@ManyToMany
	@OrderBy("name")
	protected List<ActivityType> possibleActivityTypes = new ArrayList<ActivityType>();

	/**
	 * Eine Liste von {@link TimePeriod}-Objekten, welche die Zeitwünsche des
	 * Lehrers darstellen.
	 */
	@ElementCollection
	@OneToMany
	private List<TimePeriod> timeWishes = new ArrayList<>();

	public Employee() {
	}

	public long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol.toUpperCase();
	}

	public BigDecimal getHoursPerWeek() {
		return hoursPerWeek;
	}

	public void setHoursPerWeek(BigDecimal hoursPerWeek) {
		this.hoursPerWeek = hoursPerWeek;
	}

	public BigDecimal getAllocatedHours() {
		return allocatedHours;
	}

	public void setAllocatedHours(BigDecimal allocatedHours) {
		this.allocatedHours = allocatedHours;
	}

	public List<ChargeableCompensation> getCompensations() {
		return compensations;
	}

	public void setCompensations(List<ChargeableCompensation> compensations) {
		this.compensations = compensations;
	}

	public List<AcademicYear> getGuidedAcademicYears() {
		return guidedAcademicYears;
	}

	public void setGuidedAcademicYears(List<AcademicYear> guidedAcademicYears) {
		this.guidedAcademicYears = guidedAcademicYears;
	}

	public List<Schoolclass> getGuidedSchoolclasses() {
		return guidedSchoolclasses;
	}

	public void setGuidedSchoolclasses(List<Schoolclass> guidedSchoolclasses) {
		this.guidedSchoolclasses = guidedSchoolclasses;
	}

	public List<ActivityType> getPossibleActivityTypes() {
		return possibleActivityTypes;
	}

	public void setPossibleActivityTypes(
			List<ActivityType> possibleActivityTypes) {
		this.possibleActivityTypes = possibleActivityTypes;
	}

	public List<TimePeriod> getTimeWishes() {
		return timeWishes;
	}

	public void setTimeWishes(List<TimePeriod> timeWishes) {
		this.timeWishes = timeWishes;
	}

	/**
	 * Überschreiben der toString-Methode, um in der DEBUG-Ausgabe des Loggers
	 * die Objekteigenschaften identifizieren zu können.
	 */
	@Override
	public String toString() {
		return getName() + ", " + symbol + ", " + hoursPerWeek + "hpw";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		return true;
	}

	/**
	 * Fügt das übergebenene {@linkplain ChargeableCompensation}-Objekt der
	 * entsprechenden Liste hinzu, sofern es noch nicht darin vorhanden ist.
	 * 
	 * @param compensation
	 *            - das hinzuzufügende Objekt
	 * @return {@code true}, wenn das Objekt sich noch nicht in der Liste
	 *         befindet und hinzugefügt wurde, ansonsten {@code false}
	 */
	public boolean addCompensation(final ChargeableCompensation compensation) {
		if (!compensations.contains(compensation)) {
			compensations.add(compensation);
			return true;
		}
		return false;
	}

	/**
	 * Entfernt das übergebene {@linkplain ChargeableCompensation}-Objekt aus
	 * der entsprechenden Liste.
	 * 
	 * @param compensation
	 *            - das zu entfernden Objekt
	 * @return {@code true}, wenn das Objekt entfernt wurde, ansonsten
	 *         {@code false}
	 */
	public boolean removeCompensation(final ChargeableCompensation compensation) {
		return compensations.remove(compensation);
	}

	/**
	 * Gibt {@code true} zurück, wenn sich das übergebene
	 * {@linkplain ChargeableCompensation}-Objekt in der Liste befindet,
	 * ansonsten {@code false}.
	 * 
	 * @param compensation
	 *            - das Objekt, für das geprüft werden soll, ob es sich in der
	 *            Liste befindet
	 * @return {@code true}, wenn das Objekt sich in der Liste befindet,
	 *         ansonsten {@code false}
	 */
	public boolean containsCompensation(
			final ChargeableCompensation compensation) {
		return compensations.contains(compensation);
	}

	/**
	 * Fügt das übergebenene {@linkplain AcademicYear}-Objekt der entsprechenden
	 * Liste hinzu, sofern es noch nicht darin vorhanden ist.
	 * 
	 * @param academicYear
	 *            - das hinzuzufügende Objekt
	 * @return {@code true}, wenn das Objekt sich noch nicht in der Liste
	 *         befindet und hinzugefügt wurde, ansonsten {@code false}
	 */
	public boolean addAcademicYear(final AcademicYear academicYear) {
		if (!guidedAcademicYears.contains(academicYear)) {
			guidedAcademicYears.add(academicYear);
			return true;
		}
		return false;
	}

	/**
	 * Entfernt das übergebene {@linkplain AcademicYear}-Objekt aus der
	 * entsprechenden Liste.
	 * 
	 * @param academicYear
	 *            - das zu entfernden Objekt
	 * @return {@code true}, wenn das Objekt entfernt wurde, ansonsten
	 *         {@code false}
	 */
	public boolean removeAcademicYear(final AcademicYear academicYear) {
		return guidedAcademicYears.remove(academicYear);
	}

	/**
	 * Gibt {@code true} zurück, wenn sich das übergebene
	 * {@linkplain AcademicYear}-Objekt in der Liste befindet, ansonsten
	 * {@code false}.
	 * 
	 * @param academicYear
	 *            - das Objekt, für das geprüft werden soll, ob es sich in der
	 *            Liste befindet
	 * @return {@code true}, wenn das Objekt sich in der Liste befindet,
	 *         ansonsten {@code false}
	 */
	public boolean containsAcademicYear(final AcademicYear academicYear) {
		return guidedAcademicYears.contains(academicYear);
	}

	/**
	 * Fügt das übergebenene {@linkplain Schoolclass}-Objekt der entsprechenden
	 * Liste hinzu, sofern es noch nicht darin vorhanden ist.
	 * 
	 * @param schoolclass
	 *            - das hinzuzufügende Objekt
	 * @return {@code true}, wenn das Objekt sich noch nicht in der Liste
	 *         befindet und hinzugefügt wurde, ansonsten {@code false}
	 */
	public boolean addSchoolclass(final Schoolclass schoolclass) {
		if (!guidedSchoolclasses.contains(schoolclass)) {
			guidedSchoolclasses.add(schoolclass);
			return true;
		}
		return false;
	}

	/**
	 * Entfernt das übergebene {@linkplain Schoolclass}-Objekt aus der
	 * entsprechenden Liste.
	 * 
	 * @param schoolclass
	 *            - das zu entfernden Objekt
	 * @return {@code true}, wenn das Objekt entfernt wurde, ansonsten
	 *         {@code false}
	 */
	public boolean removeSchoolclass(final Schoolclass schoolclass) {
		return guidedSchoolclasses.remove(schoolclass);
	}

	/**
	 * Gibt {@code true} zurück, wenn sich das übergebene
	 * {@linkplain Schoolclass}-Objekt in der Liste befindet, ansonsten
	 * {@code false}.
	 * 
	 * @param schoolclass
	 *            - das Objekt, für das geprüft werden soll, ob es sich in der
	 *            Liste befindet
	 * @return {@code true}, wenn das Objekt sich in der Liste befindet,
	 *         ansonsten {@code false}
	 */
	public boolean containsSchoolclass(final Schoolclass schoolclass) {
		return guidedSchoolclasses.contains(schoolclass);
	}

	/**
	 * Fügt das übergebenene {@linkplain ActivityType}-Objekt der entsprechenden
	 * Liste hinzu, sofern es noch nicht darin vorhanden ist.
	 * 
	 * @param activityType
	 *            - das hinzuzufügende Objekt
	 * @return {@code true}, wenn das Objekt sich noch nicht in der Liste
	 *         befindet und hinzugefügt wurde, ansonsten {@code false}
	 */
	public boolean addActivityType(ActivityType activityType) {
		if(activityType == null) {
			return false;
		}
		
		if(!this.possibleActivityTypes.contains(activityType)) {
			this.possibleActivityTypes.add(activityType);
			return true;
		}
		return false;
	}

	/**
	 * Entfernt das übergebene {@linkplain ActivityType}-Objekt aus der
	 * entsprechenden Liste.
	 * 
	 * @param activityType
	 *            - das zu entfernden Objekt
	 * @return {@code true}, wenn das Objekt entfernt wurde, ansonsten
	 *         {@code false}
	 */
	public boolean removeActivityType(final ActivityType activityType) {
		return possibleActivityTypes.remove(activityType);
	}

	/**
	 * Gibt {@code true} zurück, wenn sich das übergebene
	 * {@linkplain ActivityType}-Objekt in der Liste befindet, ansonsten
	 * {@code false}.
	 * 
	 * @param activityType
	 *            - das Objekt, für das geprüft werden soll, ob es sich in der
	 *            Liste befindet
	 * @return {@code true}, wenn das Objekt sich in der Liste befindet,
	 *         ansonsten {@code false}
	 */
	public boolean containsActivityType(final ActivityType activityType) {
		return possibleActivityTypes.contains(activityType);
	}

	/**
	 * Fügt das übergebenene {@linkplain TimePeriod}-Objekt der entsprechenden
	 * Liste hinzu, sofern es noch nicht darin vorhanden ist.
	 * 
	 * @param timePeriod
	 *            - das hinzuzufügende Objekt
	 * @return {@code true}, wenn das Objekt sich noch nicht in der Liste
	 *         befindet und hinzugefügt wurde, ansonsten {@code false}
	 */
	public boolean addTimeWish(final TimePeriod timePeriod) {
		if (!timeWishes.contains(timePeriod)) {
			timeWishes.add(timePeriod);
			return true;
		}
		return false;
	}

	/**
	 * Entfernt das übergebene {@linkplain TimePeriod}-Objekt aus der
	 * entsprechenden Liste.
	 * 
	 * @param timePeriod
	 *            - das zu entfernden Objekt
	 * @return {@code true}, wenn das Objekt entfernt wurde, ansonsten
	 *         {@code false}
	 */
	public boolean removeTimeWish(final TimePeriod timePeriod) {
		return timeWishes.remove(timePeriod);
	}

	/**
	 * Gibt {@code true} zurück, wenn sich das übergebene
	 * {@linkplain TimePeriod}-Objekt in der Liste befindet, ansonsten
	 * {@code false}.
	 * 
	 * @param timePeriod
	 *            - das Objekt, für das geprüft werden soll, ob es sich in der
	 *            Liste befindet
	 * @return {@code true}, wenn das Objekt sich in der Liste befindet,
	 *         ansonsten {@code false}
	 */
	public boolean containsTimeWish(final TimePeriod timePeriod) {
		return timeWishes.contains(timePeriod);
	}
}
