package Tests;

import Infrastructure.Repositories.IpInformationInMemory;
import Infrastructure.Repositories.IpInformationInMongoDB;
import Model.IpInformation;
import Model.IpInformationBuilder;
import Model.IpInformationSystem;
import com.mongodb.client.*;
import org.bson.Document;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class IpInformationSystemTests {
    @Test
    public void onlyOneQueryResult(){
        cleanDataBase();
        IpInformationBuilder informationBuilder = IpInformationBuilder.basedOnConfiguration((new File("config.properties").getAbsolutePath()));
        IpInformationSystem system = new IpInformationSystem(new IpInformationInMemory(), informationBuilder);

        system.newQueryFor("166.171.248.255");

        Map<String, String> result = system.showResult();

        Assertions.assertTrue(result.get("countryName").equals("United States"));
        Assertions.assertTrue(result.get("countryIsoCode").equals("US"));
        Assertions.assertTrue(result.get("currency").equals("USD"));
        Assertions.assertTrue(result.get("distanceToBuenosAires").equals("9017"));
       // Assertions.assertTrue(result.get("timestamp").equals("Argentina"));
        Assertions.assertTrue(result.get("languages").equals("[English]"));
        Assertions.assertTrue(result.get("quoteAgainstDollar").equals("1.0"));
        cleanDataBase();


    }

    private void cleanDataBase() {

        Properties propiedades = new Properties();
        try{
            propiedades.load(new FileReader(new File("config.properties").getAbsolutePath()));
        }catch(IOException error){
        }

        String userName = (String) propiedades.getOrDefault("DB_USERNAME", "");
        String password = (String) propiedades.getOrDefault("DB_PASSWORD", "");

        MongoClient mongoClient = MongoClients.create("mongodb+srv://" + userName + ":" + password + "@cluster0.xok7qpl.mongodb.net/cafeDB");
        MongoDatabase database = mongoClient.getDatabase("cafeDB"); //TODO: cambiar nombre
        MongoCollection<Document> collection = database.getCollection("IpInformation");
        FindIterable<Document> results = collection.find();
        MongoCursor<Document> cursor = results.iterator();


        while (cursor.hasNext()) {
            collection.deleteOne(cursor.next());
        }
    }

    @Test
    public void onlyOneQueryStatistics(){
        cleanDataBase();

        IpInformationBuilder informationBuilder = IpInformationBuilder.basedOnConfiguration((new File("config.properties").getAbsolutePath()));
        IpInformationSystem system = new IpInformationSystem(new IpInformationInMongoDB(), informationBuilder);

        system.newQueryFor("130.41.97.255");

        Map<String, String> statistics = system.getMostFarCountry();

        Assertions.assertTrue(statistics.get("country_name").equals("Argentina"));
        Assertions.assertTrue(statistics.get("country_code").equals("AR"));
        Assertions.assertTrue(statistics.get("distance").equals("0"));
        Assertions.assertTrue(statistics.get("invocations").equals("1"));

        statistics = system.getLeastFarCountry();
        Assertions.assertTrue(statistics.get("country_name").equals("Argentina"));
        Assertions.assertTrue(statistics.get("country_code").equals("AR"));
        Assertions.assertTrue(statistics.get("distance").equals("0"));
        Assertions.assertTrue(statistics.get("invocations").equals("1"));

        statistics = system.getAverageDistance();
        Assertions.assertTrue(statistics.get("averageDistance").equals("0.0"));
        cleanDataBase();

    }

    @Test
    public void twoEqualQueryResultsStatistics(){
        cleanDataBase();

        IpInformationBuilder informationBuilder = IpInformationBuilder.basedOnConfiguration((new File("config.properties").getAbsolutePath()));
        IpInformationSystem system = new IpInformationSystem(new IpInformationInMongoDB(), informationBuilder);

        system.newQueryFor("130.41.97.255");
        system.newQueryFor("130.41.97.255");

        Map<String, String> statistics = system.getMostFarCountry();

        Assertions.assertTrue(statistics.get("country_name").equals("Argentina"));
        Assertions.assertTrue(statistics.get("country_code").equals("AR"));
        Assertions.assertTrue(statistics.get("distance").equals("0"));
        Assertions.assertTrue(statistics.get("invocations").equals("2"));

        statistics = system.getLeastFarCountry();
        Assertions.assertTrue(statistics.get("country_name").equals("Argentina"));
        Assertions.assertTrue(statistics.get("country_code").equals("AR"));
        Assertions.assertTrue(statistics.get("distance").equals("0"));
        Assertions.assertTrue(statistics.get("invocations").equals("2"));

        statistics = system.getAverageDistance();
        Assertions.assertTrue(statistics.get("averageDistance").equals("0.0"));
        cleanDataBase();

    }

    @Test
    public void twoDifferentQueryResultsStatistics(){
        cleanDataBase();

        IpInformationBuilder informationBuilder = IpInformationBuilder.basedOnConfiguration((new File("config.properties").getAbsolutePath()));
        IpInformationSystem system = new IpInformationSystem(new IpInformationInMongoDB(), informationBuilder);

        system.newQueryFor("130.41.97.255");
        system.newQueryFor("192.199.248.75");

        Map<String, String> statistics = system.getMostFarCountry();

        Assertions.assertTrue(statistics.get("country_name").equals("United States"));
        Assertions.assertTrue(statistics.get("country_code").equals("US"));
        Assertions.assertTrue(statistics.get("distance").equals("9555"));
        Assertions.assertTrue(statistics.get("invocations").equals("1"));

        statistics = system.getLeastFarCountry();
        Assertions.assertTrue(statistics.get("country_name").equals("Argentina"));
        Assertions.assertTrue(statistics.get("country_code").equals("AR"));
        Assertions.assertTrue(statistics.get("distance").equals("0"));
        Assertions.assertTrue(statistics.get("invocations").equals("1"));

        statistics = system.getAverageDistance();
        Assertions.assertTrue(statistics.get("averageDistance").equals("4777.5"));
        cleanDataBase();

    }

}
