package Tests;

import Infrastructure.Repositories.IpInformationInMemory;
import Infrastructure.Repositories.IpInformationInMongoDB;
import Model.IpInformationBuilder;
import Model.IpInformationSystem;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.util.Map;

public class IpInformationSystemTests {
    @Test
    public void onlyOneQueryResult(){
        IpInformationBuilder informationBuilder = IpInformationBuilder.basedOnConfiguration((new File("config.properties").getAbsolutePath()));
        IpInformationSystem system = new IpInformationSystem(new IpInformationInMemory(), informationBuilder);

        system.newQueryFor("130.41.97.255");

        Map<String, String> result = system.showResult();

        Assertions.assertTrue(result.get("countryName").equals("Argentina"));
        Assertions.assertTrue(result.get("countryIsoCode").equals("AR"));
        Assertions.assertTrue(result.get("currency").equals("ARS"));
        Assertions.assertTrue(result.get("distanceToBuenosAires").equals("0.3728882721544832"));
       // Assertions.assertTrue(result.get("timestamp").equals("Argentina"));
        Assertions.assertTrue(result.get("languages").equals("[Spanish, Guarani]"));
        Assertions.assertTrue(result.get("quoteAgainstDollar").equals("0.004398"));

    }

    @Test
    public void onlyOneQueryStatistics(){
        IpInformationBuilder informationBuilder = IpInformationBuilder.basedOnConfiguration((new File("config.properties").getAbsolutePath()));
        IpInformationSystem system = new IpInformationSystem(new IpInformationInMongoDB(), informationBuilder);

        system.newQueryFor("130.41.97.255");

        Map<String, String> statistics = system.getMostFarCountry();

        Assertions.assertTrue(statistics.get("country_name").equals("Argentina"));
        Assertions.assertTrue(statistics.get("country_code").equals("AR"));
        Assertions.assertTrue(statistics.get("distance").equals("0.3728882721544832"));
        Assertions.assertTrue(statistics.get("invocations").equals("1"));

        statistics = system.getLeastFarCountry();
        Assertions.assertTrue(statistics.get("country_name").equals("Argentina"));
        Assertions.assertTrue(statistics.get("country_code").equals("AR"));
        Assertions.assertTrue(statistics.get("distance").equals("0.3728882721544832"));
        Assertions.assertTrue(statistics.get("invocations").equals("1"));

        statistics = system.getAverageDistance();
        Assertions.assertTrue(statistics.get("averageDistance").equals("0.3728882721544832"));
    }

    @Test
    public void twoEqualQueryResultsStatistics(){
        IpInformationBuilder informationBuilder = IpInformationBuilder.basedOnConfiguration((new File("config.properties").getAbsolutePath()));
        IpInformationSystem system = new IpInformationSystem(new IpInformationInMongoDB(), informationBuilder);

        system.newQueryFor("130.41.97.255");
        system.newQueryFor("130.41.97.255");

        Map<String, String> statistics = system.getMostFarCountry();

        Assertions.assertTrue(statistics.get("country_name").equals("Argentina"));
        Assertions.assertTrue(statistics.get("country_code").equals("AR"));
        Assertions.assertTrue(statistics.get("distance").equals("0.3728882721544832"));
        Assertions.assertTrue(statistics.get("invocations").equals("2"));

        statistics = system.getLeastFarCountry();
        Assertions.assertTrue(statistics.get("country_name").equals("Argentina"));
        Assertions.assertTrue(statistics.get("country_code").equals("AR"));
        Assertions.assertTrue(statistics.get("distance").equals("0.3728882721544832"));
        Assertions.assertTrue(statistics.get("invocations").equals("2"));

        statistics = system.getAverageDistance();
        Assertions.assertTrue(statistics.get("averageDistance").equals("0.3728882721544832"));
    }

    @Test
    public void twoDifferentQueryResultsStatistics(){
        IpInformationBuilder informationBuilder = IpInformationBuilder.basedOnConfiguration((new File("config.properties").getAbsolutePath()));
        IpInformationSystem system = new IpInformationSystem(new IpInformationInMongoDB(), informationBuilder);

        system.newQueryFor("130.41.97.255");
        system.newQueryFor("192.199.248.75");

        Map<String, String> statistics = system.getMostFarCountry();

        Assertions.assertTrue(statistics.get("country_name").equals("United States"));
        Assertions.assertTrue(statistics.get("country_code").equals("US"));
        Assertions.assertTrue(statistics.get("distance").equals("5262.293586265425"));
        Assertions.assertTrue(statistics.get("invocations").equals("1"));

        statistics = system.getLeastFarCountry();
        Assertions.assertTrue(statistics.get("country_name").equals("Argentina"));
        Assertions.assertTrue(statistics.get("country_code").equals("AR"));
        Assertions.assertTrue(statistics.get("distance").equals("0.3728882721544832"));
        Assertions.assertTrue(statistics.get("invocations").equals("1"));

        statistics = system.getAverageDistance();
        Assertions.assertTrue(statistics.get("averageDistance").equals("2.631,33323726879"));
    }

}
