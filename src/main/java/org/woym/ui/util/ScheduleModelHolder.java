package org.woym.ui.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;
import org.woym.common.objects.Employee;
import org.woym.common.objects.Entity;
import org.woym.common.objects.PedagogicAssistant;
import org.woym.common.objects.Room;
import org.woym.common.objects.Schoolclass;
import org.woym.common.objects.Teacher;
import org.woym.controller.PlanningController;
import org.woym.logic.util.ActivityParser;

/**
 * <h1>ScheduleModelHolder</h1>
 * <p>
 * Diese Klasse ist dafür zuständig, das in der Stundenplananzeige verwendete
 * {@link ScheduleModel} bereitzustellen.
 * <p>
 * Dies ermöglicht es allen Controllern auf das {@link ScheduleModel}
 * zuzugreifen.
 * 
 * @see ScheduleModel
 * @see PlanningController
 * 
 * @author Tim Hansen (tihansen)
 * @version 0.1.1
 * @since 0.1.1
 */
public class ScheduleModelHolder {

	private static ScheduleModelHolder INSTANCE = new ScheduleModelHolder();

	private static Logger LOGGER = LogManager
			.getLogger(ScheduleModelHolder.class);

	private ActivityParser activityParser = ActivityParser.getInstance();

	private ScheduleModel scheduleModel;
	private Entity entity;

	private ScheduleModelHolder() {
		scheduleModel = new DefaultScheduleModel();
	}

	/**
	 * Liefert die Instanz des {@link ScheduleModelHolder}s zurück.
	 * 
	 * @return Die Instanz
	 */
	public static ScheduleModelHolder getInstance() {
		return INSTANCE;
	}

	/**
	 * Erzeugt ein neues leeres {@link DefaultScheduleModel}.
	 */
	public ScheduleModel emptyScheduleModel() {
		return new DefaultScheduleModel();
	}

	public ScheduleModel getScheduleModel() {
		return scheduleModel;
	}

	public void setScheduleModel(ScheduleModel scheduleModel) {
		this.scheduleModel = scheduleModel;
	}

	/**
	 * Diese Methode lädt das {@link ScheduleModel} der derzeitig im
	 * {@link ScheduleModelHolder} gesetzten {@link Entity} neu.
	 * <p>
	 * Die unterstützten {@link Entity}-Typen sind:
	 * <ul>
	 * <li>Room</li>
	 * <li>Schoolclass</li>
	 * <li>Employee</li>
	 * </ul>
	 * <p>
	 * <b>Beachten:</b>
	 * Die Entity muss vorher mit {@code setEntity} gesetzt werden.
	 */
	public void updateScheduleModel() {
		if (entity instanceof Room) {
			scheduleModel = activityParser.getActivityModel((Room) entity);
		} else if (entity instanceof Teacher) {
			scheduleModel = activityParser.getActivityModel((Teacher) entity);
		} else if (entity instanceof PedagogicAssistant) {
			scheduleModel = activityParser
					.getActivityModel((PedagogicAssistant) entity);
		} else if (entity instanceof Schoolclass) {
			scheduleModel = activityParser
					.getActivityModel((Schoolclass) entity);
		}
	}

	public void setEntity(Entity entity) {
		if(entity == null) {
			throw new IllegalArgumentException("A null-entity must not be set.");
		}
		
		if (entity instanceof Room || entity instanceof Employee
				|| entity instanceof Schoolclass) {
			this.entity = entity;
		} else {
			LOGGER.warn("Illegal instance of entity was used. Ignoring.");
		}
	}

}
