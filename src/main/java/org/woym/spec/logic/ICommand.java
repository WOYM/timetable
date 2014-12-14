package org.woym.spec.logic;

public interface ICommand {
	
	/**
	 * Führt sich selber Aus. Liefert ein {@link IStatus} mit Information
	 * zurück.
	 * 
	 * @return {@link IStatus} mit Information zum Vorgang.
	 */
	public IStatus execute();

	/**
	 * Mach seine eigene Ausführung rückgängig. Liefert ein {@link IStatus} mit
	 * Information zurück.
	 * 
	 * @return {@link IStatus} mit Information zum Vorgang.
	 */
	public IStatus undo();

	/**
	 * Mach seine eigene Ausführung wieder gültig. Liefert ein {@link IStatus}
	 * mit Information zurück.
	 * 
	 * @return {@link IStatus} mit Information zum Vorgang.
	 */
	public IStatus redo();

}
