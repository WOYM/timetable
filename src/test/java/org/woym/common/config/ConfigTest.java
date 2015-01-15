package org.woym.common.config;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.woym.common.config.Config;
import org.woym.common.config.DefaultConfigEnum;

@Test(groups = "unit")
public class ConfigTest {

	@BeforeClass
	public void setUp() {
		File file = new File(Config.PROPERTIES_FILE_PATH);
		if (file.exists()) {
			file.delete();
		}
	}

	@Test
	public void initNoConfigFileExists() {
		File file = new File(Config.PROPERTIES_FILE_PATH);
		assertFalse(file.exists());
		Config.init();
		assertTrue(file.exists());
		for (DefaultConfigEnum value : DefaultConfigEnum.values()) {
			String propValue = value.getPropValue();
			if (propValue != null) {
				String[] values = Config.getPropValue(value.getPropKey());
				assertFalse(values.length == 0);
				if (values.length == 1) {
					assertEquals(value.getPropValue(), values[0]);
				}
			}
		}
	}

	@Test(dependsOnMethods = "initNoConfigFileExists")
	public void initConfigFileExists() throws IOException, URISyntaxException {
		File file = new File(Config.PROPERTIES_FILE_PATH);
		Path copyFromPath = Paths.get(
				ClassLoader.getSystemResource("timetable.properties").toURI())
				.toAbsolutePath();
		Files.copy(copyFromPath, Paths.get(file.getAbsolutePath()),
				StandardCopyOption.REPLACE_EXISTING);
		assertTrue(file.exists());
		Config.init();
		String[] values = Config.getPropValue("test_value");
		assertTrue(values.length == 1);
		assertEquals("test", values[0]);
	}

	@Test(dependsOnMethods = "initConfigFileExists")
	public void updatePropertySuccess() {
		assertTrue(Config.updateProperty("test_value", "updated_test"));
		assertEquals("updated_test", Config.getPropValue("test_value")[0]);
	}

	@AfterClass
	public void tearDown() {
		File file = new File(Config.PROPERTIES_FILE_PATH);
		if (file.exists()) {
			file.delete();
		}
	}

}
