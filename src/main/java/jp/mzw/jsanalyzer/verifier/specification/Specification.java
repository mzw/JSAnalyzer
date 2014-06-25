package jp.mzw.jsanalyzer.verifier.specification;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jsoup.nodes.Element;

import jp.mzw.jsanalyzer.config.Bin;
import jp.mzw.jsanalyzer.config.FileExtension;
import jp.mzw.jsanalyzer.config.FilePath;
import jp.mzw.jsanalyzer.core.Analyzer;
import jp.mzw.jsanalyzer.formulator.property.Property;
import jp.mzw.jsanalyzer.serialize.model.State;
import jp.mzw.jsanalyzer.util.CommandLineUtils;
import jp.mzw.jsanalyzer.util.StringUtils;
import jp.mzw.jsanalyzer.util.TextFileUtils;
import jp.mzw.jsanalyzer.xml.XMLTag;

public class Specification {
	
	protected int mId;
	protected Property mProperty;
	protected boolean mSmvResult;
	protected List<String> mCounterexample;
	public Specification(Property prop) {
		this.mId = IdGen.genId();
		this.mProperty = prop;
	}
	
	public void setCounterexmaple(List<String> counterexample) {
		this.mCounterexample = counterexample;
	}
	public List<String> getCounterexample() {
		return this.mCounterexample;
	}
	

	public int getId() {
		return this.mId;
	}
	public Property getProperty() {
		return this.mProperty;
	}
	public String getCtlFormula() {
		return this.mProperty.getCTLFormula();
	}
	public void setSmvResult(boolean result) {
		this.mSmvResult = result;
	}
	public boolean getSmvResult() {
		return this.mSmvResult;
	}

	private static class IdGen {
		private static int id_source = 0;
		public static int genId() {
			return id_source++;
		}
	}
	
	public String getResultTableBody(Analyzer analyzer) {
		String ret = "";

		ret += "\t<tr>\n";

		/// Property name
		ret += "\t\t<td>" + StringUtils.esc_xml(this.mProperty.getNameAbbr()) + "</td>\n";
		/// Requirement
		ret += "\t\t<td>" + StringUtils.esc_xml(this.mProperty.getRequirement()) + "</td>\n";
		/// Ajax Design Pattern
		ret += "\t\t<td>" + StringUtils.esc_xml(this.mProperty.getAjaxDesignPattern().getName()) + "</td>\n";
		/// Property Pattern
		ret += "\t\t<td>" + StringUtils.esc_xml(this.mProperty.getPropertyPattern().getClass().getSimpleName()) + "</td>\n";
		/// CTL Template
		ret += "\t\t<td>" + StringUtils.esc_xml(this.mProperty.getPropertyPattern().getCTLTemplate()) + "</td>\n";
		/// Guided Variables
		ret += "\t\t<td>";
		
		/// When use Web app GUI
		if(this.mProperty.getIADPInfo() != null) {
			for(Element var_elm : this.mProperty.getIADPInfo().getElementsByTag(XMLTag.IADPVeriable)) {
				String var_id = var_elm.attr(XMLTag.IADPAttrVarId);
				String var_value = var_elm.text();
				
				ret += var_value + " (" + var_id + ")<br />";
			}
		} else {
			ret += "See your program or use Web app GUI";
		}
		ret +=  "</td>\n";

		/// Result
		if(this.mSmvResult) { // Correct
			ret += "\t\t<td style=\"background-color:#7FFFD4;\">Correct</td>\n";
		} else { // Fault
			this.genCounterexampleDots(analyzer);
			ret += "\t\t<td style=\"background-color:#FA8072;\"><a target=\"_blank\" href=\"" + 
					FilePath.IADPRepositoryHttp + "/viewer.counterexample.php?projname=" + analyzer.getProject().getName() + "&specid=" + this.getId() + "&stepnum=" + this.getCounterexample().size() +
					"\">Fault</a></td>\n";
		}
		
		ret += "\t</tr>\n";
		return ret;
	}
	
