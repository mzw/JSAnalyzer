package Rule;

public class Control extends Rule {
	
	protected String attr = null;
	protected String disabled = null;
	public Control(String attr, String diabled) {
		this.attr = attr;
		this.disabled = disabled;
	}
	
	public boolean isControl(String keyword) {
		if(keyword == null || "".equals(keyword)) {
			return false;
		}
		if(keyword.equals(this.attr)) {
			return true;
		}
		return false;
	}
	
	public boolean disabled(String value) {
		if(value == null || "".equals(value)) {
			return false;
		}
		if(value.equals(this.disabled)) {
			return true;
		}
		return false;
	}
	
	public String getAttr() {
		return this.attr;
	}

}
