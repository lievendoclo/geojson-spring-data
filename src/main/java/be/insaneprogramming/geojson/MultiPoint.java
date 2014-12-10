package be.insaneprogramming.geojson;

import java.util.Arrays;
import java.util.Collection;

public class MultiPoint extends GeoJsonObject<Point> {
    @Override
    public String getType() {
        return "MultiPoint";
    }

    public MultiPoint() {
    }

    public MultiPoint(Collection<? extends Point> c) {
        super(c);
    }

    public MultiPoint(Point... points) {
        this(Arrays.asList(points));
    }

    MultiPoint(Object object) {
        super(object);
    }
}
