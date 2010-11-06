package ch.zhaw.cctd.network;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import org.apache.log4j.Logger;

public class Networking {
	
	public static abstract class Caller {
		public abstract void call(RemoteClient client, Object... params) throws NetworkException;
	}
	
	public static final int DEFAULT_PORT = 40004;
	
	private static final Logger LOG = Logger.getLogger(Networking.class);
	
	private Registry rmiregistry;
	private List<RemoteClient> remoteclients;
	
	private void startRmiRegistry(int port) throws NetworkException {
		if (rmiregistry != null) return;
		try {
			LocateRegistry.createRegistry(port);
			LOG.debug("RMI registry ready");
		} catch (RemoteException e) {
			LOG.error("RMI registry failed to start", e);
			throw new NetworkException(e);
		}
	}
	
	private void stopRmiRegistry() {
		try {
			UnicastRemoteObject.unexportObject(rmiregistry, true);
		} catch (NoSuchObjectException e) {
			// this exception can be ignored -> stopping achieved
		}
	}
	
	public void joinGame(String addresse) throws NetworkException {
		this.joinGame(addresse, DEFAULT_PORT);
	}
	
	public void joinGame(String addresse, int port) throws NetworkException {
		this.startRmiRegistry(port);
		// TODO
	}
	
	public void hostGame() throws NetworkException {
		this.hostGame(DEFAULT_PORT);
	}
	
	public void hostGame(int port) throws NetworkException {
		this.startRmiRegistry(port);
		// TODO
	}
	
	public void setMessage(Caller caller, Object... params) throws NetworkException {
		for (RemoteClient c : this.remoteclients) {
			caller.call(c, params);
		}
	}
	
	public void cleanup() {
		this.stopRmiRegistry();
	}
	
}
