package jp.mzw.jsanalyzer.core.examples;

import java.util.List;

import jp.mzw.jsanalyzer.core.Project;

public class FileDLerCorrect extends Project {

	public FileDLerCorrect() {
		super("FileDLerCorrect",
				"http://mzw.jp/research/ex/fd/motivatingExample.html",
				FileDLerCorrect.setRuleFilenames(),
				"projects/project");
	}
	
	public static List<String> setRuleFilenames() {
		List<String> ret = Project.getDefaultRuleFilenames();
		
		ret.add("res/libRules/prototype.xml");
		
		return ret;
	}
}
