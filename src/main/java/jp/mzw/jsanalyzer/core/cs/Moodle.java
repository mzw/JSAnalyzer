package jp.mzw.jsanalyzer.core.cs;

import java.util.List;

import jp.mzw.jsanalyzer.core.Project;

public class Moodle extends Project {

	private Moodle(String projName, String projUrl) {
		super(projName, projUrl,
				Moodle.setRuleFilenames(),
				"projects/moodle");
	}

	public static final int
		Original_2_3_0 = 0,
		Original_2_3_1 = 1,
		Original_2_3_2 = 2,
		Original_2_3_3 = 3,
		Original_2_3_4 = 4,
		Original_2_3_5 = 5,
		Original_2_3_6 = 6,
		Original_2_3_7 = 7,
		Original_2_3_8 = 8,
		Original_2_3_9 = 9,
		Original_2_3_10 = 10,
		Original_2_3_11 = 11;

	public static Moodle getProject(int ver) {
//		
//		switch(ver) {
//		case Moodle.Original_2_3_0:
//			return new Moodle("Moodle.2.3.0.Original", "http://maezawa.honiden.nii.ac.jp/yuta/research/test/moodle/2.3.0/");
//		case Moodle.Original_2_3_1:
//			return new Moodle("Moodle.2.3.1.Original", "http://maezawa.honiden.nii.ac.jp/yuta/research/test/moodle/2.3.1/");
//		case Moodle.Original_2_3_2:
//			return new Moodle("Moodle.2.3.2.Original", "http://maezawa.honiden.nii.ac.jp/yuta/research/test/moodle/2.3.2/");
//		case Moodle.Original_2_3_3:
//			return new Moodle("Moodle.2.3.3.Original", "http://maezawa.honiden.nii.ac.jp/yuta/research/test/moodle/2.3.3/");
//		}
		
//		return new Moodle(getProjectName(ver), getUrl(ver));

		return new Moodle("Moodle.2.3.0.Original", "http://maezawa.honiden.nii.ac.jp/yuta/research/test/moodle/moodle-v2.3.0/login/");
	}
	public static String getProjectName(int ver) {
		return "Moodle.2.3." + ver + ".Original";
	}
	public static String getUrl(int ver) {
//		return "http://maezawa.honiden.nii.ac.jp/yuta/research/test/moodle/moodle-v2.3." + ver + "/";
		return "http://localhost/~yuta/research/test/moodle/2.3." + ver + "/login/";
	}
	
	public static List<String> setRuleFilenames() {
		List<String> ret = Project.getDefaultRuleFilenames();
		
//		ret.add("res/libRules/jquery.xml");
		
		return ret;
	}
	
	///
	public static final String XPATH_LOGIN_ANCHOR = "//*[@id=\"page-header\"]/div/div/a";
}
