package org.woym.controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * <h1>GUIController</h1>
 * <p>
 * Diese Klasse ist für die zentrale Steuerung der Benutzeroberfläche zuständig.
 * <p>
 * Sie wird mit dem initialen Aufruf des Programms geladen und ist einmal pro Nutzer-Session existent. <br />
 * Die Klasse ist für das asynchrone Laden von Inhalten sowie die Darstellung von Statistiken zuständig.
 * 
 * @author Tim Hansen (tihansen)
 */
@SessionScoped
@ManagedBean(name="GUIController")
public class GUIController implements Serializable {

	private static final long serialVersionUID = 8156760488563380338L;
	
	private String page = "manageTeachers";

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}
	
}
