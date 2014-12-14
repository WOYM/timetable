/**
 * 
 */
package org.woym.logic;

import java.util.ArrayList;
import java.util.List;

import org.woym.spec.logic.IStatus;

/**
 * Representation eines erfolgreichen {@link IStatus}
 * 
 * @author JurSch
 *
 */
public class SuccessStatus implements IStatus {

	final private ArrayList<String> status = new ArrayList<>(1);

	@Override
	public List<String> report() {
		createStatus();

		return status;
	}

	private void createStatus() {
		status.add("Ayeeeee");

	}

}
