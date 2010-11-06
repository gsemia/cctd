package ch.zhaw.cctd.domain;

import ch.zhaw.cctd.domain.prototype.CreepPrototype;

public class Map {
	Match match;
	
	Region BuildRegionPlayer1;
	Region BuildRegionPlayer2;
	Region BuildRegionPlayer3;
	Region BuildRegionPlayer4;
	
	Region LaneRegion1;
	Region LaneRegion2;
	Region LaneRegion3;
	Region LaneRegion4;
	
	
	Region CreepStartLocationPlayer1;
	Region CreepStartLocationPlayer2;
	Region CreepStartLocationPlayer3;
	Region CreepStartLocationPlayer4;
	
	Region CreepWalkTargetPlayer1;
	Region CreepWalkTargetPlayer2;
	Region CreepWalkTargetPlayer3;
	Region CreepWalkTargetPlayer4;
	
	public Map (Match match) {
		this.match = match;
		//Read XML and create all targets
	}
	public Region getSpawnLocation() {
		
		Player localPlayer = match.getLocalPlayer();
		
		int playerID =  localPlayer.getPlayerId();
		
		switch (playerID) {
			case 1:
				return CreepStartLocationPlayer1;
			case 2:
				return CreepStartLocationPlayer2;
			case 3: 
				return CreepStartLocationPlayer3;
			case 4: 
				return CreepStartLocationPlayer4;
		}
		throw new IllegalStateException("Wrong Player States. No Creep Location Found for the current set of Players");
		
	}
	
	/**
	 * Spawns a creep at the current players spawn location of the specified type.
	 */
	public void spawnCreep(CreepPrototype c) {
		this.getSpawnLocation();
		// TODO create creep
		// TODO create event
	}
}
