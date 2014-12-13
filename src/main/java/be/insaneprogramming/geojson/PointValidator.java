package be.insaneprogramming.geojson;

import java.util.List;

/**
 * Interface for validators of a Point.
 */
public interface PointValidator {
    /**
     * Test if the positions within a Point are valid.
     *
     * @param position
     * @return true if positions are valid.
     */
    void validate(List<Double> position);
}
