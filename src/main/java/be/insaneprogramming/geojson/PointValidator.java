package be.insaneprogramming.geojson;

import java.util.List;

public interface PointValidator {
    /**
     * Test if the position is valid.
     *
     * @param position
     * @return true if position is valid.
     */
    void validate(List<Double> position);
}
