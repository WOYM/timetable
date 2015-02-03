package org.woym.ui.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.testng.util.Strings;
import org.woym.common.objects.Lesson;
import org.woym.common.objects.LessonType;
import org.woym.common.objects.Schoolclass;

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
	
	public PersonalPlanRow(String teacherName, BigDecimal totalHours, BigDecimal freeHours) {
		if(Strings.isNullOrEmpty(teacherName)) {
			throw new IllegalArgumentException("Name was null!");
		}
		this.teacherName = teacherName;
		this.totalHours = totalHours;
		this.freeHours = freeHours;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public String getTotalHours() {
		return totalHours.toPlainString();
	}
	
	public String getFreeHours() {
		return freeHours.toPlainString();
	}

	public Map<LessonType, Map<Schoolclass, Integer>> getCellMap() {
		return cellMap;
	}

	public void setCellMap(Map<LessonType, Map<Schoolclass, Integer>> cellMap) {
		this.cellMap = cellMap;
	}
	
	/**
	 * Diese Methode fügt anhand einer übergebenen {@link Lesson} die benötigten
	 * Werte in die Map dieser Klasse ein
	 * 
	 * @param lesson Die Lesson
	 */
	public void insertCellValue(Lesson lesson) {
		if(cellMap.containsKey(lesson.getLessonType())) {
			
			Map<Schoolclass, Integer> map = cellMap.get(lesson.getLessonType());
			
			for(Schoolclass schoolclass : lesson.getSchoolclasses()) {
				if(map.containsKey(schoolclass)) {
					Integer value = map.get(schoolclass);
					value += lesson.getTime().getDuration();
					map.put(schoolclass, value);
				} else {
					map.put(schoolclass, lesson.getTime().getDuration());
				}
			}
		} else {
			Map<Schoolclass, Integer> map = new HashMap<>();
			
			for(Schoolclass schoolclass : lesson.getSchoolclasses()) {
				map.put(schoolclass, lesson.getTime().getDuration());
			}
			
			cellMap.put(lesson.getLessonType(), map);
		}
	}
}
