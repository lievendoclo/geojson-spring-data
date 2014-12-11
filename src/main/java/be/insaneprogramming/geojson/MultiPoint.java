package be.insaneprogramming.geojson;

import java.util.Arrays;
import java.util.Collection;

public class MultiPoint extends GeoJsonObject<Point> {
    @Override
    public String getType() {
        return "MultiPoint";
    }

    public MultiPoint() {
    }

    public MultiPoint(Collection<? extends Point> c) {
        super(c);
    }

    public MultiPoint(Point... points) {
        this(Arrays.asList(points));
    }

    MultiPoint(Object object) {
        super(object);
    }

    @Override
    public void validate(PointValidator pointValidator) {
        if(this.size() == 0 ) throw new IllegalStateException("A MultiPoint must contain at least 1 point");
        for (Point point : this) {
            pointValidator.validate(point);
        }
    }
}
