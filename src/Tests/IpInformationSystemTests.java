package Tests;

import Infrastructure.Repositories.IpInformationInMemory;
import Infrastructure.Repositories.IpInformationInMongoDB;
import Model.IpInformationBuilder;
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

public class IpInformationSystemTests {
    @Test
    public void onlyOneQueryResult() throws IOException, TimeoutException {
        cleanDataBase();
        IpInformationBuilder informationBuilder = IpInformationBuilder.basedOnConfiguration((new File("config.properties").getAbsolutePath()));
        IpInformationSystem system = new IpInformationSystem(new IpInformationInMemory(), informationBuilder);

        system.newQueryFor("166.171.248.255");

        Map<String, String> result = system.getResult();

        Assertions.assertEquals("United States", result.get("countryName"));
        Assertions.assertEquals("US", result.get("countryIsoCode"));
        Assertions.assertEquals("USD", result.get("currency"));
        Assertions.assertEquals("9017", result.get("distanceToBuenosAires"));
       // Assertions.assertTrue(result.get("timestamp").equals("Argentina"));
        Assertions.assertEquals("[English]", result.get("languages"));
        Assertions.assertEquals("1.0", result.get("quoteAgainstDollar"));
        cleanDataBase();


    }

    @Test
    public void onlyOneQueryStatistics() throws IOException, TimeoutException {
        cleanDataBase();

        IpInformationBuilder informationBuilder = IpInformationBuilder.basedOnConfiguration((new File("config.properties").getAbsolutePath()));
        IpInformationSystem system = new IpInformationSystem(new IpInformationInMongoDB(), informationBuilder);
        new PeriodicalRespositoryProcess(system).start();

        system.newQueryFor("130.41.97.255");
        try{
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            //Do nothing
        }
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
    public void twoEqualQueryResultsStatistics() throws IOException, TimeoutException {
        cleanDataBase();

        IpInformationBuilder informationBuilder = IpInformationBuilder.basedOnConfiguration((new File("config.properties").getAbsolutePath()));
        IpInformationSystem system = new IpInformationSystem(new IpInformationInMongoDB(), informationBuilder);
        new PeriodicalRespositoryProcess(system).start();

        system.newQueryFor("130.41.97.255");
        system.newQueryFor("130.41.97.255");

        try{
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            //Do nothing
        }

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
    public void twoDifferentQueryResultsStatistics() throws IOException, TimeoutException {
        cleanDataBase();

        IpInformationBuilder informationBuilder = IpInformationBuilder.basedOnConfiguration((new File("config.properties").getAbsolutePath()));
        IpInformationSystem system = new IpInformationSystem(new IpInformationInMongoDB(), informationBuilder);
        new PeriodicalRespositoryProcess(system).start();

        system.newQueryFor("130.41.97.255");
        system.newQueryFor("192.199.248.75");

        try{
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            //Do nothing
        }
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

        Properties propiedades = new Properties();
        try{
            propiedades.load(new FileReader(new File("config.properties").getAbsolutePath()));
        }catch(IOException error){
            //Do nothing
        }

        String userName = (String) propiedades.getOrDefault("DB_USERNAME", "");
        String password = (String) propiedades.getOrDefault("DB_PASSWORD", "");

        MongoClient mongoClient = MongoClients.create("mongodb+srv://" + userName + ":" + password + "@cluster0.xok7qpl.mongodb.net/cafeDB");
        MongoDatabase database = mongoClient.getDatabase("cafeDB"); //TODO: cambiar nombre
        MongoCollection<Document> collection = database.getCollection("IpInformation");
        FindIterable<Document> results = collection.find();


        for (Document result : results) {
            collection.deleteOne(result);
        }
    }
}
