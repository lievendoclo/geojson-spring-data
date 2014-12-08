package be.insaneprogramming.util;

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
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.rules.ExternalResource;

import java.net.UnknownHostException;

public class EmbeddedMongoDbTest {
    @ClassRule
    public static EmbeddedMongoRule embeddedMongo = new EmbeddedMongoRule();

    @Before
    public void cleanup() {
        embeddedMongo.cleanup();
    }

    protected Mongo getMongo() {
        return embeddedMongo.getMongo();
    }

    protected DB getTestDatabase() {
        return embeddedMongo.getTestDatabase();
    }

    public static class EmbeddedMongoRule extends ExternalResource {
        MongodExecutable mongodExecutable = null;
        private int port;

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
        }

        @Override
        protected void after() {
            if (mongodExecutable != null)
                mongodExecutable.stop();
        }

        public int getPort() {
            return port;
        }

        public Mongo getMongo() {
            try {
                return new MongoClient("localhost", getPort());
            } catch (UnknownHostException e) {
                throw new IllegalStateException();
            }
        }

        public DB getTestDatabase() {
            try {
                return new MongoClient("localhost", getPort()).getDB("test");
            } catch (UnknownHostException e) {
                throw new IllegalStateException();
            }
        }

        public void cleanup() {
            getTestDatabase().dropDatabase();
        }
    }

}
