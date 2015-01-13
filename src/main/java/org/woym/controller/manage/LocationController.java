package org.woym.controller.manage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.woym.config.Config;
import org.woym.config.DefaultConfigEnum;
import org.woym.exceptions.DatasetException;
import org.woym.logic.CommandHandler;
import org.woym.logic.SuccessStatus;
import org.woym.logic.command.AddCommand;
import org.woym.logic.command.CommandCreator;
import org.woym.logic.command.MacroCommand;
import org.woym.logic.command.UpdateCommand;
import org.woym.logic.spec.IStatus;
import org.woym.messages.GenericErrorMessage;
import org.woym.messages.MessageHelper;
import org.woym.objects.Entity;
import org.woym.objects.Location;
import org.woym.objects.TravelTimeList;
import org.woym.objects.TravelTimeList.Edge;
import org.woym.objects.spec.IMemento;
import org.woym.persistence.DataAccess;

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

	private static final long serialVersionUID = 5092669270191337498L;

	private DataAccess dataAccess = DataAccess.getInstance();

	private static Logger LOGGER = LogManager
			.getLogger(LocationController.class.getName());

	private CommandHandler commandHandler = CommandHandler.getInstance();
	private CommandCreator commandCreator = CommandCreator.getInstance();

	private IMemento locationMemento;

	private Location location;

	/**
	 * Wert der Checkbox im Löschen bestätigen Dialog.
	 */
	private boolean hideDeletionDialog;
	/**
	 * Interner Wert.
	 */
	private boolean hide;

	/**
	 * Der erste der beiden Standorte im Wegzeiten Dialog.
	 */
	private Location firstLocation;

	/**
	 * Der zweite der beiden Standorte im Wegzeiten Dialog;
	 */
	private Location secondLocation;

	private String travelTimeValue;
	private Edge selectedTravelTimeEdge;

	@PostConstruct
	public void init() {
		hideDeletionDialog = Config
				.getBooleanValue(DefaultConfigEnum.HIDE_LOCATION_DELETION_DIALOG);
		hide = hideDeletionDialog;
		location = new Location();
		TravelTimeList.getInstance();
	}

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
		if (hideDeletionDialog != hide) {
			Config.updateProperty(
					DefaultConfigEnum.HIDE_LOCATION_DELETION_DIALOG
							.getPropKey(), String.valueOf(hideDeletionDialog));
		}
		MacroCommand macroCommand = commandCreator
				.createDeleteCommand(location);
		IStatus status = commandHandler.execute(macroCommand);
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
	 * Fügt der Wegzeiten Liste eine Wegzeit hinzu.
	 */
	public void addTravelTime() {
		IMemento memento = TravelTimeList.getInstance().createMemento();
		if (!TravelTimeList.getInstance().add(firstLocation, secondLocation,
				Integer.parseInt(travelTimeValue))) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Wegzeit bereits vorhanden.",
					"Eine Wegzeit für die beiden gewählten Standorte ist bereits vorhanden.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			IStatus status = commandHandler.execute(new UpdateCommand<Entity>(
					TravelTimeList.getInstance(), memento));
			if (status instanceof SuccessStatus) {
				travelTimeValue = null;
				firstLocation = null;
				secondLocation = null;
			}
			FacesContext.getCurrentInstance().addMessage(null, status.report());
		}
	}

	/**
	 * Entfernt eine Wegzeit aus der Wegzeiten-Liste.
	 */
	public void deleteTravelTime() {
		IMemento memento = TravelTimeList.getInstance().createMemento();
		TravelTimeList.getInstance().remove(selectedTravelTimeEdge);
		IStatus status = commandHandler.execute(new UpdateCommand<Entity>(
				TravelTimeList.getInstance(), memento));
		if (status instanceof SuccessStatus) {
			selectedTravelTimeEdge = null;
		}
		FacesContext.getCurrentInstance().addMessage(null, status.report());
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public boolean isHideDeletionDialog() {
		return hideDeletionDialog;
	}

	public void setHideDeletionDialog(boolean hideDeletionDialog) {
		this.hideDeletionDialog = hideDeletionDialog;
	}

	public Location getFirstLocation() {
		return firstLocation;
	}

	public void setFirstLocation(Location firstLocation) {
		this.firstLocation = firstLocation;
	}

	public Location getSecondLocation() {
		return secondLocation;
	}

	public void setSecondLocation(Location secondLocation) {
		this.secondLocation = secondLocation;
	}

	public String getTravelTimeValue() {
		return travelTimeValue;
	}

	public void setTravelTimeValue(String travelTimeValue) {
		this.travelTimeValue = travelTimeValue;
	}

	public Edge getSelectedTravelTimeEdge() {
		return selectedTravelTimeEdge;
	}

	public void setSelectedTravelTimeEdge(Edge selectedTravelTimeEdge) {
		this.selectedTravelTimeEdge = selectedTravelTimeEdge;
	}

	public List<Edge> getTravelTimeEdges() {
		return TravelTimeList.getInstance().getEdges();
	}

}
