package jp.mzw.jsanalyzer.verifier;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import jp.mzw.jsanalyzer.config.FileExtension;
import jp.mzw.jsanalyzer.config.FilePath;
import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.core.examples.*;
import jp.mzw.jsanalyzer.formulator.IADPFormulator;
import jp.mzw.jsanalyzer.formulator.adp.AjaxDesignPattern;
import jp.mzw.jsanalyzer.formulator.pp.Existence;
import jp.mzw.jsanalyzer.formulator.pp.PropertyPattern;
import jp.mzw.jsanalyzer.formulator.property.AsyncComm;
import jp.mzw.jsanalyzer.formulator.property.Property;
import jp.mzw.jsanalyzer.serialize.model.FiniteStateMachine;
import jp.mzw.jsanalyzer.serialize.model.State;
import jp.mzw.jsanalyzer.util.StringUtils;
import jp.mzw.jsanalyzer.util.TextFileUtils;
import jp.mzw.jsanalyzer.verifier.modelchecker.ModelChecker;
import jp.mzw.jsanalyzer.verifier.modelchecker.NuSMV;
import jp.mzw.jsanalyzer.verifier.modelchecker.Spin;
import jp.mzw.jsanalyzer.verifier.specification.Specification;
import jp.mzw.jsanalyzer.xml.XMLTag;

public class Verifier {
	
	public static void main(String[] args) {
		System.out.println("==============================");
		System.out.println("Running JSAnalyzer (Formulator and Verifier)");
		System.out.println("These modules are also called as <JSVerifier>");
		System.out.println("Visit: http://jsanalyzer.mzw.jp/about/jsverifier/");
		System.out.println((new Date()).toString());
		System.out.println("==============================");
		

		System.out.println("Preparing your project...");
//		Analyzer analyzer = new Analyzer("projects/test2/project.xml");
		Analyzer analyzer = new Analyzer(new QAsiteError());
//		Analyzer analyzer = new Analyzer(new QAsiteCorrect());
//		Analyzer analyzer = new Analyzer(new FileDLerError());
//		Analyzer analyzer = new Analyzer(new FileDLerCorrect());
//		Analyzer analyzer = new Analyzer(new FileDLerRetry());
//		Analyzer analyzer = new Analyzer(new SWSError());
//		Analyzer analyzer = new Analyzer(new LoginDemo());
//		Analyzer analyzer = new Analyzer(new LWA());

		
		Verifier verifier = new Verifier(analyzer);
		/// 1: Setup
		/// 2: Enter IADP info
//		verifier.setup(); // Gives URL where users enter IADP info
		/// 3: Verify
		verifier.verifyIADP();

		System.out.println("==============================");
		System.out.println("See you again!");
		System.out.println("==============================");
	}
	
	
	public void verifyIADP() {
		System.out.println("Verifying...");
		long start = System.currentTimeMillis();
		/// start

		NuSMV nusmv = new NuSMV(this.mFSM, this.mAnalyzer);
		List<Property> propList = this.readIADPInfo(this.mFSM);
		ArrayList<Specification> specList = new ArrayList<Specification>();
		/// Generates specifications based on properties
		for(Property prop : propList) {
			Specification spec = new Specification(prop);
			specList.add(spec);
		}
		/// Verifies
		for(Specification spec : specList) {
			nusmv.verify(spec);
		}
		
		/// end
		long end = System.currentTimeMillis();
		this.mVerifyTime = end - start;

		this.writeReults(specList);
	}
	
	
	///// Confirmed below

	/**
	 * Containts this project information
	 */
	protected Analyzer mAnalyzer;
	
	/**
	 * An extracted and "serialized" finite state machine
	 */
	protected FiniteStateMachine mFSM;
	
	/**
	 * A elapsed time for verifying the correctness of the extracted finite state machine
	 */
	protected long mVerifyTime;
	
	/**
	 * Constructor
	 * @param analyzer Contains this project information
	 */
	public Verifier(Analyzer analyzer) {
		this.mAnalyzer = analyzer;
		this.mFSM = this.getExtractedFSM();

		/// Needs to set original properties at one time
		Property.setOriginalPropertyList();
	}

	/**
	 * Deserializes the extracted finite state machine
	 * @return Extracted finite state machine
	 */
	public FiniteStateMachine getExtractedFSM() {
		String objName = this.mAnalyzer.getProject().getName() + ".fsm" + FileExtension.Serialized;
		Object obj = TextFileUtils.deserialize(this.mAnalyzer.getProject().getDir(), objName);
		FiniteStateMachine fsm = (FiniteStateMachine)obj;
		return fsm;
	}
	
