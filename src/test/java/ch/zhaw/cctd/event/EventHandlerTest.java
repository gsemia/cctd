package ch.zhaw.cctd.event;

import org.easymock.EasyMock;

import junit.framework.TestCase;

public class EventHandlerTest extends TestCase {
	
	private EventHandler eventHandler;
	private ChatEventListener chatEventListener;
	
	public void setUp() throws Exception {
		this.chatEventListener = EasyMock.createMock(ChatEventListener.class);
		this.eventHandler = new EventHandler();
		this.eventHandler.addChatEventListener(this.chatEventListener);
	}
	
	public void testDispatchChatEventListener() throws Exception {
		final String origin = "me";
		final String message = "kek";
		this.chatEventListener.onMessage(new ChatEvent(origin, message));
		EasyMock.expectLastCall();
		
		EasyMock.replay(this.chatEventListener);
		
		this.eventHandler.dispatchChatMessage(origin, message);
		
		EasyMock.verify(this.chatEventListener);
	}
	
}
