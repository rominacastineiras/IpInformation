package Tests.Locals;

import Model.IpInformation;
import Model.IpInformationBuilder;
import com.eclipsesource.json.JsonObject;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class IpInformationBuilderLocallyTests {

    private static final JsonObject DEFAULT_INFORMATION = new JsonObject()
            .add("countryName","UnPais")
            .add("countryIsoCode","UnCodigoIso")
            .add("currency","UnaMoneda")
            .add("timezone","UnTimezone")
            .add("longitude",12.00)
            .add("latitude",34.00)
            .add("language", "unLenguaje")
            .add("quoteAgainstDollar", 56.00);
   @Test
    public void shouldRetrieveCountryName(){

       IpInformationBuilder informationBuilder = IpInformationBuilder.basedOnConfigurationOrByDefault(this.getConfiguration(), DEFAULT_INFORMATION);
       informationBuilder.setIp("166.171.248.255");
       informationBuilder.setCountryName();

        IpInformation ipInformation = informationBuilder.build();

        Assertions.assertTrue(ipInformation.countryNameIs("UnPais"));
    }

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
    public void shouldRetrieveCountryIsoCode() {
        IpInformationBuilder informationBuilder = IpInformationBuilder.basedOnConfigurationOrByDefault(this.getConfiguration(), DEFAULT_INFORMATION);
        informationBuilder.setIp("166.171.248.255");
        informationBuilder.setCountryName();
        informationBuilder.setCountryIsoCode();

        IpInformation ipInformation = informationBuilder.build();

        Assertions.assertTrue(ipInformation.countryNameIs("UnPais"));
        Assertions.assertTrue(ipInformation.countryIsoCodeIs("UnCodigoIso"));
    }

    @Test
    public void shouldRetrieveCountryCurrency() {
        IpInformationBuilder informationBuilder = IpInformationBuilder.basedOnConfigurationOrByDefault(this.getConfiguration(), DEFAULT_INFORMATION);
        informationBuilder.setIp("166.171.248.255");
        informationBuilder.setCountryName();
        informationBuilder.setCountryIsoCode();
        informationBuilder.setCountryCurrency();

        IpInformation ipInformation = informationBuilder.build();

        Assertions.assertTrue(ipInformation.countryNameIs("UnPais"));
        Assertions.assertTrue(ipInformation.countryIsoCodeIs("UnCodigoIso"));
        Assertions.assertTrue(ipInformation.countryCurrencyIs("UnaMoneda"));
   }

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
      IpInformationBuilder informationBuilder = IpInformationBuilder.basedOnConfigurationOrByDefault(this.getConfiguration(), DEFAULT_INFORMATION);
      informationBuilder.setIp("166.171.248.255");
      informationBuilder.setCountryName();
      informationBuilder.setCountryIsoCode();
      informationBuilder.setCountryCurrency();
      //        informationBuilder.setCountryTimezone();
      informationBuilder.setDistanceToBuenosAires();

      IpInformation ipInformation = informationBuilder.build();
      Assertions.assertTrue(ipInformation.countryNameIs("UnPais"));
      Assertions.assertTrue(ipInformation.countryIsoCodeIs("UnCodigoIso"));
      Assertions.assertTrue(ipInformation.countryCurrencyIs("UnaMoneda"));
      //                informationBuilder.setCountryTimeZone(); timezone.current_time

      Assertions.assertTrue(ipInformation.distanceToBuenosAiresIs(7164));

  }
  @Test
  public void shouldRetrieveLanguages() {
      IpInformationBuilder informationBuilder = IpInformationBuilder.basedOnConfigurationOrByDefault(this.getConfiguration(), DEFAULT_INFORMATION);
      informationBuilder.setIp("130.41.97.255");
      informationBuilder.setCountryName();
      informationBuilder.setCountryIsoCode();
      informationBuilder.setCountryCurrency();
      //        informationBuilder.setCountryTimezone();
      informationBuilder.setDistanceToBuenosAires();
      informationBuilder.setLanguages();

      IpInformation ipInformation = informationBuilder.build();
      Assertions.assertTrue(ipInformation.countryNameIs("UnPais"));
      Assertions.assertTrue(ipInformation.countryIsoCodeIs("UnCodigoIso"));
      Assertions.assertTrue(ipInformation.countryCurrencyIs("UnaMoneda"));
      //                informationBuilder.setCountryTimeZone(); timezone.current_time

      Assertions.assertTrue(ipInformation.distanceToBuenosAiresIs(7164));
      Assertions.assertTrue(ipInformation.languagesAre(List.of("unLenguaje")));

  }

  @Test
  public void shouldRetrieveQuoteAgainstDollar() {
      IpInformationBuilder informationBuilder = IpInformationBuilder.basedOnConfigurationOrByDefault(this.getConfiguration(), DEFAULT_INFORMATION);
      informationBuilder.setIp("166.171.248.255");
      informationBuilder.setCountryName();
      informationBuilder.setCountryIsoCode();
      informationBuilder.setCountryCurrency();
      //        informationBuilder.setCountryTimezone();
      informationBuilder.setDistanceToBuenosAires();
      informationBuilder.setLanguages();
      informationBuilder.setQuoteAgainstDollar();

      IpInformation ipInformation = informationBuilder.build();
      Assertions.assertTrue(ipInformation.countryNameIs("UnPais"));
      Assertions.assertTrue(ipInformation.countryIsoCodeIs("UnCodigoIso"));
      Assertions.assertTrue(ipInformation.countryCurrencyIs("UnaMoneda"));
      //                informationBuilder.setCountryTimeZone(); timezone.current_time

      Assertions.assertTrue(ipInformation.distanceToBuenosAiresIs(7164));
      Assertions.assertTrue(ipInformation.languagesAre(List.of("unLenguaje")));
      Assertions.assertTrue(ipInformation.quoteAgainstDollarIs(56.00));

  }  @Test
  public void cannotRetrieveQuoteAgainstDollarIfCurrencyIsNotSet() {
        IpInformationBuilder informationBuilder = IpInformationBuilder.basedOnConfigurationOrByDefault(this.getConfiguration(), DEFAULT_INFORMATION);
      informationBuilder.setIp("166.171.248.255");
      informationBuilder.setCountryName();
      informationBuilder.setCountryIsoCode();
      //        informationBuilder.setCountryTimezone();
      informationBuilder.setDistanceToBuenosAires();
      informationBuilder.setLanguages();
      informationBuilder.setQuoteAgainstDollar();

      IpInformation ipInformation = informationBuilder.build();
        Assertions.assertTrue(ipInformation.countryNameIs("UnPais"));
        Assertions.assertTrue(ipInformation.countryIsoCodeIs("UnCodigoIso"));
      //                informationBuilder.setCountryTimeZone(); timezone.current_time

        Assertions.assertTrue(ipInformation.distanceToBuenosAiresIs(7164));
        Assertions.assertTrue(ipInformation.languagesAre(List.of("unLenguaje")));
        Assertions.assertTrue(ipInformation.quoteAgainstDollarIs(56.00));

  }

    /*
//
//                apilayer
        informationBuilder.setCountryQuoteAgainstDollar();*/
}
