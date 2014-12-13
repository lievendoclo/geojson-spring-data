package be.insaneprogramming.geojson;

import java.util.Arrays;
import java.util.Collection;

/**
 * A MultiLineString is a geometry consisting of multiple LineStrings.
 */
public class MultiLineString extends AbstractGeoJsonObject<LineString> {
    @Override
    public String getType() {
        return "MultiLineString";
    }

    public MultiLineString() {
    }

    public MultiLineString(Collection<? extends LineString> c) {
        super(c);
    }

    public MultiLineString(LineString... lineStrings) {
        this(Arrays.asList(lineStrings));
    }

    @Override
    public void validate(PointValidator pointValidator) {
        if(this.size() == 0 ) throw new IllegalStateException("A MultiLineString must contain at least 1 point");
        for (LineString lineString : this) {
            lineString.validate(pointValidator);
        }
    }

    MultiLineString(Object object) {
        super(object);
    }
}
