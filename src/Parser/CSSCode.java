package Parser;

public class CSSCode extends Code {
	private boolean isInline = false;
	public CSSCode(String url, String code) {
		super(url, code);
		this.isInline = false;
	}
	public void setIsInlinde(boolean isInline) {
		this.isInline = isInline;
	}
	public boolean getIsInline() {
		return this.isInline;
	}
}
