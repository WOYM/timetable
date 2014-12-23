package org.woym.objects;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.woym.exceptions.DatasetException;
import org.woym.persistence.DataAccess;
import org.woym.spec.objects.IMemento;
import org.woym.spec.objects.IMementoObject;

/**
 * Diese Singleton-Klasse speichert eine Liste von Kanten, welche die Wegzeiten
 * zwischen je zwei Standorten angeben.
 * 
 * @author Adrian
 *
 */
@Entity
public final class TravelTimeList extends org.woym.objects.Entity implements
		IMementoObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7064076404550326134L;

	private static final Logger LOGGER = LogManager
			.getLogger(TravelTimeList.class);

	/**
	 * Die ID. Da es nur ein Objekt dieser Klasse in der Datenbank geben soll,
	 * wird eine feste ID vergeben.
	 */
	@Id
	private final Long id = 1L;

	/**
	 * Die Liste von Kanten.
	 */
	@ElementCollection
	private List<Edge> edges = new ArrayList<Edge>();

	/**
	 * Die Singleton-Instanz. Sie wird nicht direkt erzeugt, sondern muss über
	 * einen Aufruf von {@linkplain TravelTimeList#load()} erzeugt werden.
	 */
	private static TravelTimeList INSTANCE;

	private TravelTimeList() {
	}

	/**
	 * Lädt die vorhandenen Instanz aus der Datenbank oder erzeugt eine, wenn
	 * keine vorhanden ist und gibt sie zurück.
	 * 
	 * @return die Singleton-Instanz
	 */
	public static TravelTimeList getInstance() {
		if (INSTANCE == null) {
			try {
				TravelTimeList list = DataAccess.getInstance()
						.getTravelTimeList();
				if (list == null) {
					INSTANCE = new TravelTimeList();
					INSTANCE.persist();
				} else {
					INSTANCE = list;
				}
			} catch (DatasetException e) {
				LOGGER.error("Failed to load TravelTimeList.", e);
			}
		}
		return INSTANCE;
	}

	public Long getId() {
		return id;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public void setEdges(List<Edge> edges) {
		if (edges == null) {
			throw new IllegalArgumentException();
		}
		this.edges = edges;
	}

	/**
	 * Fügt die übergebene Kante hinzu, sofern sie noch nicht vorhanden ist und
	 * gibt dann {@code true} zurück, ansonsten {@code false}.
	 * 
	 * @param edge
	 *            - die hinzuzufügende Kante
	 * @return {@code true}, wenn die übergebene Kante noch nicht vorhanden war
	 *         und hinzugefügt wurde
	 */
	public boolean addEdge(Edge edge) {
		if (!edges.contains(edge)) {
			edges.add(edge);
			return true;
		}
		return false;
	}

	/**
	 * Entfernt die übergebene Kante und gibt {@code true} zurück, wenn die
	 * Liste die übergebene Kante enthielt, ansonsten {@code false}.
	 * 
	 * @param edge
	 *            - die zu löschende Kante
	 * @return {@code true}, wenn sie vorhanden war, ansonsten {@code false}
	 */
	public boolean removeEdge(Edge edge) {
		return edges.remove(edge);
	}

	/**
	 * Erzeugt aus den übergebenen Parametern eine neue Kante und versucht sie
	 * hinzuzufügen. Ist sie noch nicht vorhanden, wird sie hinzugefügt und
	 * {@code true} zurückgegeben, ansonsten wird sie nicht hinzugefügt und
	 * {@code false} zurückgegeben.
	 * 
	 * @param location1
	 *            - ein Standort der Kante
	 * @param location2
	 *            - der andere Standort der Kante
	 * @param distance
	 *            - die Distanz (> 0)
	 * @return
	 */
	public boolean add(Location location1, Location location2, int distance) {
		Edge edge = new Edge(location1, location2, distance);
		if (!edges.contains(edge)) {
			edges.add(edge);
			return true;
		}
		return false;
	}

	/**
	 * Erzeugt aus den übergebenen zwei Standorten eine Kante und versucht die
	 * zu dieser Kante äquivalente zu entfernen. Ist eine äquivalente Kante
	 * vorhanden, wird {@code true} zurückgegeben, ansonsten {@code false}.
	 * 
	 * @param location1
	 *            - erster Standort
	 * @param location2
	 *            - zweiter Standort
	 * @return {@code true}, wenn eine äquivalente Kante vorhanden, ansonsten
	 *         {@code false}
	 */
	public boolean remove(Location location1, Location location2) {
		Edge edge = new Edge(location1, location2, 1);
		return edges.remove(edge);
	}

	/**
	 * Gibt eine Liste aller zum übergebenen Standort inzidenten Kanten zurück.
	 * 
	 * @param location
	 *            - Standort, für den alle Kanten zurückgegeben werden sollen
	 * @return Liste aller zum übergebenen Standort inzidenten Kanten
	 */
	public List<Edge> getTravelTimes(Location location) {
		if (location == null) {
			throw new IllegalArgumentException();
		}
		List<Edge> toReturn = new ArrayList<>();
		for (Edge e : edges) {
			if (e.contains(location)) {
				toReturn.add(e);
			}
		}
		return toReturn;
	}

	/**
	 * Überschreibt die persist-Methode, so dass
	 * {@linkplain DataAccess#persist(java.io.Serializable)} nur aufgerufen
	 * wird, wenn noch kein {@linkplain TravelTimeList}-Objekt in der Datenbank
	 * vorhanden ist.
	 */
	@Override
	public void persist() throws DatasetException {
		if (DataAccess.getInstance().getTravelTimeList() == null) {
			super.persist();
		}
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
	@Override
	public void setMemento(IMemento memento) {
		if (memento == null) {
			throw new IllegalArgumentException("Parameter is null.");
		}
		if (memento instanceof Memento) {
			Memento actualMemento = (Memento) memento;
			edges = actualMemento.edges;
		} else {
			throw new IllegalArgumentException(
					"Only org.woym.objects.TravelTimeList.Memento as parameter allowed.");
		}
	}

	/**
	 * Die Memento-Klasse zu {@linkplain TravelTimeList}.
	 * 
	 * @author adrian
	 *
	 */
	public static class Memento implements IMemento {

		private final List<Edge> edges;

		Memento(TravelTimeList originator) {
			if (originator == null) {
				throw new IllegalArgumentException();
			}
			edges = originator.edges;
		}
	}

	/**
	 * Die Klasse Edge repräsentiert als ungerichtete Kante zwischen zwei
	 * Standorten mit einer bestimmten Distanz eine Wegzeit.
	 * 
	 * @author Adrian
	 *
	 */
	@Embeddable
	public static class Edge {

		/**
		 * Der eine Standort der Kante.
		 */
		private Location location1;

		/**
		 * Der andere Standort der Kante.
		 */
		private Location location2;

		/**
		 * Die Distanz in Minuten.
		 */
		private int distance;

		/**
		 * Leerer Konstruktor für Eclipselink.
		 */
		public Edge() {
		}

		/**
		 * Erzeugt eine neue Kante mit den übergebenen Parametern. Ist einer der
		 * beiden übergebenen Standorte {@code null}, die Distanz kleiner als 1
		 * oder sind die beiden Standorte im Sinne von equals gleich, wird eine
		 * {@linkplain IllegalArgumentException} geworfen.
		 * 
		 * @param location1
		 *            - der eine Standort
		 * @param location2
		 *            - der andere Standort
		 * @param distance
		 *            - die Distanz in Minuten
		 */
		public Edge(Location location1, Location location2, int distance) {
			if (location1 == null || location2 == null || distance < 1
					|| location1.equals(location2)) {
				throw new IllegalArgumentException();
			}
			this.location1 = location1;
			this.location2 = location2;
			this.distance = distance;
		}

		public Location getLocation1() {
			return location1;
		}

		public void setLocation1(Location location1) {
			this.location1 = location1;
		}

		public Location getLocation2() {
			return location2;
		}

		public void setLocation2(Location location2) {
			this.location2 = location2;
		}

		public int getDistance() {
			return distance;
		}

		public void setDistance(int distance) {
			this.distance = distance;
		}

		/**
		 * Gibt {@code true} zurück, wenn der übergebene Standort Teil der Kante
		 * ist.
		 * 
		 * @param location
		 *            - zu prüfender Standort
		 * @return {@code true}, wenn der übergebene Standort Teil der Kante
		 *         ist, ansonsten {@code false}
		 */
		public boolean contains(Location location) {
			return location1.equals(location) || location2.equals(location);
		}

		/**
		 * Gibt den zum übergebenen Standort adjazenten zurück. Wird
		 * {@code null} übergeben oder ist der übergebene Standort nicht Teil
		 * der Kante, wird eine {@linkplain IllegalArgumentException} geworfen.
		 * 
		 * @param location
		 *            - der Standort, für welchen der adjazente gesucht wird
		 * @return der zum übergebenen Standort adjazente
		 */
		public Location getAdjacentLocation(Location location) {
			if (location == null || !contains(location)) {
				throw new IllegalArgumentException(
						"Passed location is null or not part of this edge.");
			}
			return location1.equals(location) ? location2 : location1;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((location1 == null) ? 0 : location1.hashCode());
			result = prime * result
					+ ((location2 == null) ? 0 : location2.hashCode());
			return result;
		}

		/**
		 * Gibt {@code true} zurück, wenn das übergebene Objekt == diese ist
		 * oder wenn das übergebene Objekt eine Kante ist und die selben
		 * Standorte besitzt wie das Objekt, auf welchem die Methode aufgerufen
		 * wird.
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Edge other = (Edge) obj;
			return (location1.equals(other.location1) && location2
					.equals(other.location2))
					|| (location1.equals(other.location2) && location2
							.equals(other.location1));
		}
	}
}
