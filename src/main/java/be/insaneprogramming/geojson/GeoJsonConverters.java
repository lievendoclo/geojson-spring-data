package be.insaneprogramming.geojson;

import be.insaneprogramming.geojson.*;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.core.convert.converter.Converter;

import java.util.ArrayList;
import java.util.List;

public class GeoJsonConverters {
    private GeoJsonConverters() {}

    public static List<Converter<?, ?>> getConvertersToRegister() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(GeoJsonObjectToDBObjectConverter.INSTANCE);
        converters.add(DBObjectToPointConverter.INSTANCE);
        converters.add(DBObjectToPolygonConverter.INSTANCE);
        converters.add(DBObjectToLineStringConverter.INSTANCE);
        converters.add(DBObjectToMultiPointConverter.INSTANCE);
        converters.add(DBObjectToMultiLineStringConverter.INSTANCE);
        converters.add(DBObjectToMultiPolygonStringConverter.INSTANCE);
        return converters;
    }

    public static enum GeoJsonObjectToDBObjectConverter implements Converter<GeoJsonObject, DBObject> {
        INSTANCE;

        @Override
        public DBObject convert(GeoJsonObject source) {
            DBObject dbObject = new BasicDBObject();
            dbObject.put("type", source.getType());
            dbObject.put("coordinates", source);
            return dbObject;
        }
    }

    public static enum DBObjectToPointConverter implements Converter<DBObject, Point> {
        INSTANCE;

        @Override
        public Point convert(DBObject source) {
            return new Point(source.get("coordinates"));
        }
    }

    public static enum DBObjectToLineStringConverter implements Converter<DBObject, LineString> {
        INSTANCE;

        @Override
        public LineString convert(DBObject source) {
            return new LineString(source.get("coordinates"));
        }
    }

    public static enum DBObjectToPolygonConverter implements Converter<DBObject, Polygon> {
        INSTANCE;

        @Override
        public Polygon convert(DBObject source) {
            return new Polygon(source.get("coordinates"));
        }
    }

    public static enum DBObjectToMultiPointConverter implements Converter<DBObject, MultiPoint> {
        INSTANCE;

        @Override
        public MultiPoint convert(DBObject source) {
            return new MultiPoint(source.get("coordinates"));
        }
    }

    public static enum DBObjectToMultiLineStringConverter implements Converter<DBObject, MultiLineString> {
        INSTANCE;

        @Override
        public MultiLineString convert(DBObject source) {
            return new MultiLineString(source.get("coordinates"));
        }
    }

    public static enum DBObjectToMultiPolygonStringConverter implements Converter<DBObject, MultiPolygon> {
        INSTANCE;

        @Override
        public MultiPolygon convert(DBObject source) {
            return new MultiPolygon(source.get("coordinates"));
        }
    }
}
