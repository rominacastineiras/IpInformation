package Tests.Locals;

import Model.IpInformationSystem;
import Model.PeriodicalRespositoryProcess;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

public class IpInformationSystemLocallyTests {

    private static final String NODATA = "No Data";

    private Properties getConfiguration() {
        Properties configuration = new Properties();
        try{
            configuration.load(new FileReader(new File("configForTestsInMemory.properties").getAbsolutePath()));
        }catch(IOException error){
            configuration =  new Properties();
        }

        return configuration;
    }

    @Test
    public void onlyOneQueryResult() throws IOException, TimeoutException {
        IpInformationSystem system = IpInformationSystem.ipInformationSystemWithCustomConfiguration(getConfiguration());

        system.newQueryFor("166.171.248.255");

        Map<String, String> result = system.getResult();

        Assertions.assertEquals(NODATA, result.get("countryName"));
        Assertions.assertEquals(NODATA, result.get("countryIsoCode"));
        Assertions.assertEquals(NODATA, result.get("currency"));
        Assertions.assertEquals("7164", result.get("distanceToBuenosAires"));
      //  Assertions.assertTrue(result.get("timestamp").equals("Argentina"));
        Assertions.assertEquals(NODATA, result.get("languages"));
    //    Assertions.assertTrue(result.get("quoteAgainstDollar").equals("1.0"));


    }

    @Test
    public void onlyOneQueryStatistics() throws IOException, TimeoutException {

        IpInformationSystem system = IpInformationSystem.ipInformationSystemWithCustomConfiguration(getConfiguration());

        new PeriodicalRespositoryProcess(system).start();

        system.newQueryFor("130.41.97.255");
        waitSomeSeconds();
        Map<String, String> statistics = system.getMostFarCountry();

        Assertions.assertEquals(NODATA, statistics.get("country_name"));
        Assertions.assertEquals(NODATA, statistics.get("country_code"));
        Assertions.assertEquals("7164", statistics.get("distance"));
        Assertions.assertEquals("1", statistics.get("invocations"));

        statistics = system.getLeastFarCountry();
        Assertions.assertEquals(NODATA, statistics.get("country_name"));
        Assertions.assertEquals(NODATA, statistics.get("country_code"));
        Assertions.assertEquals("7164", statistics.get("distance"));
        Assertions.assertEquals("1", statistics.get("invocations"));

        statistics = system.getAverageDistance();
        Assertions.assertEquals("7164.0", statistics.get("averageDistance"));

    }

    private static void waitSomeSeconds() {
        try{
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            //Do nothing
        }
    }

    @Test
    public void twoEqualQueryResultsStatistics() throws IOException, TimeoutException {

        IpInformationSystem system = IpInformationSystem.ipInformationSystemWithCustomConfiguration(getConfiguration());

        new PeriodicalRespositoryProcess(system).start();

        system.newQueryFor("130.41.97.255");
        system.newQueryFor("130.41.97.255");

        waitSomeSeconds();

        Map<String, String> statistics = system.getMostFarCountry();

        Assertions.assertEquals(NODATA, statistics.get("country_name"));
        Assertions.assertEquals(NODATA, statistics.get("country_code"));
        Assertions.assertEquals("7164", statistics.get("distance"));
        Assertions.assertEquals("2", statistics.get("invocations"));

        statistics = system.getLeastFarCountry();
        Assertions.assertEquals(NODATA, statistics.get("country_name"));
        Assertions.assertEquals(NODATA, statistics.get("country_code"));
        Assertions.assertEquals("7164", statistics.get("distance"));
        Assertions.assertEquals("2", statistics.get("invocations"));

        statistics = system.getAverageDistance();
        Assertions.assertEquals("7164.0", statistics.get("averageDistance"));

    }

    @Test
    public void twoDifferentQueryResultsStatistics() throws IOException, TimeoutException {

        IpInformationSystem system = IpInformationSystem.ipInformationSystemWithCustomConfiguration(getConfiguration());

        new PeriodicalRespositoryProcess(system).start();

        system.newQueryFor("130.41.97.255");
        system.newQueryFor("192.199.248.75");

        waitSomeSeconds();
        Map<String, String> statistics = system.getMostFarCountry();

        Assertions.assertEquals(NODATA, statistics.get("country_name"));
        Assertions.assertEquals(NODATA, statistics.get("country_code"));
        Assertions.assertEquals("7164", statistics.get("distance"));
        Assertions.assertEquals("2", statistics.get("invocations"));

        statistics = system.getLeastFarCountry();
        Assertions.assertEquals(NODATA, statistics.get("country_name"));
        Assertions.assertEquals(NODATA, statistics.get("country_code"));
        Assertions.assertEquals("7164", statistics.get("distance"));
        Assertions.assertEquals("2", statistics.get("invocations"));

        statistics = system.getAverageDistance();
        Assertions.assertEquals("7164.0", statistics.get("averageDistance"));

    }


}
