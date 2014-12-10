package be.insaneprogramming.geojson;

import java.util.Arrays;
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

    public MultiLineString(LineString... lineStrings) {
        this(Arrays.asList(lineStrings));
    }

    MultiLineString(Object object) {
        super(object);
    }
}
