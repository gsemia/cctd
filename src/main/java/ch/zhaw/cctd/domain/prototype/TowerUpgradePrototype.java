package ch.zhaw.cctd.domain.prototype;

import java.util.List;
import java.util.Map;

public class TowerUpgradePrototype extends Prototype {

	public TowerUpgradePrototype(Map<String, String> data) {
		super(data);
	}
	
	public String getImagePath() {
		return this.getValue("image.path");
	}
	
	public int getAddedDamage() {
		return this.getIntegerValue("added-damage.value");
	}
	
	public List<String> getAddedEffects() {
		return null; // TODO
	}

}
