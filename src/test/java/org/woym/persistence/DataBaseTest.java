package org.woym.persistence;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.woym.common.exceptions.DatasetException;
import org.woym.common.exceptions.InvalidFileException;

@Test(groups = "UnitTest")
public class DataBaseTest extends PowerMockTestCase {

	@BeforeClass
	public static void init() {
		DataBase.getInstance().setUp();
	}

	@Test
	public void backupSuccess() throws DatasetException, SQLException {
		String backupPath = DataBase.getInstance().backup("test");
		File file = new File(backupPath);
		AssertJUnit.assertTrue(file.exists());
		AssertJUnit.assertTrue(file.isFile());
	}

	@Test(dependsOnMethods = "backupSuccess")
	public void restoreSuccess() throws IOException, InvalidFileException,
			DatasetException, SQLException {
		DataBase.getInstance()
				.restore(DataBase.DB_BACKUP_LOCATION + "test.zip");
	}

	@Test(expectedExceptions = InvalidFileException.class)
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
