package be.insaneprogramming.geojson.validation;

import be.insaneprogramming.geojson.PointValidator;

import java.util.List;

/**
 * Validates whether a point contains a valid longitude and latitude
 */
public enum LongitudeLatitudePointValidator implements PointValidator {
    INSTANCE;

    public void validate (List<Double> position)
    {
        if (position==null) throw new IllegalStateException("Positions in a point cannot be null");
        if (position.size()!=2) throw new IllegalStateException("A point representing a longitude and latitude must contain exactly 2 numbers");
        double longitude = position.get(0);
        if ( longitude>180 || longitude<-180 ) throw new IllegalStateException("The longitude of a point must be between -180 and 180");
        double latitude = position.get(1);
        if ( latitude>90 || latitude<-90 ) throw new IllegalStateException("The latitude of a point must be between -90 and 90");
    }
}
