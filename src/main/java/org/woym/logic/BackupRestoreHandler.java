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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.faces.application.FacesMessage;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.woym.config.Config;
import org.woym.config.DefaultConfigEnum;
import org.woym.exceptions.DatasetException;
import org.woym.exceptions.InvalidFileException;
import org.woym.logic.spec.IStatus;
import org.woym.messages.GenericErrorMessage;
import org.woym.messages.GenericSuccessMessage;
import org.woym.persistence.DataBase;

/**
 * Diese abstrakte Klasse stellt Methoden bereit, die ein Backup des Systems
 * erzeugen und wiederherstellen können.
 * 
 * @author Adrian
 *
 */
public class BackupRestoreHandler implements ServletContextListener {

	/**
	 * Der Logger.
	 */
	private static final Logger LOGGER = LogManager
			.getLogger(BackupRestoreHandler.class);

	/**
	 * Der ScheduledExecutorService, welcher die automatischen Backups umsetzt.
	 */
	private static ScheduledExecutorService scheduler;

	@Override
	public void contextInitialized(ServletContextEvent event) {
		startScheduler(false);
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		stopScheduler();
	}

	/**
	 * Stoppt den Dienst für die automatischen Backups und startet ihn neu.
	 */
	public static void restartScheduler() {
		stopScheduler();
		startScheduler(false);
	}

	/**
	 * Startet den Dienst für die automatischen Backups.
	 * 
	 * @param afterRestore
	 *            {@code true}, wenn das Starten nach einer Wiederherstellung
	 *            geschieht, ansonsten {@code false}
	 */
	private static void startScheduler(boolean afterRestore) {
		scheduler = Executors.newSingleThreadScheduledExecutor();
		int backupInterval = Integer.valueOf(DefaultConfigEnum.BACKUP_INTERVAL
				.getPropValue());
		try {
			backupInterval = Config
					.getSingleIntValue(DefaultConfigEnum.BACKUP_INTERVAL);
		} catch (Exception e) {
			LOGGER.error(e);
		}

		if (backupInterval <= 0) {
			LOGGER.debug("Disabled backups.");
			return;
		} else if (backupInterval < 1440) {
			scheduler.scheduleAtFixedRate(new Runnable() {

				@Override
				public void run() {
					BackupRestoreHandler.backup(null);
				}
			}, backupInterval, backupInterval, TimeUnit.MINUTES);
			LOGGER.debug("Started automatic backup scheduler. Backup every "
					+ backupInterval + " minutes.");

		} else if (backupInterval >= 1440) {
			try {
				final SimpleDateFormat sdf = new SimpleDateFormat(
						"dd.MM.yyyy-HH:mm");
				final SimpleDateFormat day = new SimpleDateFormat("dd.MM.yyyy");
				String date = Config
						.getSingleStringValue(DefaultConfigEnum.BACKUP_NEXTDATE);
				String time = Config
						.getSingleStringValue(DefaultConfigEnum.BACKUP_TIME);

				Date currentDate = Calendar.getInstance().getTime();
				Date nextDate = sdf.parse(date + "-" + time);

				// Falls das nächste Backup-Datum vor dem aktuellen liegt
				if (nextDate.before(currentDate)) {
					// Wenn kein Restore, Backup im Nachhinein anfertigen
					if (!afterRestore) {
						BackupRestoreHandler.backup(null);
					} else {
						// Nach Restore das Backup-Intervall ab der aktuellen
						// Zeit beginnen
						nextDate = sdf.parse(day.format(currentDate) + "-"
								+ time);
					}

					currentDate = Calendar.getInstance().getTime();

					// So lange das nächste Backup-Datum nicht nach dem
					// aktuellen liegt, das Datum um das Backup-Intervall
					// erhöhen
					while (nextDate.before(currentDate)) {
						nextDate.setTime(nextDate.getTime()
								+ TimeUnit.MINUTES.toMillis(backupInterval));
						currentDate = Calendar.getInstance().getTime();
					}
					Config.updateProperty(
							DefaultConfigEnum.BACKUP_NEXTDATE.getPropKey(),
							day.format(nextDate));
				}

				// Absolute Differenz zwischen aktuellem Datum und nächstem
				// Backup-Datum
				final long initialDelay = Math.abs(currentDate.getTime()
						- nextDate.getTime());
				// ScheduledExecutorService starten

				final int interval = backupInterval;
				scheduler.scheduleAtFixedRate(new Runnable() {

					@Override
					public void run() {
						BackupRestoreHandler.backup(null);

						// Nächstes Backup-Datum in der Properties-Datei
						// aktualisieren
						Date nextBackupDate = Calendar.getInstance().getTime();
						nextBackupDate.setTime(nextBackupDate.getTime()
								+ TimeUnit.MINUTES.toMillis(interval));
						Config.updateProperty(
								DefaultConfigEnum.BACKUP_NEXTDATE.getPropKey(),
								day.format(nextBackupDate));
					}
				}, initialDelay, backupInterval, TimeUnit.MINUTES);
				LOGGER.debug("Started automatic backup scheduler. Next backup in "
						+ initialDelay
						+ "ms, then every "
						+ (backupInterval / 1440) + " days.");
			} catch (Exception e) {
				LOGGER.error(e);
			}
		}
	}

	/**
	 * Stoppt den Dienst für die automatischen Backups.
	 */
	private static void stopScheduler() {
		scheduler.shutdownNow();
		LOGGER.debug("Shut down automatic backup scheduler.");
	}

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
		stopScheduler();
		try {
			DataBase.getInstance().restore(filePath);
			CommandHandler.getInstance().emptyQueues();
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
			// Neue Properties-Datei laden
			Config.init();
			startScheduler(true);
			return new SuccessStatus(GenericSuccessMessage.RESTORE_SUCCESS);
		} catch (IOException e) {
			LOGGER.error(e);
			return new FailureStatus(
					"System nicht vollständig wiederhergestellt",
					"Die Systemeinstellungen konnten nicht erfolgreich wiederhergestellt werden. "
							+ "Kopieren Sie sie manuell aus dem gezippten Backup in das Arbeitsverzeichnis.",
					FacesMessage.SEVERITY_ERROR);
		}
	}

	/**
	 * Gibt ein eine Liste von {@linkplain File}-Objekten von Dateien zurück,
	 * die sich im Pfad {@linkplain DataBase#DB_BACKUP_LOCATION} befinden und
	 * mit ".zip" enden. Diese sind absteigend nach Erstellungsdatum sortiert.
	 * Gibt es keine solchen Dateien oder existiert der Ordner gar nicht, wird
	 * eine leere Liste zurückgegeben.
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

			Arrays.sort(files, new Comparator<File>() {

				@Override
				public int compare(File file1, File file2) {
					if (file1.lastModified() > file2.lastModified()) {
						return -1;
					}

					if (file1.lastModified() < file2.lastModified()) {
						return 1;
					}

					return 0;
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
