package be.insaneprogramming.geojson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class GeoJsonObject<T> extends ArrayList<T> {

	public abstract String getType();

	private CoordinateReferenceSystem coordinateReferenceSystem;
	private double[] boundingBox;

	public GeoJsonObject() {
	}

	public GeoJsonObject(Collection<? extends T> c) {
		super(c);
	}

	GeoJsonObject(Object o) {
		this((Collection<? extends T>) o);
	}

	private Map<String, Object> properties = new HashMap<String, Object>();

	public CoordinateReferenceSystem getCoordinateReferenceSystem() {
		return coordinateReferenceSystem;
	}

	public void setCoordinateReferenceSystem(CoordinateReferenceSystem coordinateReferenceSystem) {
		this.coordinateReferenceSystem = coordinateReferenceSystem;
	}

	public double[] getBoundingBox() {
		return boundingBox;
	}

	public void setBoundingBox(double[] boundingBox) {
		this.boundingBox = boundingBox;
	}

	public void setProperty(String key, Object value) {
		properties.put(key, value);
	}

	@SuppressWarnings("unchecked")
	public <T> T getProperty(String key) {
		return (T)properties.get(key);
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}
}
