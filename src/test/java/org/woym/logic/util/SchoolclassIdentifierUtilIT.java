package org.woym.logic.util;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;

import java.util.List;

import org.testng.annotations.Test;
import org.woym.common.objects.AcademicYear;
import org.woym.persistence.DataAccess;

@Test(groups = { "integration", "SchoolclassIdentifierUtilIT" }, dependsOnGroups = {
		"DataAccessObjectsIT", "DataAccessObjectsIT2", "CommandsDataAccessIT" })
public class SchoolclassIdentifierUtilIT {

	private DataAccess dataAccess = DataAccess.getInstance();

	@Test
	public void getAvailableCharactersSuccess() throws Exception {
		AcademicYear year = dataAccess.getOneAcademicYear(1);

		List<Character> characters = SchoolclassIdentifierUtil
				.getAvailableCharacters(year);
		assertFalse(characters.contains('A'));
		for (char c = 66; c < 91; c++) {
			assertEquals(new Character(c), characters.get(c - 66));
		}
		
		year = dataAccess.getOneAcademicYear(2);
		characters = SchoolclassIdentifierUtil.getAvailableCharacters(year);
		assertFalse(characters.contains('A'));
		assertFalse(characters.contains('B'));
		for (char c = 67; c < 91; c++) {
			assertEquals(new Character(c), characters.get(c - 67));
		}
	}
}
