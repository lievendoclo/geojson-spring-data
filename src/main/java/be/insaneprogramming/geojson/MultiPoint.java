package be.insaneprogramming.geojson;

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

    MultiPoint(Object object) {
        super(object);
    }
}
