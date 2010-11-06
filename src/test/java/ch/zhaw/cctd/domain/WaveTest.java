package ch.zhaw.cctd.domain;

import java.util.ArrayList;

import ch.zhaw.cctd.domain.prototype.WavePrototype;
import ch.zhaw.cctd.domain.prototype.WavePrototype.WaveCreepPrototype;

import junit.framework.TestCase;

/**
 * Tests the WaveHandler Thread and the Wave Spawning
 * @author Rolf
 *
 */
public class WaveTest extends TestCase {
	
	public void testNewWave() throws Exception {
		final String waveName = "10 Basic Bunnys";
		final int waveSpawnLength = 10;
		final int waveSpawnTime = 0;
		
		final Wave.SpawnMode _SpawnMode = Wave.SpawnMode.ROW;
		
		final int creepCount = 10;
		final String creepName= "basic-bunny";
		final int creepWavesCount = 1;
		
		
		
		ArrayList<Wave.Creeps> creepSet = new ArrayList<Wave.Creeps>();
		for(int i = 0; i<creepWavesCount; i++) {
			creepSet.add(new Wave.Creeps(creepCount,creepName));
		}
		Match match = new Match(null, this.getClass().getResourceAsStream("wave-testing.xml"));
		Wave w = new Wave(match, match.getPrototype(WavePrototype.class, "1"));
		assertEquals(w.getName(), waveName);
		assertEquals(w.getSpawnLength(), waveSpawnLength);
		assertEquals(w.getSpawnTime(), waveSpawnTime);
		assertEquals(w.getSpawnMode(), _SpawnMode);
		assertEquals(w.getCreepSets().size(), creepWavesCount);
		for (WaveCreepPrototype c : w.getCreepSets()) {
			assertEquals(c.amount, creepCount);
			assertEquals(c.id, creepName);
		}
	}
	
}
	
