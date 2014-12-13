package be.insaneprogramming.geojson;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A GeometryCollection is a geometry consisting of multiple different geometries. Any
 * geometry can be part of a GeometryCollection.
 */
public class GeometryCollection extends AbstractGeoJsonObject<GeoJsonObject<?>> {
    public GeometryCollection() {
    }

    public GeometryCollection(Collection<? extends AbstractGeoJsonObject<?>> c) {
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
