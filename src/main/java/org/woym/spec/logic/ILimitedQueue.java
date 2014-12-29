package org.woym.spec.logic;

public interface ILimitedQueue<E> {

	// Static final Size for all Queues
	public static final Integer LIST_SIZE = 10;

	/**
	 * Platziert das Element ans ende der schlange. Sollte dadurch die größe
	 * überschritten werden wird das erste Element verdrängt.
	 * 
	 * @param element
	 *            Einzufügendes Element
	 * 
	 */
	public void add(E element);

	/**
	 * Gibt das erste Elemente zurück. Sollte die Liste leer sein, wird
	 * {@code null} übergeben.
	 * 
	 * @return Das Element an der Stelle {@link FIRST_ELEM} wenn die Liste leer
	 *         ist {@code null}
	 */
	public E getFirst();

	/**
	 * Gibt das letzte Elemente zurück. Sollte die Liste leer sein, wird
	 * {@code null} übergeben.
	 * 
	 * @return Das Element an der Stelle {@link FIRST_ELEM} wenn die Liste leer
	 *         ist {@code null}
	 */
	public E getLast();

	/**
	 * Gibt die anzahl der Momentanen Einträge zurück.
	 * 
	 * @return Größe der Queue
	 */
	public Integer size();

	/**
	 * Leert die Queue
	 */
	public void clear();

}