	/**
	 * Setups IADP Repository for this project
	 */
	public void setup() {
		System.out.println("===Starts setup===");
		System.out.println("Writing seleactable properties...");
		boolean write_rep_list = this.writePropertyList();
		if(write_rep_list) {
//			System.out.println("Done");
		} else {
//			System.out.println("--> File exits: " + Verifier.getIADPPropListFile().getPath());
//			System.out.println("--> When you add your original properties, it needs to update this property list.");
//			System.out.println("--> Modify and rerun: " + this.getClass() + "#writePropertyList");
		}
		System.out.println("Writing your skeleton repository file...");
		boolean write_rep_skel = this.writeIADPInfoRepFileSkeleton();
		if(write_rep_skel) {
//			System.out.println("Done");
		} else {
//			System.out.println("--> File exits: " + Verifier.getIADPInfoRepFile(this.mAnalyzer).getPath());
//			System.out.println("--> Note that this repository is initialized if you remove this file.");
		}
		/// After that, open GUI
		System.out.println("--> [Success] Add your information about implemented Ajax design patterns");
		System.out.println(FilePath.IADPRepositoryHttp + "/?filename=" + Verifier.getIADPInfoRepFile(this.mAnalyzer).getName());
	}
	
	/**
	 * Property list file
	 * @return File instance representing "path/to/properties.xml", etc
	 */
	private static File getIADPPropListFile() {
		String dir = FilePath.IADPHome;
		String filename = FilePath.IADPPropListFilename;
		return new File(dir, filename);
	}
	
	/**
	 * Repository file for this project
	 * @param analyzer Containts this project information
	 * @return File instance representing "path/to/files/rep_file.xml", etc
	 */
	private static File getIADPInfoRepFile(Analyzer analyzer) {
		String dir = FilePath.IADPRepositry;
		String filename = analyzer.getProject().getName() + FileExtension.XML;
		return new File(dir, filename);
	}
	
	
	
	/**
	 * Writes a skeleton file representing IADP repositry of this project
	 */
	public boolean writeIADPInfoRepFileSkeleton() {
		String content = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
		content += "<Repository>\n";
		
		content += this.mAnalyzer.getProject().toXML4IADPInfoRep();
		content += this.mFSM.getFSMData();

		content += "</Repository>\n";
		
		File file = Verifier.getIADPInfoRepFile(this.mAnalyzer);
		if(!file.exists()) {
			TextFileUtils.write(file.getPath(), content);
			file.setWritable(true, false);
			return true;
		}
		return false;
	}

	/**
	 * Writes the selectable property list file in XML
	 * @return Success or fail to write XML file containtng all properties
	 */
	public boolean writePropertyList() {
		String content = Property.getPropertyListXML();
		
		File file = Verifier.getIADPPropListFile();
		if(!file.exists()) {
			TextFileUtils.write(file.getPath(), content);
			return true;
		}
		
		return false;
	}


	/**
	 * Reads IADP Info repository file
	 * @param fsm Contains concrete state IDs because guided information, e.g., function or event name, is abstract
	 * @return Property list to be verified
	 */
	public List<Property> readIADPInfo(FiniteStateMachine fsm) {
		ArrayList<Property> ret = new ArrayList<Property>();
		
		/// IADP info repository
		File iadp_info_rep_file = Verifier.getIADPInfoRepFile(this.mAnalyzer);
		String iadp_info_rep_filetext = TextFileUtils.cat(iadp_info_rep_file.getPath());
		Document iadp_info_rep_doc = Jsoup.parse(iadp_info_rep_filetext, iadp_info_rep_file.getName(), Parser.xmlParser());
		if(iadp_info_rep_doc == null) {
			StringUtils.printError(this, "fail to read IADP repository file");
			System.exit(-1);
		}
		/// Property list
		File prop_list_file = Verifier.getIADPPropListFile();
		String prop_list_filetext = TextFileUtils.cat(prop_list_file.getPath());
		Document prop_list_doc = Jsoup.parse(prop_list_filetext, prop_list_file.getName(), Parser.xmlParser());
		if(prop_list_doc == null) {
			StringUtils.printError(this, "fail to read Property list file");
			System.exit(-1);
		}
		
		for(Element iadp_prop : iadp_info_rep_doc.getElementsByTag(XMLTag.IADPProperty)) {
			String iadp_prop_id = iadp_prop.attr(XMLTag.IADPAttrVarId);
			
			Element prop_list_elm = null;
			for(Element _prop_elm : prop_list_doc.getElementsByTag(XMLTag.IADPProperty)) {
				if(_prop_elm.attr(XMLTag.IADPAttrVarId).equals(iadp_prop_id)) {
					prop_list_elm = _prop_elm;
					break;
				}
			}
			
			Property prop_obj = Property.getProperty(iadp_prop_id).clone();
			prop_obj.setIADPInfo(iadp_prop);
			
			String[] PSQR = new String[4]; // prop_obj.setTemplateVariables(P, S, Q, R);
			String[] P1P2 = new String[2]; // prop_obj.setTemplateVariables(P1, P2);
			for(Element var : iadp_prop.getElementsByTag(XMLTag.IADPVeriable)) {
				String var_id = var.attr(XMLTag.IADPAttrVarId);
				String var_value = var.text();
				for(Element prop_list_var : prop_list_elm.getElementsByTag(XMLTag.IADPVeriable)) {
					if(prop_list_var.attr(XMLTag.IADPAttrVarId).equals(var_id)) {
						String var_src = prop_list_var.attr(XMLTag.IADPAttrSrc);
						
						String var_str = null;
						if(var_value.equals(XMLTag.IADPAttrVar_UserEvents)) {
							var_str = NuSMV.genOr(fsm.getUserEventIdList(this.mAnalyzer));
						} else if(var_src.equals(XMLTag.IADPAttrSrc_Func)) {
							var_str = NuSMV.genOr(fsm.getFuncIdList(var_value));
						} else if(var_src.equals(XMLTag.IADPAttrSrc_Event)) {
							var_str = NuSMV.genOr(fsm.getEventIdList(var_value));
						}
						
						if(var_id.equals(XMLTag.IADPAttrVarId_P)) {
							PSQR[0] = var_str;
						} else if(var_id.equals(XMLTag.IADPAttrVarId_S)) {
							PSQR[1] = var_str;
						} else if(var_id.equals(XMLTag.IADPAttrVarId_Q)) {
							PSQR[2] = var_str;
						} else if(var_id.equals(XMLTag.IADPAttrVarId_R)) {
							PSQR[3] = var_str;
						} else if(var_id.equals(XMLTag.IADPAttrVarId_P1)) {
							P1P2[0] = var_str;
						} else if(var_id.equals(XMLTag.IADPAttrVarId_P2)) {
							P1P2[1] = var_str;
						}
						break;
					}
				}
			}
			if(P1P2[0] != null && P1P2[1] != null) {
				PSQR[0] = prop_obj.getTemplateVariableP(P1P2[0], P1P2[1], fsm);
			}
			
			prop_obj.setTemplateVariables(PSQR[0], PSQR[1], PSQR[2], PSQR[3]);
			ret.add(prop_obj);
			
		}
		
		return ret;
	}

