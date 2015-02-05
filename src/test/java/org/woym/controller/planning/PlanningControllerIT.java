package org.woym.controller.planning;

import static org.testng.AssertJUnit.assertEquals;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.testng.PowerMockTestCase;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.model.ScheduleEvent;
import org.testng.annotations.Test;
import org.woym.common.messages.GenericErrorMessage;
import org.woym.common.objects.Lesson;
import org.woym.common.objects.LessonType;
import org.woym.common.objects.Weekday;
import org.woym.persistence.DataAccess;

@Test(groups = { "PlanningControllerIT", "integration" }, dependsOnGroups = {
		"DataAccessObjectsIT", "DataAccessObjectsIT2", "CommandsDataAccessIT" })
public class PlanningControllerIT extends PowerMockTestCase {

	private PlanningController planningController;

	private DataAccess dataAccess = DataAccess.getInstance();

	private FacesContext facesContext = ContextMocker.mockFacesContext();

	@Mock
	private ScheduleEntryMoveEvent event;

	@Mock
	private ScheduleEvent scheduleEvent;

	@SuppressWarnings("deprecation")
	@Test(priority = 1)
	public void onEventMoveSuccess() throws Exception {
		init();

		Lesson lesson = dataAccess.getAllLessons(
				(LessonType) dataAccess.getOneActivityType("Mathe")).get(0);

		Mockito.when(event.getScheduleEvent()).thenReturn(scheduleEvent);
		Mockito.when(scheduleEvent.getData()).thenReturn(lesson);
		Mockito.when(event.getDayDelta()).thenReturn(2);
		Mockito.when(event.getMinuteDelta()).thenReturn(0);

		planningController.onEventMove(event);

		lesson = dataAccess.getAllLessons(
				(LessonType) dataAccess.getOneActivityType("Mathe")).get(0);
		assertEquals(Weekday.WEDNESDAY, lesson.getTime().getDay());
		assertEquals(10, lesson.getTime().getStartTime().getHours());
		assertEquals(45, lesson.getTime().getEndTime().getMinutes());

	}

	@Test(dependsOnMethods = "onEventMoveSuccess")
	public void onEventMoveInvalidWeekday() throws Exception {
		Lesson lesson = dataAccess.getAllLessons(
				(LessonType) dataAccess.getOneActivityType("Mathe")).get(0);

		Mockito.when(event.getScheduleEvent()).thenReturn(scheduleEvent);
		Mockito.when(scheduleEvent.getData()).thenReturn(lesson);
		Mockito.when(event.getDayDelta()).thenReturn(3);
		Mockito.when(event.getMinuteDelta()).thenReturn(0);

		planningController.onEventMove(event);
		List<FacesMessage> messages = facesContext.getMessageList();
		assertEquals(1, messages.size());
		assertEquals(GenericErrorMessage.INVALID_WEEKDAY.getSummary(), messages
				.get(0).getSummary());
	}

	@Test(dependsOnMethods = "onEventMoveSuccess")
	public void onEventMoveStartTimeOutOfRange() throws Exception {
		Lesson lesson = dataAccess.getAllLessons(
				(LessonType) dataAccess.getOneActivityType("Mathe")).get(0);

		Mockito.when(event.getScheduleEvent()).thenReturn(scheduleEvent);
		Mockito.when(scheduleEvent.getData()).thenReturn(lesson);
		Mockito.when(event.getDayDelta()).thenReturn(0);
		Mockito.when(event.getMinuteDelta()).thenReturn(-180);

		planningController.onEventMove(event);
		List<FacesMessage> messages = facesContext.getMessageList();
		assertEquals(1, messages.size());
		assertEquals(GenericErrorMessage.TIME_OUTSIDE_LIMIT.getSummary(),
				messages.get(0).getSummary());
	}

	@Test(dependsOnMethods = "onEventMoveSuccess")
	public void onEventMoveEndTimeOutOfRange() throws Exception {
		Lesson lesson = dataAccess.getAllLessons(
				(LessonType) dataAccess.getOneActivityType("Mathe")).get(0);

		Mockito.when(event.getScheduleEvent()).thenReturn(scheduleEvent);
		Mockito.when(scheduleEvent.getData()).thenReturn(lesson);
		Mockito.when(event.getDayDelta()).thenReturn(0);
		Mockito.when(event.getMinuteDelta()).thenReturn(320);

		planningController.onEventMove(event);
		List<FacesMessage> messages = facesContext.getMessageList();
		assertEquals(1, messages.size());
		assertEquals(GenericErrorMessage.TIME_OUTSIDE_LIMIT.getSummary(),
				messages.get(0).getSummary());
	}

	@Test(dependsOnMethods = "onEventMoveSuccess")
	public void onEventMoveTimePeriodValidationFail() throws Exception {
		Lesson lesson = dataAccess.getAllLessons(
				(LessonType) dataAccess.getOneActivityType("Mathe")).get(0);

		Mockito.when(event.getScheduleEvent()).thenReturn(scheduleEvent);
		Mockito.when(scheduleEvent.getData()).thenReturn(lesson);
		Mockito.when(event.getDayDelta()).thenReturn(-2);
		Mockito.when(event.getMinuteDelta()).thenReturn(10);

		planningController.onEventMove(event);
		List<FacesMessage> messages = facesContext.getMessageList();
		assertEquals(1, messages.size());
		assertEquals("Validierung fehlgeschlagen.", messages.get(0)
				.getSummary());
	}

	@Test(dependsOnMethods = "onEventMoveSuccess")
	public void onEventMoveNotExistingWeekday() throws Exception {
		Lesson lesson = dataAccess.getAllLessons(
				(LessonType) dataAccess.getOneActivityType("Mathe")).get(0);

		Mockito.when(event.getScheduleEvent()).thenReturn(scheduleEvent);
		Mockito.when(scheduleEvent.getData()).thenReturn(lesson);
		Mockito.when(event.getDayDelta()).thenReturn(-3);
		Mockito.when(event.getMinuteDelta()).thenReturn(0);

		planningController.onEventMove(event);
		List<FacesMessage> messages = facesContext.getMessageList();
		assertEquals(1, messages.size());
		assertEquals(
				GenericErrorMessage.INVALID_WEEKDAY_IN_DISPLAY.getSummary(),
				messages.get(0).getSummary());
	}

	private void init() {
		planningController = new PlanningController();
		ExternalContext externalContext = Mockito.mock(ExternalContext.class);
		Mockito.when(facesContext.getExternalContext()).thenReturn(
				externalContext);
		planningController.init();
	}
}
