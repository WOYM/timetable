package org.woym.persistence;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.assertTrue;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.woym.common.exceptions.DatasetException;
import org.woym.common.objects.AcademicYear;
import org.woym.common.objects.ActivityType;
import org.woym.common.objects.ChargeableCompensation;
import org.woym.common.objects.Classteam;
import org.woym.common.objects.ColorEnum;
import org.woym.common.objects.Employee;
import org.woym.common.objects.LessonType;
import org.woym.common.objects.Location;
import org.woym.common.objects.MeetingType;
import org.woym.common.objects.PedagogicAssistant;
import org.woym.common.objects.Room;
import org.woym.common.objects.Schoolclass;
import org.woym.common.objects.Teacher;
import org.woym.common.objects.TravelTimeList;
import org.woym.persistence.DataAccess;
import org.woym.persistence.DataBase;

@Test(groups = { "DataAccessObjectsIT", "integration" })
public class DataAccessObjectsIT {

	private DataAccess dataAccess = DataAccess.getInstance();

	@BeforeClass
	public void init() throws Exception {
		DataBase.getInstance().setUp();
		String path = Paths
				.get(ClassLoader.getSystemResource("backup.zip").toURI())
				.toAbsolutePath().toString();
		DataBase.getInstance().restore(path);
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	// Integration DataAccess und Location
	// ////////////////////////////////////////////////////////////////////////////////////

	@Test(priority = 1, groups = "DataAccessLocation")
	public void getAllLocationsOrderedSuccess() throws DatasetException {
		Location l = new Location();
		l.setName("Hauptstandort");
		Location l1 = new Location();
		l1.setName("Zweigstelle");
		dataAccess.persist(l1);
		dataAccess.persist(l);
		List<Location> locations = dataAccess.getAllLocations();
		assertEquals(2, locations.size());
		assertEquals(l, locations.get(0));
		assertEquals(l1, locations.get(1));
	}

	@Test(priority = 1, dependsOnMethods = "getAllLocationsOrderedSuccess")
	public void getByIdSuccess() throws DatasetException {
		List<Location> list = dataAccess.getAllLocations();
		Location l = dataAccess.getById(Location.class, list.get(0).getId());
		assertNotNull(l);
		assertEquals(list.get(0), l);
	}

	@Test(priority = 2, groups = "DataAccessLocation")
	public void getOneLocationNameSuccess() throws DatasetException {
		Location l = dataAccess.getOneLocation("Hauptstandort");
		assertNotNull(l);
		assertEquals("Hauptstandort", l.getName());
	}

	@Test(priority = 2, groups = "DataAccessLocation")
	public void getOneLocationNameLocationNotExists() throws DatasetException {
		assertNull(dataAccess.getOneLocation("Standort"));
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	// Integration Room
	// ////////////////////////////////////////////////////////////////////////////////////

	@Test(priority = 1, groups = "DataAccessRoom", dependsOnGroups = "DataAccessLocation")
	public void getOneLocationRoomSuccess() throws DatasetException {
		List<Location> locations = dataAccess.getAllLocations();

		// Räume erzeugen
		Room r = new Room();
		r.setName("Raum 100");
		r.setPurpose("Standardraum");
		Room r1 = new Room();
		r1.setName("Raum 101");
		r1.setPurpose("Standardraum");
		Room r2 = new Room();
		r2.setName("Raum 102");
		r2.setPurpose("Musikzimmer");

		// Den Standorten Räume hinzufügen
		locations.get(0).add(r);
		locations.get(0).add(r2);
		locations.get(1).add(r1);

		// Die Standorte aktualisieren. Dies persistiert auch die Räume, setzt
		// die IDs aber nicht direkt an den oben erstellen Raum-Objekten
		dataAccess.update(locations.get(0));
		dataAccess.update(locations.get(1));

		locations = dataAccess.getAllLocations();
		// Damit wir Räume mit IDs haben, werden die Standorte aus der DB geholt
		// und darauf getRooms aufgerufen
		List<Room> rooms = locations.get(0).getRooms();
		assertEquals(2, rooms.size());

		Location l = dataAccess.getOneLocation(rooms.get(0));
		assertNotNull(l);
		assertEquals("Hauptstandort", l.getName());

		rooms = locations.get(1).getRooms();

		Location l1 = dataAccess.getOneLocation(rooms.get(0));
		assertNotNull(l1);
		assertEquals("Zweigstelle", l1.getName());
	}

	@Test(priority = 2, groups = "DataAccessRoom", dependsOnGroups = "DataAccessLocation")
	public void getOneLocationRoomNull() throws DatasetException {
		Room r = new Room();
		r.setName("Raum 103");
		Location l = dataAccess.getOneLocation(r);
		assertNull(l);
	}

	@Test(priority = 2, groups = "DataAccessRoom", dependsOnGroups = "DataAccessLocation")
	public void getOneRoomSuccess() throws DatasetException {
		Room r = dataAccess.getOneRoom("Hauptstandort", "Raum 100");
		assertNotNull(r);
		assertTrue(dataAccess.getAllLocations().get(0).contains(r));

		r = dataAccess.getOneRoom("Zweigstelle", "Raum 101");
		assertNotNull(r);
		assertTrue(dataAccess.getAllLocations().get(1).contains(r));
	}

	@Test(priority = 2, groups = "DataAccessRoom", dependsOnGroups = "DataAccessLocation")
	public void getOneRoomNull() throws DatasetException {
		Room r = dataAccess.getOneRoom("Hauptstandort", "Raum 103");
		assertNull(r);
	}

	@Test(priority = 2, groups = "DataAccessRoom", dependsOnGroups = "DataAccessLocation")
	public void getAllRoomsByPurposeSuccess() throws DatasetException {
		List<Room> rooms = dataAccess.getAllRooms("StandardRaum");

		assertEquals(2, rooms.size());
		assertFalse(rooms.get(0).equals(rooms.get(1)));
		assertTrue(rooms.get(0).getName().equals("Raum 100")
				|| rooms.get(0).getName().equals("Raum 101"));
		assertTrue(rooms.get(1).getName().equals("Raum 100")
				|| rooms.get(1).getName().equals("Raum 101"));
	}

	@Test(priority = 2, groups = "DataAccessRoom", dependsOnGroups = "DataAccessLocation")
	public void getRoomPurposesSuccess() throws DatasetException {
		List<String> purposes = dataAccess.getRoomPurposes();
		assertEquals(2, purposes.size());
		assertTrue(purposes.contains("Standardraum"));
		assertTrue(purposes.contains("Musikzimmer"));
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	// Integration ActivityType
	// ////////////////////////////////////////////////////////////////////////////////////

	@Test(priority = 1, groups = "DataAccessActivityType", dependsOnGroups = "DataAccessRoom")
	public void getAllActivityTypesSuccess() throws DatasetException {
		LessonType l = new LessonType();
		l.setName("Mathe");
		l.setColor(ColorEnum.GREEN_LIGHT);
		l.setRooms(dataAccess.getAllRooms("Standardraum"));
		dataAccess.persist(l);

		LessonType l1 = new LessonType();
		l1.setName("Musik");
		l1.setRooms(dataAccess.getAllRooms("Musikzimmer"));
		l1.setColor(ColorEnum.RED_DARK);
		dataAccess.persist(l1);

		MeetingType m = new MeetingType();
		m.setName("Teamsitzung");
		m.setColor(ColorEnum.YELLOW);
		dataAccess.persist(m);

		List<ActivityType> result = dataAccess.getAllActivityTypes();
		assertEquals(3, result.size());
		assertTrue(result.contains(l));
		assertTrue(result.contains(l1));
		assertTrue(result.contains(m));
	}

	@Test(priority = 2, groups = "DataAccessActivityType", dependsOnGroups = "DataAccessRoom")
	public void getOneActivityTypeSuccess() throws DatasetException {
		ActivityType a = dataAccess.getOneActivityType("Mathe");
		assertNotNull(a);
		assertEquals("Mathe", a.getName());
		assertEquals(dataAccess.getAllRooms("Standardraum"), a.getRooms());

		a = dataAccess.getOneActivityType("Musik");
		assertNotNull(a);
		assertEquals("Musik", a.getName());
		assertEquals(dataAccess.getAllRooms("Musikzimmer"), a.getRooms());
	}

	@Test(priority = 2, groups = "DataAccessActivityType", dependsOnGroups = "DataAccessRoom")
	public void getOneActivityTypeNull() throws DatasetException {
		ActivityType a = dataAccess.getOneActivityType("Englisch");
		assertNull(a);
	}

	@Test(priority = 3, groups = "DataAccessActivityType", dependsOnGroups = "DataAccessRoom")
	public void getAllActivityTypesRoomSuccess() throws DatasetException {
		Room r = dataAccess.getOneRoom("Hauptstandort", "Raum 102");
		List<ActivityType> result = dataAccess.getAllActivityTypes(r);
		assertEquals(1, result.size());
		assertEquals(dataAccess.getOneActivityType("Musik"), result.get(0));

		r = dataAccess.getOneRoom("Zweigstelle", "Raum 101");
		result = dataAccess.getAllActivityTypes(r);
		assertEquals(1, result.size());
		assertEquals(dataAccess.getOneActivityType("Mathe"), result.get(0));
	}

	@Test(priority = 3, groups = "DataAccessActivityType", dependsOnGroups = "DataAccessRoom")
	public void getAllMeetingTypesSuccess() throws DatasetException {
		List<MeetingType> result = dataAccess.getAllMeetingTypes();
		assertEquals(1, result.size());
		assertEquals(dataAccess.getOneActivityType("Teamsitzung"),
				result.get(0));
	}

	@Test(priority = 3, groups = "DataAccessActivityType", dependsOnGroups = "DataAccessRoom")
	public void getAllLessonTypesSuccess() throws DatasetException {
		List<LessonType> result = dataAccess.getAllLessonTypes();
		assertEquals(2, result.size());
		assertTrue(result.contains(dataAccess.getOneActivityType("Musik")));
		assertTrue(result.contains(dataAccess.getOneActivityType("Mathe")));
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	// Integration Employee
	// ////////////////////////////////////////////////////////////////////////////////////

	@Test(priority = 1, groups = "DataAccessEmployee", dependsOnGroups = "DataAccessActivityType")
	public void getAllEmployeesForActivityTypeSuccess() throws DatasetException {
		// Lehrerin 1
		Teacher t = new Teacher();
		t.setName("Julia Meyer");
		t.setSymbol("MEY");
		t.setHoursPerWeek(new BigDecimal(20));
		ChargeableCompensation c = new ChargeableCompensation();
		c.setValue(1);
		c.setDescription("Pendel");
		t.add(c);
		t.add(dataAccess.getOneActivityType("Mathe"));
		t.add(dataAccess.getOneActivityType("Musik"));
		dataAccess.persist(t);

		// Päd. Mitarbeiter 1
		PedagogicAssistant p = new PedagogicAssistant();
		p.setName("Max Mustermann");
		p.setSymbol("MUS");
		p.setHoursPerWeek(new BigDecimal(30));
		p.add(dataAccess.getOneActivityType("Mathe"));
		dataAccess.persist(p);

		// Eigentlicher Test
		List<Employee> list = dataAccess.getAllEmployees(dataAccess
				.getOneActivityType("Mathe"));
		assertEquals(2, list.size());
		assertTrue(list.contains(t));
		assertTrue(list.contains(p));

		list = dataAccess.getAllEmployees(dataAccess
				.getOneActivityType("Musik"));
		assertEquals(1, list.size());
		assertTrue(list.contains(t));
	}

	@Test(priority = 2, groups = "DataAccessEmployee", dependsOnGroups = "DataAccessActivityType")
	public void getAllPAsSuccess() throws DatasetException {
		List<PedagogicAssistant> list = dataAccess.getAllPAs();
		assertEquals(1, list.size());
		PedagogicAssistant p = list.get(0);
		assertEquals("Max Mustermann", p.getName());
		assertEquals("MUS", p.getSymbol());
		assertEquals(new BigDecimal(30).setScale(Employee.SCALE),
				p.getHoursPerWeek());
		assertEquals(1, p.getPossibleActivityTypes().size());
		assertEquals(dataAccess.getOneActivityType("Mathe"), p
				.getPossibleActivityTypes().get(0));
	}

	@Test(priority = 2, groups = "DataAccessEmployee", dependsOnGroups = "DataAccessActivityType")
	public void getAllTeacherSuccess() throws DatasetException {
		List<Teacher> list = dataAccess.getAllTeachers();
		assertEquals(1, list.size());
		Teacher t = list.get(0);
		assertEquals("Julia Meyer", t.getName());
		assertEquals("MEY", t.getSymbol());
		assertEquals(new BigDecimal(20).setScale(Employee.SCALE),
				t.getHoursPerWeek());
		assertEquals(2, t.getPossibleActivityTypes().size());
		assertTrue(t.getPossibleActivityTypes().contains(
				dataAccess.getOneActivityType("Mathe")));
		assertTrue(t.getPossibleActivityTypes().contains(
				dataAccess.getOneActivityType("Musik")));
	}

	@Test(priority = 3, groups = "DataAccessEmployee", dependsOnGroups = "DataAccessActivityType")
	public void getOneEmployeeSuccess() throws DatasetException {
		// Test für Lehrer. Kürzel genau identisch geschrieben
		Employee e = dataAccess.getOneEmployee("MEY");
		assertEquals(dataAccess.getAllTeachers().get(0), e);

		// Test für päd. Mitarbeiter. Groß-/Kleinschreibung abweichend
		e = dataAccess.getOneEmployee("Mus");
		assertEquals(dataAccess.getAllPAs().get(0), e);
	}

	@Test(priority = 3, groups = "DataAccessEmployee", dependsOnGroups = "DataAccessActivityType")
	public void getOneEmployeeNull() throws DatasetException {
		Employee e = dataAccess.getOneEmployee("MUE");
		assertNull(e);
	}

	@Test(priority = 3, groups = "DataAccessEmployee", dependsOnGroups = "DataAccessActivityType")
	public void searchEmployeesSuccess() throws DatasetException {
		List<Employee> list = dataAccess.searchEmployees("m");
		assertEquals(2, list.size());
	}

	@Test(priority = 3, groups = "DataAccessEmployee", dependsOnGroups = "DataAccessActivityType")
	public void searchEmployeesNothingFound() throws DatasetException {
		List<Employee> list = dataAccess.searchEmployees("z");
		assertTrue(list.isEmpty());
	}

	@Test(priority = 3, groups = "DataAccessEmployee", dependsOnGroups = "DataAccessActivityType")
	public void searchTeachersSuccess() throws DatasetException {
		List<Teacher> list = dataAccess.searchTeachers("m");
		assertEquals(1, list.size());
		assertEquals(dataAccess.getOneEmployee("MEY"), list.get(0));
	}

	@Test(priority = 3, groups = "DataAccessEmployee", dependsOnGroups = "DataAccessActivityType")
	public void searchPASuccess() throws DatasetException {
		List<PedagogicAssistant> list = dataAccess.searchPAs("m");
		assertEquals(1, list.size());
		assertEquals(dataAccess.getOneEmployee("MUS"), list.get(0));
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	// Integration AcademicYear
	// ////////////////////////////////////////////////////////////////////////////////////

	@Test(priority = 1, groups = "DataAccessAcademicYear", dependsOnGroups = "DataAccessActivityType")
	public void getAllAcademicYearsSuccess() throws DatasetException {
		AcademicYear a = new AcademicYear();
		a.setAcademicYear(1);
		a.add((LessonType) dataAccess.getOneActivityType("Mathe"), 4);
		a.add((LessonType) dataAccess.getOneActivityType("Musik"), 1);
		dataAccess.persist(a);

		AcademicYear a1 = new AcademicYear();
		a1.setAcademicYear(2);
		dataAccess.persist(a1);

		List<AcademicYear> list = dataAccess.getAllAcademicYears();
		assertEquals(2, list.size());
		assertEquals(a, list.get(0));
		assertEquals(a1, list.get(1));
		assertEquals(2, list.get(0).getLessonDemands().size());
		assertTrue(list.get(0).contains(
				(LessonType) dataAccess.getOneActivityType("Mathe")));
		assertTrue(list.get(0).contains(
				(LessonType) dataAccess.getOneActivityType("Musik")));

		assertTrue(list.get(1).getLessonDemands().isEmpty());
	}

	@Test(priority = 2, groups = "DataAccessAcademicYear", dependsOnGroups = "DataAccessActivityType")
	public void getOneAcademicYearSuccess() throws DatasetException {
		AcademicYear a = dataAccess.getOneAcademicYear(1);

		assertEquals(1, a.getAcademicYear());
		assertEquals(2, a.getLessonDemands().size());
		assertTrue(a.contains((LessonType) dataAccess
				.getOneActivityType("Mathe")));
		assertTrue(a.contains((LessonType) dataAccess
				.getOneActivityType("Musik")));

		a = dataAccess.getOneAcademicYear(2);
		assertEquals(2, a.getAcademicYear());
		assertTrue(a.getLessonDemands().isEmpty());
	}

	@Test(priority = 2, groups = "DataAccessAcademicYear", dependsOnGroups = "DataAccessActivityType")
	public void getOneAcademicYearNull() throws DatasetException {
		AcademicYear a = dataAccess.getOneAcademicYear(3);
		assertNull(a);
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	// Integration Schoolclass
	// ////////////////////////////////////////////////////////////////////////////////////

	@Test(priority = 1, groups = "DataAccessSchoolclass", dependsOnGroups = "DataAccessAcademicYear")
	public void getOneAcademicYearSchoolclassSuccess() throws DatasetException {
		List<AcademicYear> years = dataAccess.getAllAcademicYears();

		// Schulklasse erstellen
		Schoolclass s = new Schoolclass();
		s.setIdentifier('a');
		s.setRoom(dataAccess.getOneRoom("Hauptstandort", "Raum 100"));
		s.setTeacher((Teacher) dataAccess.getOneEmployee("MEY"));
		s.setLessonDemands(years.get(0).getLessonDemands());

		// Dem Jahrgang hinzufügen und updaten
		years.get(0).add(s);
		dataAccess.update(years.get(0));

		// Zweite Schulklasse erstellen
		Schoolclass s1 = new Schoolclass();
		s1.setIdentifier('a');
		s1.setRoom(dataAccess.getOneRoom("Zweigstelle", "Raum 101"));
		s1.setTeacher((Teacher) dataAccess.getOneEmployee("MEY"));

		// Dem anderen Jahrgang hinzufügen
		years.get(1).add(s1);
		dataAccess.update(years.get(1));

		// Der eigentliche Test
		s = dataAccess.getOneAcademicYear(1).getSchoolclasses().get(0);
		AcademicYear year = dataAccess.getOneAcademicYear(s);
		assertEquals(years.get(0), year);

		s = dataAccess.getOneAcademicYear(2).getSchoolclasses().get(0);
		year = dataAccess.getOneAcademicYear(s);
		assertEquals(years.get(1), year);
	}

	@Test(priority = 1, groups = "DataAccessSchoolclass", dependsOnGroups = "DataAccessAcademicYear")
	public void getOneAcademicYearSchoolclassNull() throws DatasetException {
		Schoolclass s = new Schoolclass();
		s.setIdentifier('b');
		AcademicYear a = dataAccess.getOneAcademicYear(s);
		assertNull(a);
	}

	@Test(priority = 2, groups = "DataAccessSchoolclass", dependsOnGroups = "DataAccessAcademicYear")
	public void getOneSchoolclassByYearAndIdentifierSuccess()
			throws DatasetException {
		Schoolclass s = dataAccess.getOneSchoolclass(1, 'a');
		assertNotNull(s);
		assertEquals(dataAccess.getAllAcademicYears().get(0).getSchoolclasses()
				.get(0), s);
		assertEquals('a', s.getIdentifier());
		assertEquals(2, s.getLessonDemands().size());

		s = dataAccess.getOneSchoolclass(2, 'a');
		assertNotNull(s);
		assertEquals(dataAccess.getAllAcademicYears().get(1).getSchoolclasses()
				.get(0), s);
		assertEquals('a', s.getIdentifier());
		assertTrue(s.getLessonDemands().isEmpty());
	}

	@Test(priority = 2, groups = "DataAccessSchoolclass", dependsOnGroups = "DataAccessAcademicYear")
	public void getOneSchoolclassByYearAndIdentifierNull()
			throws DatasetException {
		Schoolclass s = dataAccess.getOneSchoolclass(1, 'b');
		assertNull(s);
	}

	@Test(priority = 3, groups = "DataAccessSchoolclass", dependsOnGroups = "DataAccessAcademicYear")
	public void getOneSchoolclassByRoomSuccess() throws DatasetException {
		Schoolclass s = dataAccess.getOneSchoolclass(dataAccess.getOneRoom(
				"Hauptstandort", "Raum 100"));
		assertNotNull(s);
		assertEquals(dataAccess.getOneSchoolclass(1, 'a'), s);
	}

	@Test(priority = 3, groups = "DataAccessSchoolclass", dependsOnGroups = "DataAccessAcademicYear")
	public void getOneSchoolclassByRoomNull() throws DatasetException {
		Room r = new Room();
		r.setName("Raum 200");
		assertNull(dataAccess.getOneSchoolclass(r));
	}

	@Test(priority = 3, groups = "DataAccessSchoolclass", dependsOnGroups = "DataAccessAcademicYear")
	public void getAllSchoolclassesForTeacherSuccess() throws DatasetException {
		List<Schoolclass> list = dataAccess
				.getAllSchoolclasses((Teacher) dataAccess.getOneEmployee("MEY"));
		assertEquals(2, list.size());
		assertTrue(list.contains(dataAccess.getOneSchoolclass(1, 'a')));
		assertTrue(list.contains(dataAccess.getOneSchoolclass(2, 'a')));
	}

	@Test(priority = 3, groups = "DataAccessSchoolclass", dependsOnGroups = "DataAccessAcademicYear")
	public void getAllSchoolclassesSuccess() throws DatasetException {
		List<Schoolclass> list = dataAccess.getAllSchoolclasses();
		assertEquals(2, list.size());
		assertTrue(list.contains(dataAccess.getOneSchoolclass(1, 'a')));
		assertTrue(list.contains(dataAccess.getOneSchoolclass(2, 'a')));
	}

	@Test(priority = 1, dependsOnGroups = "DataAccessSchoolclass")
	public void getUsedCharsAcademicYearSuccess() throws DatasetException {
		List<Character> list = dataAccess.getUsedChars(dataAccess
				.getOneAcademicYear(1));
		assertEquals(1, list.size());
		assertEquals(new Character('a'), list.get(0));
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	// Integration Classteam
	// ////////////////////////////////////////////////////////////////////////////////////

	@Test(priority = 1, groups = "DataAccessClassteam", dependsOnGroups = "DataAccessSchoolclass")
	public void getAllClassteamsSuccess() throws DatasetException {
		Teacher t = (Teacher) dataAccess.getOneEmployee("MEY");

		// Zunächst Klassenteams erstellen
		Classteam c = new Classteam();
		c.addEmployee(t);
		List<Schoolclass> schoolclasses = new ArrayList<Schoolclass>();
		schoolclasses.add(dataAccess.getOneSchoolclass(1, 'a'));
		c.setSchoolclasses(schoolclasses);
		dataAccess.persist(c);

		Classteam c1 = new Classteam();
		c1.addEmployee(t);
		c1.addEmployee(dataAccess.getAllPAs().get(0));
		schoolclasses = new ArrayList<Schoolclass>();
		schoolclasses.add(dataAccess.getOneSchoolclass(2, 'a'));
		c1.setSchoolclasses(schoolclasses);
		dataAccess.persist(c1);

		// Der eigentliche Test
		List<Classteam> classteams = dataAccess.getAllClassteams();
		assertEquals(2, classteams.size());
		assertTrue(classteams.contains(c));
		assertTrue(classteams.contains(c1));
	}

	@Test(priority = 2, groups = "DataAccessClassteam", dependsOnGroups = "DataAccessSchoolclass")
	public void getAllClassteamsEmployeeSuccess() throws DatasetException {
		List<Classteam> list = dataAccess.getAllClassteams(dataAccess
				.getOneEmployee("MEY"));
		assertEquals(2, list.size());

		List<Classteam> teams = dataAccess.getAllClassteams();
		assertTrue(list.contains(teams.get(0)));
		assertTrue(list.contains(teams.get(1)));

		list = dataAccess.getAllClassteams(dataAccess.getOneEmployee("MUS"));
		assertEquals(1, list.size());
		assertEquals(dataAccess.getOneSchoolclass(2, 'a'), list.get(0)
				.getSchoolclasses().get(0));
	}

	@Test(priority = 2, groups = "DataAccessClassteam", dependsOnGroups = "DataAccessSchoolclass")
	public void getOneClassteamSchoolclassSuccess() throws DatasetException {
		Schoolclass s = dataAccess.getOneSchoolclass(1, 'a');
		Classteam c = dataAccess.getOneClassteam(s);
		assertNotNull(c);
		assertEquals(1, c.getEmployees().size());
		assertEquals(dataAccess.getOneEmployee("MEY"), c.getEmployees().get(0));
		assertEquals(s, c.getSchoolclasses().get(0));

		s = dataAccess.getOneSchoolclass(2, 'a');
		c = dataAccess.getOneClassteam(s);
		assertNotNull(c);
		assertEquals(2, c.getEmployees().size());
		assertTrue(c.getEmployees().contains(dataAccess.getOneEmployee("MUS")));
		assertTrue(c.getEmployees().contains(dataAccess.getOneEmployee("MEY")));
		assertEquals(s, c.getSchoolclasses().get(0));
	}

	@Test(priority = 2, groups = "DataAccessClassteam", dependsOnGroups = "DataAccessSchoolclass")
	public void getOneClassteamSchoolclassNull() throws DatasetException {
		Schoolclass s = new Schoolclass();
		s.setIdentifier('b');
		Classteam c = dataAccess.getOneClassteam(s);
		assertNull(c);
	}

	@Test(priority = 2, groups = "DataAccessClassteam", dependsOnGroups = "DataAccessSchoolclass")
	public void getAllSchoolclassesWithoutClassteamSuccess() throws Exception {
		Schoolclass s = new Schoolclass();
		s.setIdentifier('b');
		AcademicYear year = dataAccess.getOneAcademicYear(1);
		year.add(s);
		year.update();
		List<Schoolclass> list = dataAccess
				.getAllSchoolclassesWithoutClassteam();
		assertEquals(1, list.size());
		assertEquals('b', list.get(0).getIdentifier());
		s = dataAccess.getOneSchoolclass(1, 'b');
		year.remove(s);
		year.update();
		s.delete();
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	// Integration TravelTimeList
	// ////////////////////////////////////////////////////////////////////////////////////

	@Test(priority = 2, groups = "DataAccessTravelTimeList")
	public void getTravelTimeListNull() throws DatasetException {
		TravelTimeList list = dataAccess.getTravelTimeList();
		assertNull(list);
	}

	@Test(priority = 2, groups = "DataAccessTravelTimeList")
	public void getTravelTimeListSuccess() throws DatasetException {
		TravelTimeList list = TravelTimeList.getInstance();
		assertEquals(list, dataAccess.getTravelTimeList());
	}

	@Test(priority = 2, groups = "DataAccessTravelTimeList", dependsOnGroups = "DataAccessLocation")
	public void getTravelTimeListSuccess2() throws DatasetException {
		TravelTimeList list = TravelTimeList.getInstance();
		list.add(dataAccess.getOneLocation("Hauptstandort"),
				dataAccess.getOneLocation("Zweigstelle"), 30);
		list.update();
		TravelTimeList list2 = dataAccess.getTravelTimeList();
		assertEquals(list, list2);
		assertEquals(1, list.getEdges().size());
		assertTrue(list.getEdges().get(0)
				.contains(dataAccess.getOneLocation("Hauptstandort")));
		assertTrue(list.getEdges().get(0)
				.contains(dataAccess.getOneLocation("Zweigstelle")));
	}

	@AfterSuite
	public void afterSuite() throws SQLException {
		DataBase.getInstance().shutDown();
		deleteFolder(new File(DataBase.DB_BACKUP_LOCATION));
		System.out.println("Cleaned up database directory.");
	}

	private void deleteFolder(File folder) {
		if (folder.exists()) {
			File[] files = folder.listFiles();
			if (files != null) {
				for (File f : files) {
					if (f.isDirectory()) {
						deleteFolder(f);
					} else {
						f.delete();
					}
				}
				folder.delete();
			}
		}
	}

}
