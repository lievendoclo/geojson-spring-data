package be.insaneprogramming.geojson.springdata;

import be.insaneprogramming.geojson.MultiPolygon;
import be.insaneprogramming.geojson.Point;
import be.insaneprogramming.geojson.Polygon;
import be.insaneprogramming.util.EmbeddedMongoDbTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class GeoJsonCriteriaWithinTest extends EmbeddedMongoDbTest {
    @Before
    public void setupTestData() {
        getMongoTemplate().save(new PointGeoLocation("One", new Point(10.,10.)));
        getMongoTemplate().save(new PointGeoLocation("Two", new Point(20.,20.)));
        getMongoTemplate().save(new PointGeoLocation("Three", new Point(30.,30.)));
        getMongoTemplate().save(new PointGeoLocation("Four", new Point(40.,40.)));
        getMongoTemplate().save(new PointGeoLocation("Five", new Point(50.,50.)));

        getMongoTemplate().save(new PolygonGeoLocation("PolyOne", new Polygon(new Point(5.,5.), new Point(15.,5.), new Point(15.,15.), new Point(5., 15.), new Point(5., 5.))));
        getMongoTemplate().save(new PolygonGeoLocation("PolyTwo", new Polygon(new Point(15.,15.), new Point(25.,15.), new Point(25.,25.), new Point(15., 25.), new Point(15., 15.))));
        getMongoTemplate().save(new PolygonGeoLocation("PolyThree", new Polygon(new Point(35.,35.), new Point(45.,35.), new Point(45.,45.), new Point(35., 45.), new Point(35., 35.))));
    }

    @Test
    public void testPointWithinPolygon() {
        Polygon polygon = new Polygon(new Point(5.,5.), new Point(15.,5.), new Point(15.,15.), new Point(5., 15.), new Point(5., 5.));
        Query query = Query.query(GeoJsonCriteria.where("location").within(polygon));
        List<PointGeoLocation> nearestLocations = getMongoTemplate().find(query, PointGeoLocation.class);
        assertThat(nearestLocations.size(), equalTo(1));
        assertThat(nearestLocations.get(0).getName(), equalTo("One"));
    }

    @Test
    public void testPointWithinMultiPolygon() {
        Polygon polygon1 = new Polygon(new Point(5.,5.), new Point(15.,5.), new Point(15.,15.), new Point(5., 15.), new Point(5., 5.));
        Polygon polygon2 = new Polygon(new Point(15.,15.), new Point(25.,15.), new Point(25.,25.), new Point(15., 25.), new Point(15., 15.));
        MultiPolygon multiPolygon = new MultiPolygon(polygon1, polygon2);
        Query query = Query.query(GeoJsonCriteria.where("location").within(multiPolygon));
        List<PointGeoLocation> nearestLocations = getMongoTemplate().find(query, PointGeoLocation.class);
        assertThat(nearestLocations.size(), equalTo(2));
        assertThat(nearestLocations.get(0).getName(), equalTo("One"));
        assertThat(nearestLocations.get(1).getName(), equalTo("Two"));
    }

    @Test
    public void testPolygonWithinPolygon() {
        Polygon polygon = new Polygon(new Point(0.,0.), new Point(25.,0.), new Point(25.,25.), new Point(0., 25.), new Point(0., 0.));
        Query query = Query.query(GeoJsonCriteria.where("location").within(polygon));
        List<PolygonGeoLocation> nearestLocations = getMongoTemplate().find(query, PolygonGeoLocation.class);
        assertThat(nearestLocations.size(), equalTo(2));
        assertThat(nearestLocations.get(0).getName(), equalTo("PolyOne"));
        assertThat(nearestLocations.get(1).getName(), equalTo("PolyTwo"));
    }

    @Document
    public static class PointGeoLocation {
        private String name;
        @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
        private Point location;

        public PointGeoLocation(String name, Point point) {
            this.name = name;
            this.location = point;
        }

        public PointGeoLocation() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Point getLocation() {
            return location;
        }

        public void setLocation(Point location) {
            this.location = location;
        }
    }

    @Document
    public static class PolygonGeoLocation {
        private String name;
        @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
        private Polygon location;

        public PolygonGeoLocation(String name, Polygon point) {
            this.name = name;
            this.location = point;
        }

        public PolygonGeoLocation() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Polygon getLocation() {
            return location;
        }

        public void setLocation(Polygon location) {
            this.location = location;
        }
    }
}
