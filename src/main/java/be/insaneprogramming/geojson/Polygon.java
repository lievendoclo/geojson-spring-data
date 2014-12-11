package be.insaneprogramming.geojson;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

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
    public void validate(PointValidator pointValidator) {
        if(this.size() == 0 ) throw new IllegalStateException("A Polygon must contain at least 1 ring");
        for (List<Point> ring : this) {
            if(ring.size() < 4) throw new IllegalStateException("A ring must contain at least 4 points");
            for (Point point : ring) {
                pointValidator.validate(point);
            }
            Point first = ring.get(0);
            Point last = ring.get(ring.size() - 1);
            if(!Objects.equals(first, last)) throw new IllegalStateException("The first and last postion of a ring must be the same");
        }
    }

    @Override
    public String getType() {
        return "Polygon";
    }
}