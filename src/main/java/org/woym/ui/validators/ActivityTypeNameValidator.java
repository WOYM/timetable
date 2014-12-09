package org.woym.ui.validators;


import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.util.StringUtils;
import org.woym.exceptions.DatasetException;
import org.woym.persistence.ActivityTypeDAO;

/**
 * <h1>ActivityTypeNameValidator</h1>
 * <p>
 * Dieser Validator prüft den Bezeichner eines 
 * Aktivitätstypen auf Gültigkeit.
 * <p>
 * Ein Bezeichner ist dann gültig, wenn er...: 
 * <ul>
 * 		<li>
 * 			<b>Nicht</b> {@code null} oder leer ist.
 * 		</li>
 * 		<li>
 * 			<b>Nicht</b> bereits von einem anderen
 * 			Objekt verwendet wird.
 * 		</li>
 * </ul>
 * <p>
 * <h2>Zusätzliche Informationen</h2>
 * Vor der Validierung wird ein Bezeichner getrimmt, 
 * das heißt, es werden vorrangehende oder 
 * nachfolgende Leerzeichen entfernt.<br>
 * Ein Leerzeichen im Inneren eines Symbols ist
 * gültig.<br>
 * Ein Bezeichner wird in Groß- und Kleinschreibung
 * nicht verändert.
 * 
 * @author Tim Hansen (tihansen)
 *
 */
@FacesValidator("org.woym.ActivityTypeNameValidator")
public class ActivityTypeNameValidator implements Validator{

	private static Logger LOGGER = LogManager.getLogger(ActivityTypeNameValidator.class);
	
	ActivityTypeDAO activityTypeDAO = ActivityTypeDAO.getInstance();

	@Override
	public void validate(FacesContext context, UIComponent uiComponent, Object value)
			throws ValidatorException {
		
		String name = value.toString();
		
		if(StringUtils.isNullOrEmpty(name)) {
			FacesMessage msg = 
					new FacesMessage("Ungültiger Bezeichner.", 
							"Bezeichner darf nicht leer sein.");
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(msg);
		}
		
		name = name.trim();
		
		if(StringUtils.isNullOrEmpty(name)) {
			FacesMessage msg = 
					new FacesMessage("Ungültiger Bezeichner.", 
							"Bezeichner darf nicht leer sein.");
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(msg);
		}
		
		try {
			if(activityTypeDAO.getOne(name) != null) {
				FacesMessage msg = 
						new FacesMessage("Ungültiger Bezeichner.", 
								"Der Bezeichner wird bereis verwendet.");
					msg.setSeverity(FacesMessage.SEVERITY_ERROR);
					throw new ValidatorException(msg);
			}
			
		} catch (DatasetException e) {
			LOGGER.error(e);
			FacesMessage msg = 
					new FacesMessage("Datenbankfehler", 
							"Bei der Kommunikation mit der Datenbank ist ein Fehler aufgetreten.");
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(msg);
		}
		
		
	}

}
