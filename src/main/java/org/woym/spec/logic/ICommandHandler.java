package org.woym.spec.logic;

public interface ICommandHandler {

/**
 * Entnimmt das letzte Object der Liste, des CommangsHandlers
 * und führt das {@link ICommmamd} aus.
 * Gibt ein {@link IStatus} mit Information zurück.
 * 
 * @return
 * 		{@link IStatus}  mit Information zum Vorgang.
 */
public IStatus undo();

}
