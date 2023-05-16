package Tests.Remotes;

import Model.IpInformation;
import Model.IpInformationBuilder;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class IpInformationBuilderRemotellyTests {

   @Test
    public void shouldRetrieveCountryName(){

       IpInformationBuilder informationBuilder = IpInformationBuilder.basedOnConfiguration(this.getConfiguration());
       informationBuilder.setIp("166.171.248.255");
       informationBuilder.setCountryName();

        IpInformation ipInformation = informationBuilder.build();

        Assertions.assertTrue(ipInformation.countryNameIs("United States"));
    }

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
    public void shouldRetrieveCountryIsoCode() {
        IpInformationBuilder informationBuilder = IpInformationBuilder.basedOnConfiguration(this.getConfiguration());
        informationBuilder.setIp("166.171.248.255");
        informationBuilder.setCountryName();
        informationBuilder.setCountryIsoCode();

        IpInformation ipInformation = informationBuilder.build();

        Assertions.assertTrue(ipInformation.countryNameIs("United States"));
        Assertions.assertTrue(ipInformation.countryIsoCodeIs("US"));
    }

    @Test
    public void shouldRetrieveCountryCurrency() {
        IpInformationBuilder informationBuilder = IpInformationBuilder.basedOnConfiguration(this.getConfiguration());
        informationBuilder.setIp("166.171.248.255");
        informationBuilder.setCountryName();
        informationBuilder.setCountryIsoCode();
        informationBuilder.setCountryCurrency();

        IpInformation ipInformation = informationBuilder.build();

        Assertions.assertTrue(ipInformation.countryNameIs("United States"));
        Assertions.assertTrue(ipInformation.countryIsoCodeIs("US"));
        Assertions.assertTrue(ipInformation.countryCurrencyIs("USD"));
    }
    //TODO: agregar tests para cuando se quiere mostrar la

  /*  @Test //TODO: ver como fijar el reloj
    public void shouldRetrieveCountryTimeZone() {
        IpInformationBuilder informationBuilder = IpInformationBuilder.basedOnConfiguration((new File("config.properties").getAbsolutePath()), "166.171.248.255");
        informationBuilder.setIp("166.171.248.255");
        informationBuilder.setCountryName();
        informationBuilder.setCountryIsoCode();
        informationBuilder.setCountryCurrency();
        informationBuilder.setCountryTimezone();

        IpInformation ipInformation = informationBuilder.build();

        Assertions.assertTrue(ipInformation.countryNameIs("United States"));
        Assertions.assertTrue(ipInformation.countryIsoCodeIs("US"));
        Assertions.assertTrue(ipInformation.countryCurrencyIs("USD"));
        Assertions.assertTrue(ipInformation.countryTimeZoneIs("USD"));
                informationBuilder.setCountryTimeZone(); timezone.current_time

    }*/
  @Test
  public void shouldRetrieveDistanceToBuenosAires() {
      IpInformationBuilder informationBuilder = IpInformationBuilder.basedOnConfiguration(this.getConfiguration());
      informationBuilder.setIp("166.171.248.255");
      informationBuilder.setCountryName();
      informationBuilder.setCountryIsoCode();
      informationBuilder.setCountryCurrency();
      //        informationBuilder.setCountryTimezone();
      informationBuilder.setDistanceToBuenosAires();

      IpInformation ipInformation = informationBuilder.build();
      Assertions.assertTrue(ipInformation.countryNameIs("United States"));
      Assertions.assertTrue(ipInformation.countryIsoCodeIs("US"));
      Assertions.assertTrue(ipInformation.countryCurrencyIs("USD"));
      //                informationBuilder.setCountryTimeZone(); timezone.current_time

      Assertions.assertTrue(ipInformation.distanceToBuenosAiresIs(9017));

  }
  @Test
  public void shouldRetrieveLanguages() {
      IpInformationBuilder informationBuilder = IpInformationBuilder.basedOnConfiguration(this.getConfiguration());
      informationBuilder.setIp("130.41.97.255");
      informationBuilder.setCountryName();
      informationBuilder.setCountryIsoCode();
      informationBuilder.setCountryCurrency();
      //        informationBuilder.setCountryTimezone();
      informationBuilder.setDistanceToBuenosAires();
      informationBuilder.setLanguages();

      IpInformation ipInformation = informationBuilder.build();
      Assertions.assertTrue(ipInformation.countryNameIs("Argentina"));
      Assertions.assertTrue(ipInformation.countryIsoCodeIs("AR"));
      Assertions.assertTrue(ipInformation.countryCurrencyIs("ARS"));
      //                informationBuilder.setCountryTimeZone(); timezone.current_time

      Assertions.assertTrue(ipInformation.distanceToBuenosAiresIs(0));
      Assertions.assertTrue(ipInformation.languagesAre(Arrays.asList("Spanish","Guarani")));

  }

  @Test
  public void shouldRetrieveQuoteAgainstDollar() {
      IpInformationBuilder informationBuilder = IpInformationBuilder.basedOnConfiguration(this.getConfiguration());
      informationBuilder.setIp("166.171.248.255");
      informationBuilder.setCountryName();
      informationBuilder.setCountryIsoCode();
      informationBuilder.setCountryCurrency();
      //        informationBuilder.setCountryTimezone();
      informationBuilder.setDistanceToBuenosAires();
      informationBuilder.setLanguages();
      informationBuilder.setQuoteAgainstDollar();

      IpInformation ipInformation = informationBuilder.build();
      Assertions.assertTrue(ipInformation.countryNameIs("United States"));
      Assertions.assertTrue(ipInformation.countryIsoCodeIs("US"));
      Assertions.assertTrue(ipInformation.countryCurrencyIs("USD"));
      //                informationBuilder.setCountryTimeZone(); timezone.current_time

      Assertions.assertTrue(ipInformation.distanceToBuenosAiresIs(9017));
      Assertions.assertTrue(ipInformation.languagesAre(List.of("English")));
      Assertions.assertTrue(ipInformation.quoteAgainstDollarIs(1.0)); //Si la Api llega a su límite este test puede ser un falso positivo

  }  @Test
  public void cannotRetrieveQuoteAgainstDollarIfCurrencyIsNotSet() {
      IpInformationBuilder informationBuilder = IpInformationBuilder.basedOnConfiguration(this.getConfiguration());
      informationBuilder.setIp("166.171.248.255");
      informationBuilder.setCountryName();
      informationBuilder.setCountryIsoCode();
      //        informationBuilder.setCountryTimezone();
      informationBuilder.setDistanceToBuenosAires();
      informationBuilder.setLanguages();
      informationBuilder.setQuoteAgainstDollar();

      IpInformation ipInformation = informationBuilder.build();
      Assertions.assertTrue(ipInformation.countryNameIs("United States"));
      Assertions.assertTrue(ipInformation.countryIsoCodeIs("US"));
      //                informationBuilder.setCountryTimeZone(); timezone.current_time

      Assertions.assertTrue(ipInformation.distanceToBuenosAiresIs(9017));
      Assertions.assertTrue(ipInformation.languagesAre(List.of("English")));
      Assertions.assertTrue(ipInformation.quoteAgainstDollarIs(0.00));

  }
}
