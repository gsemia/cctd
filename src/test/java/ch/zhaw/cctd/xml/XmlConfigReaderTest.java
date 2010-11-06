package ch.zhaw.cctd.xml;

import java.io.InputStream;
import java.util.List;

import ch.zhaw.cctd.domain.prototype.PrototypeList;
import ch.zhaw.cctd.domain.prototype.TowerPrototype;

import junit.framework.TestCase;

public class XmlConfigReaderTest extends TestCase {
	
	public void testRead() throws Exception {
		InputStream instream = this.getClass().getResourceAsStream("test-cctd.xml");
		PrototypeList data = XmlConfigReader.readData(instream);
		assertNotNull(data);
		assertEquals(5, data.size());
		// check tower
		List<TowerPrototype> towers = data.getPrototypes(TowerPrototype.class);
		assertNotNull(towers);
		assertEquals(1, towers.size());
//		List<List<String>> towerEffects = towers.get(0).getEffects();
//		assertEquals(1, towerEffects.size());
//		assertEquals("range", towerEffects.get(0).get(0));
	}
	
}
