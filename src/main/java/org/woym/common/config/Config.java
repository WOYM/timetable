package org.woym.common.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.woym.common.objects.Weekday;

/**
 * Diese Klasse verwaltet die Konfiguration der Software.
 * 
 * @author Adrian
 *
 */
public final class Config {

	/**
	 * Der Name der Konfigurations-Datei.
	 */
	public static final String PROPERTIES_FILE_NAME = "timetable.properties";

	/**
	 * Der Pfad zur Konfigurationsdatei.
	 */
	public static final String PROPERTIES_FILE_PATH = System
			.getProperty("user.dir")
			+ System.getProperty("file.separator")
			+ PROPERTIES_FILE_NAME;

	/**
	 * Der Einstellungswerte, für Schulklassenbezeichner in Kleinbuchstaben.
	 */
	public static final String IDENTIFIER_LOWER_CASE = "lower";

	/**
	 * Der einstellungswert für Schulklassenbezeichner in Großbuchstaben.
	 */
	public static final String IDENTIFIER_UPPER_CASE = "upper";

	/**
	 * Der Einstellungswert für Schulklassenbezeichner in Groß- und
	 * Kleinbuchstaben.
	 */
	public static final String IDENTIFIER_BOTH_CASES = "both";

	/**
	 * String anhand dessen sich die Einträge in der Properties-Datei
	 * identifizieren lassen, welche das Verstecken von Löschen-Dialogen
	 * betreffen.
	 */
	public static final String RESET_DIALOGS_IDENTFIER_STRING = "dialog";

	/**
	 * Der Logger dieser Klasse.
	 */
	private static final Logger LOGGER = LogManager.getLogger("Config");

	/**
	 * Die Properties.
	 */
	private static PropertiesConfiguration propertiesConfig;

	/**
	 * Privater Konstruktor, um eine Instanziierung zu verhindern.
	 */
	private Config() {
	}

	/**
	 * Initialisiert die Konfiguration. Indem die Datei mit dem Pfad
	 * {@linkplain Config#PROPERTIES_FILE_PATH} eingelesen wird. W
	 */
	public static void init() {
		try {
			propertiesConfig = new PropertiesConfiguration(PROPERTIES_FILE_PATH);
			propertiesConfig.setListDelimiter(',');
			propertiesConfig.setAutoSave(true);
			LOGGER.info("Configuration file loaded: " + PROPERTIES_FILE_PATH);
		} catch (ConfigurationException e) {
			LOGGER.warn("No configuration file found.");
			LOGGER.info("Creating new configuration file: "
					+ PROPERTIES_FILE_PATH);
			createNewConfig();
		}
	}

	/**
	 * Gibt ein String-Array der mit dem übergebenen Property-Namen assoziierten
	 * Strings zurück. Besteht keine Assoziation mit dem übergebenen String als
	 * Key, wird ein leeres Array zurückgegeben.
	 * 
	 * @param propKey
	 *            - Name/Key der gesuchten Property
	 * @return ein String-Array mit den assoziierten Strings oder ein leeres
	 *         Array, wenn es keine assoziierten Strings gibt
	 */
	public static String[] getPropValue(String propKey) {
		return propertiesConfig.getStringArray(propKey);
	}

	/**
	 * Aktualisiert den Wert des übergebenen Property-Keys mit dem Wert des
	 * übergebenen Property-Values, sofern der Key vorhanden ist. Anschließend
	 * wird {@code true} zurückgegeben. Existiert der übergebene Key nicht, wird
	 * {@code false} zurückgegeben. Sollen zu dem Key mehrere Werte gespeichert
	 * werden, sind diese durch Kommata zu trennen.
	 * 
	 * @param propKey
	 *            - der Key, zu welchem der Wert aktualisiert werden soll
	 * @param propValue
	 *            - der neue Wert, sollen mehrere Werte zu dem Key gespeichert
	 *            werden, sind diese durch Kommata zu trennen
	 * @return {@code true}, wenn der Key vorhanden ist und aktualisiert wurde,
	 *         ansonsten {@code false}
	 */
	public static boolean updateProperty(String propKey, String propValue) {
		if (propertiesConfig.containsKey(propKey)) {
			if (propertiesConfig.getString(propKey).equals(propValue)) {
				return true;
			}
			propertiesConfig.clearProperty(propKey);
			propertiesConfig.addProperty(propKey, propValue);
			return true;
		}
		return false;
	}

	/**
	 * Fügt der PropertiesConfiguration eine neue Property aus dem übergebenem
	 * Schlüssel und Wert hinzu, sofern der Schlüssel noch nicht vorhanden ist.
	 * 
	 * @param propKey
	 *            - Property Schlüssel
	 * @param propValue
	 *            - Property Wert
	 * @return {@code true}, wenn hinzugefügt, sonst {@code false}
	 */
	public static boolean addProperty(String propKey, String propValue) {
		if (!propertiesConfig.containsKey(propKey)) {
			propertiesConfig.addProperty(propKey, propValue);
			return true;
		}
		return false;
	}

