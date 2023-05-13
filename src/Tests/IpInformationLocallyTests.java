package Tests;

import Model.IpInformation;
import Model.IpInformationBuilder;
import com.eclipsesource.json.JsonObject;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class IpInformationLocallyTests {

    @Test
    public void shouldRetrieveCountryName(){
        IpInformationBuilder informationBuilder = IpInformationBuilder.basedOnConfigurationOrByDefault("NoFile", new JsonObject()
                .add("country_name","Argentina"), "123.456.789.012");
        informationBuilder.setCountryName();

        IpInformation ipInformation = informationBuilder.build();

        Assertions.assertTrue(ipInformation.countryNameIs("Argentina"));
    }

    @Test
    public void shouldRetrieveCountryIsoCode(){
        IpInformationBuilder informationBuilder = IpInformationBuilder.basedOnConfigurationOrByDefault("NoFile", new JsonObject()
                .add("alpha3Code","ARG"), "123.456.789.012");
        informationBuilder.setCountryIsoCode();

        IpInformation ipInformation = informationBuilder.build();

        Assertions.assertTrue(ipInformation.countryIsoCodeIs("ARG"));
    }

}
