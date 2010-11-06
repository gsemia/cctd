package ch.zhaw.cctd.domain.prototype;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class PrototypeList extends AbstractList<Prototype> {

	public PrototypeList(List<Prototype> data) {
		this.data = data.toArray(new Prototype[data.size()]);
	}
	
	private final Prototype[] data;
	
	@SuppressWarnings("unchecked")
	public <T extends Prototype> List<T> getPrototypes(Class<T> clazz) {
		List<T> pdata = new ArrayList<T>();
		if (clazz == null) return pdata;
		for (Prototype p : this.data) {
			if (clazz.equals(p.getClass()))
				pdata.add((T)p);
		}
		return pdata;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Prototype> T getPrototype(Class<T> clazz, String id) {
		if (clazz == null) return null;
		for (Prototype p : this.data) {
			if (clazz.equals(p.getClass()) && p.getId().equals(id))
				return (T)p;
		}
		return null;
	}
	
	@Override
	public Prototype get(int index) {
		return this.data[index];
	}

	@Override
	public int size() {
		return this.data.length;
	}

}
