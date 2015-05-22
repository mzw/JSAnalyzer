package jp.mzw.jsanalyzer.revmutator.test.pwdunmask;

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class ExampleTestSet {
//	protected String url = "http://localhost/yuta/research/example/pwdunmask/0.init/";
	protected String url = "http://localhost/yuta/research/example/pwdunmask/0.1.mut/";
	protected static final int TIMEOUT = 3; // second

	protected static final String filepath_to_upload = "/Users/yuta/Documents/paper/icse15.pdf";
	protected static final String password_to_upload = "top_conf";

	@Test
	public void testSubmit() {
		WebDriver driver = new FirefoxDriver();
		driver.get(url);
		driver.findElement(By.id("paper")).sendKeys(filepath_to_upload);
		driver.findElement(By.id("pwd")).sendKeys(password_to_upload);
//		driver.findElement(By.id("pwdunmask")).click();
		driver.findElement(By.id("submit")).click();
		
        driver.quit();
	}
}
