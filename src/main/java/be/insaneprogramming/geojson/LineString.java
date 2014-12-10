package be.insaneprogramming.geojson;

import java.util.Arrays;
import java.util.Collection;

public class LineString extends MultiPoint {
    public LineString() {
    }

    public LineString(Collection<? extends Point> c) {
        super(c);
    }

    public LineString(Point... points) {
        this(Arrays.asList(points));
    }

    LineString(Object object) {
        super(object);
    }

    @Override
    public String getType() {
        return "LineString";
    }
}
