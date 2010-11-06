package ch.zhaw.cctd.network;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.log4j.Logger;

import ch.zhaw.cctd.event.ChatEventListener;
import ch.zhaw.cctd.event.EventListener;

public class EventListenerToNetwork implements InvocationHandler {

	private static final Logger LOG = Logger.getLogger(EventListenerToNetwork.class);
	
	@SuppressWarnings("unchecked")
	private static final Class<EventListener>[] eventhandlers = new Class[] { 
		ChatEventListener.class 
	};
	public static EventListener getGeneralEventListener(Networking net) {
		
		InvocationHandler handler = new EventListenerToNetwork(net);
		EventListener l = (EventListener) Proxy.newProxyInstance(
				EventListener.class.getClassLoader(), 
				EventListenerToNetwork.eventhandlers, 
				handler);
		
		return l;
	}
	
	private EventListenerToNetwork(Networking net) {
		this.net = net;
	}
	
	private Networking net;

	@Override
	public Object invoke(Object proxy, Method method, final Object[] args) throws Throwable {
		if (LOG.isDebugEnabled())
			LOG.debug("event sending -> network: "+method.getName());
		
		
		/*
		net.setMessage(new Networking.Caller() {
			@Override
			public void call(RemoteClient client, Object... params) throws NetworkException {
				client.dispatchEvent(null, (Event)(args[0]));
			}}, null);
		*/
		// TODO send to other clients
		return null;
	}

}
