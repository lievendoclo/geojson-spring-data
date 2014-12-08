package be.insaneprogramming.geojson.springdata;

import be.insaneprogramming.geojson.GeoJsonObject;
import be.insaneprogramming.geojson.MultiPolygon;
import be.insaneprogramming.geojson.Polygon;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.data.mongodb.InvalidMongoDbApiUsageException;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

import static org.springframework.util.ObjectUtils.nullSafeHashCode;

/**
 * Central class for creating geospatial queries with GeoJSON. It follows a fluent API style so that you can easily chain together multiple
 * criteria. Static import of the 'GeoJsonCriteria.where' method will improve readability.
 *
 * @author Lieven Doclo
 */
public class GeoJsonCriteria implements CriteriaDefinition {
    /**
     * Custom "not-null" object as we have to be able to work with {@literal null} values as well.
     */
    private static final Object NOT_SET = new Object();

    private String key;

    private List<GeoJsonCriteria> criteriaChain;
    private LinkedHashMap<String, Object> criteria = new LinkedHashMap<String, Object>();
    private Object isValue = NOT_SET;

    public GeoJsonCriteria() {
        this.criteriaChain = new ArrayList<GeoJsonCriteria>();
    }

    public GeoJsonCriteria(String key) {
        this.criteriaChain = new ArrayList<GeoJsonCriteria>();
        this.criteriaChain.add(this);
        this.key = key;
    }

    protected GeoJsonCriteria(List<GeoJsonCriteria> criteriaChain, String key) {
        this.criteriaChain = criteriaChain;
        this.criteriaChain.add(this);
        this.key = key;
    }

    /**
     * Static factory method to create a Criteria using the provided key
     *
     * @param key
     * @return
     */
    public static GeoJsonCriteria where(String key) {
        return new GeoJsonCriteria(key);
    }

    /**
     * Static factory method to create a Criteria using the provided key
     *
     * @return
     */
    public GeoJsonCriteria and(String key) {
        return new GeoJsonCriteria(this.criteriaChain, key);
    }

    /**
     * Creates a criterion using the {@literal $not} meta operator which affects the clause directly following
     *
     * @see http://docs.mongodb.org/manual/reference/operator/query/not/
     * @return
     */
    public GeoJsonCriteria not() {
        return not(null);
    }

    /**
     * Creates a criterion using the {@literal $not} operator.
     *
     * @see http://docs.mongodb.org/manual/reference/operator/query/not/
     * @param value
     * @return
     */
    private GeoJsonCriteria not(Object value) {
        criteria.put("$not", value);
        return this;
    }

    /**
     * Creates a geospatial criterion using a {@literal $near} operation.
     *
     * @see http://docs.mongodb.org/manual/reference/operator/query/near/
     * @param geoJsonObject must not be {@literal null}
     * @return
     */
    public GeoJsonCriteria near(GeoJsonObject<?> geoJsonObject) {
        criteria.put("$near", new GeometryWrapper(geoJsonObject));
        return this;
    }

    /**
     * Creates a geospatial criterion using a {@literal $nearSphere} operation.
     *
     * @see http://docs.mongodb.org/manual/reference/operator/query/nearSphere/
     * @param geoJsonObject must not be {@literal null}
     * @return
     */
    public GeoJsonCriteria nearSphere(GeoJsonObject<?> geoJsonObject) {
        criteria.put("$nearSphere", new GeometryWrapper(geoJsonObject));
        return this;
    }

    /**
     * Creates a geospatical criterion using a {@literal $maxDistance} operation, for use with $near
     *
     * @see http://docs.mongodb.org/manual/reference/operator/query/maxDistance/
     * @param maxDistance
     * @return
     */
    public GeoJsonCriteria maxDistance(double maxDistance) {
        criteria.put("$maxDistance", maxDistance);
        return this;
    }

