package be.insaneprogramming.geojson;

import java.util.HashMap;
import java.util.Map;

public class CoordinateReferenceSystem {

	private String type = "name";
	private Map<String, Object> properties = new HashMap<String, Object>();

	public String getType() {
		return type;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}
}
