package org.woym.persistence;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import org.woym.exceptions.DatasetException;
import org.woym.exceptions.InvalidFileException;

@RunWith(PowerMockRunner.class)
public class DataBaseTest {

	@BeforeClass
	public static void init() {
		DataBase.getInstance().setUp();
	}

	@Test
	public void testAll() throws DatasetException, SQLException, IOException,
			InvalidFileException {
		String path = backupSuccess();
		restoreSuccess(path);
	}

	public String backupSuccess() throws DatasetException, SQLException {
		String backupPath = DataBase.getInstance().backup("test");
		File file = new File(backupPath);
		assertTrue(file.exists());
		assertTrue(file.isFile());
		return backupPath;
	}

	public void restoreSuccess(String path) throws IOException,
			InvalidFileException, DatasetException, SQLException {
		DataBase.getInstance().restore(path);
	}

	@Test(expected = InvalidFileException.class)
	public void restoreInvalidFilePath() throws IOException,
			InvalidFileException, DatasetException, SQLException {
		DataBase.getInstance().restore(DataBase.DB_BACKUP_LOCATION);
	}

	@AfterClass
	public static void cleanUp() throws SQLException {
		DataBase.getInstance().shutDown();
		File folder = new File(DataBase.DB_BACKUP_LOCATION);
		deleteFolder(folder);
	}

	private static void deleteFolder(File folder) {
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
