package jp.mzw.jsanalyzer.core.cs;

import java.util.List;

import jp.mzw.jsanalyzer.core.Project;

public class Tudu extends Project {

	private Tudu(String projName, String projUrl) {
		super(projName, projUrl,
				Tudu.setRuleFilenames(),
				"projects/tudu");
	}

	public static final int
		Original = 0,
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

	public static Tudu getProject(int ver) {
		
		switch(ver) {
		case Tudu.Original:
			return new Tudu("Tudu.Original", "http://localhost:8080/tudu-dwr/welcome.action");
		}
		
		return null;
	}
	
	public static List<String> setRuleFilenames() {
		List<String> ret = Project.getDefaultRuleFilenames();
		
//		ret.add("res/libRules/jquery.xml");
		
		return ret;
	}
	
	///
	public static final String XPATH_LOGIN_ANCHOR = "//*[@id=\"page-header\"]/div/div/a";
}
