package ch.zhaw.cctd.domain.prototype;

import java.util.List;
import java.util.Map;

public class TowerPrototype extends Prototype {
	
	public static class TowerEffectPrototype {
		public TowerEffectPrototype(String type, String value) {
			this.type = type;
			this.value = value;
		}
		public final String type;
		public final String value;
	}
	
	public TowerPrototype(Map<String,String> data, List<TowerUpgradePrototype> upgrades) {
		super(data);
		this.upgrades = upgrades;
		this.effects = this.getDataList("effects.effect", 
				new DataListCallback<TowerEffectPrototype>() {
					@Override
					public TowerEffectPrototype call(String... values) {
						return new TowerEffectPrototype(values[0], values[1]);
					}
			}, "type", "value");
	}
	
	private List<TowerUpgradePrototype> upgrades;
	private List<TowerEffectPrototype> effects;
	
	public String getTitle() {
		return this.getValue("title");
	}
	
	public String getImagePath() {
		return this.getValue("image.path");
	}
	
	public int getDamage() {
		return this.getIntegerValue("damage.#text");
	}
	
	public List<String> getAbilities() {
		return this.getListValue("abilities.ability.type");
	}
	
	public List<TowerEffectPrototype> getEffects() {
		return this.effects;
	}
	
	public List<TowerUpgradePrototype> getUpgrades() {
		return this.upgrades;
	}
	
}
