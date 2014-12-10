package be.insaneprogramming.geojson;

import java.util.Arrays;
import java.util.List;

public class Point extends GeoJsonObject<Double> implements List<Double> {
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
	public String getType() {
		return "Point";
	}

	public Point() {
	}
}