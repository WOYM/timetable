package org.woym.persistence;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;

/**
 * Dieses Servlet dient dazu die Datenbank mit dem Start des Containers zu
 * starten.
 * 
 * @author Adrian
 *
 */
public class DataBaseStartupServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3149436653576028981L;

	/**
	 * Ruft {@linkplain DataBase#setUp()} auf, um die Datenbankverbindung
	 * herzustellen.
	 */
	@Override
	public void init(ServletConfig servletConfig) {
		DataBase.getInstance().setUp();
	}

	@Override
	public void destroy() {
	}

}
