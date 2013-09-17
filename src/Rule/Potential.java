package Rule;

public class Potential extends Rule {
	
	private String interact = null;
	private String via = null;
	private String func = null;
	private String target = null;
	private String event = null;
	private String callback = null;
	public Potential(
			String interact,
			String via,
			String func,
			String target,
			String event,
			String callback
		) {
		this.interact = interact;
		this.via = via;
		this.func = func;
		this.target = target;
		this.event = event;
		this.callback = callback;
	}
	
	public boolean isPotential(String keyword) {
		if(keyword == null || "".equals(keyword)) {
			return false;
		}
		if(keyword.equals(this.func)) {
			return true;
		}
		return false;
	}
	public String getTarget() {
		return this.target;
	}
	public String getEvent() {
		return this.event;
	}
	public String getCallback() {
		return this.callback;
	}
	public String getFunc() {
		return this.func;
	}
}
