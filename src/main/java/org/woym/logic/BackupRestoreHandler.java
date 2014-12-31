package org.woym.logic;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.woym.config.Config;
import org.woym.exceptions.DatasetException;
import org.woym.exceptions.InvalidFileException;
import org.woym.messages.GenericErrorMessage;
import org.woym.messages.GenericSuccessMessage;
import org.woym.persistence.DataBase;
import org.woym.spec.logic.IStatus;

/**
 * Diese abstrakte Klasse stellt Methoden bereit, die ein Backup des Systems
 * erzeugen und wiederherstellen können.
 * 
 * @author Adrian
 *
 */
public abstract class BackupRestoreHandler {

	/**
	 * Der Logger.
	 */
	private final static Logger LOGGER = LogManager
			.getLogger(BackupRestoreHandler.class);

	/**
	 * Diese statische Methode erzeugt ein Backup mit dem übergebenen Namen
	 * oder, wenn {@code null} übergeben wird, mit einem generierten Namen. Es
	 * wird zunächst ein gezipptes Backup der Datenbank erzeugt. Sollte dabei
	 * eine {@linkplain DatasetException} auftreten, wird die eventuell bereits
	 * erzeugte Datei wieder gelöscht und ein entsprechendes
	 * {@linkplain FailureStatus}-Objekt mit zurückgegeben. <br>
	 * Tritt kein Fehler auf wird die Properties-Datei, die sich im Pfad
	 * {@linkplain Config#PROPERTIES_FILE_PATH} befindet, in das Zip-Archiv
	 * kopiert. Ist der Vorgang erfolgreich, wird eine
	 * {@linkplain SuccessStatus}-Objekt zurückgegeben. Tritt ein Fehler auf,
	 * wird versucht eine möglicherweise bestehende Datei zu löschen und es wird
	 * ein entsprechendes {@linkplain FailureStatus}-Objekt zurückgegeben.
	 * 
	 * @param backupName
	 *            - der Name für das Backup. Wird {@code null} übergeben, wird
	 *            dieser automatisch im Format "yyyy-mm-dd_HH.mm.ss" generiert.
	 * @return ein {@linkplain IStatus}-Objekt. Im Erfolgsfall vom Typ
	 *         {@linkplain SuccessStatus}, ansonsten vom Typ
	 *         {@linkplain FailureStatus}
	 */
	public static IStatus backup(String backupName) {
		String backupPath = null;
		try {
			backupPath = DataBase.getInstance().backup(backupName);
		} catch (DatasetException e) {
			// Backup-Datei wieder löschen, falls sie bereits erstellt wurde
			File file = new File(backupPath);
			if (file.exists()) {
				file.delete();
			}
			return new FailureStatus(GenericErrorMessage.BACKUP_FAILURE,
					FacesMessage.SEVERITY_ERROR);
		} catch (SQLException e) {
			// Nur loggen, da Exception lediglich auftritt, wenn ein Statement
			// oder eine Connection nicht geschlossen werden kann
			LOGGER.error(e);
		}

		try (FileSystem fs = createFileSystem(backupPath)) {
			Path properties = Paths.get(Config.PROPERTIES_FILE_PATH);
			Path zippath = fs.getPath(Config.PROPERTIES_FILE_NAME);
			Files.copy(properties, zippath, StandardCopyOption.REPLACE_EXISTING);
			LOGGER.info("Successfully added properties file to backup.");
			return new SuccessStatus(GenericSuccessMessage.BACKUP_SUCCESS);
		} catch (IOException e) {
			LOGGER.error("Deleting already created backup due to error.", e);
			// Backup-Datei wieder löschen, falls sie bereits erstellt wurde
			File file = new File(backupPath);
			if (file.exists()) {
				file.delete();
			}
			return new FailureStatus(GenericErrorMessage.BACKUP_FAILURE,
					FacesMessage.SEVERITY_ERROR);
		}
	}

