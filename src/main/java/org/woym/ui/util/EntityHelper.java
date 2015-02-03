package org.woym.ui.util;

import org.woym.common.objects.AcademicYear;
import org.woym.common.objects.Entity;
import org.woym.common.objects.Location;
import org.woym.common.objects.PedagogicAssistant;
import org.woym.common.objects.Room;
import org.woym.common.objects.Schoolclass;
import org.woym.common.objects.Teacher;
import org.woym.controller.planning.PlanningController;

/**
 * <h1>EntityHelper</h1>
 * <p>
 * Diese Klasse wird vom {@link PlanningController} sowie den einzelnen
 * Controller der verschiedenen Aktivitätstypen verwendet, um die in der GUI
 * gesetzte {@link Entity} bestimmen zu können.
 * 
 * @author Tim Hansen (tihansen)
 * @version 0.1.0
 * @since 0.1.0
 * 
 * @see Entity
 * @see PlanningController
 */
public class EntityHelper {

	private static EntityHelper INSTANCE = new EntityHelper();

	private Teacher teacher;
	private PedagogicAssistant pedagogicAssistant;
	private Location location;
	private Room room;
	private AcademicYear academicYear;
	private Schoolclass schoolclass;

	private EntityHelper() {

	}

	/**
	 * Liefert eine Instanz des {@link EntityHelper} zurück.
	 * 
	 * @return Eine Instanz des {@link EntityHelper}
	 */
	public static EntityHelper getInstance() {
		return INSTANCE;
	}

	/**
	 * Liefert einen Wahrheitswert, ob in der Klasse bereits eine {@link Entity}
	 * gesetzt wurde.
	 * 
	 * @return Wahrheitswert, ob eine {@link Entity} gesetzt wurde
	 */
	public Boolean getHasEntity() {
		return (teacher != null || pedagogicAssistant != null || room != null || schoolclass != null);
	}

	/**
	 * Setzt alle Objekte außer dem Übergebenen {@code null}.
	 * <p>
	 * Bei einer Klasse oder einem Raum wird das Elternobjekt, also Jahrgang
	 * oder Standort nicht zurückgesetzt.
	 * 
	 * @param entity
	 *            Die Entity, die nicht null gesetzt werden soll.
	 */
	private void unsetAllExcept(Entity entity) {
		if (!(entity instanceof Teacher)) {
			teacher = null;
		}
		if (!(entity instanceof PedagogicAssistant)) {
			pedagogicAssistant = null;
		}
		if (!(entity instanceof AcademicYear)
				&& !(entity instanceof Schoolclass)) {
			academicYear = null;
		}
		if (!(entity instanceof Schoolclass)) {
			schoolclass = null;
		}
		if (!(entity instanceof Location) && !(entity instanceof Room)) {
			location = null;
		}
		if (!(entity instanceof Room)) {
			room = null;
		}
	}
	
	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
		unsetAllExcept(teacher);
	}

	public PedagogicAssistant getPedagogicAssistant() {
		return pedagogicAssistant;
	}

	public void setPedagogicAssistant(PedagogicAssistant pedagogicAssistant) {
		this.pedagogicAssistant = pedagogicAssistant;
		unsetAllExcept(pedagogicAssistant);
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
		unsetAllExcept(room);
	}

	public Schoolclass getSchoolclass() {
		return schoolclass;
	}

	public void setSchoolclass(Schoolclass schoolclass) {
		this.schoolclass = schoolclass;
		unsetAllExcept(schoolclass);
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
		unsetAllExcept(location);
	}

	public AcademicYear getAcademicYear() {
		return academicYear;
	}

	public void setAcademicYear(AcademicYear academicYear) {
		this.academicYear = academicYear;
		unsetAllExcept(academicYear);
	}
}
