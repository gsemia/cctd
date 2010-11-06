package ch.zhaw.cctd.domain;

import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.zhaw.cctd.domain.prototype.CreepPrototype;
import ch.zhaw.cctd.domain.prototype.WavePrototype;
import ch.zhaw.cctd.domain.prototype.WavePrototype.WaveCreepPrototype;

/**
 * Responsible for Spawning the Creeps once SpawnWave is triggered. It must be
 * started from a Thread because it is using Thread.Sleep to wait between each
 * Creep it has spawned
 * 
 * Each Wave is configured via cctd.XML. 
 * 
 * Supported XML Config Structure:
 * 
 * <waves>
 *  <wave id="1">
 *   <spawntime>90</spawntime>
 *   <spawnlength>15</spawnlength>
 *   <name>20 Basic Bunnys</name>
 *   <creeps spawnmode="random">
 *    <creep refid="basic-bunny" amount="30"/>
 *   </creeps>
 *  </wave>
 * </waves>
 * 
 * @author Rolf
 */
public class Wave {
	//
	// <spawntime>90</spawntime>
	// <spawnlength>15</spawnlength>
	// <name>20 Basic Bunnys</name>
	// <creeps spawnmode="random">
	// <creep refid="basic-bunny" amount="30"/>
	// </creeps>

	// Logger
	private static final Logger logger = LoggerFactory.getLogger(WavesHandler.class);

	private Match match;
	// Variables (Reflected from XML)
	private int spawnTime;
	private int spawnLength;
	private String name;
	private SpawnMode spawnMode;
	private List<WaveCreepPrototype> creepSets;

	/**
	 * new Wave
	 * 
	 * @param spawnTime
	 * @param spawnLength
	 * @param name
	 * @param spawnMode
	 * @param creepSets
	 */
	public Wave(Match match, WavePrototype prototype) {
		this.match = match;
		setSpawnTime(prototype.getSpawntime());
		setSpawnLength(prototype.getSpawnlength());
		setName(prototype.getName());
		setSpawnMode(prototype.getSpawnmode());
		setCreepSets(prototype.getCreeps());
		logger.trace("SpawnTime: " + spawnTime + ", SpawnLength: " + spawnLength + " Name: " + name + ", SpawnMode: " + spawnMode + ", CreepSets: "
				+ creepSets.toString());
	}

	// Setters
	private void setSpawnTime(int spawnTime) {
		this.spawnTime = spawnTime;
	}

	private void setSpawnLength(int spawnLength) {
		this.spawnLength = spawnLength;
	}

	private void setName(String name) {
		this.name = name;
	}

	private void setSpawnMode(String spawnMode) {
		if (spawnMode.toLowerCase().equals("row")) {
			setSpawnMode(SpawnMode.ROW);
			return;
		}
		if (spawnMode.toLowerCase().equals("mixed")) {
			setSpawnMode(SpawnMode.MIXED);
			return;
		}
		if (spawnMode.toLowerCase().equals("random")) {
			setSpawnMode(SpawnMode.RANDOM);
			return;
		}
		throw new IllegalArgumentException("Wrong Parameter for SpawnMode: " + spawnMode);
	}

	private void setSpawnMode(SpawnMode spawnMode) {

		this.spawnMode = spawnMode;
	}

	private void setCreepSets(List<WaveCreepPrototype> creepSets) {
		this.creepSets = creepSets;
	}

	// Getters
	public int getSpawnTime() {
		return spawnTime;
	}

	public int getSpawnLength() {
		return spawnLength;
	}

	public String getName() {
		return name;
	}

	public SpawnMode getSpawnMode() {
		return spawnMode;
	}

	public List<WaveCreepPrototype> getCreepSets() {
		return creepSets;
	}

