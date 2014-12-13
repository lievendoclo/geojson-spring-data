package be.insaneprogramming.geojson;

import java.util.*;

public abstract class AbstractGeoJsonObject<T> extends ArrayList<T> implements GeoJsonObject<T> {

	private CoordinateReferenceSystem coordinateReferenceSystem;
	private double[] boundingBox;

	protected AbstractGeoJsonObject() {
	}

	protected AbstractGeoJsonObject(Collection<? extends T> c) {
		super(c);
	}

	AbstractGeoJsonObject(Object o) {
		this((Collection<? extends T>) o);
	}

	private Map<String, Object> properties = new HashMap<String, Object>();

	@Override
	public CoordinateReferenceSystem getCoordinateReferenceSystem() {
		return coordinateReferenceSystem;
	}

	public void setCoordinateReferenceSystem(CoordinateReferenceSystem coordinateReferenceSystem) {
		this.coordinateReferenceSystem = coordinateReferenceSystem;
	}

	@Override
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

	@Override
	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

}
