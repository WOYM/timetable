package org.woym.config;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;

/**
 * Diese Klasse lädt als Servlet die Konfigurationen des Systems.
 * 
 * @author Adrian
 *
 */
public class ConfigServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7621068974106280773L;

	@Override
	public void init(ServletConfig servletConfig) {
		Config.init();
	}
}
