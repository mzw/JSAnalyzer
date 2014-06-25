package jp.mzw.jsanalyzer.core.examples;

import java.util.List;

import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.core.Project;
import jp.mzw.jsanalyzer.modeler.Modeler;
import jp.mzw.jsanalyzer.serialize.Serializer;
import jp.mzw.jsanalyzer.verifier.Verifier;

public class QAsiteError extends Project {
	
	public static void main(String[] args) {
		Project project = new QAsiteError();
		Analyzer analyzer = new Analyzer(project);
		
//		Modeler modeler = new Modeler(analyzer);
//		jp.mzw.jsanalyzer.modeler.model.fsm.FiniteStateMachine fsm = modeler.extract();
//		Serializer.serialze(analyzer, fsm);
		
		Verifier verifier = new Verifier(analyzer);
//		verifier.setup();
		verifier.verifyIADP();
	}
	
	public QAsiteError() {
		super("QAsite",
				"http://localhost/~yuta/research/ex/QAsite/error/index.html",
				setRuleFilenames(),
				"projects/project");
	}

	public static List<String> setRuleFilenames() {
		List<String> ret = Project.getDefaultRuleFilenames();
		
		ret.add("res/libRules/prototype.xml");
		
		return ret;
	}
	
}
