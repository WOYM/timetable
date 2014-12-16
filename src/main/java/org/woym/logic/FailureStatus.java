/**
 * 
 */
package org.woym.logic;

import java.util.ArrayList;
import java.util.List;

import org.woym.spec.logic.IStatus;

/**
 * @author Whatever
 *
 */
public class FailureStatus implements IStatus {

	ArrayList<Exception> exceptions = new ArrayList<>();

	/**
	 * Fügt eine Exception zu der internen List ehinzu.
	 * 
	 * @param exception
	 * 		Hinzufügende Exception
	 * 
	 * @return
	 * 		{@code true} beim erfolgreichen Hinzufügen
	 * 		{@code false} beim Misserfolg
	 */
	public boolean addException(Exception exception) {
		return exceptions.add(exception);
	}
	
	@Override
	public List<String> report() {
		throw new UnsupportedOperationException();
	}

}
