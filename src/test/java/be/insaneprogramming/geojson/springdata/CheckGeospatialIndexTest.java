package be.insaneprogramming.geojson.springdata;

import be.insaneprogramming.geojson.*;
import be.insaneprogramming.util.EmbeddedMongoDbTest;
import org.junit.Test;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;

public class CheckGeospatialIndexTest extends EmbeddedMongoDbTest {

    @Test
    public void testPointGeospatialIndexing() {
        getMongoTemplate().save(new PointDocument(new Point(10., 20.)));
    }

    @Test
    public void testLineStringGeospatialIndexing() {
        getMongoTemplate().save(new LineStringDocument(new LineString(Arrays.asList(new Point(10., 20.), new Point(20., 20.)))));
    }

    @Test
    public void testPolygonGeospatialIndexing() {
        getMongoTemplate().save(new PolygonDocument(new Polygon(Arrays.asList(Arrays.asList(new Point(100., 0.), new Point(101., 0.), new Point(101., 1.), new Point(100., 1.), new Point(100., 0.))))));
    }

    @Test
    public void testMultiPointGeospatialIndexing() {
        getMongoTemplate().save(new MultiPointDocument(new MultiPoint(Arrays.asList(new Point(10., 20.), new Point(20., 20.)))));
    }

    @Test
    public void testMultiLineStringGeospatialIndexing() {
        getMongoTemplate().save(new MultiLineStringDocument(new MultiLineString(Arrays.asList(new LineString(Arrays.asList(new Point(10., 20.), new Point(20., 20.)))))));
    }

    @Test
    public void testMultiPolygonGeospatialIndexing() {
        getMongoTemplate().save(new MultiPolygonDocument(new MultiPolygon(Arrays.asList(Arrays.asList(Arrays.asList(new Point(100., 0.), new Point(101., 0.), new Point(101., 1.), new Point(100., 1.), new Point(100., 0.)))))));
    }

    @Document
    public static class PointDocument {
        @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
        Point point;

        public PointDocument(Point point) {
            this.point = point;
        }
    }

    @Document
    public static class LineStringDocument {
        @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
        LineString lineString;

        public LineStringDocument(LineString lineString) {
            this.lineString = lineString;
        }
    }

    @Document
    public static class PolygonDocument {
        @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
        Polygon polygon;

        public PolygonDocument(Polygon polygon) {
            this.polygon = polygon;
        }
    }

    @Document
    public static class MultiPointDocument {
        @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
        MultiPoint multiPoint;

        public MultiPointDocument(MultiPoint multiPoint) {
            this.multiPoint = multiPoint;
        }
    }

    @Document
    public static class MultiLineStringDocument {
        @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
        MultiLineString multiLineString;

        public MultiLineStringDocument(MultiLineString multiLineString) {
            this.multiLineString = multiLineString;
        }
    }

    @Document
    public static class MultiPolygonDocument {
        @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
        MultiPolygon multiPolygon;

        public MultiPolygonDocument(MultiPolygon multiPolygon) {
            this.multiPolygon = multiPolygon;
        }
    }
}
