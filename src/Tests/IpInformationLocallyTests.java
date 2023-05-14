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
    public void shouldRetrieveCountryName(){
        IpInformationBuilder informationBuilder = IpInformationBuilder.basedOnConfigurationOrByDefault("NoFile", new JsonObject()
                .add("countryName","Argentina"));
        informationBuilder.setIp("123.456.789.012");
        informationBuilder.setCountryName();

        IpInformation ipInformation = informationBuilder.build();

        Assertions.assertTrue(ipInformation.countryNameIs("Argentina"));
    }

    @Test
    public void shouldRetrieveCountryIsoCode(){
        IpInformationBuilder informationBuilder = IpInformationBuilder.basedOnConfigurationOrByDefault("NoFile", new JsonObject()
                .add("countryIsoCode","ARG"));
        informationBuilder.setIp("123.456.789.012");
        informationBuilder.setCountryIsoCode();

        IpInformation ipInformation = informationBuilder.build();

        Assertions.assertTrue(ipInformation.countryIsoCodeIs("ARG"));
    }

}
