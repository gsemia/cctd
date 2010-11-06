package ch.zhaw.cctd.domain.prototype;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import junit.framework.TestCase;

public class PrototypeListTest extends TestCase {
	
	public void testGetEmpty() throws Exception {
		PrototypeList list = new PrototypeList(new ArrayList<Prototype>());
		assertEquals(0, list.size());
		List<TowerPrototype> towers = list.getPrototypes(TowerPrototype.class);
		assertNotNull(towers);
		assertEquals(0, towers.size());
	}
	
	public void testGetPrototypes() throws Exception {
		PrototypeList list = new PrototypeList(
			Arrays.asList(new Prototype[] {
				new TowerPrototype(new HashMap<String,String>(), new ArrayList<TowerUpgradePrototype>())
			})
		);
		List<TowerPrototype> towers = list.getPrototypes(TowerPrototype.class);
		assertNotNull(towers);
		assertEquals(1, towers.size());
	}
	
}
