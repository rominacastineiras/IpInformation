package Tests;

import Model.IpInformation;
import Model.IpInformationBuilder;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.util.Arrays;

public class IpInformationRemotelyTests {

   @Test
    public void shouldRetrieveCountryName(){
       IpInformationBuilder informationBuilder = IpInformationBuilder.basedOnConfiguration((new File("config.properties").getAbsolutePath()));
       informationBuilder.setIp("166.171.248.255");
       informationBuilder.setCountryName();

        IpInformation ipInformation = informationBuilder.build();

        Assertions.assertTrue(ipInformation.countryNameIs("United States"));
    }

    @Test
    public void shouldRetrieveCountryIsoCode() {
        IpInformationBuilder informationBuilder = IpInformationBuilder.basedOnConfiguration((new File("config.properties").getAbsolutePath()));
        informationBuilder.setIp("166.171.248.255");
        informationBuilder.setCountryName();
        informationBuilder.setCountryIsoCode();

        IpInformation ipInformation = informationBuilder.build();

        Assertions.assertTrue(ipInformation.countryNameIs("United States"));
        Assertions.assertTrue(ipInformation.countryIsoCodeIs("US"));
    }

    @Test
    public void shouldRetrieveCountryCurrency() {
        IpInformationBuilder informationBuilder = IpInformationBuilder.basedOnConfiguration((new File("config.properties").getAbsolutePath()));
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
      IpInformationBuilder informationBuilder = IpInformationBuilder.basedOnConfiguration((new File("config.properties").getAbsolutePath()));
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
      IpInformationBuilder informationBuilder = IpInformationBuilder.basedOnConfiguration((new File("config.properties").getAbsolutePath()));
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
  public void shouldRetrieveQuoteAgainstDollar() { //TODO: este valor varia con el tiempo, es un test que va a fallar
      IpInformationBuilder informationBuilder = IpInformationBuilder.basedOnConfiguration((new File("config.properties").getAbsolutePath()));
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
      Assertions.assertTrue(ipInformation.languagesAre(Arrays.asList("English")));
      Assertions.assertTrue(ipInformation.quoteAgainstDollarIs(1));

  }

    /*
//
//                apilayer
        informationBuilder.setCountryQuoteAgainstDollar();*/
}