    /**
     * Creates a geospatial criterion using a {@literal $geoWithin} operation.
     *
     * @see http://docs.mongodb.org/manual/reference/operator/query/geoWithin/
     * @param polygon
     * @return
     */
    public GeoJsonCriteria within(Polygon polygon) {
        criteria.put("$geoWithin", new GeometryWrapper(polygon));
        return this;
    }

    /**
     * Creates a geospatial criterion using a {@literal $geoWithin} operation.
     *
     * @see http://docs.mongodb.org/manual/reference/operator/query/geoWithin/
     * @param multiPolygon
     * @return
     */
    public GeoJsonCriteria within(MultiPolygon multiPolygon) {
        criteria.put("$geoWithin", new GeometryWrapper(multiPolygon));
        return this;
    }

    /**
     * Creates a geospatial criterion using a {@literal $geoIntersects} operation.
     *
     * @see http://docs.mongodb.org/manual/reference/operator/query/geoIntersects/
     * @param geoJsonObject
     * @return
     */
    public GeoJsonCriteria intersects(GeoJsonObject<?> geoJsonObject) {
        criteria.put("$geoIntersects", new GeometryWrapper(geoJsonObject));
        return this;
    }

    /**
     * Creates an 'or' criteria using the $or operator for all of the provided criteria
     * <p>
     * Note that mongodb doesn't support an $or operator to be wrapped in a $not operator.
     * <p>
     *
     * @throws IllegalArgumentException if {@link #orOperator(Criteria...)} follows a not() call directly.
     * @param criteria
     */
    public GeoJsonCriteria orOperator(Criteria... criteria) {
        BasicDBList bsonList = createCriteriaList(criteria);
        return registerCriteriaChainElement(new GeoJsonCriteria("$or").is(bsonList));
    }

    /**
     * Creates a 'nor' criteria using the $nor operator for all of the provided criteria.
     * <p>
     * Note that mongodb doesn't support an $nor operator to be wrapped in a $not operator.
     * <p>
     *
     * @throws IllegalArgumentException if {@link #norOperator(Criteria...)} follows a not() call directly.
     * @param criteria
     */
    public GeoJsonCriteria norOperator(Criteria... criteria) {
        BasicDBList bsonList = createCriteriaList(criteria);
        return registerCriteriaChainElement(new GeoJsonCriteria("$nor").is(bsonList));
    }

    /**
     * Creates an 'and' criteria using the $and operator for all of the provided criteria.
     * <p>
     * Note that mongodb doesn't support an $and operator to be wrapped in a $not operator.
     * <p>
     *
     * @throws IllegalArgumentException if {@link #andOperator(Criteria...)} follows a not() call directly.
     * @param criteria
     */
    public GeoJsonCriteria andOperator(Criteria... criteria) {
        BasicDBList bsonList = createCriteriaList(criteria);
        return registerCriteriaChainElement(new GeoJsonCriteria("$and").is(bsonList));
    }

    private GeoJsonCriteria is(Object o) {

        if (!isValue.equals(NOT_SET)) {
            throw new InvalidMongoDbApiUsageException(
                    "Multiple 'is' values declared. You need to use 'and' with multiple criteria");
        }

        if (lastOperatorWasNot()) {
            throw new InvalidMongoDbApiUsageException("Invalid query: 'not' can't be used with 'is' - use 'ne' instead.");
        }

        this.isValue = o;
        return this;
    }

    private GeoJsonCriteria registerCriteriaChainElement(GeoJsonCriteria criteria) {

        if (lastOperatorWasNot()) {
            throw new IllegalArgumentException("operator $not is not allowed around criteria chain element: "
                    + criteria.getCriteriaObject());
        } else {
            criteriaChain.add(criteria);
        }
        return this;
    }

    private boolean lastOperatorWasNot() {
        return this.criteria.size() > 0 && "$not".equals(this.criteria.keySet().toArray()[this.criteria.size() - 1]);
    }

