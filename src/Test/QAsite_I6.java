package Test;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static com.thoughtworks.selenium.SeleneseTestCase.assertEquals;
import static junit.framework.Assert.fail;

public class QAsite_I6 extends WebAppTestBase {
	
	@Test
	public void testGoodClick() {
		gotoUrl(TestConstants.QAsite_I6_URL);
		waitAndClickElementLocated(By.id("username"));
		waitAndClickElementLocated(By.id("password"));
		driver.findElement(By.id("username")).sendKeys("hoge");
		waitAndClickElementLocated(By.id("password"));
		waitAndClickElementLocated(By.id("good"));
	}

}
