package org.woym.controller.manage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.woym.exceptions.DatasetException;
import org.woym.logic.CommandHandler;
import org.woym.logic.SuccessStatus;
import org.woym.logic.command.AddCommand;
import org.woym.logic.command.DeleteCommand;
import org.woym.logic.command.UpdateCommand;
import org.woym.messages.GenericErrorMessage;
import org.woym.messages.MessageHelper;
import org.woym.objects.Location;
import org.woym.persistence.DataAccess;
import org.woym.spec.logic.IStatus;
import org.woym.spec.objects.IMemento;

/**
 * <h1>LocationController</h1>
 * <p>
 * This controller manages locations and rooms.
 * 
 * @author Tim Hansen (tihansen)
 *
 */
@ViewScoped
@ManagedBean(name = "locationController")
public class LocationController implements Serializable {

	private static final long serialVersionUID = 1L;

	private DataAccess dataAccess = DataAccess.getInstance();

	private static Logger LOGGER = LogManager
			.getLogger(LocationController.class);

	private CommandHandler commandHandler = CommandHandler.getInstance();

	private IMemento locationMemento;

	private Location location;

	/**
	 * Liefert eine Liste mit allen Standorten zurück.
	 * 
	 * @return Liste mit allen Standorten
	 */
	public List<Location> getLocations() {
		FacesMessage msg;
		try {
			return dataAccess.getAllLocations();
		} catch (DatasetException e) {
			LOGGER.error(e);
			msg = MessageHelper.generateMessage(
					GenericErrorMessage.DATABASE_COMMUNICATION_ERROR,
					FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return new ArrayList<Location>();
		}
	}

	public void generateLocationMemento() {
		locationMemento = location.createMemento();
	}

	/**
	 * Speichert einen bearbeiteten Standort in die Datenbank.
	 */
	public void editLocation() {
		UpdateCommand<Location> command = new UpdateCommand<>(location,
				locationMemento);
		IStatus status = commandHandler.execute(command);
		FacesMessage msg = status.report();

		if (status instanceof SuccessStatus) {
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('wEditLocationDialog').hide();");
		}

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	/**
	 * Löscht einen Standort aus der Datenbank.
	 */
	public void deleteLocation() {
		DeleteCommand<Location> command = new DeleteCommand<Location>(location);
		IStatus status = commandHandler.execute(command);
		FacesMessage msg = status.report();

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	/**
	 * Fügt einen Standort zur Datenbank hinzu
	 */
	public void addLocation() {
		AddCommand<Location> command = new AddCommand<>(location);
		IStatus status = commandHandler.execute(command);
		FacesMessage msg = status.report();

		if (status instanceof SuccessStatus) {
			location = new Location();
		}

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	/**
	 * Öffnet einen neuen Dialog, mit dem sich ein Standort hinzufügen lässt.
	 */
	public void addLocationDialog() {
		location = new Location();
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('wAddLocationDialog').show();");
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
}