    public String getKey() {
        return this.key;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.mongodb.core.query.CriteriaDefinition#getCriteriaObject()
     */
    public DBObject getCriteriaObject() {

        if (this.criteriaChain.size() == 1) {
            return criteriaChain.get(0).getSingleCriteriaObject();
        } else if (CollectionUtils.isEmpty(this.criteriaChain) && !CollectionUtils.isEmpty(this.criteria)) {
            return getSingleCriteriaObject();
        } else {
            DBObject criteriaObject = new BasicDBObject();
            for (GeoJsonCriteria c : this.criteriaChain) {
                DBObject dbo = c.getSingleCriteriaObject();
                for (String k : dbo.keySet()) {
                    setValue(criteriaObject, k, dbo.get(k));
                }
            }
            return criteriaObject;
        }
    }

    protected DBObject getSingleCriteriaObject() {

        DBObject dbo = new BasicDBObject();
        boolean not = false;

        for (String k : this.criteria.keySet()) {
            Object value = this.criteria.get(k);
            if (not) {
                DBObject notDbo = new BasicDBObject();
                notDbo.put(k, value);
                dbo.put("$not", notDbo);
                not = false;
            } else {
                if ("$not".equals(k) && value == null) {
                    not = true;
                } else {
                    dbo.put(k, value);
                }
            }
        }

        if (!StringUtils.hasText(this.key)) {
            if (not) {
                return new BasicDBObject("$not", dbo);
            }
            return dbo;
        }

        DBObject queryCriteria = new BasicDBObject();

        if (!NOT_SET.equals(isValue)) {
            queryCriteria.put(this.key, this.isValue);
            queryCriteria.putAll(dbo);
        } else {
            queryCriteria.put(this.key, dbo);
        }

        return queryCriteria;
    }

    private BasicDBList createCriteriaList(Criteria[] criteria) {
        BasicDBList bsonList = new BasicDBList();
        for (Criteria c : criteria) {
            bsonList.add(c.getCriteriaObject());
        }
        return bsonList;
    }

    private void setValue(DBObject dbo, String key, Object value) {
        Object existing = dbo.get(key);
        if (existing == null) {
            dbo.put(key, value);
        } else {
            throw new InvalidMongoDbApiUsageException("Due to limitations of the com.mongodb.BasicDBObject, "
                    + "you can't add a second '" + key + "' expression specified as '" + key + " : " + value + "'. "
                    + "GeoCriteria already contains '" + key + " : " + existing + "'.");
        }
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null || !getClass().equals(obj.getClass())) {
            return false;
        }

        GeoJsonCriteria that = (GeoJsonCriteria) obj;

        if (this.criteriaChain.size() != that.criteriaChain.size()) {
            return false;
        }

        for (int i = 0; i < this.criteriaChain.size(); i++) {

            GeoJsonCriteria left = this.criteriaChain.get(i);
            GeoJsonCriteria right = that.criteriaChain.get(i);

            if (!simpleCriteriaEquals(left, right)) {
                return false;
            }
        }

        return true;
    }

    private boolean simpleCriteriaEquals(GeoJsonCriteria left, GeoJsonCriteria right) {

        boolean keyEqual = left.key == null ? right.key == null : left.key.equals(right.key);
        boolean criteriaEqual = left.criteria.equals(right.criteria);
        boolean valueEqual = isEqual(left.isValue, right.isValue);

        return keyEqual && criteriaEqual && valueEqual;
    }

    /**
     * Checks the given objects for equality. Handles {@link java.util.regex.Pattern} and arrays correctly
     */
    private boolean isEqual(Object left, Object right) {

        if (left == null) {
            return right == null;
        }

        if (left instanceof Pattern) {
            return right instanceof Pattern && ((Pattern) left).pattern().equals(((Pattern) right).pattern());
        }

        return ObjectUtils.nullSafeEquals(left, right);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {

        int result = 17;

        result += nullSafeHashCode(key);
        result += criteria.hashCode();
        result += nullSafeHashCode(isValue);

        return result;
    }
}
