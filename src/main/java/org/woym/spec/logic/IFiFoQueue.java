package org.woym.spec.logic;

public interface IFiFoQueue<E> {

	// Static final Size for all Queues
	public static final Integer LIST_SIZE = 5;

	// Static Variable for the first Element
	public static final Integer FIRST_ELEM = 0;

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
	 * Gibt das elemente an der Stelle von {@link FIRST_ElEM} zurück. Sollte die
	 * Liste leer sein, wird {@code null} übergeben.
	 * 
	 * @return Das Element an der Stelle {@link FIRST_ELEM} wenn die Liste leer
	 *         ist {@code null}
	 */
	public E get();
	
	/**
	 * Gibt die anzahl der Momentanen Einträge zurück.
	 * 
	 * @return
	 * 		Größe der Queue
	 */
	public Integer size();

}
