package jp.mzw.jsanalyzer.core.examples;

import java.util.List;

import jp.mzw.jsanalyzer.core.Project;

public class FileDLerError extends Project {

	public FileDLerError() {
		super("FileDLerError",
				"http://mzw.jp/research/ex/fd/motivatingExample_fault.html",
				FileDLerCorrect.setRuleFilenames(),
				"projects/project");
	}
	
	public static List<String> setRuleFilenames() {
		List<String> ret = Project.getDefaultRuleFilenames();
		
		ret.add("res/libRules/prototype.xml");
		
		return ret;
	}
}
