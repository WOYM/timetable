package org.woym.ui.util;

import org.primefaces.model.ScheduleModel;
import org.woym.common.objects.Employee;
import org.woym.controller.planning.PlanningController;
import org.woym.logic.util.ActivityParser;

/**
 * <h1>EmployeeDailyViewHelper</h1>
 * <p>
 * Diese Klasse dient zur Darstellung einer Tagesansicht im Tagesplan für
 * Mitarbeiter.
 * 
 * @author Tim Hansen (tihansen)
 * @version 0.2.0
 * @since 0.2.0
 * 
 * @see PlanningController
 * @see Employee
 * @see ScheduleModel
 * @see ActivityParser
 */
public class EmployeeDailyViewHelper {

	private Employee employee;
	private ScheduleModel scheduleModel;

	/**
	 * Erzeugt eine neue Darstellung einer Tagesansicht.
	 * 
	 * @param employee
	 *            Der {@link Employee}, für den die Tagesansicht dargestellt werden
	 *            soll
	 * @param scheduleModel
	 * 		Das {@link ScheduleModel}, das die entsprechenden Events beinhaltet
	 */
	public EmployeeDailyViewHelper(Employee employee,
			ScheduleModel scheduleModel) {
		if (employee == null || scheduleModel == null) {
			throw new IllegalArgumentException("Employee or model was null.");
		}

		this.employee = employee;
		this.scheduleModel = scheduleModel;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public ScheduleModel getScheduleModel() {
		return scheduleModel;
	}

	public void setScheduleModel(ScheduleModel scheduleModel) {
		this.scheduleModel = scheduleModel;
	}
}
