package be.insaneprogramming.geojson;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * List of converters to be used with Spring Data to convert GeoJSON class instances
 * into MongoDB DBObject instances
 */
public class GeoJsonConverters {
    private GeoJsonConverters() {}

    /**
     * Get all the converters for GeoJSON to DBObject conversion and vice versa.
     * @return A list of all the converters
     */
    public static List<Converter<?, ?>> getConvertersToRegister() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(PointToDBObjectConverter.INSTANCE);
        converters.add(LineStringToDBObjectConverter.INSTANCE);
        converters.add(PolygonToDBObjectConverter.INSTANCE);
        converters.add(MultiLineStringToDBObjectConverter.INSTANCE);
        converters.add(MultiPointToDBObjectConverter.INSTANCE);
        converters.add(MultiPolygonToDBObjectConverter.INSTANCE);
        converters.add(GeometryCollectionToDBObjectConverter.INSTANCE);
        converters.add(DBObjectToPointConverter.INSTANCE);
        converters.add(DBObjectToPolygonConverter.INSTANCE);
        converters.add(DBObjectToLineStringConverter.INSTANCE);
        converters.add(DBObjectToMultiPointConverter.INSTANCE);
        converters.add(DBObjectToMultiLineStringConverter.INSTANCE);
        converters.add(DBObjectToMultiPolygonConverter.INSTANCE);
        converters.add(DBObjectToGeometryCollectionConverter.INSTANCE);
        return converters;
    }

    private static class ConverterUtil {
        private ConverterUtil() {}

        public static void addBasicProperties(GeoJsonObject object, DBObject dbObject) {
            dbObject.put("type", object.getType());
            if(object.getBoundingBox() != null && object.getBoundingBox().length > 0) {
                dbObject.put("bbox", object.getBoundingBox());
            }
            if(object.getProperties() != null && object.getProperties().size() > 0) {
                dbObject.put("properties", object.getProperties());
            }
        }
    }

    @WritingConverter
    public static enum PointToDBObjectConverter implements Converter<Point, DBObject> {
        INSTANCE;

        @Override
        public DBObject convert(Point source) {
            DBObject dbObject = new BasicDBObject();
            ConverterUtil.addBasicProperties(source, dbObject);
            dbObject.put("coordinates", source.getCoordinates());
            return dbObject;
        }
    }

    @WritingConverter
    public static enum LineStringToDBObjectConverter implements Converter<LineString, DBObject> {
        INSTANCE;

        @Override
        public DBObject convert(LineString source) {
            DBObject dbObject = new BasicDBObject();
            ConverterUtil.addBasicProperties(source, dbObject);
            BasicDBList list = new BasicDBList();
            for (Point point : source) {
                list.add(point.getCoordinates());
            }
            dbObject.put("coordinates", list);
            return dbObject;
        }
    }

    @WritingConverter
    public static enum PolygonToDBObjectConverter implements Converter<Polygon, DBObject> {
        INSTANCE;

        @Override
        public DBObject convert(Polygon source) {
            DBObject dbObject = new BasicDBObject();
            ConverterUtil.addBasicProperties(source, dbObject);
            BasicDBList list = new BasicDBList();
            for (List<Point> lists : source) {
                BasicDBList innerList = new BasicDBList();
                    for (Point point : lists) {
                        innerList.add(point.getCoordinates());
                    }
                list.add(innerList);
            }
            dbObject.put("coordinates", list);
            return dbObject;
        }
    }

    @WritingConverter
    public static enum MultiPointToDBObjectConverter implements Converter<MultiPoint, DBObject> {
        INSTANCE;

        @Override
        public DBObject convert(MultiPoint source) {
            DBObject dbObject = new BasicDBObject();
            ConverterUtil.addBasicProperties(source, dbObject);
            BasicDBList list = new BasicDBList();
            for (Point point : source) {
                list.add(point.getCoordinates());
            }
            dbObject.put("coordinates", list);
            return dbObject;
        }
    }

    @WritingConverter
    public static enum MultiLineStringToDBObjectConverter implements Converter<MultiLineString, DBObject> {
        INSTANCE;

        @Override
        public DBObject convert(MultiLineString source) {
            DBObject dbObject = new BasicDBObject();
            ConverterUtil.addBasicProperties(source, dbObject);
            BasicDBList list = new BasicDBList();
            for (LineString lineString : source) {
                BasicDBList innerList = new BasicDBList();
                for (Point point : lineString) {
                    innerList.add(point.getCoordinates());
                }
                list.add(innerList);
            }
            dbObject.put("coordinates", list);
            return dbObject;
        }
    }

    @WritingConverter
    public static enum MultiPolygonToDBObjectConverter implements Converter<MultiPolygon, DBObject> {
        INSTANCE;

        @Override
        public DBObject convert(MultiPolygon source) {
            DBObject dbObject = new BasicDBObject();
            ConverterUtil.addBasicProperties(source, dbObject);
            BasicDBList list = new BasicDBList();
            for (Polygon polygon : source) {
                BasicDBList innerList = new BasicDBList();
                for (List<Point> pointList : polygon) {
                    BasicDBList innerInnerList = new BasicDBList();
                    for (Point point : pointList) {
                        innerInnerList.add(point.getCoordinates());
                    }
                    innerList.add(innerInnerList);
                }
                list.add(innerList);
            }
            dbObject.put("coordinates", list);
            return dbObject;
        }
    }

    @WritingConverter
    public static enum GeometryCollectionToDBObjectConverter implements Converter<GeometryCollection, DBObject> {
        INSTANCE;

        @Override
        public DBObject convert(GeometryCollection source) {
            DBObject dbObject = new BasicDBObject();
            ConverterUtil.addBasicProperties(source, dbObject);
            List<DBObject> geometries = new ArrayList<>();
            for (GeoJsonObject<?> object : source) {
                switch(object.getClass().getSimpleName()) {
                    case "Point":
                        geometries.add(PointToDBObjectConverter.INSTANCE.convert((Point) object));
                        break;
                    case "Polygon":
                        geometries.add(PolygonToDBObjectConverter.INSTANCE.convert((Polygon) object));
                        break;
                    case "LineString":
                        geometries.add(LineStringToDBObjectConverter.INSTANCE.convert((LineString) object));
                        break;
                    case "MultiPoint":
                        geometries.add(MultiPointToDBObjectConverter.INSTANCE.convert((MultiPoint) object));
                        break;
                    case "MultiLineString":
                        geometries.add(MultiLineStringToDBObjectConverter.INSTANCE.convert((MultiLineString) object));
                        break;
                    case "MultiPolygon":
                        geometries.add(MultiPolygonToDBObjectConverter.INSTANCE.convert((MultiPolygon) object));
                        break;
                }
            }
            dbObject.put("geometries", geometries);
            return dbObject;
        }
    }

    @ReadingConverter
    public static enum DBObjectToPointConverter implements Converter<DBObject, Point> {
        INSTANCE;

        @Override
        public Point convert(DBObject source) {
            return new Point(source.get("coordinates"));
        }
    }

    @ReadingConverter
    public static enum DBObjectToLineStringConverter implements Converter<DBObject, LineString> {
        INSTANCE;

        @Override
        public LineString convert(DBObject source) {
            return new LineString(source.get("coordinates"));
        }
    }

    @ReadingConverter
    public static enum DBObjectToPolygonConverter implements Converter<DBObject, Polygon> {
        INSTANCE;

        @Override
        public Polygon convert(DBObject source) {
            return new Polygon(source.get("coordinates"));
        }
    }

    @ReadingConverter
    public static enum DBObjectToMultiPointConverter implements Converter<DBObject, MultiPoint> {
        INSTANCE;

        @Override
        public MultiPoint convert(DBObject source) {
            return new MultiPoint(source.get("coordinates"));
        }
    }

    @ReadingConverter
    public static enum DBObjectToMultiLineStringConverter implements Converter<DBObject, MultiLineString> {
        INSTANCE;

        @Override
        public MultiLineString convert(DBObject source) {
            return new MultiLineString(source.get("coordinates"));
        }
    }

    @ReadingConverter
    public static enum DBObjectToMultiPolygonConverter implements Converter<DBObject, MultiPolygon> {
        INSTANCE;

        @Override
        public MultiPolygon convert(DBObject source) {
            return new MultiPolygon(source.get("coordinates"));
        }
    }

    @ReadingConverter
    public static enum DBObjectToGeometryCollectionConverter implements Converter<DBObject, GeometryCollection> {
        INSTANCE;

        @Override
        public GeometryCollection convert(DBObject source) {
            GeometryCollection geometryCollection = new GeometryCollection();
            BasicDBList dbObjects = (BasicDBList) source.get("geometries");
            for (Object dbObject : dbObjects) {
                DBObject object = (DBObject) dbObject;
                switch(object.get("type").toString()) {
                    case "Point":
                        geometryCollection.add(DBObjectToPointConverter.INSTANCE.convert(object));
                        break;
                    case "Polygon":
                        geometryCollection.add(DBObjectToPolygonConverter.INSTANCE.convert(object));
                        break;
                    case "LineString":
                        geometryCollection.add(DBObjectToLineStringConverter.INSTANCE.convert(object));
                        break;
                    case "MultiPoint":
                        geometryCollection.add(DBObjectToMultiPointConverter.INSTANCE.convert(object));
                        break;
                    case "MultiLineString":
                        geometryCollection.add(DBObjectToMultiLineStringConverter.INSTANCE.convert(object));
                        break;
                    case "MultiPolygon":
                        geometryCollection.add(DBObjectToMultiPolygonConverter.INSTANCE.convert(object));
                        break;
                }
             }
            return geometryCollection;
        }
    }
}
