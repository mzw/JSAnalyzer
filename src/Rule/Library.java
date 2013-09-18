package Rule;

import Analyzer.Config;
import Analyzer.Util;

public class Library extends Rule {
	private String filename = null;
	private String rulefile = null;
	private boolean isSkip = true;
	private boolean isLoaded = false;
	
	private boolean isStartsWith = false;
	private String strStartsWith = null;
	
	public Library(String filename, String rulefile) {
		this.filename = filename;
		this.rulefile = rulefile;
		
		this.output = this.filename + ", " + this.rulefile;
		
		// for "startsWith" by mzw at 2013.09.18
		if(filename != null && filename.startsWith(Config.Lib_StartsWith)) {
			this.isStartsWith = true;
			
			int index = 0;
			String tmp = null;
			
			index = filename.indexOf(Config.Lib_StartsWith);
			tmp = filename.substring(index + Config.Lib_StartsWith.length());
			index = tmp.indexOf(")");
			tmp = tmp.substring(0, index);
			tmp = Util.removeQuote(tmp);
			
			this.strStartsWith = tmp;
		}
	}
	
	public void setIsSkip(boolean isSkip) {
		this.isSkip = isSkip;
	}
	public boolean isSkip() {
		return this.isSkip;
	}
	
	public boolean isRuledJsLib(String filename) {
		// for "startsWith" by mzw at 2013.09.18
		if(this.isStartsWith) {
			this.isLoaded = true;
			return true;
		}
		
		if(this.filename.equals(filename)) {
			this.isLoaded = true;
			return true;
		}
		return false;
	}
	
	public boolean isLoaded() {
		return this.isLoaded;
	}
	
	public String getRuleFile() {
		return this.rulefile;
	}
}