	/**
	 * Diese statische Methode versucht ein Backup, welches den übergebenen
	 * String als Pfad hat, wiederherzustellen. Zunächst wird das
	 * Datenbank-Backup wiederhergestellt. Sollte dabei ein Fehler auftreten,
	 * wird ein entsprechendes {@linkplain FailureStatus}-Objekt zurückgegeben. <br>
	 * Anschließend wird die Properties-Datei aus dem gezippten Backup nach
	 * {@linkplain Config#PROPERTIES_FILE_PATH} kopiert. Ist dies erfolgreich
	 * wird ein {@linkplain SuccessStatus}-Objekt zurückgegeben, ansonsten ein
	 * {@linkplain FailureStatus}-Objekt.
	 * 
	 * @param filePath
	 *            - der absolute Pfad zum wiederherzustellenden Backup
	 * @return eine {@linkplain IStatus}-Objekt. Bei erfolgreicher Ausführung
	 *         vom Typ {@linkplain SuccessStatus}, ansonsten vom Typ
	 *         {@linkplain FailureStatus}
	 */
	public static IStatus restore(String filePath) {
		try {
			DataBase.getInstance().restore(filePath);
		} catch (DatasetException | IOException e) {
			return new FailureStatus(GenericErrorMessage.RESTORE_FAILURE,
					FacesMessage.SEVERITY_ERROR);
		} catch (SQLException e) {
			// Nur loggen, da Exception lediglich auftritt, wenn ein Statement
			// oder eine Connection nicht geschlossen werden kann
			LOGGER.error(e);
		} catch (InvalidFileException e) {
			return new FailureStatus(GenericErrorMessage.INVALID_FILE,
					FacesMessage.SEVERITY_ERROR);
		}
		try (FileSystem fs = createFileSystem(filePath)) {
			Path properties = Paths.get(Config.PROPERTIES_FILE_PATH);
			Path zippath = fs.getPath(Config.PROPERTIES_FILE_NAME);
			Files.copy(zippath, properties, StandardCopyOption.REPLACE_EXISTING);
			return new SuccessStatus(GenericSuccessMessage.RESTORE_SUCCESS);
		} catch (IOException e) {
			LOGGER.error(e);
			return new FailureStatus(GenericErrorMessage.BACKUP_FAILURE,
					FacesMessage.SEVERITY_ERROR);
		}
	}

	/**
	 * Gibt ein eine Liste von {@linkplain File}-Objekten von Dateien zurück,
	 * die sich im Pfad {@linkplain DataBase#DB_BACKUP_LOCATION} befinden und
	 * mit ".zip" enden. Gibt es keine solchen Dateien oder existiert der Ordner
	 * gar nicht, wird eine leere Liste zurückgegeben.
	 * 
	 * @return {@linkplain File}-Liste mit allen zip-Dateien aus dem Ordner
	 *         {@linkplain DataBase#DB_BACKUP_LOCATION} oder eine leere
	 *         {@linkplain File}-Liste, wenn keine Dateien vorhanden bzw. der
	 *         Ordner nicht existiert
	 */
	public static List<File> getAllBackups() {
		File folder = new File(DataBase.DB_BACKUP_LOCATION);
		if (folder.exists() && folder.isDirectory()) {
			File[] files = folder.listFiles(new FilenameFilter() {

				@Override
				public boolean accept(File dir, String name) {
					return name.toLowerCase().endsWith(".zip");
				}
			});
			return Arrays.asList(files);
		}
		return new ArrayList<File>();
	}

	/**
	 * Erzeugt ein {@linkplain FileSystem} mit den Eigenschaften
	 * "create = false" und "encoding = UTF-8" für den übergebenen Pfad und gibt
	 * es zurück.
	 * 
	 * @param path
	 *            - Pfad, für welchen das {@linkplain FileSystem} erzeugt werden
	 *            soll
	 * @return das erzeugte {@linkplain FileSystem}
	 * @throws IOException
	 */
	private static FileSystem createFileSystem(String path) throws IOException {
		Map<String, String> env = new HashMap<>();
		env.put("create", "false");
		env.put("encoding", "UTF-8");
		URI uri;
		if (File.separator.equals("/")) {
			uri = URI.create("jar:file:" + path);
		} else {
			uri = URI.create("jar:file:/" + path.replace('\\', '/'));
		}
		return FileSystems.newFileSystem(uri, env);
	}
}
