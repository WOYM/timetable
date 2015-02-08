package org.woym.controller.planning;

import static org.testng.AssertJUnit.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.testng.PowerMockTestCase;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.model.ScheduleEvent;
import org.testng.annotations.Test;
import org.woym.common.messages.GenericErrorMessage;
import org.woym.common.objects.Employee;
import org.woym.common.objects.Lesson;
import org.woym.common.objects.LessonType;
import org.woym.common.objects.Meeting;
import org.woym.common.objects.MeetingType;
import org.woym.common.objects.Weekday;
import org.woym.logic.command.CommandCreator;
import org.woym.persistence.DataAccess;

@Test(groups = { "PlanningControllerIT", "integration" }, dependsOnGroups = {
		"DataAccessObjectsIT", "DataAccessObjectsIT2", "CommandsDataAccessIT" })
public class PlanningControllerIT extends PowerMockTestCase {

	private PlanningController planningController;

	private DataAccess dataAccess = DataAccess.getInstance();

	private FacesContext facesContext = ContextMocker.mockFacesContext();

	@Mock
	private ScheduleEntryMoveEvent moveEvent;

	@Mock
	private ScheduleEntryResizeEvent resizeEvent;

	@Mock
	private ScheduleEvent scheduleEvent;

	@Test(priority = 1)
	public void init() throws Exception {
		planningController = new PlanningController();
		ExternalContext externalContext = Mockito.mock(ExternalContext.class);
		Mockito.when(facesContext.getExternalContext()).thenReturn(
				externalContext);
		planningController.init();

		Meeting meeting = dataAccess.getAllMeetings(
				(MeetingType) dataAccess.getOneActivityType("Teamsitzung"))
				.get(0);
		CommandCreator.getInstance().createEmployeeUpdateAddWorkingHours(
				meeting);
	}

	@SuppressWarnings("deprecation")
	@Test(dependsOnMethods = "init")
	public void onEventMoveSuccess() throws Exception {
		Lesson lesson = dataAccess.getAllLessons(
				(LessonType) dataAccess.getOneActivityType("Mathe")).get(0);

		Mockito.when(moveEvent.getScheduleEvent()).thenReturn(scheduleEvent);
		Mockito.when(scheduleEvent.getData()).thenReturn(lesson);
		Mockito.when(moveEvent.getDayDelta()).thenReturn(2);
		Mockito.when(moveEvent.getMinuteDelta()).thenReturn(0);

		planningController.onEventMove(moveEvent);

		lesson = dataAccess.getAllLessons(
				(LessonType) dataAccess.getOneActivityType("Mathe")).get(0);
		assertEquals(Weekday.WEDNESDAY, lesson.getTime().getDay());
		assertEquals(10, lesson.getTime().getStartTime().getHours());
		assertEquals(45, lesson.getTime().getEndTime().getMinutes());
	}

	// Funktioniert nur, wenn Samstag als nicht zu verplanend angegeben
	@Test(dependsOnMethods = "onEventMoveSuccess")
	public void onEventMoveInvalidWeekday() throws Exception {
		Lesson lesson = dataAccess.getAllLessons(
				(LessonType) dataAccess.getOneActivityType("Mathe")).get(0);

		Mockito.when(moveEvent.getScheduleEvent()).thenReturn(scheduleEvent);
		Mockito.when(scheduleEvent.getData()).thenReturn(lesson);
		Mockito.when(moveEvent.getDayDelta()).thenReturn(3);
		Mockito.when(moveEvent.getMinuteDelta()).thenReturn(0);

		planningController.onEventMove(moveEvent);
		List<FacesMessage> messages = facesContext.getMessageList();
		assertEquals(1, messages.size());
		assertEquals(GenericErrorMessage.INVALID_WEEKDAY.getSummary(), messages
				.get(0).getSummary());
	}

	// Funktioniert nur mit Default-Startzeit von 08:00 Uhr
	@Test(dependsOnMethods = "init")
	public void onEventMoveStartTimeOutOfRange() throws Exception {
		Lesson lesson = dataAccess.getAllLessons(
				(LessonType) dataAccess.getOneActivityType("Mathe")).get(0);

		Mockito.when(moveEvent.getScheduleEvent()).thenReturn(scheduleEvent);
		Mockito.when(scheduleEvent.getData()).thenReturn(lesson);
		Mockito.when(moveEvent.getDayDelta()).thenReturn(0);
		Mockito.when(moveEvent.getMinuteDelta()).thenReturn(-180);

		planningController.onEventMove(moveEvent);
		List<FacesMessage> messages = facesContext.getMessageList();
		assertEquals(1, messages.size());
		assertEquals(GenericErrorMessage.TIME_OUTSIDE_LIMIT.getSummary(),
				messages.get(0).getSummary());
	}

