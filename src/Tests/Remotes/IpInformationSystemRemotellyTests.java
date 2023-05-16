package Tests.Remotes;

import Model.IpInformationSystem;
import Model.PeriodicalRespositoryProcess;
import com.mongodb.client.*;
import org.bson.Document;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

public class IpInformationSystemRemotellyTests {
    private Properties getConfiguration() {
        Properties configuration = new Properties();
        try{
            configuration.load(new FileReader(new File("configForTestsInMongoDB.properties").getAbsolutePath()));
        }catch(IOException error){
            configuration =  new Properties();
        }

        return configuration;
    }

    @Test
    public void shouldRetrieveOnlyOneQueryResultWhenOneQueryWasProcessed() throws IOException, TimeoutException {
        cleanDataBase();
        IpInformationSystem system = IpInformationSystem.ipInformationSystemWithCustomConfiguration(getConfiguration());

        system.newQueryFor("166.171.248.255");

        Map<String, String> result = system.getResult();

        Assertions.assertEquals("United States", result.get("countryName"));
        Assertions.assertEquals("US", result.get("countryIsoCode"));
        Assertions.assertEquals("USD", result.get("currency"));
        Assertions.assertEquals("9017", result.get("distanceToBuenosAires"));
        // Assertions.assertTrue(result.get("timestamp").equals("Argentina"));
        Assertions.assertEquals("English", result.get("languages"));
        cleanDataBase();
    }

    @Test
    public void shouldRetrieveSameQueryStatisticsWhenOneQueryWasProcessed() throws IOException, TimeoutException {
        cleanDataBase();

        IpInformationSystem system = IpInformationSystem.ipInformationSystemWithCustomConfiguration(getConfiguration());

        new PeriodicalRespositoryProcess(system).start();

        system.newQueryFor("130.41.97.255");
        waitSomeSeconds(4000);
        Map<String, String> statistics = system.getMostFarCountry();

        Assertions.assertEquals("Argentina", statistics.get("country_name"));
        Assertions.assertEquals("AR", statistics.get("country_code"));
        Assertions.assertEquals("0", statistics.get("distance"));
        Assertions.assertEquals("1", statistics.get("invocations"));

        statistics = system.getLeastFarCountry();
        Assertions.assertEquals("Argentina", statistics.get("country_name"));
        Assertions.assertEquals("AR", statistics.get("country_code"));
        Assertions.assertEquals("0", statistics.get("distance"));
        Assertions.assertEquals("1", statistics.get("invocations"));

        statistics = system.getAverageDistance();
        Assertions.assertEquals("0.0", statistics.get("averageDistance"));
        cleanDataBase();

    }

    @Test
    public void shouldRetrieveSameQueryStatisticsWhenTwoQueriesWithSameIpWereProcessed() throws IOException, TimeoutException {
        cleanDataBase();

        IpInformationSystem system = IpInformationSystem.ipInformationSystemWithCustomConfiguration(getConfiguration());

        new PeriodicalRespositoryProcess(system).start();

        system.newQueryFor("130.41.97.255");
        waitSomeSeconds(2000);

        system.newQueryFor("130.41.97.255");

        waitSomeSeconds(4000);

        Map<String, String> statistics = system.getMostFarCountry();

        Assertions.assertEquals("Argentina", statistics.get("country_name"));
        Assertions.assertEquals("AR", statistics.get("country_code"));
        Assertions.assertEquals("0", statistics.get("distance"));
        Assertions.assertEquals("2", statistics.get("invocations"));

        statistics = system.getLeastFarCountry();
        Assertions.assertEquals("Argentina", statistics.get("country_name"));
        Assertions.assertEquals("AR", statistics.get("country_code"));
        Assertions.assertEquals("0", statistics.get("distance"));
        Assertions.assertEquals("2", statistics.get("invocations"));

        statistics = system.getAverageDistance();
        Assertions.assertEquals("0.0", statistics.get("averageDistance"));
        cleanDataBase();

    }

    @Test
    public void shouldRetrieveDifferentQueryStatisticsWhenTwoQueriesWithSameIpWereProcessed() throws IOException, TimeoutException {
        cleanDataBase();

        IpInformationSystem system = IpInformationSystem.ipInformationSystemWithCustomConfiguration(getConfiguration());

        new PeriodicalRespositoryProcess(system).start();

        system.newQueryFor("130.41.97.255");
        waitSomeSeconds(2000);
        system.newQueryFor("192.199.248.75");
        waitSomeSeconds(4000);

        Map<String, String> statistics = system.getMostFarCountry();

        Assertions.assertEquals("United States", statistics.get("country_name"));
        Assertions.assertEquals("US", statistics.get("country_code"));
        Assertions.assertEquals("9555", statistics.get("distance"));
        Assertions.assertEquals("1", statistics.get("invocations"));

        statistics = system.getLeastFarCountry();
        Assertions.assertEquals("Argentina", statistics.get("country_name"));
        Assertions.assertEquals("AR", statistics.get("country_code"));
        Assertions.assertEquals("0", statistics.get("distance"));
        Assertions.assertEquals("1", statistics.get("invocations"));

        statistics = system.getAverageDistance();
        Assertions.assertEquals("4777.5", statistics.get("averageDistance"));
        cleanDataBase();

    }


    private void cleanDataBase() {


        Properties configuration = getConfiguration();

        String userName = (String) configuration.getOrDefault("DB_USERNAME", "");
        String password = (String) configuration.getOrDefault("DB_PASSWORD", "");
        String collectionName = (String) configuration.getOrDefault("COLLECTION_NAME", "");

        MongoClient mongoClient = MongoClients.create("mongodb+srv://" + userName + ":" + password + "@cluster0.xok7qpl.mongodb.net/cafeDB");
        MongoDatabase database = mongoClient.getDatabase("IpInformation");
        MongoCollection<Document> collection = database.getCollection(collectionName);
        FindIterable<Document> results = collection.find();


        for (Document result : results) {
            collection.deleteOne(result);
        }
    }

    private static void waitSomeSeconds(int seconds) {
        try{
            Thread.sleep(seconds);
        } catch (InterruptedException e) {
            //Do nothing
        }
    }
}
