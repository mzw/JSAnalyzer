package jp.mzw.jsanalyzer.core.cs;

import java.util.ArrayList;
import java.util.List;

import jp.mzw.jsanalyzer.config.FileExtension;
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

public class UCDChina extends Project {
	
	public static void main(String[] args) {
		Project project = UCDChina.getProject(UCDChina.Debug1);
		Analyzer analyzer = new Analyzer(project);
		
//		Modeler modeler = new Modeler(analyzer);
//		jp.mzw.jsanalyzer.modeler.model.fsm.FiniteStateMachine fsm = modeler.extract();
//		Serializer.serialze(analyzer, fsm);
		
		Verifier verifier = new Verifier(analyzer);
//		verifier.setup();
		List<Specification> specList = UCDChina.getSpecList(analyzer, verifier.getExtractedFSM());
		verifier.verifyIADP(specList);

//		Preventer preventer = new Preventer(analyzer);
////		UCDChina.insertDelay(analyzer, preventer, UCDChina.UEHRegist);
//		UCDChina.insertDelay(analyzer, preventer, UCDChina.FDValid);
	}
	
	private UCDChina(String projName, String projUrl) {
		super(projName, projUrl,
				UCDChina.setRuleFilenames(),
				"projects/project");
	}

	public static final int
		Original = 0,
		UEHRegist = 1,
		FDValid = 2,
		///
		Debug = 10,
		Debug1 = 11;
	
	public static UCDChina getProject(int ver) {
		switch(ver) {
		case UCDChina.Original:
			return new UCDChina("UCDChina.Original", "http://localhost/~yuta/research/cs/ucdchina/0.origin/ucdchina.html");
		case UCDChina.UEHRegist:
			return new UCDChina("UCDChina.UEHRegist", "http://localhost/~yuta/research/cs/ucdchina/0.origin/ucdchina.html.ver.1.html");
		case UCDChina.FDValid:
			return new UCDChina("UCDChina.FDValid", "http://localhost/~yuta/research/cs/ucdchina/0.origin/ucdchina.html.ver.2.html");
		case UCDChina.Debug:
			return new UCDChina("UCDChina.Debug", "http://localhost/~yuta/research/cs/ucdchina/10.debug/ucdchina.html");
		case UCDChina.Debug1:
			return new UCDChina("UCDChina.Debug1", "http://localhost/~yuta/research/cs/ucdchina/10.debug/ucdchina.html.ver.1.html");
		}
		return null;
	}

	public static void insertDelay(Analyzer analyzer, Preventer preventer, int ver) {
		
		long start = System.currentTimeMillis();
		
		/// Preliminary
		String base_dir = "/Users/yuta/Sites/research/cs/ucdchina/0.origin/";
		/// files
		String html_filename = "ucdchina.html";
		String js_filename = "./ucdchina_files/validate.js";
		
		String postfix = ".ver."; // should add extension at tail
		String output_filename = null; // should set "filename.inserted.property.ext"
		
		int html_offset = -1;
		String html_code = null;
		
		
		switch(ver) {
		case UCDChina.Original:
			break;
		case UCDChina.UEHRegist:
			
			html_offset = preventer.getHTMLScriptOffset(base_dir, html_filename, "./ucdchina_files/validate.js");
			html_code = preventer.getMutatedHTMLCode(base_dir, html_filename, html_offset);
			/// Writes
			output_filename = html_filename + postfix + UCDChina.UEHRegist + FileExtension.HTML;
			TextFileUtils.write(base_dir, output_filename, html_code);
			
			break;
		case UCDChina.FDValid:
			/// Executable fault on CSS-disabled browser
			break;
		}

		long end = System.currentTimeMillis();
		System.out.println("Insertion time: " + (end - start));
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
//		Property pAsyncComm_get = Property.getPropertyByNameAbbr("AsyncComm").clone();
//		pAsyncComm_get.setTemplateVariables(NuSMV.genOr(fsm.getFuncIdList("jQuery.get")), NuSMV.genOr(fsm.getUserEventIdList(analyzer)), null, null);
//		ret.add(new Specification(pAsyncComm_get));

		Property pAsyncComm_post = Property.getPropertyByNameAbbr("AsyncComm").clone();
		pAsyncComm_post.setTemplateVariables(NuSMV.genOr(fsm.getFuncIdList("jQuery.post")), NuSMV.genOr(fsm.getUserEventIdList(analyzer)), null, null);
		ret.add(new Specification(pAsyncComm_post));
		
		/// ACRetry
		// Could not find any failure events
		
		/// SRWait
		// response: used only in callback
		
		/// UEHRegist
		Property pUEHRegist_onload = Property.getPropertyByNameAbbr("UEHRegist").clone();
		pUEHRegist_onload.setTemplateVariables(NuSMV.genOr(fsm.getUserEventIdList(analyzer)), NuSMV.genOr(fsm.getEventIdList("ready")), null, null);
		ret.add(new Specification(pUEHRegist_onload));
		
		/// UEHSingle
		// Could not determine
		
		/////
		/// Additional properties
		/////
		
		/// Properties from Live Form Pattern
		/// b/c keyword "onblur=checkInput" at NerdyData
		
		/// FDValid
		/// for original 
//		String checkInput = NuSMV.genOr(fsm.getFuncIdList("checkInput"));
//		String checkInput = NuSMV.genExpr(fsm.getFuncId("checkInput", 1, 0));
//		String sendMail = NuSMV.genExpr(fsm.getFuncId("sendMail", 1, 0));
		
		/// for debug
//		String checkInput = NuSMV.genExpr(fsm.getFuncId("checkInput", 8, 9));
//		String sendMail = NuSMV.genExpr(fsm.getFuncId("sendMail", 66, 7));

		/// for debug1
		String checkInput = NuSMV.genExpr(fsm.getFuncId("checkInput", 8, 9));
		String sendMail = NuSMV.genExpr(fsm.getFuncId("sendMail", 66, 7));
		
		
		Property pFDValid = Property.getPropertyByNameAbbr("FDValid").clone();
		pFDValid.setTemplateVariables(sendMail, checkInput, null, null);
		ret.add(new Specification(pFDValid));

		
		
		return ret;
	}
}
