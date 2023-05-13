package Model;

import com.eclipsesource.json.JsonObject;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class IpInformationBuilder {
    private final String ip;
    private IpInformation ipInformation;
    private Properties propiedades = new Properties();
    private JsonObject defaultInformation;
    private static final  JsonObject DEFAULT_INFORMATION = new JsonObject()
            .add("country_name","No disponible")
            .add("alpha3Code","No disponible");
    private String countryName;
    private String countryIsoCode;
    private String currency;
    private double latitude;
    private double longitude;
    private List<String> languages;
    private double quoteAgainstDollar;
    private InformationProviderNotDefined informationProviderNotDefined;
    private IpInformationFromIpapi ipInformationFromIpapi;
    private IpInformationFromAbstractApi ipInformationFromAbstractApi;
    private IpInformationFromApilayer ipInformationFromApilayer;
    private IpInformationInterface ipInformationInterface;

    public static IpInformationBuilder basedOnConfiguration(String configurationFileName, String ip){

        return basedOnConfigurationOrByDefault(configurationFileName, DEFAULT_INFORMATION, ip);
    }

    public static IpInformationBuilder basedOnConfigurationOrByDefault(String configurationFileName, JsonObject defaultJson, String ip){

        return new IpInformationBuilder(configurationFileName, defaultJson, ip);
    }
    private IpInformationBuilder(String configurationFileName, JsonObject defaultJson, String ip) {
        this.ip = ip;

        try{
            propiedades.load(new FileReader(configurationFileName));
        }catch(IOException error){
            defaultInformation = defaultJson;
        }
    }

    public void setCountryName(){
        this.countryName = getProviderFor("NAME_PROVIDER").retrieveCountryName();
    }

    private IpInformationInterface getProviderFor(String aProviderName) {

        if(IpInformationFromAbstractApi.handle((String) propiedades.getOrDefault(aProviderName, "")))
            return getipinformationfromAbstractapi();
        else
            if(IpInformationFromIpapi.handle((String) propiedades.getOrDefault(aProviderName, "")))
                return getIpInformationFromIpapi();
            else
                if(IpInformationFromApilayer.handle((String) propiedades.getOrDefault(aProviderName, "")))
                    return getIpInformationFromApilayer();
                else
                    return getInformationProviderNotDefined();
        /*
        List<InformationProvider> ipInformationProviders = getIpInformationProviders();
        int ipInformationProvidersIndex = 0;

        Optional<InformationProvider> potentialInformationProviders = ipInformationProviders.stream().filter(ipInformationProvider -> ipInformationProvider.handle(propiedades.getProperty("NAME_PROVIDER"));

        InformationProvider informationProvider = potentialInformationProviders.ifPresentOrElse(
                {informationProviderFound -> informationProviderFound.getInstance()},
                {new InformationProviderNotDefined()}
                );

        return informationProvider;*/
    }

    private IpInformationFromApilayer getIpInformationFromApilayer() {
        if(ipInformationFromApilayer == null)
            ipInformationFromApilayer = new IpInformationFromApilayer(currency, propiedades.getProperty("APILAYER_KEY"));

        return ipInformationFromApilayer;
    }
    private IpInformationFromIpapi getIpInformationFromIpapi() {
        if(ipInformationFromIpapi == null)
            ipInformationFromIpapi = new IpInformationFromIpapi(ip, propiedades.getProperty("IPAPI_ACCESS_KEY"));

        return ipInformationFromIpapi;
    }

    private IpInformationFromAbstractApi getipinformationfromAbstractapi() {
        if(ipInformationFromAbstractApi == null)
            ipInformationFromAbstractApi = new IpInformationFromAbstractApi(ip, propiedades.getProperty("ABSTRACTAPI_ACCESS_KEY"));

        return ipInformationFromAbstractApi;
    }
    private InformationProviderNotDefined getInformationProviderNotDefined() {
        if(informationProviderNotDefined == null)
            informationProviderNotDefined = new InformationProviderNotDefined(defaultInformation);

        return informationProviderNotDefined;
    }

    public IpInformation build() {
        return new IpInformation(
                countryName,
                countryIsoCode,
                currency,
                longitude,
                latitude,
                languages,
                quoteAgainstDollar
        );
    }

    public void setCountryIsoCode() {

        this.countryIsoCode = getProviderFor("ISO_CODE_PROVIDER").retrieveCountryIsoCode();

        //USAR ABSTRACTAPI PARA NOMBRE Y CODIGO ISO
        //ADEMAS EN EL BUILDER VAN A HABER DATOS QUE SI O SI LOS TIENE QUE TENER DESDE ANTES, COMO POR EJEMPLO LA MONEDA
            //PARA ESOS CASOS CREO QUE DEBERIA TIRAR UNA EXCEPCION SI NO LO TIENE, PARA QUE SE SEPA EN QUE ORDEN EJECUTARLO
    }
     public void setCountryCurrency() {

        this.currency = getProviderFor("CURRENCY_PROVIDER").retrieveCountryCurrency();

    }

    public void setDistanceToBuenosAires() {
        this.latitude = getProviderFor("LATITUDE_AND_LONGITUDE_PROVIDER").retrieveCountryLatitude();
        this.longitude = getProviderFor("LATITUDE_AND_LONGITUDE_PROVIDER").retrieveCountryLongitude();
    }

    public void setLanguages() {
        this.languages = getProviderFor("LANGUAGES_PROVIDER").retrieveCountryLanguages();
    }

    public void setQuoteAgainstDollar() {
        this.quoteAgainstDollar = getProviderFor("QUOTE_PROVIDER").retrieveQuoteAgainstDollar();
    }

/*
    private List<InformationProvider> getIpInformationProviders() {
        List<InformationProvider> ipInformationProviders = new ArrayList<InformationProvider>();

        ipInformationProviders.add(IpapiInformationProvider);

        return ipInformationProviders;
    }

*/
}
