package be.insaneprogramming.geojson.springdata;

import be.insaneprogramming.geojson.*;
import com.mongodb.BasicDBObject;

public class GeometryWrapper extends BasicDBObject {

    public GeometryWrapper(AbstractGeoJsonObject<?> geoJsonObject) {
        switch(geoJsonObject.getClass().getSimpleName()) {
            case "Point":
                this.put("$geometry", GeoJsonConverters.PointToDBObjectConverter.INSTANCE.convert((Point) geoJsonObject));
                break;
            case "Polygon":
                this.put("$geometry", GeoJsonConverters.PolygonToDBObjectConverter.INSTANCE.convert((Polygon) geoJsonObject));
                break;
            case "LineString":
                this.put("$geometry", GeoJsonConverters.LineStringToDBObjectConverter.INSTANCE.convert((LineString) geoJsonObject));
                break;
            case "MultiPoint":
                this.put("$geometry", GeoJsonConverters.MultiPointToDBObjectConverter.INSTANCE.convert((MultiPoint) geoJsonObject));
                break;
            case "MultiLineString":
                this.put("$geometry", GeoJsonConverters.MultiLineStringToDBObjectConverter.INSTANCE.convert((MultiLineString) geoJsonObject));
                break;
            case "MultiPolygon":
                this.put("$geometry", GeoJsonConverters.MultiPolygonToDBObjectConverter.INSTANCE.convert((MultiPolygon) geoJsonObject));
                break;
        }
    }
}