	/**
	 * Spawns all Creeps for one Player
	 * 
	 * @param wavesHandler
	 *            Thread its started from to check Stop conditions
	 * @throws InterruptedException
	 *             If stopped
	 */
	public void startSpawn(WavesHandler wavesHandler, Match match) throws InterruptedException, Exception {
		if(logger.isTraceEnabled()) logger.trace("STARTED: startSpawn");
		// Count all Creeps to spawn in this Wave
		int totalCreepCount = 0;
		for (WaveCreepPrototype c : creepSets) {
			// Count only Creeps with Amount>0
			if (c.amount > 0) {
				totalCreepCount += c.amount;
			}
		}

		// Calculate the Waittime between each Spawn
		int waitTime = this.spawnLength * 1000 / totalCreepCount;
		if(logger.isTraceEnabled()) logger.trace("Default WaitTime: " + waitTime);
		long lastCreepSchedule = Calendar.getInstance().getTimeInMillis();

		switch (spawnMode) {
		case ROW:
			
			for (WaveCreepPrototype waveCreepPrototype : creepSets) {
				int creepcount = waveCreepPrototype.amount;
				while (creepcount>0) {

					// Spawn the Creep on the Spawnlocation of the Player (Each
					// player is doing this for himself)
					CreepPrototype creepPrototype = this.match.getPrototype(CreepPrototype.class, waveCreepPrototype.id);
					if(logger.isTraceEnabled()) 
						logger.trace("Spawning Creep " + creepPrototype.getId());
					this.match.getMap().spawnCreep(creepPrototype);

					creepcount--;
					
					// Calculate the exact Wait time
					// get the current Time
					long currentTime = Calendar.getInstance().getTimeInMillis();
					// Calculate the time Correction
					long timeCorrection = currentTime - lastCreepSchedule;
					// Calculate the Final Wait time
					long timeToWait = waitTime - timeCorrection;
					// Sleep until the next Creep is Spawned
					if(logger.isTraceEnabled()) 
						logger.trace("Thread Sleeping at CreepSpawning for " + timeToWait);
					if(timeToWait>0) {
						Thread.sleep(timeToWait);
					}
					if (!wavesHandler.isWorking()) {
						if(logger.isTraceEnabled()) logger.trace("Wave Spawning interupted by Working Flag");
						throw new InterruptedException("Stopped by WavesHandler.stop()");
					}

					// Save the time of the last Creep Schedule for next Creep
					lastCreepSchedule = Calendar.getInstance().getTimeInMillis();
				}

			}
			/*
		case MIXED:
			while (totalCreepCount > 0) {
				for (int i = 0; i < creepSets.size(); i++) {
					Creeps creeps = creepSets.get(i);
					if (creeps.getAmount() > 0) {
						// Spawning Creep and reduce amount by one
						//FIXME: match.getMap().getSpawnLocation().Spawn(creeps.getCreepid());
						creeps.lowerAmount();
						totalCreepCount--;
					} else {
						// No more Creep of this Type to spawn.. Skipping to
						// next one
					}
				}

			}
		case RANDOM:
			while (totalCreepCount > 0) {
				// Find a valid Random Creep ID for a creep type with amount > 0
				double creepTypeCount = (double) creepSets.size();
				int randomId = (int) Math.floor((Math.random() * creepTypeCount));
				int loopfixer = 0;
				;
				while (creepSets.get(randomId).getAmount() <= 0) {
					// Cicle through all possible Creep IDs.
					if (creepSets.size() == randomId + 1) {

						randomId = 0;
					} else {
						randomId++;
					}

					if (loopfixer > creepSets.size()) {
						throw new Exception("Endless Loop detected in random wave spawning");
					}
					loopfixer++;

				}

				// Get the Creep
				Creeps creeps = creepSets.get(randomId);
				if (creeps.getAmount() > 0) {
					// Spawning Creep and reduce amount by one
					//FIXME: match.getMap().getSpawnLocation().Spawn(creeps.getCreepid());
					creeps.lowerAmount();
					totalCreepCount--;
				} else {
					// Shoult never get here

					throw new Exception("Something went terribly Wrong here..");
				}
			}
*/
		}
		if(logger.isTraceEnabled()) logger.trace("ENDED: startSpawn");
	}

	/**
	 * Used to build a Set of amount + CreepID
	 * 
	 * @author Rolf
	 * 
	 */
	public static class Creeps {
		private int amount;
		private String creepid;

		public Creeps(int a, String c) {
			this.amount = a;
			this.creepid = c;
		}

		public void lowerAmount() {
			amount--;
		}

		public int getAmount() {
			return amount;
		}

		public String getCreepid() {
			return creepid;
		}

		public String toString() {
			return "Amount: " + amount + " CreepId: " + creepid;
		}
	}

	/**
	 * Spawn Mode is used by the Spawner to determine in what order the Creep
	 * Types should be spawned
	 * 
	 * @author Rolf
	 * 
	 */
	public enum SpawnMode {
		/**
		 * Order: Creeps[0], Creeps[0], Creeps[0], Creeps[0],
		 * Creeps[0],Creeps[0] ... then Creeps[1],Creeps[1],Creeps[1],Creeps[1]
		 * ...
		 */
		ROW,
		/**
		 * Order: Creeps[0], Creeps[1], Creeps[0], Creeps[1], Creeps[0],
		 * Creeps[1], Creeps[0], Creeps[1] ...
		 */
		MIXED,
		/**
		 * Order: In completly random order. ID for the Creeps Array is chosen
		 * Randomly and after each spawn the amount of Creeps for this ID is
		 * reduced by one
		 */
		RANDOM
	}

}
