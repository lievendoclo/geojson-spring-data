package be.insaneprogramming.geojson;

import be.insaneprogramming.util.EmbeddedMongoDbTest;
import com.mongodb.DBObject;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class GeoJsonConvertersTest extends EmbeddedMongoDbTest {

    @Test
    public void testPointConverter() {
        Point point = new Point(100., 200.);
        DBObject object = GeoJsonConverters.PointToDBObjectConverter.INSTANCE.convert(point);
        getTestDatabase().getCollection("test").save(object);
        DBObject dbObject = getTestDatabase().getCollection("test").findOne();
        Point dbPoint = GeoJsonConverters.DBObjectToPointConverter.INSTANCE.convert(dbObject);
        assertThat(dbPoint, equalTo(point));
    }

    @Test
    public void testMultiPointConverter() {
        List<Point> points = Arrays.asList(
                new Point(100., 200.),
                new Point(300., 400.));
        MultiPoint multiPoint = new MultiPoint(points);
                DBObject object = GeoJsonConverters.MultiPointToDBObjectConverter.INSTANCE.convert(multiPoint);
        getTestDatabase().getCollection("test").save(object);
        DBObject dbObject = getTestDatabase().getCollection("test").findOne();
        MultiPoint dbMultiPoint = GeoJsonConverters.DBObjectToMultiPointConverter.INSTANCE.convert(dbObject);
        assertThat(dbMultiPoint, equalTo(multiPoint));
    }

    @Test
    public void testLineStringConverter() {
        List<Point> points = Arrays.asList(
                new Point(100., 200.),
                new Point(300., 400.));
        LineString multiPoint = new LineString(points);
        DBObject object = GeoJsonConverters.LineStringToDBObjectConverter.INSTANCE.convert(multiPoint);
        getTestDatabase().getCollection("test").save(object);
        DBObject dbObject = getTestDatabase().getCollection("test").findOne();
        LineString dbMultiPoint = GeoJsonConverters.DBObjectToLineStringConverter.INSTANCE.convert(dbObject);
        assertThat(dbMultiPoint, equalTo(multiPoint));
    }

    @Test
    public void testSimplePolygonConverter() {
        Polygon polygon = new Polygon(Arrays.asList(new Point(300., 400.), new Point(400., 500.), new Point(500., 600.)));
        DBObject object = GeoJsonConverters.PolygonToDBObjectConverter.INSTANCE.convert(polygon);
        getTestDatabase().getCollection("test").save(object);
        DBObject dbObject = getTestDatabase().getCollection("test").findOne();
        Polygon dbPolygon = GeoJsonConverters.DBObjectToPolygonConverter.INSTANCE.convert(dbObject);
        assertThat(dbPolygon, equalTo(polygon));
    }

    @Test
    public void testExtendedPolygonWithInnerRingsConverter() {
        List<List<Point>> points = Arrays.asList(
                Arrays.asList(new Point(100., 200.), new Point(200., 300.)),
                Arrays.asList(new Point(300., 400.), new Point(400., 500.)));
        Polygon polygon = new Polygon(points);
        DBObject object = GeoJsonConverters.PolygonToDBObjectConverter.INSTANCE.convert(polygon);
        getTestDatabase().getCollection("test").save(object);
        DBObject dbObject = getTestDatabase().getCollection("test").findOne();
        Polygon dbPolygon = GeoJsonConverters.DBObjectToPolygonConverter.INSTANCE.convert(dbObject);
        assertThat(dbPolygon, equalTo(polygon));
    }

    @Test
    public void testMultiLineStringConverter() {
        List<LineString> points = Arrays.asList(
                new LineString(Arrays.asList(new Point(100., 200.), new Point(200., 300.))),
                new LineString(Arrays.asList(new Point(300., 400.), new Point(400., 500.))));
        MultiLineString multiLineString = new MultiLineString(points);
        DBObject object = GeoJsonConverters.MultiLineStringToDBObjectConverter.INSTANCE.convert(multiLineString);
        getTestDatabase().getCollection("test").save(object);
        DBObject dbObject = getTestDatabase().getCollection("test").findOne();
        MultiLineString dbMultiLineString = GeoJsonConverters.DBObjectToMultiLineStringConverter.INSTANCE.convert(dbObject);
        assertThat(dbMultiLineString, equalTo(multiLineString));
    }

    @Test
    public void testMultiPolygonConverter() {
        List<Polygon> polygons = Arrays.asList(
               new Polygon(Arrays.asList(new Point(300., 400.), new Point(400., 500.), new Point(500., 600.))),
                new Polygon(Arrays.asList(new Point(300., 400.), new Point(400., 500.), new Point(500., 600.))));
        MultiPolygon multiPolygon = new MultiPolygon(polygons);
        DBObject object = GeoJsonConverters.MultiPolygonToDBObjectConverter.INSTANCE.convert(multiPolygon);
        getTestDatabase().getCollection("test").save(object);
        DBObject dbObject = getTestDatabase().getCollection("test").findOne();
        MultiPolygon dbMultiPolygon  = GeoJsonConverters.DBObjectToMultiPolygonConverter.INSTANCE.convert(dbObject);
        assertThat(dbMultiPolygon, equalTo(multiPolygon));
    }

    @Test
    public void testGeometryCollectionConverter() {
        Point point = new Point(100., 200.);
        MultiPoint multiPoint = new MultiPoint(Arrays.asList(
                new Point(100., 200.),
                new Point(300., 400.)));
        LineString lineString = new LineString(Arrays.asList(
                new Point(100., 200.),
                new Point(300., 400.)));
        Polygon polygon = new Polygon(Arrays.asList(new Point(300., 400.), new Point(400., 500.), new Point(500., 600.)));
        Polygon extendedPolygon = new Polygon(Arrays.asList(
                Arrays.asList(new Point(100., 200.), new Point(200., 300.)),
                Arrays.asList(new Point(300., 400.), new Point(400., 500.))));
        MultiLineString multiLineString = new MultiLineString(Arrays.asList(
                new LineString(Arrays.asList(new Point(100., 200.), new Point(200., 300.))),
                new LineString(Arrays.asList(new Point(300., 400.), new Point(400., 500.)))));
        MultiPolygon multiPolygon = new MultiPolygon(Arrays.asList(
                new Polygon(Arrays.asList(new Point(300., 400.), new Point(400., 500.), new Point(500., 600.))),
                new Polygon(Arrays.asList(new Point(300., 400.), new Point(400., 500.), new Point(500., 600.)))));
        GeometryCollection collection = new GeometryCollection(Arrays.asList(point, multiPoint, lineString, polygon, extendedPolygon, multiLineString, multiPolygon));
        DBObject object = GeoJsonConverters.GeometryCollectionToDBObjectConverter.INSTANCE.convert(collection);
        getTestDatabase().getCollection("test").save(object);
        DBObject dbObject = getTestDatabase().getCollection("test").findOne();
        GeometryCollection geometryCollection  = GeoJsonConverters.DBObjectToGeometryCollectionConverter.INSTANCE.convert(dbObject);
        assertThat(geometryCollection.get(0).getType(), equalTo("Point"));
        assertThat(geometryCollection.get(1).getType(), equalTo("MultiPoint"));
        assertThat(geometryCollection.get(2).getType(), equalTo("LineString"));
        assertThat(geometryCollection.get(3).getType(), equalTo("Polygon"));
        assertThat(geometryCollection.get(4).getType(), equalTo("Polygon"));
        assertThat(geometryCollection.get(5).getType(), equalTo("MultiLineString"));
        assertThat(geometryCollection.get(6).getType(), equalTo("MultiPolygon"));
    }
}
