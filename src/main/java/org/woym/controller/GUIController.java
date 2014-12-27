package org.woym.controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.woym.logic.CommandHandler;
import org.woym.spec.logic.IStatus;

/**
 * <h1>GUIController</h1>
 * <p>
 * Diese Klasse verwaltet den CommandHandler, bzw. die Undo- und
 * Redofunktionalität, die durch den Nutzer ausgelöst werden kann.
 * 
 * @author Tim Hansen (tihansen)
 */
@SessionScoped
@ManagedBean(name = "GUIController")
public class GUIController implements Serializable {

	private static final long serialVersionUID = 8156760488563380338L;
	
	private CommandHandler commandHandler = CommandHandler.getInstance();
	
	/**
	 * Diese Methode macht ein Command rückgängig.
	 */
	public void undo() {
		IStatus status = commandHandler.undo();
		FacesContext.getCurrentInstance().addMessage(null, status.report());
	}
	
	/**
	 * Diese Methode wiederholt ein Command.
	 */
	public void redo() {
		IStatus status = commandHandler.redo();
		FacesContext.getCurrentInstance().addMessage(null, status.report());
	}

}
