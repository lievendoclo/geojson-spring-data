package be.insaneprogramming.geojson;

import be.insaneprogramming.util.EmbeddedMongoDbTest;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class GeoJsonConvertersTest extends EmbeddedMongoDbTest {

    @Test
    public void testPointConverter() {
        Point point = new Point(100., 200.);
        DBObject object = GeoJsonConverters.GeoJsonObjectToDBObjectConverter.INSTANCE.convert(point);
        getTestDatabase().getCollection("test").save(object);
        DBObject dbObject = getTestDatabase().getCollection("test").findOne();
        System.out.println(JSON.serialize(dbObject));
        Point dbPoint = GeoJsonConverters.DBObjectToPointConverter.INSTANCE.convert(dbObject);
        Assert.assertThat(dbPoint, CoreMatchers.equalTo(point));
    }

    @Test
    public void testMultiPointConverter() {
        List<Point> points = Arrays.asList(
                new Point(100., 200.),
                new Point(300., 400.));
        MultiPoint multiPoint = new MultiPoint(points);
                DBObject object = GeoJsonConverters.GeoJsonObjectToDBObjectConverter.INSTANCE.convert(multiPoint);
        getTestDatabase().getCollection("test").save(object);
        DBObject dbObject = getTestDatabase().getCollection("test").findOne();
        System.out.println(JSON.serialize(dbObject));
        MultiPoint dbMultiPoint = GeoJsonConverters.DBObjectToMultiPointConverter.INSTANCE.convert(dbObject);
        Assert.assertThat(dbMultiPoint, CoreMatchers.equalTo(multiPoint));
    }

    @Test
    public void testLineStringConverter() {
        List<Point> points = Arrays.asList(
                new Point(100., 200.),
                new Point(300., 400.));
        LineString multiPoint = new LineString(points);
        DBObject object = GeoJsonConverters.GeoJsonObjectToDBObjectConverter.INSTANCE.convert(multiPoint);
        getTestDatabase().getCollection("test").save(object);
        DBObject dbObject = getTestDatabase().getCollection("test").findOne();
        System.out.println(JSON.serialize(dbObject));
        LineString dbMultiPoint = GeoJsonConverters.DBObjectToLineStringConverter.INSTANCE.convert(dbObject);
        Assert.assertThat(dbMultiPoint, CoreMatchers.equalTo(multiPoint));
    }

    @Test
    public void testSimplePolygonConverter() {
        Polygon polygon = new Polygon(Arrays.asList(new Point(300., 400.), new Point(400., 500.), new Point(500., 600.)));
        DBObject object = GeoJsonConverters.GeoJsonObjectToDBObjectConverter.INSTANCE.convert(polygon);
        getTestDatabase().getCollection("test").save(object);
        DBObject dbObject = getTestDatabase().getCollection("test").findOne();
        System.out.println(JSON.serialize(dbObject));
        Polygon dbPolygon = GeoJsonConverters.DBObjectToPolygonConverter.INSTANCE.convert(dbObject);
        Assert.assertThat(dbPolygon, CoreMatchers.equalTo(polygon));
    }

    @Test
    public void testExtendedPolygonWithInnerRingsConverter() {
        List<List<Point>> points = Arrays.asList(
                Arrays.asList(new Point(100., 200.), new Point(200., 300.)),
                Arrays.asList(new Point(300., 400.), new Point(400., 500.)));
        Polygon polygon = new Polygon(points);
        DBObject object = GeoJsonConverters.GeoJsonObjectToDBObjectConverter.INSTANCE.convert(polygon);
        getTestDatabase().getCollection("test").save(object);
        DBObject dbObject = getTestDatabase().getCollection("test").findOne();
        System.out.println(JSON.serialize(dbObject));
        Polygon dbPolygon = GeoJsonConverters.DBObjectToPolygonConverter.INSTANCE.convert(dbObject);
        Assert.assertThat(dbPolygon, CoreMatchers.equalTo(polygon));
    }

    @Test
    public void testMultiLineStringConverter() {
        List<LineString> points = Arrays.asList(
                new LineString(Arrays.asList(new Point(100., 200.), new Point(200., 300.))),
                new LineString(Arrays.asList(new Point(300., 400.), new Point(400., 500.))));
        MultiLineString multiLineString = new MultiLineString(points);
        DBObject object = GeoJsonConverters.GeoJsonObjectToDBObjectConverter.INSTANCE.convert(multiLineString);
        getTestDatabase().getCollection("test").save(object);
        DBObject dbObject = getTestDatabase().getCollection("test").findOne();
        System.out.println(JSON.serialize(dbObject));
        MultiLineString dbMultiLineString = GeoJsonConverters.DBObjectToMultiLineStringConverter.INSTANCE.convert(dbObject);
        Assert.assertThat(dbMultiLineString, CoreMatchers.equalTo(multiLineString));
    }

    @Test
    public void testMultiPolygonConverter() {
        List<Polygon> polygons = Arrays.asList(
               new Polygon(Arrays.asList(new Point(300., 400.), new Point(400., 500.), new Point(500., 600.))),
                new Polygon(Arrays.asList(new Point(300., 400.), new Point(400., 500.), new Point(500., 600.))));
        MultiPolygon multiPolygon = new MultiPolygon(polygons);
        DBObject object = GeoJsonConverters.GeoJsonObjectToDBObjectConverter.INSTANCE.convert(multiPolygon);
        getTestDatabase().getCollection("test").save(object);
        DBObject dbObject = getTestDatabase().getCollection("test").findOne();
        System.out.println(JSON.serialize(dbObject));
        MultiPolygon dbMultiPolygon  = GeoJsonConverters.DBObjectToMultiPolygonStringConverter.INSTANCE.convert(dbObject);
        Assert.assertThat(dbMultiPolygon, CoreMatchers.equalTo(multiPolygon));
    }
}
