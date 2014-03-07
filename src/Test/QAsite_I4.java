package Test;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import static junit.framework.Assert.fail;

public class QAsite_I4 extends WebAppTestBase {
	
	@SuppressWarnings("deprecation")
	@Test
	public void testMultipleLogin() {
		gotoUrl(TestConstants.QAsite_I4_URL);
		waitAndClickElementLocated(By.id("login"));
		try {
			waitAndClickElementLocated(By.id("login"));
			fail();
		} catch (WebDriverException e) {
			// Expected exception.
		}
	}

}
