package jp.mzw.jsanalyzer.preventer.test.cs2020m;

import static junit.framework.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import jp.mzw.jsanalyzer.core.Project;
import jp.mzw.jsanalyzer.core.cs.CS2020m;
import jp.mzw.jsanalyzer.preventer.test.WebAppTestBase;

public class SeedRetrieve extends WebAppTestBase {

	@SuppressWarnings("deprecation")
	@Test
	public void testSeedRetrieve() {
		
//		Project project = CS2020m.getProject(CS2020m.Original);
		Project project = CS2020m.getProject(CS2020m.SeedRetrieve);
		
		final String startUrl = project.getUrl();
		
		try {
			driver.manage().timeouts().pageLoadTimeout(2000, TimeUnit.MILLISECONDS);
			gotoUrl(startUrl);
			this.prepareJsErrorCollector();
		} catch (TimeoutException e){
            // Expected exception, when timeout occurs, move to next operations.
        }

		WebElement user = driver.findElement(By.id("user"));
		user.sendKeys("yuta");

		WebElement pass = driver.findElement(By.id("pass"));
		pass.sendKeys("maezawa");

		WebElement loginbtn = driver.findElement(By.id("loginbtn"));
		loginbtn.click();

		WebElement msg = driver.findElement(By.id("msg"));
		String text = msg.getText();
		
		if(!"Hello, yuta".equals(text)) {
			fail();
		}
		
		this.assertNoJsErrorObserved();
		
	}
	
}