	/**
	 * Entfernt den Property-Eintrag für den übergebenen Property-Key.
	 * 
	 * @param propKey
	 *            - Schlüssel der zu entfernden Property
	 */
	public static void clearProperty(String propKey) {
		propertiesConfig.clearProperty(propKey);
	}

	/**
	 * Diese Methode lädt eine Einstellung mit genau einem Zahlenwert aus der
	 * Konfiguration.
	 * 
	 * Prüft nur nach dem ersten Wert des Konfigurationselementes.
	 * 
	 * @param defaultConfigEnum
	 *            Der Key
	 * @return Die Value
	 */
	public static int getSingleIntValue(DefaultConfigEnum defaultConfigEnum) {
		if (defaultConfigEnum == null) {
			throw new IllegalArgumentException();
		}
		return propertiesConfig.getInt(defaultConfigEnum.getPropKey());
	}

	/**
	 * Diese Methode lädt eine Einstellung mit genau einem String-Wert aus der
	 * Konfiguration. Hat eine Einstellung mehr als einen Wert, wird nur der
	 * erste zurückgegeben. Hat sie keinen Wert, {@code null}.
	 * 
	 * @param defaultConfigEnum
	 *            - die Einstellung
	 * @return der Wert
	 */
	public static String getSingleStringValue(
			DefaultConfigEnum defaultConfigEnum) {
		if (defaultConfigEnum == null) {
			throw new IllegalArgumentException();
		}
		return propertiesConfig.getString(defaultConfigEnum.getPropKey());
	}

	/**
	 * Gibt den Boolean-Wert zur übergebenen Property zurück.
	 * 
	 * @param defaultConfigEnum
	 *            - die Property
	 * @return boolean-Wert des Property-Keys
	 */
	public static boolean getBooleanValue(DefaultConfigEnum defaultConfigEnum) {
		if (defaultConfigEnum == null) {
			throw new IllegalArgumentException();
		}
		return propertiesConfig.getBoolean(defaultConfigEnum.getPropKey());
	}

	/**
	 * 
	 * Fügt die in der Properties-Datei mit {@code true} stehenden Wochentage,
	 * der Liste von als zu verplanend gewählten (und damit validen) Wochentagen
	 * hinzu und gibt diese Liste zurück.
	 *
	 * @return Liste der in der Properties-Datei mit {@code true} angegebenen
	 *         Wochentage
	 */
	public static List<Weekday> getValidWeekdays() {
		List<Weekday> validWeekdays = new ArrayList<Weekday>();
		if (getBooleanValue(DefaultConfigEnum.WEEKDAY_MONDAY)) {
			validWeekdays.add(Weekday.MONDAY);
		}
		if (getBooleanValue(DefaultConfigEnum.WEEKDAY_TUESDAY)) {
			validWeekdays.add(Weekday.TUESDAY);
		}
		if (getBooleanValue(DefaultConfigEnum.WEEKDAY_WEDNESDAY)) {
			validWeekdays.add(Weekday.WEDNESDAY);
		}
		if (getBooleanValue(DefaultConfigEnum.WEEKDAY_THURSDAY)) {
			validWeekdays.add(Weekday.THURSDAY);
		}
		if (getBooleanValue(DefaultConfigEnum.WEEKDAY_FRIDAY)) {
			validWeekdays.add(Weekday.FRIDAY);
		}
		if (getBooleanValue(DefaultConfigEnum.WEEKDAY_SATURDAY)) {
			validWeekdays.add(Weekday.SATURDAY);
		}
		if (getBooleanValue(DefaultConfigEnum.WEEKDAY_SUNDAY)) {
			validWeekdays.add(Weekday.SUNDAY);
		}
		return validWeekdays;
	}

	/**
	 * Erzeugt eine neue Konfigurationsdatei mit den Standardeinstellungen aus
	 * {@linkplain DefaultConfigEnum}, die dann unter
	 * {@linkplain Config#PROPERTIES_FILE_PATH} zu finden ist.
	 */
	private static void createNewConfig() {
		try {
			File file = new File(PROPERTIES_FILE_PATH);
			if (file.exists()) {
				if (!file.delete()) {
					throw new IOException();
				}
			}
			if (!file.createNewFile()) {
				throw new IOException();
			}
			propertiesConfig = new PropertiesConfiguration(PROPERTIES_FILE_PATH);
			propertiesConfig.setAutoSave(true);
			propertiesConfig.setListDelimiter(',');
			for (DefaultConfigEnum value : DefaultConfigEnum.values()) {
				propertiesConfig.addProperty(value.getPropKey(),
						value.getPropValue());
			}
			propertiesConfig.save();
		} catch (IOException e) {
			LOGGER.error("Exception while creating new configuration file", e);
		} catch (ConfigurationException e) {
			LOGGER.error("Exception while creating new configuration object.",
					e);
		}
	}
}
