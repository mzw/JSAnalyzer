package jp.mzw.jsanalyzer.core.examples;

import java.util.List;

import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.core.Project;
import jp.mzw.jsanalyzer.formulator.property.Property;
import jp.mzw.jsanalyzer.modeler.Modeler;
import jp.mzw.jsanalyzer.serialize.Serializer;
import jp.mzw.jsanalyzer.util.TextFileUtils;
import jp.mzw.jsanalyzer.verifier.Verifier;

public class SWSError extends Project {

	public static void main(String[] args) {
		Project project = new SWSError();
		Analyzer analyzer = new Analyzer(project);
		
		Modeler modeler = new Modeler(analyzer);
		jp.mzw.jsanalyzer.modeler.model.fsm.FiniteStateMachine fsm = modeler.extract();
		Serializer.serialze(analyzer, fsm);
		
//		Property.setOriginalPropertyList();
//		TextFileUtils.write("/Users/yuta/Sites/research/ex/iadp/", "properties.xml", Property.getPropertyListXML());
		
		Verifier verifier = new Verifier(analyzer);
//		verifier.setup();
 		verifier.verifyIADP();
	}

	public SWSError() {
		super("SWSError",
				"http://mzw.jp/research/ex/tse_sws/fault_quick.html",
				setRuleFilenames(),
				"projects/project");
	}
	
	public static List<String> setRuleFilenames() {
		List<String> ret = Project.getDefaultRuleFilenames();
		
		ret.add("res/libRules/jquery.xml");
		
		return ret;
	}
}