	private void genCounterexampleDots(Analyzer analyzer) {

		String dir = analyzer.getProject().getDir() + FilePath.VerifyResult + FilePath.Counterexample;
		
		for(int step = 0; step < this.getCounterexample().size(); step++) {
			String filename = analyzer.getProject().getName() + ".spec." + this.getId() + ".step." + (step) + FileExtension.Dot;
			String output = filename + ".png";
			
			String[] cmd_dot = { Bin.Dot, "-Tpng", filename, "-o", output };
			try {
				CommandLineUtils.exec(dir, cmd_dot);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			File _output_dir = new File(FilePath.IADPResult, analyzer.getProject().getName());
			File output_dir = new File(_output_dir.getPath(), FilePath.Counterexample);
			String[] cmd_cp = { "cp", output, output_dir.getPath() };
			try {
				CommandLineUtils.exec(dir, cmd_cp);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	

	//////////
	private static int id_assign = 0;
	protected int id;
	protected String description;
	protected String formula;
	protected boolean isEvaluated;
	protected int genNCResult;
	protected int spinResult;
	protected int gccResult;
	protected int panResult;
	protected boolean isFault;
	protected String anti_example;
	
	public Specification(String description, String formula) {
		this.id = id_assign++;
		this.description = description;
		this.formula = formula;
		this.isEvaluated = false;
		
		this.anti_example = "";
	}
	
//	public int getId() {
//		return this.id;
//	}
	
	public String getDescription() {
		return this.description;
	}
	
	public String getFormula() {
		return this.formula;
	}
	
	public boolean getIsEvaluated() {
		return this.isEvaluated;
	}
	public void setGenNCResult(int result) {
		this.genNCResult = result;
	}
	public int getGenNCResult() {
		return this.genNCResult;
	}
	public void setSpinResult(int result) {
		this.spinResult = result;
	}
	public int getSpinResult() {
		return this.spinResult;
	}
	public void setGccResult(int result) {
		this.gccResult = result;
	}
	public int getGccResult() {
		return this.gccResult;
	}
	public void setPanResult(int result) {
		this.panResult = result;
	}
	public int getPanResult() {
		return this.panResult;
	}
	public void setIsFault(boolean isFault) {
		this.isFault = isFault;
		this.isEvaluated = true;
	}
	public boolean getIsFault() {
		return this.isFault;
	}
	
	public boolean isFault() {
		if(this.isEvaluated && this.isFault) {
			return true;
		}
		return false;
	}
	
	public void setAntiExample(String anti_example) {
		this.anti_example = anti_example;
	}
	public String getAntiExample() {
		return this.anti_example;
	}
	
	
	public String toXML() {
		String ret = "";

		ret += "\t<Specification id=" + this.id + ">\n";

		ret += "\t\t<Description>" + StringUtils.esc_xml(this.description) + "</Description>\n";
		ret += "\t\t<LTL_formula>" + StringUtils.esc_xml(this.formula) + "</LTL_formula>\n";
		ret += "\t\t<Result>" + (this.isEvaluated ? (this.isFault ? "fault" : "correct") : "") + "</Result>\n";
		
		ret += "\t</Specification>\n";
		
		return ret;
	}
	
	public String toHTML() {
		String ret = "";

		ret += "\t<tr><td>" + this.id + "</td>\n";

		ret += "\t<td>" + StringUtils.esc_xml(this.description) + "</td>\n";
		ret += "\t<td>" + StringUtils.esc_xml(this.formula) + "</td>\n";
		ret += "\t<td " + (this.isEvaluated ? (this.isFault ? "style=\"background-color:#FA8072;\" onclick=\"alert('" + StringUtils.esc_xml(this.anti_example) + "')\">fault" : "style=\"background-color:#7FFFD4;\">correct") : "") + "</td>\n";
		
		ret += "\t</tr>\n";
		
		return ret;
	}
	
	
}

