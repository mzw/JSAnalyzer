package jp.mzw.jsanalyzer.core.examples;

import java.util.List;

import jp.mzw.jsanalyzer.core.Project;

public class FileDLerRetry extends Project {

	public FileDLerRetry() {
		super("FileDLerRetry",
				"http://mzw.jp/research/ex/fd/motivatingExample.retry.html",
				FileDLerCorrect.setRuleFilenames(),
				"projects/test2");
	}
	
	public static List<String> setRuleFilenames() {
		List<String> ret = Project.getDefaultRuleFilenames();
		
		ret.add("res/libRules/prototype.xml");
		
		return ret;
	}
}
