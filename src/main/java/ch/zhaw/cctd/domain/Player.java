package ch.zhaw.cctd.domain;

public class Player {

	private boolean local;
	private int playerId;
	private String name;
	
	/**
	 * Represents a Player in the Game. 
	 * @param name Player Name
	 * @param local If he is the local Player or not
	 */
	public Player(String name, int playerId, boolean local) {
		this.name=name;
		this.playerId = playerId;
		this.local=local;
	}
	
	/**
	 * Returns true or False if the Player is the Player on the localhost Machine
	 * @return
	 */
	public boolean isLocal() {
		return this.local;
	}

	/**
	 * Player Name
	 * @return
	 */
	public String getName() {
		return this.name;
	}
	/**
	 * Player id (1-4) which represents his location
	 * @return
	 */
	public int getPlayerId() {
		return this.playerId;
	}
}
