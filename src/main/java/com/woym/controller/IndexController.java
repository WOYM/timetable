package com.woym.controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * <h1>indexController</h1>
 * <p>
 * Diese Klasse verarbeitet die Eingaben der Seite {@code index.xhtml}.
 * <p>
 * Die Methoden dieser Klasse werden, sofern sie {@code public} sind, von der
 * {@code xhtml}-Seite per EL (Expression Language) angesprochen.
 * <p>
 * Diese Klasse arbeitet also nur, wenn der Nutzer dies über die {@code xhtml}
 * -Seite veranlasst hat, bzw. wenn dort eine Eigenschaft dieser Klasse im
 * Ladevorgang gebraucht wird.
 * <p>
 * Die Bean ist {@code SessionScoped}, das heißt, sie existiert so lange, wie
 * eine Session mit dem Nutzer besteht.
 *
 * @author Tim Hansen
 */
@ManagedBean(name = "indexController")
@SessionScoped
public class IndexController implements Serializable{

	private static final long serialVersionUID = -7535821830387986791L;

	/**
	 * <p>
	 * Die Überschrift der index-Seite.
	 */
	private static final String HEADLINE = "Willkommen!";

	/**
	 * <p>
	 * Der Name des Nutzers.
	 */
	private String name = null;

	/**
	 * <p>
	 * Diese Methode liefert die Überschrift zurück.
	 * 
	 * @return Die Überschrift
	 */
	public final String getHeadline() {
		return HEADLINE;
	}

	/**
	 * <p>
	 * Diese Methode liefert den Namen des Nutzers zurück.
	 * 
	 * @return Der Name des Nutzers
	 */
	public final String getName() {
		return name;
	}

	/**
	 * <p>
	 * Setzt den Namen des Nutzers.
	 * 
	 * @param name
	 *            Der Name des Nutzers
	 */
	public final void setName(final String name) {
		this.name = name;
	}

}
