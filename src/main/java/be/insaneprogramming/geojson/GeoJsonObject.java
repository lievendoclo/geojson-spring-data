package be.insaneprogramming.geojson;

import java.util.List;
import java.util.Map;


public interface GeoJsonObject<T> extends List<T> {
	String getType();

	CoordinateReferenceSystem getCoordinateReferenceSystem();

	double[] getBoundingBox();

	Map<String, Object> getProperties();

	/**
	 * Validate this geometry and the points within it.
	 * @param pointValidator The validator which validates the individual points
	 */
	void validate(PointValidator pointValidator);
}
