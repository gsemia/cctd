package ch.zhaw.cctd.event;

public class ChatEvent implements Event {
	
	private static final long serialVersionUID = 3536893329906247368L;

	public ChatEvent() {}
	public ChatEvent(ChatEvent e) {
		this(e.origin, e.message);
	}
	public ChatEvent(String origin, String message) {
		this.origin = origin;
		this.message = message;
	}
	
	private String origin;
	private String message;
	
	public String getMessage() {
		return this.message;
	}
	
	public String getOriginUsername() {
		return this.origin;
	}
	
	public int hashCode() {
		return this.origin.hashCode() + this.message.hashCode();
	}
	
	public boolean equals(Object obj) {
		return obj == this || (obj != null && obj instanceof ChatEvent && obj.hashCode() == this.hashCode());
	}
	
}
