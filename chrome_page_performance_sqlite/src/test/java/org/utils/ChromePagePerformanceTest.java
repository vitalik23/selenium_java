package org.utils;

import org.utils.ChromePagePerformanceObject;
import org.utils.ChromePagePerformanceUtil;

import org.junit.Test;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ById;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.sqlite.Function;
import org.sqlite.SQLiteConnection;

public class ChromePagePerformanceTest {

	private static WebDriver driver;
	private static Connection conn;

	@BeforeClass
	public static void setup() throws IOException {
		System.setProperty("webdriver.chrome.driver",
				(new File("c:/java/selenium/chromedriver.exe")).getAbsolutePath());
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		ChromeOptions options = new ChromeOptions();

		HashMap<String, Object> chromePrefs = new HashMap<>();
		chromePrefs.put("profile.default_content_settings.popups", 0);
		String downloadFilepath = System.getProperty("user.dir")
				+ System.getProperty("file.separator") + "target"
				+ System.getProperty("file.separator");
		chromePrefs.put("download.default_directory", downloadFilepath);
		chromePrefs.put("enableNetwork", "true");
		options.setExperimentalOption("prefs", chromePrefs);

		for (String optionAgrument : (new String[] {
				"allow-running-insecure-content", "allow-insecure-localhost",
				"enable-local-file-accesses", "disable-notifications",
				/* "start-maximized" , */
				"browser.download.folderList=2",
				"--browser.helperApps.neverAsk.saveToDisk=image/jpg,text/csv,text/xml,application/xml,application/vnd.ms-excel,application/x-excel,application/x-msexcel,application/excel,application/pdf",
				String.format("browser.download.dir=%s", downloadFilepath)
				/* "user-data-dir=/path/to/your/custom/profile"  , */
		})) {
			options.addArguments(optionAgrument);
		}

		// options for headless
		/*
		for (String optionAgrument : (new String[] { "headless",
				"window-size=1200x600", })) {
			options.addArguments(optionAgrument);
		}
		*/
		capabilities.setBrowserName(DesiredCapabilities.chrome().getBrowserName());

		capabilities.setCapability(ChromeOptions.CAPABILITY, options);
		capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		driver = new ChromeDriver(capabilities);
		try {
			// origin:
			// https://github.com/xerial/sqlite-jdbc/blob/master/demo/Sample.java
			// http://www.codejava.net/java-se/jdbc/connect-to-sqlite-via-jdbc
			// http://www.sqlitetutorial.net/sqlite-java/sqlite-jdbc-driver/
			// https://www.tutorialspoint.com/sqlite/sqlite_java.htm
			Class.forName("org.sqlite.JDBC");
			String dbURL = "jdbc:sqlite:product.db";
			conn = DriverManager.getConnection(dbURL);
			if (conn != null) {
				System.out.println("Connected to the database");
				DatabaseMetaData dm = (DatabaseMetaData) conn.getMetaData();
				System.out.println("Driver name: " + dm.getDriverName());
				System.out.println("Driver version: " + dm.getDriverVersion());
				System.out.println("Product name: " + dm.getDatabaseProductName());
				System.out
						.println("Product version: " + dm.getDatabaseProductVersion());
				conn.close();
			}
		} catch (ClassNotFoundException | SQLException ex) {
			ex.printStackTrace();
		} finally {
			// TODO: refactor after the saving data is implemented
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}

	@AfterClass
	public static void teardown() {
		driver.close();
		driver.quit();
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}

	@Test
	public void testSetTimer() {
		String url = "https://www.royalcaribbean.com/";
		double test = new ChromePagePerformanceObject(driver, url,
				new ById("find-a-cruise")).getLoadTime();
		System.out.println(test);
	}

	@Test
	public void testUtil() {
		ChromePagePerformanceUtil pageLoadTimer = ChromePagePerformanceUtil
				.getInstance();
		double test = pageLoadTimer.getLoadTime(driver,
				"https://www.royalcaribbean.com/", new ById("find-a-cruise"));
		System.out.println(test);
	}
}