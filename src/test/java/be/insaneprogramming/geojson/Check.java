package be.insaneprogramming.geojson;

import be.insaneprogramming.geojson.springdata.GeoJsonCriteria;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

/**
 * Created by lievendoclo on 08.12.14.
 */
public class Check {
    public static void main(String[] args) {
        DBObject object = GeoJsonCriteria.where("location").near(new Point(100., 200.)).maxDistance(100.).getCriteriaObject();
        System.out.println(JSON.serialize(object));
    }
}