	/**
	 * Writes results in HTML file
	 * @param specList Contains each verification result
	 */
	public void writeReults(List<Specification> specList) {
//		for(Specification spec : specList) {
//			Element iadp = spec.getProperty().getIADPInfo();
//		}
//		
		
//		File file = Verifier.getVerifyResultFile(this.mAnalyzer);
//		
		/// Generates output directory
		File dir = new File(FilePath.IADPResult, this.mAnalyzer.getProject().getName());
		boolean success = dir.mkdir();
		if(!success) { // exist previous results
			dir.delete();
			dir.mkdir();
		}

		/// Generates and move counterexample files
		File ce_dir = new File(dir.getPath(), FilePath.Counterexample);
		boolean ce_success = ce_dir.mkdir();
		if(!ce_success) { // exist previous results
			ce_dir.delete();
			ce_dir.mkdir();
		}

		/// Generates and writes index.html
		String skeleton = TextFileUtils.cat(FilePath.VerifyResultsSkeletonHTML);
		skeleton = skeleton.replace("@ProjectName", this.mAnalyzer.getProject().getName());
		skeleton = skeleton.replace("@ProjectUrl", this.mAnalyzer.getProject().getUrl());
		String body = "";
		for(Specification spec : specList) {
			body += spec.getResultTableBody(this.mAnalyzer);
		}
		skeleton = skeleton.replace("@ResultTableBody", body);
		TextFileUtils.write(dir.getPath(), "index.html", skeleton);

//		String viewer = TextFileUtils.cat(FilePath.ViewCounterexampleSkeletonPHP);
//		TextFileUtils.write(dir.getPath(), "viewer.php", viewer);
		
		
		System.out.println("--> [Success] Verification time: " + this.mVerifyTime);
//		System.out.println("To view verification results, follow the instructions below (to be automated):");
//		String curDir = (new File(".")).getAbsolutePath();
//		curDir = curDir.substring(0, curDir.length()-2); // remove /path/to/. <- last "/."
//		System.out.println("user@host:~$: cd " + curDir + "/" + this.mAnalyzer.getProject().getDir() + FilePath.VerifyResult + FilePath.Counterexample);
//		System.out.println("user@host:counterexample$: chmod +x " + this.mAnalyzer.getProject().getName() + ".spec.<#>.sh");
//		System.out.println("user@host:counterexample$: ./" + this.mAnalyzer.getProject().getName() + ".spec.<#>.sh");
//		System.out.println("user@host:counterexample$: cp " + this.mAnalyzer.getProject().getName() + ".spec.*.dot.png " + FilePath.IADPResult);
//		System.out.println(FilePath.IADPRepositoryHttp + "/view_verification_results.php?filename=" + this.mAnalyzer.getProject().getName() + FileExtension.XML);
		System.out.println(FilePath.IADPRepositoryHttp + "/result/" + this.mAnalyzer.getProject().getName() + "/");
	}
	
	/**
	 * Repository file for this project
	 * @param analyzer Containts this project information
	 * @return File instance representing "path/to/files/rep_file.xml", etc
	 */
	private static File getVerifyResultFile(Analyzer analyzer) {
		String dir = FilePath.IADPResult;
		String filename = analyzer.getProject().getName() + FileExtension.HTML;
		return new File(dir, filename);
	}
}
