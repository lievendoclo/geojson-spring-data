package be.insaneprogramming.geojson;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MultiPolygon extends GeoJsonObject<Polygon> {
    @Override
    public String getType() {
        return "MultiPolygon";
    }

    public MultiPolygon() {
    }

    public MultiPolygon(Collection<? extends Polygon> c) {
        super(c);
    }

    public MultiPolygon(Polygon... polygons) {
        this(Arrays.asList(polygons));
    }

    MultiPolygon(Object object) {
        super(object);
    }
}
