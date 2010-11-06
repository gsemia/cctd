package ch.zhaw.cctd.event;

import org.apache.log4j.Logger;

public class EventHandler {
	
	private static final Logger LOG = Logger.getLogger(EventHandler.class);
	
	public void addChatEventListener(ChatEventListener l) {
		this.add(ChatEventListener.class, l);
	}
	
	public void removeChatEventListener(ChatEventListener l) {
		this.remove(ChatEventListener.class, l);
	}
	
	public void dispatchChatMessage(String origin, String message) {
		if (LOG.isDebugEnabled())
			LOG.debug("dispatching ChatEventListener#onMessage("+origin+", "+message+")");
		this.dispatch(ChatEventListener.class, new EventHandler.CallBack<ChatEventListener>() {
			@Override
			public void call(ChatEventListener l, Object... params) {
				l.onMessage(new ChatEvent((String)params[0], (String)params[1]));
			}
		}, origin, message);
	}
	
	private abstract class CallBack<T extends EventListener> {
		public abstract void call(T l, Object... params);
	}
	
	@SuppressWarnings("unchecked")
	private <T extends EventListener >void dispatch(Class<T> c, EventHandler.CallBack<T> callback, Object... params) {
		for (int i = listenerList.length-2;i>=0;i-=2) {
			if (listenerList[i]==c) {
				callback.call((T)listenerList[i+1], params);
			}
		}
	}
	
	private transient Object[] listenerList = new Object[0];
	
	private synchronized <T extends EventListener> void add(Class<T> c, T l) {
		if (l == null) return;
		if (!c.isInstance(l)) throw new IllegalArgumentException("Listener "+l+"not of type "+c);
		if (listenerList.length == 0) {
			listenerList = new Object[] { c, l };
		} else {
			int length = listenerList.length;
			Object[] tmp = new Object[length+2];
			System.arraycopy(listenerList, 0, tmp, 0, length);
			tmp[length] = c;
			tmp[length+1] = l;
			listenerList = tmp;
		}
	}
	
	private synchronized <T extends EventListener> void remove(Class<T> c, T l) {
		if (l == null) return;
		if (!c.isInstance(l)) throw new IllegalArgumentException("Listener "+l+"not of type "+c);
		int index = -1;
		for (int i = listenerList.length-2; i>=0; i-=2) {
			if ((listenerList[i]==c) && (listenerList[i+1].equals(l))) {
				index = i;
				break;
			}
		}

		if (index != -1) {
			Object[] tmp = new Object[listenerList.length-2];
			System.arraycopy(listenerList, 0, tmp, 0, index);
			if (index < tmp.length)
				System.arraycopy(listenerList, index+2, tmp, index, tmp.length-index);
			listenerList = tmp;
		}
	}

}
