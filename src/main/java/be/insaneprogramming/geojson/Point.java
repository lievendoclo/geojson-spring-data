package be.insaneprogramming.geojson;

import java.util.Arrays;
import java.util.List;

public class Point extends GeoJsonObject<Double> {
	public Point(List<Double> coordinates) {
		super(coordinates);
	}

	public Point(Double... coordinates) {
		super(Arrays.asList(coordinates));
	}

	public Point(Object o) {
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

	Double[] getCoordinates() {
		return this.toArray(new Double[this.size()]);
	}
}