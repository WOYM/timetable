package org.woym.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Observable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.woym.common.exceptions.DatasetException;
import org.woym.common.exceptions.InvalidFileException;

/**
 * Diese Singleton-Klasse initialisiert den EntityManager, welcher für alle
 * Datenbanktransaktionen verwendet wird. Zudem bietet sie Methoden für das
 * Erstellen und Wiederherstellen eines Backups. Sie erweitert außerdem
 * {@linkplain Observable}, um alle Observer über eine Änderung des Zustandes
 * des EntityManagers zu informieren.
 * 
 * @author Adrian
 *
 */
public class DataBase extends Observable implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6237080407072299976L;

	/**
	 * Die Singleton-Instanz dieser Klasse.
	 */
	private static final DataBase INSTANCE = new DataBase();

	/**
	 * Die Datenbank URL.
	 */
	public static final String DB_URL = "jdbc:h2:~/WOYM/timetable.db/timetable";

	/**
	 * Der Pfad zu dem Ordner, in welchem sich die vom System genutzte Datebank
	 * befindet.
	 */
	public static final String DB_LOCATION_PATH = System
			.getProperty("user.home")
			+ File.separator
			+ "WOYM"
			+ File.separator + "timetable.db";

	/**
	 * Der Pfad zu dem Ordner, in welchem die Backups abgelegt werden.
	 */
	public static final String DB_BACKUP_LOCATION = System
			.getProperty("user.home")
			+ File.separator
			+ "WOYM"
			+ File.separator;

	/**
	 * Der User-Name für die Datenbank.
	 */
	private static final String USER = "timetable";

	/**
	 * Das Passwort für die Datenbank.
	 */
	private static final String PASSWORD = "timetable";

	/**
	 * Der Logger.
	 */
	private static Logger LOGGER = LogManager.getLogger("DataBase");

	/**
	 * Der EntityManager.
	 */
	private transient EntityManager entityManager;

	/**
	 * Der private Konstruktor.
	 */
	private DataBase() {
	}

	/**
	 * Gibt die Singleton-Instanz dieser Klasse zurück.
	 * 
	 * @return die Singleton-Instanz dieser Klasse
	 */
	public static DataBase getInstance() {
		return INSTANCE;
	}

	/**
	 * Gibt die Instanz von {@linkplain EntityManager} zurück.
	 * 
	 * @return {@linkplain EntityManager} - Instanz des EntityManager
	 */
	EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * Erzeugt ein Backup der Datenbank. Im Verzeichnis
	 * {@linkplain DataBase#DB_BACKUP_LOCATION}. Das Backup trägt den
	 * übergebenen Namen. Wird {@code null} für den Namen übergeben, wird
	 * {@linkplain DataBase#backup()} ausgeführt. Es wird der Pfad zum erzeugten
	 * Backup zurückgegeben.Tritt beim Backup ein Fehler auf, wird eine
	 * {@linkplain DatasetException} geworfen. Schlägt das Schließen des
	 * {@linkplain Statement} oder der {@linkplain Connection} fehl, wird eine
	 * {@linkplain SQLException} geworfen.
	 * 
	 * @param backupName
	 *            - Name des Backups
	 * @return den Pfad zum erzeugten Backup
	 * @throws DatasetException
	 * @throws SQLException
	 *             wenn das Schließen des {@linkplain Statement} oder der
	 *             {@linkplain Connection} fehlschlägt
	 */
	public String backup(String backupName) throws DatasetException,
			SQLException {
		if (backupName == null) {
			return backup();
		}
		Statement stm = null;
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
			stm = conn.createStatement();
			stm.execute("SCRIPT TO '" + DB_BACKUP_LOCATION + backupName
					+ ".zip' COMPRESSION ZIP");
			LOGGER.info("Database backup created: " + backupName + ".zip");
			return DB_BACKUP_LOCATION + backupName + ".zip";
		} catch (Exception e) {
			LOGGER.error("Exception while backing up database", e);
			throw new DatasetException("Error while backing up database: "
					+ e.getMessage());
		} finally {
			if (stm != null) {
				stm.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}

	/**
	 * Stellt ein Backup wieder her. Prüft zunächst, ob es sich bei dem
	 * übergebenen Pfad, um einen Pfad zu einer gültigen ZIP-Datei handelt. Ist
	 * dies nicht der Fall, wird eine {@linkplain InvalidFileException}
	 * geworfen. Ansonsten wird der EnitityManager geschlossen, die Datenbank
	 * heruntergefahren und der aktuelle Datenbankordner gelöscht. Anschließend
	 * wird das SQL-Skript ausgeführt und die Datenbankverbindung
	 * wiederhergestellt. <br>
	 * Tritt beim Überprüfen der Backup-Datei ein Fehler auf, wird eine
	 * {@link IOException} geworfen. <br>
	 * Tritt beim Schließen des {@linkplain Statement} oder der
	 * {@linkplain Connection} ein Fehler auf, wird {@linkplain SQLException}
	 * geworfen. <br>
	 * Tritt ein anderer Fehler auf, wird eine {@link DatasetException}
	 * geworfen.
	 * 
	 * @param filePath
	 *            - der absolute Pfad zum wiederherzustellenden Backup
	 * @throws IOException
	 *             wenn ein Fehler beim Überprüfen des Dateipfades auftritt
	 * @throws InvalidFileException
	 *             wenn ein Pfad zu einer ungültigen Datei angegeben wurde
	 *             (keine ZIP-Datei oder kein SQL-Skript enthalten.
	 * @throws DatasetException
	 *             wenn ein anderweitiger Fehler auftritt
	 * @throws SQLException
	 *             wenn das Schließen des Statements fehlschlägt
	 */
	public void restore(String filePath) throws IOException,
			InvalidFileException, DatasetException, SQLException {
		Statement stm = null;
		Connection conn = null;
		File tempDirectory = new File(DB_LOCATION_PATH + ".tmp");
		File dbDirectory = new File(DB_LOCATION_PATH);
		try {
			if (!filePath.endsWith(".zip") || !checkZip(filePath)) {
				LOGGER.error("Invalid file path.");
				throw new InvalidFileException();
			}

			entityManager.close();
			conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
			stm = conn.createStatement();
			stm.execute("SHUTDOWN");
			stm.close();
			conn.close();
			LOGGER.info("Shut down database for backup restoration.");

			// Sicherungskopie erstellen
			copyDirectory(dbDirectory, tempDirectory);

			// Datenbankverzeichnis löschen
			deleteFolder(dbDirectory);

			conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
			stm = conn.createStatement();
			stm.execute("RUNSCRIPT FROM '" + filePath + "'  COMPRESSION ZIP");
			LOGGER.info("Backup " + filePath + " restored.");
			entityManager = null;
			setUp();
			deleteFolder(tempDirectory);
		} catch (IOException e) {
			LOGGER.error("Error while checking backup file: ", e);
			throw new IOException();
		} catch (InvalidFileException e) {
			throw new InvalidFileException();
		} catch (Exception e) {
			// Sicherungskopie wiederherstellen
			copyDirectory(tempDirectory, dbDirectory);
			setUp();
			LOGGER.error("Exception while restoring database from backup: ", e);
			throw new DatasetException(
					"Error while restoring database from backup: "
							+ e.getMessage());
		} finally {
			if (stm != null) {
				stm.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}

	/**
	 * Initialisiert den EntityManager, sofern dieser noch nicht initialisiert
	 * wurde, also null ist.
	 * 
	 * @throws PersistenceException
	 *             wenn beim Initialisieren ein Fehler auftritt
	 */
	void setUp() throws PersistenceException {
		if (entityManager == null) {
			LOGGER.info("Establishing database-connection...");
			try {
				EntityManagerFactory factory;
				factory = Persistence.createEntityManagerFactory("timetable");
				entityManager = factory.createEntityManager();
				setChanged();
				notifyObservers();
				LOGGER.info("Connection established.");
			} catch (Exception e) {
				LOGGER.error("Exception while establishing database connection.");
				throw new PersistenceException(
						"Could not initialize persistence component: "
								+ e.getMessage());
			}
		}
	}

	void shutDown() throws SQLException {
		Connection conn = null;
		Statement stm = null;
		try {
			entityManager.close();
			entityManager = null;
			conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
			stm = conn.createStatement();
			stm.execute("SHUTDOWN");
		} finally {
			if (stm != null) {
				stm.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}

	/**
	 * Erzeugt ein Backup, das das aktuelle Datum plus Uhrzeit im Format
	 * "dd-MM-yyyy_HH.mm.ss" als Namen trägt.
	 * 
	 * @throws DatasetException
	 * @throws SQLException
	 */
	private String backup() throws DatasetException, SQLException {
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH.mm.ss");
		Calendar cal = Calendar.getInstance();
		String time = dateFormat.format(cal.getTime());
		return backup(time);
	}

	/**
	 * Überprüft, es sich bei dem übergebenen Dateipfad, um einen Pfad zu einem
	 * gültigen Backup handelt.
	 * 
	 * @param zipFile
	 *            - der zu prüfende Pfad
	 * @return {@code true}, wenn es sich um einen gültigen Pfad handelt,
	 *         ansonsten {@code false}
	 * @throws IOException
	 *             wenn ein Fehler beim Lesen der Datei auftritt
	 */
	private boolean checkZip(String filePath) throws IOException {
		File zip = new File(filePath);
		if (!zip.canRead()) {
			return false;
		}
		ZipInputStream zis = new ZipInputStream(new FileInputStream(zip));
		ZipEntry ze = zis.getNextEntry();

		while (ze != null) {
			String fileName = ze.getName();
			if (fileName.equals("script.sql")) {
				zis.close();
				return true;
			}
			ze = zis.getNextEntry();
		}
		zis.close();
		return false;
	}

	/**
	 * Löscht den übergebenen Ordner.
	 * 
	 * @param folder
	 *            - der zu löschende Ordner
	 */
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

	/**
	 * Kopiert das übergebene src-Directory an die Position des
	 * dest-Directories. Existiert das src-Directory nicht oder ist kein
	 * Directory, wird eine {@linkplain IllegalArgumentException} geworfen.
	 * Ansonsten wird das Verzeichnis rekursiv kopiert.
	 * 
	 * @param src
	 *            - das Verzeichnis, welches kopiert werden soll
	 * @param dest
	 *            - das Zielverzeichnis
	 * @throws IOException
	 */
	private void copyDirectory(File src, File dest) throws IOException {
		if (!src.exists() || !src.isDirectory()) {
			throw new IllegalArgumentException();
		}

		if (dest.exists()) {
			dest.delete();
		}
		if (!dest.exists()) {
			dest.mkdirs();
		}

		File[] files = src.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return !name.toLowerCase().endsWith(".lock.db");
			}
		});
		for (File f : files) {
			if (f.isDirectory()) {
				copyDirectory(f, new File(dest.getAbsolutePath()
						+ File.separator + f.getName()));
			} else {
				File tempFile = new File(dest.getAbsolutePath()
						+ File.separator + f.getName());
				Files.copy(Paths.get(f.getAbsolutePath()),
						Paths.get(tempFile.getAbsolutePath()),
						StandardCopyOption.REPLACE_EXISTING);
			}
		}
	}
}
