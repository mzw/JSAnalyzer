package jp.mzw.jsanalyzer.preventer.test.ucdchina;

import static org.junit.Assert.*;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.WebDriverWait;

import jp.mzw.jsanalyzer.util.VersionUtils;
import jp.mzw.jsanalyzer.util.BrowserUtils;
import jp.mzw.jsanalyzer.core.Project;
import jp.mzw.jsanalyzer.core.cs.UCDChina;
import jp.mzw.jsanalyzer.preventer.test.WebAppTestBase;

public class FDValid extends WebAppTestBase {

    @Override
    public void setupBrowser() throws Exception {
    	File file = new File(BrowserUtils.getFirefoxBin(VersionUtils.get(10, 0, 2)));
    	FirefoxBinary binary = new FirefoxBinary(file);
    	FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("webdriver.load.strategy", "unstable");
        /// Disable CSS
        profile.setPreference("permissions.default.stylesheet", 2);
    	
        driver = new FirefoxDriver(binary, profile);
        wait = new WebDriverWait(driver, TIMEOUT_SEC);
    }

	@Test
	public void testUEHRegist() {

		Project project = UCDChina.getProject(UCDChina.Original);
		
		final String startUrl = project.getUrl();
		
		try {
			driver.manage().timeouts().pageLoadTimeout(2000, TimeUnit.MILLISECONDS);
			gotoUrl(startUrl);
			this.prepareJsErrorCollector();
			/// Disable inline CSS
			this.disableCssStylesheet();
		} catch (TimeoutException e){
            // Expected exception, when timeout occurs, move to next operations.
        }
		
		try {
			this.waitAndClickElementLocated(By.id("tmp_submit"));
			fail();
		} catch (WebDriverException e) {
			// Expected exception.
		}
		
		this.assertNoJsErrorObserved();
		
	}
	
}
