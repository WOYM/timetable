package org.woym.spec.logic;

import java.util.List;

public interface IStatus {
	
	/**
	 * erstellt einen für den Kunden 
	 * anzeigbaren Text mit Statusmeldungen.
	 * 
	 * @return
	 * 		Eine Liste mit Statusmeldungen, 
	 * 		der größe {@code > 0}
	 * 
	 */
	public List<String> report();

}
