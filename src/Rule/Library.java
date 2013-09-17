package Rule;

public class Library extends Rule {
	private String filename = null;
	private String rulefile = null;
	private boolean isSkip = true;
	private boolean isLoaded = false;
	public Library(String filename, String rulefile) {
		this.filename = filename;
		this.rulefile = rulefile;
		
		this.output = this.filename + ", " + this.rulefile;
	}
	
	public void setIsSkip(boolean isSkip) {
		this.isSkip = isSkip;
	}
	
	public boolean isRuledJsLib(String filename) {
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
