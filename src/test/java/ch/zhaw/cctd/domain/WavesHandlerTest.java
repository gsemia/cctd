package ch.zhaw.cctd.domain;

import org.junit.Test;

import ch.zhaw.cctd.domain.prototype.CreepPrototype;
import ch.zhaw.cctd.domain.prototype.WavePrototype;

import junit.framework.TestCase;

/**
 * Tests the WaveHandler Thread and the Wave Spawning
 * @author Rolf
 *
 */
public class WavesHandlerTest extends TestCase {
	WavesHandler wavesHandler;
	
	private class MapMock extends Map {
		public MapMock() { 
			super(null); // TODO: ???
			reset(); 
		}
		public void reset() {
			spawnCreepCount = 0;
		}
		public int spawnCreepCount = 0;
		public void spawnCreep(CreepPrototype c) {
			this.spawnCreepCount++;
		}
	}
	
	private MapMock map = new MapMock();
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		map.reset();
		Match match = new Match(null, this.getClass().getResourceAsStream("wave-testing.xml"));
		match.setMap(this.map);
		wavesHandler = new WavesHandler(match, match.getPrototypeList().getPrototypes(WavePrototype.class));
	}
	
	//FIXME: Dieser Test ist eventuell viel zu gross und testet viel zu viel. Müsste man eventuell irgendwie eingrenzen
	@Test(timeout=12)
	public void testStartWaveHandling() throws Exception {
		wavesHandler.setWorking(true);
		wavesHandler.startWaveHandling();
		assertEquals(10, this.map.spawnCreepCount);
	}
	
	public void testStop() throws Exception {
		wavesHandler.stop();
		assertEquals(wavesHandler.isWorking(), false);
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		wavesHandler=null;
		
	}
	
}
	
