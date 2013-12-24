package jp.mzw.jsanalyzer.verifier;

import java.util.ArrayList;
import java.util.List;

import Analyzer.Util;

public class Specification {
	private int mSpecId;
	private String mSpecName;
	private String mSpecDescr;
	private String mFormula;
	
	protected int mGenNCResult;
	protected int mSpinResult;
	protected int mGccResult;
	protected int mPanResult;
	
	protected String mAntiExample;
	protected boolean mIsFault;
	protected boolean mVerified;
	
	public Specification() {
		
	}
	

	public int getId() {
		return this.mSpecId;
	}
	
	public String getDescription() {
		return this.mSpecDescr;
	}
	
	public String getFormula() {
		return this.mFormula;
	}
	
//	public boolean getIsEvaluated() {
//		return this.isEvaluated;
//	}
	public void setGenNCResult(int result) {
		this.mGenNCResult = result;
	}
	public int getGenNCResult() {
		return this.mGenNCResult;
	}
	public void setSpinResult(int result) {
		this.mSpinResult = result;
	}
	public int getSpinResult() {
		return this.mSpinResult;
	}
	public void setGccResult(int result) {
		this.mGccResult = result;
	}
	public int getGccResult() {
		return this.mGccResult;
	}
	public void setPanResult(int result) {
		this.mPanResult = result;
	}
	public int getPanResult() {
		return this.mPanResult;
	}
	public void setIsFault(boolean isFault) {
		this.mIsFault = isFault;
		this.mVerified = true;
	}
	public boolean getIsFault() {
		return this.mIsFault;
	}
	
	public boolean isFault() {
		if(this.mVerified && this.mIsFault) {
			return true;
		}
		return false;
	}
	
	public void setAntiExample(String anti_example) {
		this.mAntiExample = anti_example;
	}
	public String getAntiExample() {
		return this.mAntiExample;
	}
	
	public static List<Specification> read(String filename) {
		List<Specification> ret = new ArrayList<Specification>();
		
		
		return ret;
	}
	
	
	public String toHTML() {
		String ret = "";

		ret += "\t<tr><td>" + this.mSpecId + "</td>\n";

		ret += "\t<td>" + Util.esc_xml(this.mSpecDescr) + "</td>\n";
		ret += "\t<td>" + Util.esc_xml(this.mFormula) + "</td>\n";
		ret += "\t<td " + (this.mVerified ? (this.mIsFault ? "style=\"background-color:#FA8072;\" onclick=\"alert('" + Util.esc_xml(this.mAntiExample) + "')\">fault" : "style=\"background-color:#7FFFD4;\">correct") : "") + "</td>\n";
		
		ret += "\t</tr>\n";
		
		return ret;
	}
}
