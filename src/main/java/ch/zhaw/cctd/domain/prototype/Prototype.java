package ch.zhaw.cctd.domain.prototype;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Prototype {
		
	private Map<String,String> prototypeData = new HashMap<String,String>();
	
	public Prototype(Map<String,String> data) {
		this.prototypeData.putAll(data);
	}
	
	protected String getValue(String key) {
		return this.prototypeData.get(key);
	}
	
	protected Integer getIntegerValue(String key) {
		if (this.getValue(key) == null) return null;
		return Integer.parseInt(this.getValue(key));
	}
	
	protected List<String> getListValue(String key) {
		return Arrays.asList(this.getValue(key).split("[;]"));
	}
	
	protected static abstract class DataListCallback<T> {
		public abstract T call(String... values);
	}
	
	protected <T> List<T> getDataList(
			String basekey, DataListCallback<T> callback, String... subkeys
	) {
		List<T> datalist = new ArrayList<T>();
		Integer count = this.getIntegerValue(basekey);
		if (count == null) {
			String[] values = new String[subkeys.length];
			for (int i = 0;i < subkeys.length;i++) {
				values[i] = this.getValue(basekey+"."+subkeys[i]);
			}
			datalist.add(callback.call(values));
		} else {
			for (int index = 0;index < count;index++) {
				String[] values = new String[subkeys.length];
				for (int i = 0;i < subkeys.length;i++) {
					values[i] = this.getValue(basekey+"."+index+"."+subkeys[i]);
				}
				datalist.add(callback.call(values));
			}
		}
		return datalist;
	}
	
	public String getId() {
		return this.getValue("id");
	}
	
}
