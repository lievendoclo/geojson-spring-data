package be.insaneprogramming.geojson;

import java.util.ArrayList;
import java.util.Collection;

public class GeometryCollection extends ArrayList<GeoJsonObject<?>> {
    public GeometryCollection() {
    }

    public GeometryCollection(Collection<? extends GeoJsonObject<?>> c) {
        super(c);
    }

    public String getType() {
        return "GeometryCollection";
    }

    public void validate(PointValidator validator) {
        for (GeoJsonObject<?> geoJsonObject : this) {
            geoJsonObject.validate(validator);
        }
    }
}
