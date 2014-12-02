package org.woym.ui.validators;

import java.util.List;

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
import org.woym.objects.Teacher;
import org.woym.persistence.TeacherDAO;

/**
 * <h1>SymbolValidator</h1>
 * <p>
 * This validator validates a symbol.
 * <p>
 * A name is valid if it is 
 * <ul>
 * 		<li>
 * 			<b>Not</b> null or empty, eg. "" or " "
 * 		</li>
 * </ul>
 * 
 * <h2>Additional information</h2>
 * 
 * @author Tim Hansen (tihansen)
 *
 */
@FacesValidator("org.woym.SymbolValidator")
public class SymbolValidator implements Validator{
	
	private static Logger LOGGER = LogManager.getLogger(SymbolValidator.class);
	
	TeacherDAO teacherDAO = new TeacherDAO();

	@Override
	public void validate(FacesContext context, UIComponent uiComponent, Object value)
			throws ValidatorException {
		
		String symbol = value.toString();
		
		if(StringUtils.isNullOrEmpty(symbol)) {
			FacesMessage msg = 
					new FacesMessage("Ungültiges Kürzel.", 
							"Kürzel darf nicht leer sein.");
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(msg);
		}
		
		// Check for existance of other teachers with same symbol
		// TODO Get that to work
		try {
			List<Teacher> teacherList = teacherDAO.getBySymbol(symbol);
			
			if(teacherList.size() != 0) {
				FacesMessage msg = 
						new FacesMessage("Ungültiges Kürzel.", 
								"Das Kürzel wird bereis verwendet.");
					msg.setSeverity(FacesMessage.SEVERITY_ERROR);
					throw new ValidatorException(msg);
			}
		} catch (DatasetException e) {
			LOGGER.error(e);
		}
		
		
	}

}
