package org.woym.logic.spec;

public interface IMacroCommand extends ICommand {

	
	/**
	 * {@inheritDoc}
	 * Geht dafür die gesammte Liste mit {@link ICommand}
	 * durch. Führt die execute von {@link ICommand} aus.
	 * 
	 */
	@Override
	public IStatus execute();

}
