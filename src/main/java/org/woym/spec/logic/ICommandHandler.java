package org.woym.spec.logic;

public interface ICommandHandler {

	/**
	 * Führt das übergebene {@link ICommand} aus. Füllt es Anschließend in die
	 * Undo-Liste. Gibt ein {@link IStatus} mit Information zurück.
	 * 
	 * @param command
	 *            Auszuführendes Command
	 * 
	 * @return {@link IStatus} mit Information zum Vorgang.
	 */
	public IStatus execute(ICommand command);

	/**
	 * Entnimmt das letzte Object der Liste, des CommangsHandlers (undo) und
	 * führt das {@link ICommmamd} aus. Füllt es in die Redo-Liste. Gibt ein
	 * {@link IStatus} mit Information zurück.
	 * 
	 * @return {@link IStatus} mit Information zum Vorgang.
	 */
	public IStatus undo();

	/**
	 * Entnimmt das letzte Object der Liste, des CommangsHandlers (redo) und
	 * führt das {@link ICommmamd} aus. Füllt es in die Undo-Liste. Gibt ein
	 * {@link IStatus} mit Information zurück.
	 * 
	 * @return {@link IStatus} mit Information zum Vorgang.
	 */
	public IStatus redo();

}
