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
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import org.woym.exceptions.DatasetException;
import org.woym.logic.CommandHandler;
import org.woym.logic.SuccessStatus;
import org.woym.logic.command.AddCommand;
import org.woym.logic.command.UpdateCommand;
import org.woym.messages.GenericErrorMessage;
import org.woym.messages.MessageHelper;
import org.woym.messages.SuccessMessage;
import org.woym.objects.ActivityType;
import org.woym.objects.PedagogicAssistant;
import org.woym.persistence.DataAccess;
import org.woym.spec.logic.IStatus;
import org.woym.spec.objects.IMemento;

/**
 * <h1>PedagogicAssistantController</h1>
 * <p>
 * Dieser Controller ist für die allgemeine Mitarbeiterverwaltung zuständig.
 * 
 * @author Tim Hansen (tihansen)
 *
 */
@ViewScoped
@ManagedBean(name = "pedagogicAssistantController")
public class PedagogicAssistantController implements Serializable {

	private static final long serialVersionUID = 1L;

	private static Logger LOGGER = LogManager
			.getLogger(PedagogicAssistantController.class);

	private DataAccess dataAccess = DataAccess.getInstance();
	
	private CommandHandler commandHandler = CommandHandler.getInstance();

	private PedagogicAssistant pedagogicAssistant;
	private IMemento pedagogicAssistantMemento;

	private DualListModel<ActivityType> activityTypes;
	
	@PostConstruct
	public void init() {
		pedagogicAssistant = new PedagogicAssistant();
	}

	/**
	 * Diese Methode liefert ein Modell zweier Listen zurück. In diesen Listen
	 * befinden sich die verfügbaren und die gewählten Unterrichtsinhalte für
	 * einen Mitarbeiter
	 * 
	 * @return Liste mit Unterrichtstypen
	 */
	public DualListModel<ActivityType> getActivityTypes() {
		List<ActivityType> allActivityTypes;
		List<ActivityType> possibleActivityTypes;

		// Logic to display correct lists
		try {
			allActivityTypes = new ArrayList<>();
			possibleActivityTypes = pedagogicAssistant.getPossibleActivityTypes();

			for (ActivityType activityType : dataAccess.getAllActivityTypes()) {
				if (!possibleActivityTypes.contains(activityType)) {
					allActivityTypes.add(activityType);
				}
			}

			activityTypes = new DualListModel<ActivityType>(allActivityTypes,
					possibleActivityTypes);
		} catch (DatasetException e) {
			LOGGER.error(e);
			FacesMessage msg = new FacesMessage(
					GenericErrorMessage.DATABASE_COMMUNICATION_ERROR
							.getSummary(),
					GenericErrorMessage.DATABASE_COMMUNICATION_ERROR
							.getStatusMessage());
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
		}

		return activityTypes;
	}

	public void setActivityTypes(DualListModel<ActivityType> activityTypes) {
		this.activityTypes = activityTypes;
	}

	/**
	 * Event wird beim Transfer im Dialog gefeuert. Enthält Aktivitätstypen.
	 * 
	 * @param event
	 *            Das Event
	 */
	public void onTransfer(TransferEvent event) {
		pedagogicAssistant.setPossibleActivityTypes(activityTypes.getTarget());
	}

	/**
	 * Liefert eine Liste mit allen Lehrkräften zurück.
	 * 
	 * @return Liste mit allen Lehrkräften
	 */
	public List<PedagogicAssistant> getPedagogicAssistants() {
		try {
			return dataAccess.getAllPAs();
		} catch (DatasetException e) {
			LOGGER.error(e);
			FacesMessage msg = new FacesMessage(
					GenericErrorMessage.DATABASE_COMMUNICATION_ERROR.getSummary(),
					GenericErrorMessage.DATABASE_COMMUNICATION_ERROR
							.getStatusMessage());
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return new ArrayList<PedagogicAssistant>();
		}
	}

	/**
	 * Öffnet einen neuen Dialog, mit dem sich ein Lehrer hinzufügen lässt.
	 */
	public void addPedagogicAssistantDialog() {
		pedagogicAssistant = new PedagogicAssistant();
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('wAddPedagogicAssistantDialog').show();");
	}
	
	public void generatePedagogicAssistantMemento() {
		pedagogicAssistantMemento = pedagogicAssistant.createMemento();
	}

	/**
	 * Speichert einen aktualisierten Mitarbeiter.
	 */
	public void editPedagogicAssistant() {
		UpdateCommand<PedagogicAssistant> command = new UpdateCommand<>(pedagogicAssistant, pedagogicAssistantMemento);			
		IStatus status = commandHandler.execute(command);
		FacesMessage msg = status.report();
		
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	/**
	 * Löscht den selektierten Mitarbeiter.
	 */
	public void deletePedagogicAssistant() {
		if (pedagogicAssistant != null) {
			try {
				dataAccess.delete(pedagogicAssistant);
				FacesMessage msg = MessageHelper.generateMessage(SuccessMessage.DELETE_OBJECT_SUCCESS, pedagogicAssistant, FacesMessage.SEVERITY_INFO);
				FacesContext.getCurrentInstance().addMessage(null, msg);
			} catch (DatasetException e) {
				LOGGER.error(e);
				FacesMessage msg = new FacesMessage(
						GenericErrorMessage.DATABASE_COMMUNICATION_ERROR.getSummary(),
						GenericErrorMessage.DATABASE_COMMUNICATION_ERROR
								.getStatusMessage());
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		}
	}

	/**
	 * Fügt einen Mitarbeiter der Persistenz hinzu. Der momentane Mitarbeiter im
	 * Zwischenspeicher wird mit einem neuen Objekt ersetzt.
	 */
	public void addPedagogicAssistant() {
		AddCommand<PedagogicAssistant> command = new AddCommand<>(pedagogicAssistant);			
		IStatus status = commandHandler.execute(command);
		FacesMessage msg = status.report();
		
		if(status instanceof SuccessStatus) {
			pedagogicAssistant = new PedagogicAssistant();
		}
		
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public PedagogicAssistant getPedagogicAssistant() {
		return pedagogicAssistant;
	}

	public void setPedagogicAssistant(PedagogicAssistant pedagogicAssistant) {
		this.pedagogicAssistant = pedagogicAssistant;
	}
	
	
}
