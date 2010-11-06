package ch.zhaw.cctd.network;

public class NetworkException extends Exception {
	
	private static final long serialVersionUID = 7464542870706356106L;

	public NetworkException(Exception parent) {
		super(parent);
	}
	
}
