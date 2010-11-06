package ch.zhaw.cctd.domain.prototype;

import java.util.List;
import java.util.Map;

public class CreepPrototype extends Prototype {
	
	public CreepPrototype(Map<String,String> data) {
		super(data);
	}
	
	public String getTitle() {
		return this.getValue("title");
	}
	
	public String getImagePath() {
		return this.getValue("image.path");
	}
	
	public int getLife() {
		return this.getIntegerValue("life.#text");
	}
	
	public int getSpeed() {
		return this.getIntegerValue("speed#text");
	}

	public List<String> getAbilities() {
		return this.getListValue("abilities.ability.type");
	}
}
