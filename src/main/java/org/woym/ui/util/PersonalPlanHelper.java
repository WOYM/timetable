package org.woym.ui.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.woym.common.exceptions.DatasetException;
import org.woym.common.objects.Activity;
import org.woym.common.objects.EmployeeTimePeriods;
import org.woym.common.objects.Lesson;
import org.woym.common.objects.Teacher;
import org.woym.controller.planning.PlanningController;
import org.woym.persistence.DataAccess;

/**
 * <h1>PersonalPlanHelper</h1>
 * <p>
 * Diese Klasse dient dazu, eine Liste von {@link PersonalPlanRow}s zu erzeugen,
 * die dann verwendet werden kann, um den Auslastungsplan der Lehrkräfte
 * darzustellen.
 * <p>
 * Die Klasse wird im {@link PlanningController} verwendet, um den Plan
 * anzuzeigen.
 * 
 * @author Tim Hansen (tihansen)
 * @version 0.2.0
 * @since 0.2.0
 * 
 * @see PlanningController
 * @see PersonalPlanRow
 */
public class PersonalPlanHelper {

	private static final Logger LOGGER = LogManager
			.getLogger(PersonalPlanHelper.class);

	private static final PersonalPlanHelper INSTANCE = new PersonalPlanHelper();

	private DataAccess dataAccess = DataAccess.getInstance();

	private PersonalPlanHelper() {

	}

	/**
	 * Liefert eine Instanz des {@link PersonalPlanHelper}s zurück.
	 * 
	 * @return Eine Instanz des {@link PersonalPlanHelper}s
	 */
	public static PersonalPlanHelper getInstance() {
		return INSTANCE;
	}

	/**
	 * Diese Methode erzeugt den Lehrereinsatzplan.
	 * <p>
	 * Dafür werden alle dem System bekannten Objekte vom Typ {@link Activity}
	 * daraufhin überprüft, ob es sie vom Typ {@link Lesson} sind und in dem
	 * Fall für alle teilnehmenden Objekte vom Typ {@link Teacher} in der
	 * entsprechenden {@link Map} entweder ein Eintrag angelegt oder dessen
	 * Daten erneuert.
	 * <p>
	 * Anhand dieser Daten wird dann eine Liste von zur Darstellung verwendbaren
	 * {@link PersonalPlanRow}-Objekten erzeugt und zurückgeliefert
	 * 
	 * @return Eine {@link List} mit darstellbaren {@link PersonalPlanRow}
	 *         -Objekten
	 */
	public List<PersonalPlanRow> getPersonalPlanRows() {
		List<PersonalPlanRow> personalPlanRows = new ArrayList<>();

		try {
			Map<Teacher, PersonalPlanRow> teacherMap = produceTeacherMap();

			List<Activity> activities = dataAccess.getAllActivities();

			// Filter for Lessons
			List<Lesson> lessons = new ArrayList<>();
			for (Activity activity : activities) {
				if (activity instanceof Lesson) {
					Lesson lesson = (Lesson) activity;
					lessons.add(lesson);
				}
			}

			// Iterate Lessons
			for (Lesson lesson : lessons) {
				for (EmployeeTimePeriods employeeTimePeriods : lesson
						.getEmployeeTimePeriods()) {

					if (employeeTimePeriods.getEmployee() instanceof Teacher) {
						Teacher teacher = (Teacher) employeeTimePeriods
								.getEmployee();

						teacherMap.get(teacher).insertCellValue(lesson);

					}
				}
			}

			// Reduce to List
			personalPlanRows = new ArrayList<PersonalPlanRow>(
					teacherMap.values());

		} catch (DatasetException e) {
			LOGGER.error(e);
		}

		return personalPlanRows;
	}

	/**
	 * Erzeugt eine neue {@link Map} mit {@link Teacher}-Objekten.
	 * 
	 * @return {@link Map} mit {@link Teacher}-Objekten
	 */
	private Map<Teacher, PersonalPlanRow> produceTeacherMap() {
		Map<Teacher, PersonalPlanRow> teacherMap = new HashMap<>();

		try {
			List<Teacher> teachers = dataAccess.getAllTeachers();

			for (Teacher teacher : teachers) {
				BigDecimal freeHours = teacher.getHoursPerWeek().subtract(
						teacher.getAllocatedHours());

				PersonalPlanRow personalPlanRow = new PersonalPlanRow(
						teacher.getSymbol(), teacher.getHoursPerWeek(),
						freeHours);

				teacherMap.put(teacher, personalPlanRow);
			}
		} catch (DatasetException e) {
			LOGGER.error(e);
		}

		return teacherMap;
	}
}
