package ch.zhaw.cctd.domain;

import ch.zhaw.cctd.event.ChatEventListener;
import ch.zhaw.cctd.event.EventHandler;
import ch.zhaw.cctd.network.NetworkException;
import ch.zhaw.cctd.network.Networking;

public final class DomainFacade {
	
	private static EventHandler eventhandler;
	private static Networking networking;
	
	public static void onExit() {
		DomainFacade.leaveGame();
	}
	
	public static void registerChatEventListener(ChatEventListener l) {
		DomainFacade.eventhandler.addChatEventListener(l);
	}
	
	public static void sendChatMessage(String message) {
		// TODO get the player object
		DomainFacade.eventhandler.dispatchChatMessage(null, message);
	}
	
	public static void startHostedGame(int port) throws NetworkException {
		DomainFacade.networking = new Networking();
		DomainFacade.networking.hostGame(port);
	}
	
	public static void startHostedGame() throws NetworkException {
		DomainFacade.networking = new Networking();
		DomainFacade.networking.hostGame();
	}
	
	public static void joinGame(String addresse) throws NetworkException {
		DomainFacade.networking = new Networking();
		DomainFacade.networking.joinGame(addresse);
	}
	
	public static void joinGame(String addresse, int port) throws NetworkException {
		DomainFacade.networking = new Networking();
		DomainFacade.networking.joinGame(addresse, port);
	}
	
	public static void leaveGame() {
		DomainFacade.networking.cleanup();
	}
	
}
