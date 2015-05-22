package jp.mzw.jsanalyzer.core.examples;

import java.util.ArrayList;
import java.util.List;

import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.core.Project;
import jp.mzw.jsanalyzer.formulator.property.Property;
import jp.mzw.jsanalyzer.formulator.property.UEHRegist;
import jp.mzw.jsanalyzer.formulator.property.UEHSingle;
import jp.mzw.jsanalyzer.modeler.Modeler;
import jp.mzw.jsanalyzer.serialize.Serializer;
import jp.mzw.jsanalyzer.serialize.model.FiniteStateMachine;
import jp.mzw.jsanalyzer.verifier.Verifier;
import jp.mzw.jsanalyzer.verifier.modelchecker.NuSMV;
import jp.mzw.jsanalyzer.verifier.specification.Specification;

public class PhDExampleFaulty extends Project {

	public static void main(String[] args) {
		Project project = new PhDExampleFaulty();
		Analyzer analyzer = new Analyzer(project);
		
//		Modeler modeler = new Modeler(analyzer);
//		jp.mzw.jsanalyzer.modeler.model.fsm.FiniteStateMachine fsm = modeler.extract();
//		Serializer.serialze(analyzer, fsm);
		
//		Property.setOriginalPropertyList();
//		TextFileUtils.write("/Users/yuta/Sites/research/ex/iadp/", "properties.xml", Property.getPropertyListXML());
		
		Verifier verifier = new Verifier(analyzer);
////		verifier.setup();
// 		verifier.verifyIADP();
		List<Specification> specList = PhDExampleFaulty.getSpecList(analyzer, verifier.getExtractedFSM());
 		verifier.verifyIADP(specList);
	}

	public PhDExampleFaulty() {
		super("PhDExampleFaulty",
				"http://mzw.jp/yuta/research/ex/phd/example/faulty.html",
				FileDLerCorrect.setRuleFilenames(),
				"projects/project");
	}

	public static List<String> setRuleFilenames() {
		List<String> ret = Project.getDefaultRuleFilenames();
		
		ret.add("res/libRules/jquery.xml");
		
		return ret;
	}

	public static List<Specification> getSpecList(Analyzer analyzer, FiniteStateMachine fsm) {
		ArrayList<Specification> ret = new ArrayList<Specification>();

		/////
		// Fundamental properties
		/////
		
		/// UEHRegist
		UEHRegist pUEHRegist = new UEHRegist();
		pUEHRegist.setTemplateVariables(NuSMV.genOr(fsm.getUserEventIdList(analyzer)), NuSMV.genOr(fsm.getEventIdList("onload")));
		ret.add(new Specification(pUEHRegist));
		
		/// UEHSingle
		UEHSingle pUEHSingle = new UEHSingle();
		pUEHSingle.setTemplateVariables(NuSMV.genOr(fsm.getFuncIdList("addCart")), NuSMV.genOr(fsm.getEventIdList("onclick")), true);
		ret.add(new Specification(pUEHSingle));
		
		return ret;
	}
}
