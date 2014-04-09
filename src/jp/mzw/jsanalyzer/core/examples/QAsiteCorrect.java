package jp.mzw.jsanalyzer.core.examples;

import java.util.List;

import jp.mzw.jsanalyzer.core.Project;

public class QAsiteCorrect extends Project {
	
	public QAsiteCorrect() {
		super("QAsiteCorrect",
				"http://mzw.jp/research/ex/QAsite/correct/index.html",
				setRuleFilenames(),
				"projects/project");
	}

	public static List<String> setRuleFilenames() {
		List<String> ret = Project.getDefaultRuleFilenames();
		
		ret.add("res/libRules/prototype.xml");
		
		return ret;
	}

}
