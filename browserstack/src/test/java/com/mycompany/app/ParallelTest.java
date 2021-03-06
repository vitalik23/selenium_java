package com.mycompany.app;

import java.net.URL;
import java.net.MalformedURLException;

import java.util.concurrent.TimeUnit;

import org.testng.annotations.Test;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.WebDriverException;


public class ParallelTest {

	private static final String USERNAME = "********";
	private static final String ACCESS_KEY = "********";

	private WebDriver driver;

	@Parameters(value = { "browser", "version", "platform" })
	@BeforeClass
	public void setUp(String browser, String version, String platform ) throws MalformedURLException, InterruptedException, WebDriverException {
		DesiredCapabilities capability = new DesiredCapabilities();
		capability.setCapability("platform", platform);
		capability.setCapability("browserName", browser);
		capability.setCapability("browserVersion", version);
		capability.setCapability("project", "P1");
		capability.setCapability("build", "1.0");
		driver = new RemoteWebDriver(new URL(
				"http://" + USERNAME + ":" + ACCESS_KEY + "@hub.browserstack.com/wd/hub" ), capability);
	}

	@Test
	public void test() throws Exception {
		driver.get("http://www.google.com");
		System.out.println("Page title is: " + driver.getTitle());
		Assert.assertEquals("Google", driver.getTitle());
		WebElement element = driver.findElement(By.name("q"));
		element.sendKeys("Browser Stack");
		element.submit();
	}

	@AfterClass
	public void tearDown() throws Exception {
		driver.quit();
	}
}
