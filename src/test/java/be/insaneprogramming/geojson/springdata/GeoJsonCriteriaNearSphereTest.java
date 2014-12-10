package be.insaneprogramming.geojson.springdata;

import be.insaneprogramming.geojson.Point;
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

public class GeoJsonCriteriaNearSphereTest extends EmbeddedMongoDbTest {
    @Before
    public void setupTestData() {
        getMongoTemplate().save(new GeoLocation("One", new Point(10.,10.)));
        getMongoTemplate().save(new GeoLocation("Two", new Point(20.,20.)));
        getMongoTemplate().save(new GeoLocation("Three", new Point(30.,30.)));
        getMongoTemplate().save(new GeoLocation("Four", new Point(40.,40.)));
        getMongoTemplate().save(new GeoLocation("Five", new Point(50.,50.)));
    }

    @Test
    public void testPointNearSpherePoint() {
        Query query = Query.query(GeoJsonCriteria.where("location").nearSphere(new Point(0., 0.)));
        List<GeoLocation> nearestLocations = getMongoTemplate().find(query, GeoLocation.class);
        assertThat(nearestLocations.get(0).getName(), equalTo("One"));
    }

    @Document
    public static class GeoLocation {
        private String name;
        @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
        private Point location;

        public GeoLocation(String name, Point point) {
            this.name = name;
            this.location = point;
        }

        public GeoLocation() {
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
}
