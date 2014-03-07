package Test;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static com.thoughtworks.selenium.SeleneseTestCase.assertEquals;
import static junit.framework.Assert.fail;

public class QAsite_init extends WebAppTestBase {
	
	@Test
	public void testMultipleLogin() {
		gotoUrl(TestConstants.QAsite_URL);
		waitAndClickElementLocated(By.id("login"));
		try {
			waitAndClickElementLocated(By.id("login"));
			fail();
		} catch (WebDriverException e) {
			// Expected exception.
		}
	}

}
