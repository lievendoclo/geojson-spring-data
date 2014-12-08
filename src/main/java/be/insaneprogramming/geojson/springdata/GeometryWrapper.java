package be.insaneprogramming.geojson.springdata;

import be.insaneprogramming.geojson.GeoJsonConverters;
import be.insaneprogramming.geojson.GeoJsonObject;
import com.mongodb.BasicDBObject;

public class GeometryWrapper extends BasicDBObject {

    public GeometryWrapper(GeoJsonObject<?> geoJsonObject) {
        this.put("$geometry", GeoJsonConverters.GeoJsonObjectToDBObjectConverter.INSTANCE.convert(geoJsonObject));
    }
}
