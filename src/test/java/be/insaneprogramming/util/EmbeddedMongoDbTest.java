package be.insaneprogramming.util;

import com.mongodb.DB;
import org.junit.Before;
import org.junit.ClassRule;

public class EmbeddedMongoDbTest {
    @ClassRule
    public static EmbeddedMongoRule embeddedMongo = new EmbeddedMongoRule();

    @Before
    public void clearDatabase() {
        embeddedMongo.clearTestDatabase();
    }

    protected DB getTestDatabase() {
        return embeddedMongo.getTestDatabase();
    }
}
