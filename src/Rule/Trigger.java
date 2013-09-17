package Rule;

public class Trigger extends Rule {
	private String interact = null;
	private String via = null;
	private String event = null;
	public Trigger(String interact, String via, String event) {
		this.interact = interact;
		this.via = via;
		this.event = event;
	}
	
	public boolean isTrigger(String keyword) {
		if(keyword == null || "".equals(keyword)) {
			return false;
		}
		if(keyword.equals(this.event)) {
			return true;
		}
		return false;
	}
	
	public boolean isUserEvent() {
		if(this.interact.equals("User")) {
			return true;
		}
		return false;
	}
}
