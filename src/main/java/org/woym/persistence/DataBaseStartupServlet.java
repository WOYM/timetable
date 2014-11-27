package org.woym.persistence;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;

public class DataBaseStartupServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3149436653576028981L;

	@Override
	public void init(ServletConfig servletConfig) {
		DataBase.getInstance().setUp();
	}

	@Override
	public void destroy() {
	}

}
