package org.woym.logic.command;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.woym.exceptions.DatasetException;
import org.woym.logic.CommandCreator;
import org.woym.objects.AcademicYear;
import org.woym.objects.Activity;
import org.woym.objects.Classteam;
import org.woym.objects.Location;
import org.woym.objects.Location.Memento;
import org.woym.objects.Room;
import org.woym.objects.Schoolclass;
import org.woym.objects.TravelTimeList;
import org.woym.persistence.DataAccess;
import org.woym.spec.objects.IActivityObject;
import org.woym.spec.objects.IMemento;

/**
 * @author JurSch
 *
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({ DataAccess.class, TravelTimeList.class })
public class TestCommandCreator {

	@Mock
	private DataAccess dataAccess;

	@Mock
	private Activity activity;

	@Mock
	private Activity activity1;

	@Mock
	private org.woym.objects.Activity.Memento mementoActivity;

	@Mock
	private Activity activity2;
	
	@Mock
	private Location location;
	
	@Mock
	private Classteam team;
	
	@Mock
	private Room room;

	@Mock
	private AcademicYear year;

	@Mock
	private Schoolclass schoolclass;

	@InjectMocks
	private CommandCreator creator;

	List<Activity> activities;

	@Before
	public void init() throws DatasetException {
		PowerMockito.suppress(PowerMockito.constructor(DataAccess.class));
		PowerMockito.mockStatic(DataAccess.class);
		PowerMockito.when(DataAccess.getInstance()).thenReturn(dataAccess);

		Mockito.when(activity1.remove(Mockito.any(IActivityObject.class)))
				.thenReturn(1);
		Mockito.when(activity2.remove(Mockito.any(IActivityObject.class)))
				.thenReturn(0);
		Mockito.when(activity1.createMemento()).thenReturn(mementoActivity);

		activities = new ArrayList<>(Arrays.asList(activity1, activity2));
		
		Mockito.when(
				DataAccess.getInstance().getAllActivities(
						Mockito.any(IActivityObject.class))).thenReturn(activities);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullEntity() {
		CommandCreator.getInstance().createDeleteCommand(null);
	}

	@Test
	public void testActvity() {
		MacroCommand macro = CommandCreator.getInstance().createDeleteCommand(
				activity);

		assertEquals(1, macro.getCommands().size());
		assertTrue(macro.getCommands().get(0) instanceof DeleteCommand);
	}

	@Test
	public void testAcademicYearWhithoutClasses() {
		MacroCommand macro = CommandCreator.getInstance().createDeleteCommand(
				year);

		assertEquals(1, macro.getCommands().size());
		assertTrue(macro.getCommands().get(0) instanceof DeleteCommand);
	}

	@Test
	public void testSchoolclass() throws Exception {

		IMemento memento = Mockito
				.mock(org.woym.objects.Classteam.Memento.class);

		Mockito.when(DataAccess.getInstance().getOneClassteam(schoolclass))
				.thenReturn(team);
		Mockito.when(team.remove(schoolclass)).thenReturn(true);
		Mockito.when(team.createMemento()).thenReturn(memento);
		
		Mockito.when(DataAccess.getInstance().getOneAcademicYear(schoolclass)).thenReturn(year);
		IMemento yearMemento = Mockito
				.mock(org.woym.objects.AcademicYear.Memento.class);
		Mockito.when(year.createMemento()).thenReturn((org.woym.objects.AcademicYear.Memento) yearMemento);

		MacroCommand macro = CommandCreator.getInstance().createDeleteCommand(
				schoolclass);

		assertEquals(5, macro.getCommands().size());

		assertTrue(macro.getCommands().get(0) instanceof UpdateCommand);
		assertTrue(macro.getCommands().get(1) instanceof DeleteCommand);

		assertTrue(macro.getCommands().get(2) instanceof UpdateCommand);
		assertTrue(macro.getCommands().get(3) instanceof UpdateCommand);
		assertTrue(macro.getCommands().get(4) instanceof DeleteCommand);

	}
	
	@Test
	public void testRoom() throws Exception {
		IMemento mementoLocation = Mockito.mock(org.woym.objects.Location.Memento.class);
		IMemento menentoSchoolclass = Mockito.mock(org.woym.objects.Schoolclass.Memento.class);
		
		Mockito.when(DataAccess.getInstance().getOneLocation(room)).thenReturn(location);
		Mockito.when(location.createMemento()).thenReturn((Memento) mementoLocation);
		
		Mockito.when(DataAccess.getInstance()
					.getOneSchoolclass(room)).thenReturn(schoolclass);
		Mockito.when(schoolclass.createMemento()).thenReturn((org.woym.objects.Schoolclass.Memento) menentoSchoolclass);
		
		MacroCommand macro = CommandCreator.getInstance().createDeleteCommand(
				room);

		assertEquals(5, macro.getCommands().size());

		assertTrue(macro.getCommands().get(0) instanceof UpdateCommand);
		assertTrue(macro.getCommands().get(1) instanceof DeleteCommand);

		assertTrue(macro.getCommands().get(2) instanceof UpdateCommand);
		assertTrue(macro.getCommands().get(3) instanceof UpdateCommand);
		assertTrue(macro.getCommands().get(4) instanceof DeleteCommand);
	}

}
