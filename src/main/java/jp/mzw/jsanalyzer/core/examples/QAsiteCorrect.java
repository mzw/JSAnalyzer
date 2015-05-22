package jp.mzw.jsanalyzer.core.examples;

import java.util.ArrayList;
import java.util.List;

import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.core.Project;
import jp.mzw.jsanalyzer.formulator.property.Property;
import jp.mzw.jsanalyzer.formulator.property.SRWait;
import jp.mzw.jsanalyzer.formulator.property.UEHRegist;
import jp.mzw.jsanalyzer.formulator.property.UEHSingle;
import jp.mzw.jsanalyzer.modeler.Modeler;
import jp.mzw.jsanalyzer.serialize.Serializer;
import jp.mzw.jsanalyzer.serialize.model.FiniteStateMachine;
import jp.mzw.jsanalyzer.verifier.Verifier;
import jp.mzw.jsanalyzer.verifier.modelchecker.NuSMV;
import jp.mzw.jsanalyzer.verifier.specification.Specification;

public class QAsiteCorrect extends Project {

	public static void main(String[] args) {
		Project project = new QAsiteCorrect();
		Analyzer analyzer = new Analyzer(project);
		
		Modeler modeler = new Modeler(analyzer);
		jp.mzw.jsanalyzer.modeler.model.fsm.FiniteStateMachine fsm = modeler.extract();
		Serializer.serialze(analyzer, fsm);
		
		Verifier verifier = new Verifier(analyzer);
//		verifier.setup();
//		verifier.verifyIADP();
		List<Specification> specList = QAsiteCorrect.getSpecList(analyzer, verifier.getExtractedFSM());
 		verifier.verifyIADP(specList);
	}
	
	public QAsiteCorrect() {
		super("QAsiteCorrect",
//				"http://mzw.jp/yuta/research/ex/QAsite/correct/index.html",
				"http://localhost/~yuta/research/ex/QAsite/correct/index.html",
				setRuleFilenames(),
				"projects/project");
	}

	public static List<String> setRuleFilenames() {
		List<String> ret = Project.getDefaultRuleFilenames();
		
		ret.add("res/libRules/prototype.xml");
		
		return ret;
	}

	public static List<Specification> getSpecList(Analyzer analyzer, FiniteStateMachine fsm) {
		ArrayList<Specification> ret = new ArrayList<Specification>();
		
		/// SRWait
		SRWait pSRWait = new SRWait();
		pSRWait.setTemplateVariables(NuSMV.genOr(fsm.getFuncIdList("onGood")), NuSMV.genOr(fsm.getFuncIdList("successLogin")));
		ret.add(new Specification(pSRWait));
		
		/// UEHRegist
		UEHRegist pUEHRegist = new UEHRegist();
		pUEHRegist.setTemplateVariables(NuSMV.genOr(fsm.getUserEventIdList(analyzer)), NuSMV.genOr(fsm.getEventIdList("onload")));
		ret.add(new Specification(pUEHRegist));
		
		/// UEHSingle
		UEHSingle pUEHSingle = new UEHSingle();
		pUEHSingle.setTemplateVariables(NuSMV.genOr(fsm.getFuncIdList("validateLogin")), NuSMV.genOr(fsm.getEventIdList("onmousedown")), true);
		ret.add(new Specification(pUEHSingle));

		Property pSeedRetrieval = Property.getPropertyByNameAbbr("SeedRetrieval").clone();
		pSeedRetrieval.setTemplateVariables(NuSMV.genOr(fsm.getFuncIdList("validateLogin")), NuSMV.genOr(fsm.getFuncIdList("handleHttpGetSeed")), null, null);
		ret.add(new Specification(pSeedRetrieval));

		Property pLFDisable = Property.getPropertyByNameAbbr("LFDisable").clone();
		String ppLFDisable = Property.genTemplateVariableP(NuSMV.genOr(fsm.getFuncIdList("successLogin")), NuSMV.genOr(fsm.getEventIdList("onmousedown")), fsm);
		pLFDisable.setTemplateVariables(ppLFDisable, null, null, null);
		ret.add(new Specification(pLFDisable));
		
		return ret;
	}
}
