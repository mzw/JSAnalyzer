package Test;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static com.thoughtworks.selenium.SeleneseTestCase.assertEquals;
import static junit.framework.Assert.fail;

public class QAsite_I8_mutated extends WebAppTestBase {
	
	@Test
    public void tryLogin() {
        gotoUrl(TestConstants.QAsite_I8_MUTATED_URL);
        waitAndClickElementLocated(By.id("username"));
        waitAndClickElementLocated(By.id("password"));
        driver.findElement(By.id("username")).sendKeys("g");
        driver.findElement(By.id("username")).sendKeys("t");
        driver.findElement(By.id("login")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout")));
    }

}
