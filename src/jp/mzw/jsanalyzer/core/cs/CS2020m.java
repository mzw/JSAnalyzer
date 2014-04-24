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
import jp.mzw.jsanalyzer.util.TextFileUtils;
import jp.mzw.jsanalyzer.verifier.Verifier;
import jp.mzw.jsanalyzer.verifier.modelchecker.NuSMV;
import jp.mzw.jsanalyzer.verifier.specification.Specification;

public class CS2020m extends Project {
	
	public static void main(String[] args) {
		Project project = CS2020m.getProject(CS2020m.Original);
		Analyzer analyzer = new Analyzer(project);
		
//		Modeler modeler = new Modeler(analyzer);
//		jp.mzw.jsanalyzer.modeler.model.fsm.FiniteStateMachine fsm = modeler.extract();
//		Serializer.serialze(analyzer, fsm);
		
//		Verifier verifier = new Verifier(analyzer);
////		verifier.setup();
//		List<Specification> specList = CS2020m.getSpecList(analyzer, verifier.getExtractedFSM());
//		verifier.verifyIADP(specList);
		
		Preventer preventer = new Preventer(analyzer);
		CS2020m.insertDelay(analyzer, preventer, CS2020m.SeedRetrieve);
		
	}
	
	private CS2020m(String projName, String projUrl) {
		super(projName, projUrl,
				CS2020m.setRuleFilenames(),
				"projects/project");
	}

	public static final int
		Original = 0,
		UEHRegist = 1,
		SeedRetrieve = 2;
	
	public static CS2020m getProject(int ver) {
		switch(ver) {
		case CS2020m.Original:
			return new CS2020m("2020m.Original", "http://localhost/~yuta/research/cs/2020m/0.origin/2020m.html");
		case CS2020m.UEHRegist:
			return new CS2020m("2020m.UEHRegist", "http://localhost/~yuta/research/cs/2020m/1.uehregist/2020m.html");
		case CS2020m.SeedRetrieve:
			return new CS2020m("2020m.SeedRetrieve", "http://localhost/~yuta/research/cs/2020m/2.seedretrieve/2020m.html");
		}
		return null;
	}
	
	public static List<String> setRuleFilenames() {
		List<String> ret = Project.getDefaultRuleFilenames();
		
		ret.add("res/libRules/jquery.xml");
		
		return ret;
	}
	
	public static void insertDelay(Analyzer analyzer, Preventer preventer, int ver) {
		
		long start = System.currentTimeMillis();
		
		switch(ver) {
		case CS2020m.Original:
			break;
		case CS2020m.UEHRegist:
			String uehregist_dir = "/Users/yuta/Sites/research/cs/2020m/0.origin";
			String uehregist_infile = "2020m.html";
			String uehregist_outfile = "inserted." + uehregist_infile;
			
			/// for HTML code
			/// Known: onclick -> forgotPasswordClick -> login_controller.js
			String uehregist_target = "2020m_files/login_controller.js";
			int uehregist_offset = preventer.getHTMLScriptOffset(uehregist_dir, uehregist_infile, uehregist_target);
			String uehregist_content = preventer.getMutatedHTMLCode(uehregist_dir, uehregist_infile, uehregist_offset);
			TextFileUtils.write(uehregist_dir, uehregist_outfile, uehregist_content);
			
			break;
		case CS2020m.SeedRetrieve:
			String sd_dir = "/Users/yuta/Sites/research/cs/2020m/0.origin/";
			String sd_infile = "2020m_files/login_controller.js";
			String sd_outfile = "inserted." + sd_infile;
			
			
			/// for JS code
			/// Known: $.post("include/login.php", { task: 'getseed' }, function...
			int pos = 899;
			String inserted_jscode = preventer.getMutatedJSCode(sd_dir, sd_infile, pos);
			System.out.println(inserted_jscode);
			
			break;
		}

		long end = System.currentTimeMillis();
		System.out.println("Insertion time: " + (end - start));
	}
	
	public static List<Specification> getSpecList(Analyzer analyzer, FiniteStateMachine fsm) {
		ArrayList<Specification> ret = new ArrayList<Specification>();
		

		/////
		// Fundamental properties
		/////
		
		/// AyncComm
		// Implemented but not called from anywhere, i.e., function va(id) {...}
//		Property pAsyncComm_get = Property.getPropertyByNameAbbr("AsyncComm").clone();
//		pAsyncComm_get.setTemplateVariables(NuSMV.genOr(fsm.getFuncIdList("jQuery.get")), NuSMV.genOr(fsm.getUserEventIdList(analyzer)), null, null);
//		ret.add(new Specification(pAsyncComm_get));

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
