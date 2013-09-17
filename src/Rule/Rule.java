package Rule;

public class Rule {
	public boolean isRuledJsLib(String filename) {
		return false;
		
	}
	
	protected String output = null;
	public void output() {
		System.out.println(this.output);
	}
	
	protected boolean unimplemented = false;
	public void setUnimplemented(boolean unimplemented) {
		this.unimplemented = unimplemented;
	}
	
	/// utilities
	public static boolean containArg(String str) {
		String regex = "arg_[0-9]+";
		java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
		java.util.regex.Matcher matcher = pattern.matcher(str);
		return matcher.find();
	}
	
	
	public static int getArgNum(String str) {
		String regex = "arg_[0-9]+";
		java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
		java.util.regex.Matcher matcher = pattern.matcher(str);

		boolean find = matcher.find();
		if(find) {
			int start = matcher.toMatchResult().start();
			int end = matcher.toMatchResult().end();
			String _str = str.substring(start, end);
			
			String num = _str.substring("arg_".length());
			return Integer.parseInt(num);
		}	
		return -1;
	}
}
