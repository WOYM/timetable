package org.woym.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ConfigServletListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent event) {
		Config.init();
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}
}
