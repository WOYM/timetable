package org.woym.logic.util;

import java.util.List;

import javax.faces.application.FacesMessage;

import org.woym.exceptions.DatasetException;
import org.woym.logic.CommandHandler;
import org.woym.logic.FailureStatus;
import org.woym.logic.SuccessStatus;
import org.woym.logic.command.CommandCreator;
import org.woym.logic.command.MacroCommand;
import org.woym.logic.spec.IStatus;
import org.woym.messages.GenericErrorMessage;
import org.woym.objects.Activity;
import org.woym.persistence.DataAccess;

public abstract class ConfigControllerUtil {

	/**
	 * Löscht alle vorhandenen Aktivitäten und gibt ein entsprechendes
	 * IStatus-Objekt zurück.
	 * 
	 * @return
	 */
	public static IStatus deleteAllActivities() {
		try {
			List<Activity> activities = DataAccess.getInstance()
					.getAllActivities();
			if (activities.isEmpty()) {
				return new SuccessStatus("Keine Aktivitäten zu löschen.",
						"Es sind keine zu löschenden Aktivitäten vorhanden.",
						FacesMessage.SEVERITY_INFO);
			}
			MacroCommand macro = new MacroCommand();
			for (Activity a : activities) {
				macro.add(CommandCreator.getInstance().createDeleteCommand(a));
			}
			return CommandHandler.getInstance().execute(macro);
		} catch (DatasetException e) {
			return new FailureStatus(
					GenericErrorMessage.DATABASE_COMMUNICATION_ERROR,
					FacesMessage.SEVERITY_ERROR);
		}
	}
}
