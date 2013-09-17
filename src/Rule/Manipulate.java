package Rule;

public class Manipulate extends Rule {
	
	private String func = null;
	private String semantic = null;
	private String by = null;
	private String value = null;
	private String target = null;
	public Manipulate(String func, String semantic, String by, String value, String target) {
		this.func = func;
		this.semantic = semantic;
		this.by = by;
		this.value = value;
		this.target = target;
	}

	public boolean isManipulate(String keyword) {
		if(keyword == null || "".equals(keyword)) {
			return false;
		}
		if(keyword.equals(this.func)) {
			return true;
		}
		return false;
	}
	
	public String getSemantic() {
		return this.semantic;
	}
	public String getBy() {
		return this.by;
	}
	public String getValue() {
		return this.value;
	}
	public String getTarget() {
		return this.target;
	}
}
