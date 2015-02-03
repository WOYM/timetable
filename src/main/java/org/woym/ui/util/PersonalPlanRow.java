package org.woym.ui.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.util.Strings;
import org.woym.common.config.Config;
import org.woym.common.config.DefaultConfigEnum;
import org.woym.common.objects.Employee;
import org.woym.common.objects.Lesson;
import org.woym.common.objects.LessonType;
import org.woym.common.objects.Schoolclass;
import org.woym.common.objects.Teacher;

/**
 * <h1>PersonalPlanRow</h1>
 * <p>
 * Diese Klasse stellt eine Abbildung einer Zeile im Personaleinsatzplan dar.
 * 
 * @author Tim Hansen (tihansen)
 * @version 0.2.0
 * @since 0.2.0
 */
public class PersonalPlanRow {

	private String teacherName;
	private BigDecimal totalHours;
	private BigDecimal freeHours;

	private Map<LessonType, Map<Schoolclass, Integer>> cellMap;

	/**
	 * Erzeugt eine neue Abbildung im Auslastungsplan
	 * 
	 * @param teacherName
	 *            Name der Lehrkraft
	 * @param totalHours
	 *            Alle Stunden
	 * @param freeHours
	 *            Freie Stunden
	 */
	public PersonalPlanRow(String teacherName, BigDecimal totalHours,
			BigDecimal freeHours) {
		if (Strings.isNullOrEmpty(teacherName)) {
			throw new IllegalArgumentException("Name was null!");
		}
		this.teacherName = teacherName;
		this.totalHours = totalHours;
		this.freeHours = freeHours;

		cellMap = new HashMap<LessonType, Map<Schoolclass, Integer>>();
	}

	/**
	 * Diese Methode fügt anhand einer übergebenen {@link Lesson} die benötigten
	 * Werte in die Map dieser Klasse ein
	 * 
	 * @param lesson
	 *            Die Lesson
	 */
	public void insertCellValue(Lesson lesson) {
		if (cellMap.containsKey(lesson.getLessonType())) {

			Map<Schoolclass, Integer> map = cellMap.get(lesson.getLessonType());

			for (Schoolclass schoolclass : lesson.getSchoolclasses()) {
				if (map.containsKey(schoolclass)) {
					Integer value = map.get(schoolclass);
					value += lesson.getTime().getDuration();
					map.put(schoolclass, value);
				} else {
					map.put(schoolclass, lesson.getTime().getDuration());
				}
			}
		} else {
			Map<Schoolclass, Integer> map = new HashMap<>();

			for (Schoolclass schoolclass : lesson.getSchoolclasses()) {
				map.put(schoolclass, lesson.getTime().getDuration());
			}

			cellMap.put(lesson.getLessonType(), map);
		}
	}

	/**
	 * Diese Methode liefert alle Objekte des Typs {@link Schoolclass} und die
	 * Dauer des Unterrichts des {@link Teacher}s bei der {@link Schoolclass}
	 * für den übergebenen {@link LessonType}.
	 * 
	 * @param lessonType
	 *            Der {@link LessonType}
	 * @return Eine {@link Map} mit {@link Schoolclass}-Objekten und der Dauer
	 *         als Zahl
	 */
	public List<Schoolclass> getSchoolclasses(LessonType lessonType) {
		if (lessonType == null) {
			return new ArrayList<Schoolclass>();
		}

		if (!cellMap.containsKey(lessonType)) {
			return new ArrayList<Schoolclass>();
		}

		return new ArrayList<Schoolclass>(cellMap.get(lessonType).keySet());
	}

	/**
	 * Liefert die Gesamtdauer für eine {@link Schoolclass} und einen
	 * {@link LessonType} zurück
	 * 
	 * @param lessonType
	 *            Der {@link LessonType}
	 * @param schoolclass
	 *            DIe {@link Schoolclass}
	 * @return Die Gesamtdauer als String
	 */
	public String getTotalDuration(LessonType lessonType,
			Schoolclass schoolclass) {
		String string = "";

		// Failsafe
		if (lessonType == null || schoolclass == null) {
			return string;
		}

		if (cellMap.containsKey(lessonType)) {
			if (cellMap.get(lessonType).containsKey(schoolclass)) {
				BigDecimal hourlySettlement = new BigDecimal(
						Config.getSingleIntValue(DefaultConfigEnum.TEACHER_HOURLY_SETTLEMENT));

				BigDecimal minutes = new BigDecimal(cellMap.get(lessonType)
						.get(schoolclass));

				string = minutes.divide(hourlySettlement, Employee.SCALE,
						RoundingMode.HALF_UP).toPlainString();
			}
		}

		return string;
	}

	/**
	 * Liefert einen Wahrheitswert, ob es noch freie Zeit gibt.
	 * 
	 * @return Wahrheitswert, ob es noch freie Zeit gibt
	 */
	public Boolean getHasRemainingHours() {
		int retVal = freeHours.compareTo(BigDecimal.ZERO);
		if (retVal == 1) {
			return true;
		}
		return false;
	}

	/**
	 * Wahrheitswert, ob der Lehrkraft mehr Zeit vergeben wurde als zulässig.
	 * 
	 * @return Wahrheitswert, ob zu viel Zeit vergeben wurde
	 */
	public Boolean getHasOverflow() {
		int retVal = freeHours.compareTo(BigDecimal.ZERO);
		if (retVal == -1) {
			return true;
		}
		return false;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public String getFreeHours() {
		return freeHours.toPlainString();
	}

	public String getTotalHours() {
		return totalHours.toPlainString();
	}

	public String getOverflowHours() {
		return freeHours.negate().toPlainString();
	}

	public Map<LessonType, Map<Schoolclass, Integer>> getCellMap() {
		return cellMap;
	}

}
