package org.woym.objects;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Diese Klasse repräsentiert ein Projekt.
 * 
 * @author Adrian
 *
 */
@Entity
@DiscriminatorValue("Project")
public class Project extends Activity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8279381157083717967L;

	private ProjectType projectType;

	public Project() {
	}

	public ProjectType getProjectType() {
		return projectType;
	}

	public void setProjectType(ProjectType projectType) {
		this.projectType = projectType;
	}

	/**
	 * Erzeugt ein neues {@linkplain Memento} und gibt es zurück.
	 * 
	 * @return ein {@linkplain Memento} mit dem aktuellen Zustand des Objektes
	 */
	public Memento createMemento() {
		return new Memento(this);
	}

	/**
	 * Setzt den Status des {@linkplain Project}-Objektes auf den Status des
	 * übergebenen {@linkplain Memento}-Objektes.
	 * 
	 * @param memento
	 *            - das Memento-Objekt, von welchem das {@linkplain Project}
	 *            -Objekt den Status annehmen soll
	 */
	public void setMemento(Memento memento) {
		super.setMemento(memento);
		projectType = memento.projectType;
	}

	/**
	 * Die Memento-Klasse zu {@linkplain Project}.
	 * 
	 * @author adrian
	 *
	 */
	public static class Memento extends Activity.Memento {

		private final ProjectType projectType;

		public Memento(Project originator) {
			super(originator);
			projectType = originator.projectType;
		}
	}
}
