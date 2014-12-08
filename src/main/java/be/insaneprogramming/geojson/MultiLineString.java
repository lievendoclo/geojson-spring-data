package be.insaneprogramming.geojson;

import java.util.Collection;

public class MultiLineString extends GeoJsonObject<LineString> {
    @Override
    public String getType() {
        return "MultiLineString";
    }

    public MultiLineString() {
    }

    public MultiLineString(Collection<? extends LineString> c) {
        super(c);
    }

    MultiLineString(Object object) {
        super(object);
    }
}
