package org.woym.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ComponentSystemEvent;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.woym.exceptions.DatasetException;
import org.woym.exceptions.InvalidFileException;

/**
 * Diese Klasse initialisiert den EntityManager, welcher für alle
 * Datenbanktransaktionen verwendet wird.
 * 
 * @author Adrian
 *
 */
@ManagedBean
@SessionScoped
public class DataBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6237080407072299976L;

	private static final String DB_URL = "jdbc:h2:~/WOYM/timetable.db/timetable";

	private static final String DB_LOCATION_PATH = System
			.getProperty("user.home")
			+ File.separator
			+ "WOYM"
			+ File.separator + "timetable.db";

	private static final String DB_BACKUP_LOCATION = System
			.getProperty("user.home")
			+ File.separator
			+ "WOYM"
			+ File.separator;

	private static final String USER = "timetable";

	private static final String PASSWORD = "timetable";

	/**
	 * Der Logger.
	 */
	private static Logger LOGGER = LogManager.getLogger("DataBase");

	/**
	 * Der EntityManager.
	 */
	private static EntityManager ENTITY_MANAGER;

	/**
	 * Gibt die Instanz von {@linkplain EntityManager} zurück.
	 * 
	 * @return {@linkplain EntityManager} - Instanz des EntityManager
	 */
	public static EntityManager getEntityManager() {
		return ENTITY_MANAGER;
	}

	/**
	 * Initialisiert den EntityManager, sofern dieser noch nicht initialisiert
	 * wurde, also null ist.
	 * 
	 * @param event
	 *            - JSF Event
	 * @throws PersistenceException
	 *             wenn beim Initialisieren ein Fehler auftritt
	 */
	public void setUp(ComponentSystemEvent event) throws DatasetException {
		init();
	}

	/**
	 * Erzeugt ein Backup der Datenbank. Im Home-Verzeichnis im Ordner WOYM. Das
	 * Backup trägt das aktuelle Datum plus Uhrzeit als Namen. Tritt beim Backup
	 * ein Fehler auf, wird eine {@linkplain DatasetException} geworfen.
	 * 
	 * @throws DatasetException
	 */
	public void backup() throws DatasetException {
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
			Calendar cal = Calendar.getInstance();
			String time = dateFormat.format(cal.getTime());
			Statement stm = DriverManager.getConnection(
					"jdbc:h2:~/WOYM/timetable.db/timetable", USER, PASSWORD)
					.createStatement();
			stm.execute("SCRIPT TO '" + DB_BACKUP_LOCATION + time
					+ ".zip' COMPRESSION ZIP");
			LOGGER.info("Database backup created: " + time + ".zip");
		} catch (Exception e) {
			LOGGER.error("Exception while backing up database", e);
			throw new DatasetException("Error while backing up database: "
					+ e.getMessage());
		}
	}

	/**
	 * Stellt ein Backup wieder her. Prüft zunächst, ob es sich bei dem
	 * übergebenen Pfad, um einen Pfad zu einer gültigen ZIP-Datei handelt. Ist
	 * dies nicht der Fall, wird eine {@linkplain InvalidFileException}
	 * geworfen. Ansonsten wird der EnitityManager geschlossen, die Datenbank
	 * heruntergefahren und der aktuelle Datenbankordner gelöscht. Anschließend
	 * wird das SQL-Skript ausgeführt und die Datenbankverbindung
	 * wiederhergestellt. Tritt beim Überprüfen der Backup-Datei ein Fehler auf,
	 * wird eine {@link IOException} geworfen. Tritt ein anderer Fehler auf,
	 * wird eine {@link DatasetException} geworfen.
	 * 
	 * @param filePath
	 *            - der Pfad zum wiederherzustellenden Backup
	 * @throws IOException
	 *             wenn ein Fehler beim Überprüfen des Dateipfades auftritt
	 * @throws InvalidFileException
	 *             wenn ein Pfad zu einer ungültigen Datei angegeben wurde
	 *             (keine ZIP-Datei oder kein SQL-Skript enthalten.
	 * @throws DatasetException
	 *             wenn ein anderweitiger Fehler auftritt
	 */
	public void restore(String filePath) throws IOException,
			InvalidFileException, DatasetException {
		try {
			if (!filePath.endsWith(".zip") || !checkZip(filePath)) {
				LOGGER.error("Invalid file path.");
				throw new InvalidFileException();
			}
			ENTITY_MANAGER.close();
			Statement stm = DriverManager.getConnection(DB_URL, USER, PASSWORD)
					.createStatement();
			stm.execute("SHUTDOWN");
			LOGGER.info("Shut down database.");

			File dbLocation = new File(DB_LOCATION_PATH);
			deleteFolder(dbLocation);

			DriverManager.getConnection(DB_URL, USER, PASSWORD);
			stm = DriverManager.getConnection(DB_URL, USER, PASSWORD)
					.createStatement();
			stm.execute("RUNSCRIPT FROM '" + filePath + "'  COMPRESSION ZIP");
			LOGGER.info("Backup " + filePath + " restored.");
			ENTITY_MANAGER = null;
			init();
		} catch (IOException e) {
			LOGGER.error("Error while checking backup file: ", e);
			throw new IOException();
		} catch (Exception e) {
			LOGGER.error("Exception while restoring database from backup: ", e);
			throw new DatasetException(
					"Error while restoring database from backup: "
							+ e.getMessage());
		}
	}

	/**
	 * Initialisiert den EntityManager, sofern dieser noch nicht initialisiert
	 * wurde, also null ist.
	 * 
	 * @throws PersistenceException
	 *             wenn beim Initialisieren ein Fehler auftritt
	 */
	private void init() {
		if (ENTITY_MANAGER == null) {
			LOGGER.info("Establishing database-connection...");
			try {
				EntityManagerFactory factory;
				factory = Persistence.createEntityManagerFactory("timetable");
				ENTITY_MANAGER = factory.createEntityManager();
				LOGGER.info("Connection established.");
			} catch (Exception e) {
				throw new PersistenceException(
						"Could not initialize persistence component: "
								+ e.getMessage());
			}
		}
	}

	private boolean checkZip(String zipFile) throws IOException {
		File zip = new File(zipFile);
		if (!zip.canRead()) {
			throw new IllegalArgumentException();
		}
		ZipInputStream zis = new ZipInputStream(new FileInputStream(zip));
		ZipEntry ze = zis.getNextEntry();

		boolean check = false;

		while (ze != null) {
			String fileName = ze.getName();
			if (fileName.equals("script.sql")) {
				check = true;
			}
			ze = zis.getNextEntry();
		}
		zis.close();
		return check;
	}

	private void deleteFolder(File folder) {
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
