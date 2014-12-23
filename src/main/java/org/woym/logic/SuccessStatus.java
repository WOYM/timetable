/**
 * 
 */
package org.woym.logic;

import javax.faces.application.FacesMessage;

import org.woym.spec.logic.IStatus;

/**
 * Representation eines erfolgreichen {@link IStatus}
 * 
 * @author JurSch
 *
 */
public class SuccessStatus implements IStatus {

	@Override
	public FacesMessage report() {
		// TODO: sinnvolle Implementierung notwendig. Nachrichten bei Erfolg
		// werden momentan direkt in den Controllern erzeugt
		return null;
	}

}