	// Funktioniert nur mit Default-Endzeit von 16:00
	@Test(dependsOnMethods = "init")
	public void onEventMoveEndTimeOutOfRange() throws Exception {
		Lesson lesson = dataAccess.getAllLessons(
				(LessonType) dataAccess.getOneActivityType("Mathe")).get(0);

		Mockito.when(moveEvent.getScheduleEvent()).thenReturn(scheduleEvent);
		Mockito.when(scheduleEvent.getData()).thenReturn(lesson);
		Mockito.when(moveEvent.getDayDelta()).thenReturn(0);
		Mockito.when(moveEvent.getMinuteDelta()).thenReturn(320);

		planningController.onEventMove(moveEvent);
		List<FacesMessage> messages = facesContext.getMessageList();
		assertEquals(1, messages.size());
		assertEquals(GenericErrorMessage.TIME_OUTSIDE_LIMIT.getSummary(),
				messages.get(0).getSummary());
	}

	@Test(dependsOnMethods = "onEventMoveSuccess")
	public void onEventMoveTimePeriodValidationFail() throws Exception {
		Lesson lesson = dataAccess.getAllLessons(
				(LessonType) dataAccess.getOneActivityType("Mathe")).get(0);

		Mockito.when(moveEvent.getScheduleEvent()).thenReturn(scheduleEvent);
		Mockito.when(scheduleEvent.getData()).thenReturn(lesson);
		Mockito.when(moveEvent.getDayDelta()).thenReturn(-2);
		Mockito.when(moveEvent.getMinuteDelta()).thenReturn(10);

		planningController.onEventMove(moveEvent);
		List<FacesMessage> messages = facesContext.getMessageList();
		assertEquals(1, messages.size());
		assertEquals("Validierung fehlgeschlagen.", messages.get(0)
				.getSummary());
	}

	@Test(dependsOnMethods = "init")
	public void onEventMoveNotExistingWeekday() throws Exception {
		Lesson lesson = dataAccess.getAllLessons(
				(LessonType) dataAccess.getOneActivityType("Mathe")).get(0);

		Mockito.when(moveEvent.getScheduleEvent()).thenReturn(scheduleEvent);
		Mockito.when(scheduleEvent.getData()).thenReturn(lesson);
		Mockito.when(moveEvent.getDayDelta()).thenReturn(-3);
		Mockito.when(moveEvent.getMinuteDelta()).thenReturn(0);

		planningController.onEventMove(moveEvent);
		List<FacesMessage> messages = facesContext.getMessageList();
		assertEquals(1, messages.size());
		assertEquals(
				GenericErrorMessage.INVALID_WEEKDAY_IN_DISPLAY.getSummary(),
				messages.get(0).getSummary());
	}

	// Funktioniert nur mit zeitlicher Abrechnung von 45 min für Lehrer und 60
	// min für päd. Mitarbeiter
	@SuppressWarnings("deprecation")
	@Test(dependsOnMethods = "init")
	public void onEventResizeSuccess() throws Exception {
		Meeting meeting = dataAccess.getAllMeetings(
				(MeetingType) dataAccess.getOneActivityType("Teamsitzung"))
				.get(0);

		Mockito.when(resizeEvent.getScheduleEvent()).thenReturn(scheduleEvent);
		Mockito.when(scheduleEvent.getData()).thenReturn(meeting);
		Mockito.when(resizeEvent.getDayDelta()).thenReturn(0);
		Mockito.when(resizeEvent.getMinuteDelta()).thenReturn(15);

		planningController.onEventResize(resizeEvent);

		assertEquals(new BigDecimal("1.33").setScale(Employee.SCALE),
				dataAccess.getOneEmployee("MEY").getAllocatedHours());
		assertEquals(new BigDecimal(1).setScale(Employee.SCALE), dataAccess
				.getOneEmployee("MUS").getAllocatedHours());

		meeting.refresh();

		assertEquals(10, meeting.getTime().getStartTime().getHours());
		assertEquals(45, meeting.getTime().getStartTime().getMinutes());
		assertEquals(11, meeting.getTime().getEndTime().getHours());
		assertEquals(45, meeting.getTime().getEndTime().getMinutes());
		assertEquals(60, meeting.getTime().getDuration());
	}
	
	@Test(dependsOnMethods = "init")
	public void onEventResizeTimeOutOfRange() throws Exception{
		Meeting meeting = dataAccess.getAllMeetings(
				(MeetingType) dataAccess.getOneActivityType("Teamsitzung"))
				.get(0);

		Mockito.when(resizeEvent.getScheduleEvent()).thenReturn(scheduleEvent);
		Mockito.when(scheduleEvent.getData()).thenReturn(meeting);
		Mockito.when(resizeEvent.getDayDelta()).thenReturn(0);
		Mockito.when(resizeEvent.getMinuteDelta()).thenReturn(300);
		
		planningController.onEventResize(resizeEvent);
		
		assertEquals(GenericErrorMessage.TIME_OUTSIDE_LIMIT.getSummary(), facesContext.getMessageList().get(0).getSummary());
	}
}
