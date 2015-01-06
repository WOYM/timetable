package org.woym.logic.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.woym.config.Config;
import org.woym.config.DefaultConfigEnum;
import org.woym.exceptions.DatasetException;
import org.woym.objects.AcademicYear;
import org.woym.persistence.DataAccess;

/**
 * Hilfsklasse, die eine Methode bereitstellt welche alle verfügbaren Bezeichner
 * für Schulklassen eines übergebenen Jahrgangs zurückgibt.
 * 
 * @author Adrian
 *
 */
public abstract class SchoolclassIdentifierUtil {

	private static List<Character> lowerCase = getAllLowerCase();

	private static List<Character> upperCase = getAllUpperCase();

	/**
	 * Gibt eine Liste aller noch verfügbaren Bezeichner für Schulklassen des
	 * übergebenen Jahrgangs zurück. Wird {@code null} übergeben oder befindet
	 * sich ein unbekannter Property Wert für
	 * {@linkplain DefaultConfigEnum#SCHOOLCLASS_IDENTIFIER_CASE} in der
	 * Properties-Datei, wird eine {@linkplain IllegalArgumentException}
	 * geworfen.
	 * 
	 * @param academicYear
	 *            - Jahrgang, für welchen die noch verfügbaren Bezeichner
	 *            erwartet werden
	 * @return Liste der noch verfügbaren Bezeichner für den übergebenen
	 *         Jahrgang
	 * @throws DatasetException
	 */
	public static List<Character> getAvailableCharacters(
			AcademicYear academicYear) throws DatasetException {
		if (academicYear == null) {
			throw new IllegalArgumentException();
		}
		List<Character> list = DataAccess.getInstance().getUsedChars(
				academicYear);

		String[] property = Config
				.getPropValue(DefaultConfigEnum.SCHOOLCLASS_IDENTIFIER_CASE
						.getPropKey());

		List<Character> toReturn = new LinkedList<Character>();

		if (property.length == 1) {
			switch (property[0]) {

			case Config.IDENTIFIER_BOTH_CASES:
				toReturn.addAll(lowerCase);
				toReturn.addAll(upperCase);
				break;

			case Config.IDENTIFIER_LOWER_CASE:
				toReturn.addAll(lowerCase);
				break;

			case Config.IDENTIFIER_UPPER_CASE:
				toReturn.addAll(upperCase);
				break;
			}
			toReturn.removeAll(list);
			return toReturn;
		} else {
			throw new IllegalArgumentException();
		}
	}

	private static List<Character> getAllLowerCase() {
		List<Character> toReturn = new ArrayList<Character>();
		for (char c = 'a'; c <= 'z'; c++) {
			toReturn.add(c);
		}
		return toReturn;
	}

	private static List<Character> getAllUpperCase() {
		List<Character> toReturn = new ArrayList<Character>();
		for (char c = 'A'; c <= 'Z'; c++) {
			toReturn.add(c);
		}
		return toReturn;
	}
}
