package be.insaneprogramming.geojson;

import java.util.Collection;

public class LineString extends MultiPoint {
    public LineString() {
    }

    public LineString(Collection<? extends Point> c) {
        super(c);
    }

    LineString(Object object) {
        super(object);
    }

    @Override
    public String getType() {
        return "LineString";
    }
}
