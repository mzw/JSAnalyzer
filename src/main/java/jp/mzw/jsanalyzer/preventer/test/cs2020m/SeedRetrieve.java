package jp.mzw.jsanalyzer.preventer.test.cs2020m;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import jp.mzw.jsanalyzer.core.Project;
import jp.mzw.jsanalyzer.core.cs.CS2020m;
import jp.mzw.jsanalyzer.preventer.test.WebAppTestBase;

public class SeedRetrieve extends WebAppTestBase {

	@Test
	public void testSeedRetrieve() {
		
		Project project = CS2020m.getProject(CS2020m.Original);
//		Project project = CS2020m.getProject(CS2020m.SeedRetrieve);
		
		final String startUrl = project.getUrl();
		
		try {
			driver.manage().timeouts().pageLoadTimeout(2000, TimeUnit.MILLISECONDS);
			gotoUrl(startUrl);
		} catch (TimeoutException e){
            // Expected exception, when timeout occurs, move to next operations.
        }
		this.prepareJsErrorCollector();

		WebElement user = driver.findElement(By.id("user"));
		user.sendKeys("yuta");

		WebElement pass = driver.findElement(By.id("pass"));
		pass.sendKeys("maezawa");

		WebElement loginbtn = driver.findElement(By.id("loginbtn"));

		this.assertNoJsErrorObserved(); // before page transition
		
		loginbtn.click();
		
		String successful_logged_in_url = driver.getCurrentUrl();
		Assert.assertNotEquals(startUrl, successful_logged_in_url);
		
		
	}
	
}
