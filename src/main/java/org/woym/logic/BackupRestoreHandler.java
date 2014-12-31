package org.woym.logic;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.faces.application.FacesMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.woym.config.Config;
import org.woym.exceptions.DatasetException;
import org.woym.exceptions.InvalidFileException;
import org.woym.messages.GenericErrorMessage;
import org.woym.messages.MessageHelper;
import org.woym.persistence.DataBase;

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
	 * erzeugte Datei wieder gelöscht und eine {@linkplain FacesMessage} mit
	 * Fehlermeldung zurückgegeben. <br>
	 * Tritt kein Fehler auf wird die Properties-Datei, die sich im Pfad
	 * {@linkplain Config#PROPERTIES_FILE_PATH} befindet, in das Zip-Archiv
	 * kopiert. Ist der Vorgang erfolgreich, wird eine {@linkplain FacesMessage}
	 * zurückgegeben, die dies mitteilt. Tritt ein Fehler auf, wird versucht
	 * eine möglicherweise bestehende Datei zu löschen und es wird eine
	 * entsprechende {@linkplain FacesMessage} mit Fehlermeldung zurückgegeben.
	 * 
	 * @param backupName
	 *            - der Name für das Backup. Wird {@code null} übergeben, wird
	 *            dieser automatisch im Format "yyyy-mm-dd_HH.mm.ss" generiert.
	 * @return eine {@linkplain FacesMessage}, die entweder den Erfolg des
	 *         Backups oder einen Fehlschlag angibt
	 */
	public static FacesMessage backup(String backupName) {
		String backupPath = null;
		try {
			backupPath = DataBase.getInstance().backup(backupName);
		} catch (DatasetException e) {
			// Backup-Datei wieder löschen, falls sie bereits erstellt wurde
			File file = new File(backupPath);
			if (file.exists()) {
				file.delete();
			}
			return MessageHelper.generateMessage(
					GenericErrorMessage.DATABASE_BACKUP_FAILURE,
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
			return new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Backup erfolgreich",
					"Die Backuperstellung war erfolgreich.");
		} catch (IOException e) {
			// Backup-Datei wieder löschen, falls sie bereits erstellt wurde
			File file = new File(backupPath);
			if (file.exists()) {
				file.delete();
			}
			return MessageHelper.generateMessage(
					GenericErrorMessage.DATABASE_BACKUP_FAILURE,
					FacesMessage.SEVERITY_ERROR);
		}

	}

	/**
	 * Diese statische Methode versucht ein Backup, welches den übergebenen
	 * String als Pfad hat, wiederherzustellen. Zunächst wird das
	 * Datenbank-Backup wiederhergestellt. Sollte dabei ein Fehler auftreten,
	 * wird eine entsprechende {@linkplain FacesMessage} zurückgegeben. <br>
	 * Anschließend wird die Properties-Datei aus dem gezippten Backup nach
	 * {@linkplain Config#PROPERTIES_FILE_PATH} kopiert. Je nachdem, ob dies
	 * erfolgreich ist oder dabei ein Fehler auftritt, wird eine entsprechende
	 * {@linkplain FacesMessage} zurückgegeben.
	 * 
	 * @param filePath
	 *            - der absolute Pfad zum wiederherzustellenden Backup
	 * @return eine {@linkplain FacesMessage}, die entweder den Erfolg der
	 *         Wiederherstellung oder ihren Fehlschlag angibt
	 */
	public static FacesMessage restore(String filePath) {
		try {
			DataBase.getInstance().restore(filePath);
		} catch (DatasetException | IOException e) {
			return MessageHelper.generateMessage(
					GenericErrorMessage.DATABASE_RESTORE_FAILURE,
					FacesMessage.SEVERITY_ERROR);
		} catch (SQLException e) {
			// Nur loggen, da Exception lediglich auftritt, wenn ein Statement
			// oder eine Connection nicht geschlossen werden kann
			LOGGER.error(e);
		} catch (InvalidFileException e) {
			return MessageHelper.generateMessage(
					GenericErrorMessage.INVALID_FILE,
					FacesMessage.SEVERITY_ERROR);
		}
		try (FileSystem fs = createFileSystem(filePath)) {
			Path properties = Paths.get(Config.PROPERTIES_FILE_PATH);
			Path zippath = fs.getPath(Config.PROPERTIES_FILE_NAME);
			Files.copy(zippath, properties, StandardCopyOption.REPLACE_EXISTING);
			return new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Backup-Wiederherstellung erfolgreich",
					"Das Datenbank-Backup wurde erfolgreich wiederhergestellt.");
		} catch (IOException e) {
			return MessageHelper.generateMessage(
					GenericErrorMessage.DATABASE_RESTORE_FAILURE,
					FacesMessage.SEVERITY_ERROR);
		}
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
		URI uri = URI.create("jar:file:/" + path.replace('\\', '/'));
		return FileSystems.newFileSystem(uri, env);
	}
}
