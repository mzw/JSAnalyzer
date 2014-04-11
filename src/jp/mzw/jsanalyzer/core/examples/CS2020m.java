package jp.mzw.jsanalyzer.core.examples;

import java.util.ArrayList;
import java.util.List;

import jp.mzw.jsanalyzer.config.FileExtension;
import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.core.Project;
import jp.mzw.jsanalyzer.formulator.adp.AjaxDesignPattern;
import jp.mzw.jsanalyzer.formulator.pp.Existence;
import jp.mzw.jsanalyzer.formulator.pp.PropertyPattern;
import jp.mzw.jsanalyzer.formulator.property.ACRetry;
import jp.mzw.jsanalyzer.formulator.property.AsyncComm;
import jp.mzw.jsanalyzer.formulator.property.Property;
import jp.mzw.jsanalyzer.formulator.property.SRWait;
import jp.mzw.jsanalyzer.formulator.property.UEHRegist;
import jp.mzw.jsanalyzer.serialize.model.Event;
import jp.mzw.jsanalyzer.serialize.model.FiniteStateMachine;
import jp.mzw.jsanalyzer.serialize.model.State;
import jp.mzw.jsanalyzer.serialize.model.Transition;
import jp.mzw.jsanalyzer.util.TextFileUtils;
import jp.mzw.jsanalyzer.verifier.Verifier;
import jp.mzw.jsanalyzer.verifier.modelchecker.NuSMV;
import jp.mzw.jsanalyzer.verifier.specification.Specification;

public class CS2020m extends Project {
	
	public static void main(String[] args) {
		Project project = CS2020m.getProject(CS2020m.INIT);
		Analyzer analyzer = new Analyzer(project);
		
		Verifier verifier = new Verifier(analyzer);
//		verifier.setup();
		
		List<Specification> specList = CS2020m.getSpecList(analyzer, verifier.getExtractedFSM());
		verifier.verifyIADP(specList);
	}
	
	private CS2020m(String projName, String projUrl) {
		super(projName, projUrl,
				CS2020m.setRuleFilenames(),
				"projects/project");
	}

	public static final int
		INIT = 0,
		MutatedError = 1;
	
	public static CS2020m getProject(int ver) {
		switch(ver) {
		case CS2020m.INIT:
			return new CS2020m("CS2020mError", "http://localhost/~yuta/research/cs/2020m/2020m.html");
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
		
		Property pAsyncComm = Property.getPropertyByNameAbbr("AsyncComm").clone();
		pAsyncComm.setTemplateVariables(NuSMV.genOr(fsm.getFuncIdList("jQuery.post")), NuSMV.genOr(fsm.getUserEventIdList(analyzer)), null, null);
		ret.add(new Specification(pAsyncComm));
		
		
		
		Property pUEHRegist_onload = Property.getPropertyByNameAbbr("UEHRegist").clone();
		pUEHRegist_onload.setTemplateVariables(NuSMV.genOr(fsm.getUserEventIdList(analyzer)), NuSMV.genOr(fsm.getEventIdList("onload")), null, null);
		ret.add(new Specification(pUEHRegist_onload));
		
		Property pUEHRegist_ready = Property.getPropertyByNameAbbr("UEHRegist").clone();
		pUEHRegist_ready.setTemplateVariables(NuSMV.genOr(fsm.getUserEventIdList(analyzer)), NuSMV.genOr(fsm.getEventIdList("ready")), null, null);
		ret.add(new Specification(pUEHRegist_ready));
		
		
		// to be mod
//		Property pUEHSingle = Property.getPropertyByNameAbbr("UEHSingle").clone();
//		pUEHSingle.setTemplateVariables(NuSMV.genOr(fsm.getUserEventIdList(analyzer)), NuSMV.genOr(fsm.getEventIdList("submit")), null, null);
//		ret.add(new Specification(pUEHSingle));
		
		

		Property pSeedRetrieval = Property.getPropertyByNameAbbr("SeedRetrieval").clone();
		pSeedRetrieval.setTemplateVariables(NuSMV.genOr(fsm.getFuncIdList("validateLogin")), NuSMV.genOr(fsm.getFuncIdList("getSeed")), null, null);
		ret.add(new Specification(pSeedRetrieval));
		


		Property pLFDisable = Property.getPropertyByNameAbbr("LFDisable").clone();
		pLFDisable.setTemplateVariables(
				NuSMV.genOr(fsm.getFuncIdList("window.location.replace ")),
				NuSMV.genOr(fsm.getEventIdList("submit")),
				null,
				null
				);
		ret.add(new Specification(pLFDisable));
		
		return ret;
	}
}
