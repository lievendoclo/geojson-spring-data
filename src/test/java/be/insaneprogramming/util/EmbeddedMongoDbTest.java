package be.insaneprogramming.util;

import be.insaneprogramming.geojson.GeoJsonConverters;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.rules.ExternalResource;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.net.UnknownHostException;

public class EmbeddedMongoDbTest {
    @ClassRule
    public static EmbeddedMongoRule embeddedMongo = new EmbeddedMongoRule();

    private MongoTemplate mongoTemplate;

    @Before
    public void setup() {
        embeddedMongo.cleanup();
        MongoDbFactory factory = new SimpleMongoDbFactory(embeddedMongo.getMongo(), embeddedMongo.getTestDatabaseName());
        MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(factory), new MongoMappingContext());
        CustomConversions customConversions = new CustomConversions(GeoJsonConverters.getConvertersToRegister());
        converter.setCustomConversions(customConversions);
        customConversions.registerConvertersIn((GenericConversionService) converter.getConversionService());
        mongoTemplate = new MongoTemplate(factory, converter);
    }

    protected Mongo getMongo() {
        return embeddedMongo.getMongo();
    }

    protected DB getTestDatabase() {
        return embeddedMongo.getTestDatabase();
    }

    protected MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    public static class EmbeddedMongoRule extends ExternalResource {
        MongodExecutable mongodExecutable = null;
        private int port;
        private Mongo mongo;

        @Override
        protected void before() throws Throwable {
            MongodStarter starter = MongodStarter.getDefaultInstance();
            port = Network.getFreeServerPort();
            IMongodConfig mongodConfig = new MongodConfigBuilder()
                    .version(Version.Main.PRODUCTION)
                    .net(new Net(port, Network.localhostIsIPv6()))
                    .build();
            mongodExecutable = starter.prepare(mongodConfig);
            mongodExecutable.start();
            mongo = new MongoClient("localhost", getPort());
        }

        @Override
        protected void after() {
            mongo.close();
            if (mongodExecutable != null)
                mongodExecutable.stop();
        }

        public int getPort() {
            return port;
        }

        public Mongo getMongo() {
            return mongo;
        }

        public DB getTestDatabase() {
            return getMongo().getDB(getTestDatabaseName());
        }

        public String getTestDatabaseName() {
            return "test";
        }

        public void cleanup() {
            getTestDatabase().dropDatabase();
        }
    }

}
