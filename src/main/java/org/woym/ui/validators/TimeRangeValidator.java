package org.woym.ui.validators;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.woym.common.config.Config;
import org.woym.common.config.DefaultConfigEnum;

@FacesValidator("org.woym.TimeRangeValidator")
public class TimeRangeValidator implements Validator {

	private static Logger LOGGER = LogManager.getLogger(TimeRangeValidator.class);
	
	/**
	 * Dieser Validator validiert zwei Date Objekte, b
	 */
	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		if (value == null) {
			return;
		}

		UIInput startTimeValue = (UIInput) component.getAttributes().get(
				"startTime");
		if (startTimeValue == null) {
			return;
		}

		Date startTime = (Date) startTimeValue.getValue();
		Date endTime = (Date) value;

		if (!startTime.before(endTime)) {
			startTimeValue.setValid(false);
			throw new ValidatorException(new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Startzeit muss vor der Endzeit liegen.",
					"Die Startzeit muss vor der Endzeit liegen."));
		}
		
		if(isTimeInRange(startTime, endTime)) {
			throw new ValidatorException(new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Die Zeit lag außerhablb der Grenzen.",
					"Die Zeit muss innerhalb der Grenzen liegen."));
		}
	}
	
	/**
	 * Bestimmt ob die übergebenen Zeiter außerhalb , der durch die
	 * Einstellungen festgelegten, Grenzen liegen.
	 * 
	 * @param startTime
	 *            Startzeit
	 * @param endTime
	 *            Endzeit
	 * @return Wahrheitswert ob sie außerhalb der Grenzen sind.
	 */
	@SuppressWarnings("deprecation")
	private boolean isTimeInRange(final Date startTime, final Date endTime) {
		SimpleDateFormat timeLimit = new SimpleDateFormat("HH:mm");
		Date startingTimeLimit = new Date();
		startingTimeLimit.setTime(startTime.getTime());
		Date endingTimeLimit = new Date();
		endingTimeLimit.setTime(endTime.getTime());
		try {
			Date localDate = timeLimit.parse(Config
					.getSingleStringValue(DefaultConfigEnum.WEEKDAY_STARTTIME));
			startingTimeLimit.setMinutes(localDate.getMinutes());
			startingTimeLimit.setHours(localDate.getHours());

			localDate = timeLimit.parse(Config
					.getSingleStringValue(DefaultConfigEnum.WEEKDAY_ENDTIME));
			endingTimeLimit.setMinutes(localDate.getMinutes());
			endingTimeLimit.setHours(localDate.getHours());
		} catch (ParseException e) {
			LOGGER.warn("Parse Error on: " + e.getMessage());
		}
		return startTime.before(startingTimeLimit)
				|| endTime.after(endingTimeLimit);

	}

}
