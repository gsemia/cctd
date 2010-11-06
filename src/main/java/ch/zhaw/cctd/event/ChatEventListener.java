package ch.zhaw.cctd.event;

public interface ChatEventListener extends EventListener {
	
	public void onMessage(ChatEvent e);
	
}
