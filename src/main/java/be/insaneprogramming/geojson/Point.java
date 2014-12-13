package be.insaneprogramming.geojson;

import java.util.Arrays;
import java.util.List;

/**
 * A Point is a geometry consisting of a single position
 */
public class Point extends AbstractGeoJsonObject<Double> {
	public Point(List<Double> coordinates) {
		super(coordinates);
	}

	public Point(Double... coordinates) {
		super(Arrays.asList(coordinates));
	}

	Point(Object o) {
		super(o);
	}

	@Override
	public void validate(PointValidator pointValidator) {
		pointValidator.validate(this);
	}

	@Override
	public String getType() {
		return "Point";
	}

	public Point() {
	}

	/**
	 * Get the coordinates for this point in the form of a array of doubles
	 * @return The coordinates for this point
	 */
	Double[] getCoordinates() {
		return this.toArray(new Double[this.size()]);
	}
}