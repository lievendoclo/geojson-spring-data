# Support for GeoJSON classes in Spring Data for MongoDB

Currently Spring Data for MongoDB has support for geospatial geometries, but they do not use the GeoJSON representation.
Also, not all geometries covered by GeoJSON are included by default in Spring Data.

This library provides support for all geometries supported by MongoDB.

## Configuring the GeoJSON converters

Register the providers in your Spring context's MongoTemplate. If you're using Spring Boot, this means you'll have to create a configuration that extends
AbstractMongoConfiguration and override the customConversions bean.

    ...
    @Override
    public CustomConversions customConversions() {
        return new CustomConversions(GeoJsonConverters.getConvertersToRegister());
    }
    ...

If you're not using Spring Boot, you'll have to manually configure your MongoTemplate.

    MongoDbFactory factory = new SimpleMongoDbFactory(mongo, "DatabaseName");
    MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(factory), new MongoMappingContext());
    CustomConversions customConversions = new CustomConversions(GeoJsonConverters.getConvertersToRegister());
    converter.setCustomConversions(customConversions);
    customConversions.registerConvertersIn((GenericConversionService) converter.getConversionService());
    mongoTemplate = new MongoTemplate(factory, converter);

## Using the GeoJSON classes

You can use any of the geometries in your document classes. For example to use a point you can create a document like

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

## Querying

To query for documents using the GeoJSON geometries a custom criteria class `GeoJsonCriteria` has been built. This class support all
the geospatial methods supported by MongoDB. For example, to query for locations near a point, you can use the following query:

    Query query = Query.query(GeoJsonCriteria.where("location").near(new Point(0., 0.)));
    List<GeoLocation> nearestLocations = mongoTemplate.find(query, GeoLocation.class);

