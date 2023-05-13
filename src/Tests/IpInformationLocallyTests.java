package Tests;

import Model.IpInformation;
import Model.IpInformationBuilder;
import com.eclipsesource.json.JsonObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class IpInformationLocallyTests {

    @Test
    public void testDB(){
        MongoClient mongoClient = MongoClients.create("mongodb+srv://{DB_USERNAME}:{DB_PASSWORD}@cluster0.xok7qpl.mongodb.net/cafeDB");
        MongoDatabase database = mongoClient.getDatabase("cafeDB");
        MongoCollection<Document> collection = database.getCollection("categorias");
        Document student1 = collection.find(new Document("nombre", "GALLETITAS")).first();
    }
    @Test
    public void shouldRetrieveCountryName(){
        IpInformationBuilder informationBuilder = IpInformationBuilder.basedOnConfigurationOrByDefault("NoFile", new JsonObject()
                .add("country_name","Argentina"));
        informationBuilder.setIp("123.456.789.012");
        informationBuilder.setCountryName();

        IpInformation ipInformation = informationBuilder.build();

        Assertions.assertTrue(ipInformation.countryNameIs("Argentina"));
    }

    @Test
    public void shouldRetrieveCountryIsoCode(){
        IpInformationBuilder informationBuilder = IpInformationBuilder.basedOnConfigurationOrByDefault("NoFile", new JsonObject()
                .add("alpha3Code","ARG"));
        informationBuilder.setIp("123.456.789.012");
        informationBuilder.setCountryIsoCode();

        IpInformation ipInformation = informationBuilder.build();

        Assertions.assertTrue(ipInformation.countryIsoCodeIs("ARG"));
    }

}
