package be.insaneprogramming.geojson;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

// TODO: provide validation of valid polygon state
public class Polygon extends GeoJsonObject<List<Point>> {
    public Polygon() {
    }

    public Polygon(Collection<? extends List<Point>> c) {
        super(c);
    }

    public Polygon(List<Point> points) {
        add(points);
    }

    public Polygon(Point... points) {
        add(Arrays.asList(points));
    }

    public Polygon(LineString lineString) {
        addLineString(lineString);
    }

    public Polygon(LineString... lineStrings) {
        for (LineString lineString : lineStrings) {
            addLineString(lineString);
        }
    }

    void addLineString(LineString lineString) {
        add(lineString);
    }

    Polygon(Object object) {
        super(object);
    }

    @Override
    public String getType() {
        return "Polygon";
    }
}