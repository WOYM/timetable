package org.woym.objects;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.woym.spec.objects.IMemento;

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
	 * {@inheritDoc}
	 */
	@Override
	public Memento createMemento() {
		return new Memento(this);
	}

	/**
	 * Bei Übergabe von {@code null} oder einem Parameter, der nicht vom Typ
	 * {@linkplain Memento} ist, wird eine {@linkplain IllegalArgumentException}
	 * geworfen. Ansonsten wird der Status des Objektes auf den des übergebenen
	 * Memento-Objektes gesetzt.
	 * 
	 * @param memento
	 *            - das {@linkplain Memento}-Objekt, von welchem dieses Objekt
	 *            den Status annehmen soll
	 */
	public void setMemento(IMemento memento) {
		super.setMemento(memento);
		if (memento instanceof Memento) {
			Memento actualMemento = (Memento) memento;
			projectType = actualMemento.projectType;
		} else {
			throw new IllegalArgumentException(
					"Only org.woym.objects.Project.Memento as parameter allowed.");
		}

	}

	/**
	 * Die Memento-Klasse zu {@linkplain Project}.
	 * 
	 * @author adrian
	 *
	 */
	public static class Memento extends Activity.Memento {

		private final ProjectType projectType;

		Memento(Project originator) {
			super(originator);
			projectType = originator.projectType;
		}
	}
}
