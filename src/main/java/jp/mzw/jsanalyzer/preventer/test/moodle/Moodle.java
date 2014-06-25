package jp.mzw.jsanalyzer.preventer.test.moodle;

public class Moodle {
	private static final String baseUrl = "http://localhost/~yuta/research/test/moodle/";
	
	///// Login
	public static final String login_acc_id = "username";
	public static final String login_acc_val_admin = "admin";
	public static final String login_acc_val_student = "iamstudent";
	public static final String login_pwd_id = "password";
	public static final String login_pwd_val_admin = "admin-Adm1n";
	public static final String login_pwd_val_student = "pwd.5tudenT";
	public static final String login_btn_id = "loginbtn";
	/**
	 * 
	 * @param ver
	 * @return
	 */
	public static String getLoginUrl(String ver) {
		return Moodle.baseUrl + ver + "/login/";
	}
	
	
	
	/// Course
	public static String getCourseUrl(String ver, int courseId) {
		return Moodle.baseUrl + ver + "/course/view.php?id=" + courseId;
	}
	
	
	public static final String course_xpath = "//*[@id=\"region-main\"]/div/ul/li/div/div[1]/h3/a";
	public static final String course_users_xpath = "//*[@id=\"settingsnav\"]/ul/li[1]/ul/li[3]/p/span";
	public static final String course_enrollment_methods_xpath = "//*[@id=\"settingsnav\"]/ul/li[1]/ul/li[3]/ul/li[2]/p/a";

	public static final String enrollment_method_enable_xpath = "//*[@id=\"region-main\"]/div/div/table/tbody/tr[3]/td[4]/a[2]";
	public static final String enrollment_method_enable_img_xpath = "//*[@id=\"region-main\"]/div/div/table/tbody/tr[3]/td[4]/a[2]/img";
	public static final String enrollment_method_edit_xpath = "//*[@id=\"region-main\"]/div/div/table/tbody/tr[3]/td[4]/a[3]";
	
	/// Setting self enrollment
	public static final String enrollment_status_id = "id_status";
	public static final String enrollment_password_id = "id_password";
	public static final String enrollment_password_val = "admin-Adm1n";
	
	/// Password masking
	public static final String password_mask_id = "id_passwordunmask";
}
