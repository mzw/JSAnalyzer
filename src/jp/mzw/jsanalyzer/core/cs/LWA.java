package jp.mzw.jsanalyzer.core.cs;

import java.util.ArrayList;
import java.util.List;

import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.core.Project;
import jp.mzw.jsanalyzer.formulator.property.Property;
import jp.mzw.jsanalyzer.modeler.Modeler;
import jp.mzw.jsanalyzer.preventer.Preventer;
import jp.mzw.jsanalyzer.serialize.Serializer;
import jp.mzw.jsanalyzer.serialize.model.FiniteStateMachine;
import jp.mzw.jsanalyzer.verifier.Verifier;
import jp.mzw.jsanalyzer.verifier.modelchecker.NuSMV;
import jp.mzw.jsanalyzer.verifier.specification.Specification;

public class LWA extends Project {
	

	public static void main(String[] args) {
		Project project = LWA.getProject(CS2020m.Original);
		Analyzer analyzer = new Analyzer(project);
		
		Modeler modeler = new Modeler(analyzer);
		jp.mzw.jsanalyzer.modeler.model.fsm.FiniteStateMachine fsm = modeler.extract();
		Serializer.serialze(analyzer, fsm);
		
//		Verifier verifier = new Verifier(analyzer);
//		verifier.setup();
//		List<Specification> specList = CS2020m.getSpecList(analyzer, verifier.getExtractedFSM());
//		verifier.verifyIADP(specList);
		
//		Preventer preventer = new Preventer(analyzer);
////		CS2020m.insertDelay(analyzer, preventer, CS2020m.UEHRegist);
////		CS2020m.insertDelay(analyzer, preventer, CS2020m.SeedRetrieve);
//		CS2020m.insertDelay(analyzer, preventer, CS2020m.ValidateLogin);
		
	}
	


	private LWA(String projName, String projUrl) {
		super(projName, projUrl,
				LWA.setRuleFilenames(),
				"projects/project");
	}

	public static final int
		Original = 0,
		UEHRegist = 1,
		SeedRetrieve = 2,
		ValidateLogin = 3;

	public static LWA getProject(int ver) {
		switch(ver) {
		case LWA.Original:
			return new LWA("LWA.Original", "http://mzw.jp/test/wp/index.php");
		}
		return null;
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
		
		/// AyncComm
		// Implemented but not called from anywhere, i.e., function va(id) {...}
		Property pAsyncComm_get = Property.getPropertyByNameAbbr("AsyncComm").clone();
		pAsyncComm_get.setTemplateVariables(NuSMV.genOr(fsm.getFuncIdList("jQuery.get")), NuSMV.genOr(fsm.getUserEventIdList(analyzer)), null, null);
		ret.add(new Specification(pAsyncComm_get));

		Property pAsyncComm_post = Property.getPropertyByNameAbbr("AsyncComm").clone();
		pAsyncComm_post.setTemplateVariables(NuSMV.genOr(fsm.getFuncIdList("jQuery.post")), NuSMV.genOr(fsm.getUserEventIdList(analyzer)), null, null);
		ret.add(new Specification(pAsyncComm_post));
		
		/// ACRetry
		// Could not find any failure events
		
		/// SRWait
		// Same as SeedRetrieve
		
		/// UEHRegist
		Property pUEHRegist_ready = Property.getPropertyByNameAbbr("UEHRegist").clone();
		pUEHRegist_ready.setTemplateVariables(NuSMV.genOr(fsm.getUserEventIdList(analyzer)), NuSMV.genOr(fsm.getEventIdList("ready")), null, null);
		ret.add(new Specification(pUEHRegist_ready));
		
		/// UEHSingle
		// Same as LFDisable
		

		/////
		/// Additional properties
		/////
		
		/// Properties from Direct Login Pattern
		/// b/c keyword "login_controller.js" at NerdyData
		///
		
		/// SeedRetrieval
		Property pSeedRetrieval = Property.getPropertyByNameAbbr("SeedRetrieval").clone();
		pSeedRetrieval.setTemplateVariables(NuSMV.genOr(fsm.getFuncIdList("validateLogin")), NuSMV.genOr(fsm.getFuncIdList("getSeed")), null, null);
		ret.add(new Specification(pSeedRetrieval));
		
		/// LFDisable
		String loginEventId = fsm.getEventId("submit", 21, 15);
		String succLoginStateId = fsm.getFuncId("window.location.replace", 34, 0);
		String P = Property.genTemplateVariableP(NuSMV.genExpr(succLoginStateId), NuSMV.genExpr(loginEventId), fsm);
		
		Property pLFDisable = Property.getPropertyByNameAbbr("LFDisable").clone();
		pLFDisable.setTemplateVariables(P, null, null, null);
		ret.add(new Specification(pLFDisable));
		
		return ret;
	}
}
