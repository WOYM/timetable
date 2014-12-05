package org.woym.config;

import java.io.File;
import java.io.IOException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
	private static final String PROPERTIES_FILE_NAME = "timetable.properties";

	/**
	 * Der Pfad zur Konfigurationsdatei.
	 */
	private static final String PROPERTIES_FILE_PATH = System
			.getProperty("user.dir")
			+ System.getProperty("file.separator")
			+ PROPERTIES_FILE_NAME;

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
			System.out.println(PROPERTIES_FILE_PATH);
			propertiesConfig = new PropertiesConfiguration(PROPERTIES_FILE_PATH);
			propertiesConfig.setListDelimiter(',');
			propertiesConfig.setAutoSave(true);
		} catch (ConfigurationException e) {
			LOGGER.warn("No configuration file found.");
			LOGGER.info("Creating new configuration file.");
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

	public static boolean updateProperty(String propKey, String propValue) {
		if(propertiesConfig.containsKey(propKey)){
			propertiesConfig.clearProperty(propKey);
			propertiesConfig.addProperty(propKey, propValue);
			return true;
		}
		return false;
	}

	/**
	 * Erzeugt eine neue Konfigurationsdatei mit den Standardeinstellungen aus
	 * {@linkplain DefaultConfigEnum}, die dann unter
	 * {@linkplain Config#PROPERTIES_FILE_PATH} zu finden ist.
	 */
	private static void createNewConfig() {
		try {
			File file = new File(PROPERTIES_FILE_PATH);
			file.createNewFile();
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
