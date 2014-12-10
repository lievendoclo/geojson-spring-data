package be.insaneprogramming.geojson;

import java.util.Collection;
import java.util.List;

public class Polygon extends GeoJsonObject<List<Point>> {
    @Override
    public String getType() {
        return "Polygon";
    }

    public Polygon() {
    }

    public Polygon(Collection<? extends List<Point>> c) {
        super(c);
    }

    public Polygon(List<Point> points) {
        add(points);
    }

    Polygon(Object object) {
        super(object);
    }
}