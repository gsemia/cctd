package ch.zhaw.cctd.domain.prototype;

import java.util.List;
import java.util.Map;

public class WavePrototype extends Prototype {
	
	public static class WaveCreepPrototype {
		public WaveCreepPrototype(String id, int amount) {
			this.id = id;
			this.amount = amount;
		}
		public final String id;
		public final int amount;
	}
	
	public WavePrototype(Map<String,String> data) {
		super(data);
		this.wavecreeps = this.getDataList("creeps.creep", 
				new Prototype.DataListCallback<WaveCreepPrototype>() {
					@Override
					public WaveCreepPrototype call(String... values) {
						return new WaveCreepPrototype(values[0], Integer.parseInt(values[1]));
					}
				}, "refid", "amount");
	}
	
	private List<WaveCreepPrototype> wavecreeps;
	
	public String getName() {
		return this.getValue("name.#text");
	}
	
	public int getSpawntime() {
		return this.getIntegerValue("spawntime.#text");
	}
	
	public int getSpawnlength() {
		return this.getIntegerValue("spawnlength.#text");
	}
	
	public String getSpawnmode() {
		return this.getValue("creeps.spawnmode");
	}
	
	public List<WaveCreepPrototype> getCreeps() {
		return this.wavecreeps;
	}
	
}
