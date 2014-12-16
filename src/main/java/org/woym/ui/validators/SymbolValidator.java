package org.woym.ui.validators;

import javax.el.ELContext;
import javax.el.ValueExpression;
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
import org.woym.messages.StatusMessageEnum;
import org.woym.objects.Employee;
import org.woym.objects.Teacher;
import org.woym.persistence.DataAccess;

/**
 * <h1>SymbolValidator</h1>
 * <p>
 * Dieser Validator prüft ein Symbol auf Gültigkeit.
 * <p>
 * Ein Symbol ist dann gültig, wenn es...:
 * <ul>
 * <li>
 * <b>Nicht</b> {@code null} oder leer ist.</li>
 * <li>
 * <b>Nicht</b> bereits von einem anderen Objekt verwendet wird.</li>
 * </ul>
 * <p>
 * <h2>Zusätzliche Informationen</h2>
 * Vor der Validierung wird ein Symbol getrimmt, das heißt, es werden
 * vorrangehende oder nachfolgende Leerzeichen entfernt.<br>
 * Ein Leerzeichen im Inneren eines Symbols ist gültig.<br>
 * Ein Symbol wird immer in Großbuchstaben umgewandelt
 * 
 * @author Tim Hansen (tihansen)
 *
 */
@FacesValidator("org.woym.SymbolValidator")
public class SymbolValidator implements Validator {

	public static final String BEAN_NAME = "teacherBean";
	
	private static Logger LOGGER = LogManager.getLogger(SymbolValidator.class);
	private DataAccess dataAccess = DataAccess.getInstance();

	@Override
	public void validate(FacesContext context, UIComponent uiComponent,
			Object value) throws ValidatorException {

		String symbol = value.toString();
		
		ELContext elContext = context.getELContext();
		ValueExpression valueExpression = uiComponent.getValueExpression(BEAN_NAME);
		Object teacherBean = valueExpression.getValue(elContext);

		if (teacherBean instanceof Teacher) {
			if (StringUtils.isNullOrEmpty(symbol)) {
				FacesMessage msg = new FacesMessage(
						StatusMessageEnum.SYMBOL_IS_EMPTY.getSummary(),
						StatusMessageEnum.SYMBOL_IS_EMPTY.getStatusMessage());
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(msg);
			}

			symbol = symbol.trim();

			if (StringUtils.isNullOrEmpty(symbol)) {
				FacesMessage msg = new FacesMessage(
						StatusMessageEnum.SYMBOL_IS_EMPTY.getSummary(),
						StatusMessageEnum.SYMBOL_IS_EMPTY.getStatusMessage());
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(msg);
			}

			try {
				Employee employee = dataAccess.getOneEmployee(symbol);

				if (employee != null && employee != teacherBean) {
					FacesMessage msg = new FacesMessage(
							StatusMessageEnum.SYMBOL_ALREADY_EXISTS
									.getSummary(),
							StatusMessageEnum.SYMBOL_ALREADY_EXISTS
									.getStatusMessage());
					msg.setSeverity(FacesMessage.SEVERITY_ERROR);
					throw new ValidatorException(msg);
				}

			} catch (DatasetException e) {
				LOGGER.error(e);
				FacesMessage msg = new FacesMessage(
						StatusMessageEnum.DATABASE_COMMUNICATION_ERROR
								.getSummary(),
						StatusMessageEnum.DATABASE_COMMUNICATION_ERROR
								.getStatusMessage());
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(msg);
			}
		}

	}

}
