package jp.mzw.jsanalyzer.preventer.test.moodle;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import jp.mzw.jsanalyzer.preventer.test.WebAppTestBase;
import static org.junit.Assert.fail;

public class MDL_33560_0_init extends WebAppTestBase {

	@Test
	public void test() {
		final String ver = "2.3.1";
		final int courseId = 2;
		
		/// 0. Login
		/// As admin
		gotoUrl(Moodle.getLoginUrl(ver));
		
		waitAndSendKeysToElementLocated(By.id(Moodle.login_acc_id), Moodle.login_acc_val_admin);
		waitAndSendKeysToElementLocated(By.id(Moodle.login_pwd_id), Moodle.login_pwd_val_admin);
		waitAndClickElementLocated(By.id(Moodle.login_btn_id));
		sleep(1000); /// Page transition
		
		/// 1. Go to any course
		waitAndClickElementLocated(By.xpath(Moodle.course_xpath));
		sleep(1000);

		waitAndClickElementLocated(By.xpath(Moodle.course_users_xpath));
		waitAndClickElementLocated(By.xpath(Moodle.course_enrollment_methods_xpath));
		sleep(1000);
		
		/// 2. Enable self enrollment
		WebElement enrollment_method_enable_img = driver.findElement(By.xpath(Moodle.enrollment_method_enable_img_xpath));
		String enable_status = enrollment_method_enable_img.getAttribute("alt");
		if("Disable".equals(enable_status)) { // make it disable, meaning currently enable
			// NOP
		} else {
			waitAndClickElementLocated(By.xpath(Moodle.enrollment_method_enable_xpath));
			sleep(1000);
		}
		
		/// 3. Click to edit the self enrollment settings
		waitAndClickElementLocated(By.xpath(Moodle.enrollment_method_edit_xpath));
		sleep(1000);
		
		/// 4. Enter a password
		///// Allow self enrollments
		Select enrollment_status_select = new Select(driver.findElement(By.id(Moodle.enrollment_status_id)));
		enrollment_status_select.selectByVisibleText("Yes");
		///// Enter
		waitAndClearKeysToElementLocated(By.id(Moodle.enrollment_password_id));
		waitAndSendKeysToElementLocated(By.id(Moodle.enrollment_password_id), Moodle.enrollment_password_val);
		
		/// 5. Make sure you can tick/untick 'Unmask' to show/hide the password
		String pre_mask_status = driver.findElement(By.id(Moodle.enrollment_password_id)).getAttribute("type"); /// "text" or "password"
		String cur_mask_status = null;

		/// Tick or (Untick)
		waitAndClickElementLocated(By.id(Moodle.password_mask_id));
		cur_mask_status = driver.findElement(By.id(Moodle.enrollment_password_id)).getAttribute("type"); /// get
		if(pre_mask_status.equals(cur_mask_status)) {
			fail("Do not work: show/hide the password");
		}
		pre_mask_status = cur_mask_status; /// update
		
		/// Untick (or tick)
		waitAndClickElementLocated(By.id(Moodle.password_mask_id));
		cur_mask_status = driver.findElement(By.id(Moodle.enrollment_password_id)).getAttribute("type"); /// get
		if(pre_mask_status.equals(cur_mask_status)) {
			fail("Do not work: show/hide the password");
		}
		pre_mask_status = cur_mask_status; /// update
	}
	
	
}
