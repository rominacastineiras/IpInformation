package Model;

import Infrastructure.RemoteApisImplementation.IpInformationFromAbstractApi;
import Infrastructure.RemoteApisImplementation.IpInformationFromApilayer;
import Infrastructure.RemoteApisImplementation.IpInformationFromIpapi;
import Interfaces.IpInformationInterface;
import com.eclipsesource.json.JsonObject;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class IpInformationBuilder {
    private String ip;
    private IpInformation ipInformation;
    private Properties propiedades = new Properties();
    private JsonObject defaultInformation;
    private static final String NODATA = "No Data";

    private static final  JsonObject DEFAULT_INFORMATION = new JsonObject()
            .add("countryName",NODATA)
            .add("countryIsoCode",NODATA)
            .add("currency",NODATA)
            .add("timezone",NODATA)
            .add("longitude",0.00)
            .add("latitude",0.00)
            .add("languages", NODATA)
            .add("quoteAgainstDollar", 0.00);
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
    private Map<String, IpInformationFromAbstractApi> ipsForAbstractApi = new HashMap<>();
    private IpInformationFromApilayer ipInformationFromApilayer;
    private IpInformationInterface ipInformationInterface;
    private Map<String, IpInformationFromIpapi>  ipsForIpapi = new HashMap<>();
    private Map<String, IpInformationFromApilayer> ipsForApilayer = new HashMap<>();


    public static IpInformationBuilder basedOnConfiguration(String configurationFileName){

        return basedOnConfigurationOrByDefault(configurationFileName, DEFAULT_INFORMATION);
    }

    public static IpInformationBuilder basedOnConfigurationOrByDefault(String configurationFileName, JsonObject defaultJson){

        return new IpInformationBuilder(configurationFileName, defaultJson);
    }
    private IpInformationBuilder(String configurationFileName, JsonObject defaultJson) {
        defaultInformation = defaultJson;

        try{
            propiedades.load(new FileReader(configurationFileName));
        }catch(IOException error){
        }
    }

    public void setCountryName(){
        this.countryName = getProviderFor("NAME_PROVIDER").retrieveCountryName();
    }

    private IpInformationInterface getProviderFor(String providerNameField) {

        String providerName = (String) propiedades.getOrDefault(providerNameField, "");
        
        if(IpInformationFromAbstractApi.handle(providerName))
            return getipinformationfromAbstractapi();
        else
            if(IpInformationFromIpapi.handle(providerName))
                return getIpInformationFromIpapi();
            else
                if(IpInformationFromApilayer.handle(providerName))
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
        if(ipsForApilayer.get(this.currency) != null)
            ipInformationFromApilayer = ipsForApilayer.get(this.currency);
        else {
            ipInformationFromApilayer = new IpInformationFromApilayer(this.currency, propiedades.getProperty("APILAYER_KEY"));
            ipsForApilayer.put(this.currency, ipInformationFromApilayer);
        }
        return ipInformationFromApilayer;
        
    }
    private IpInformationFromIpapi getIpInformationFromIpapi() {
        if(ipsForIpapi.get(this.ip) != null)
            ipInformationFromIpapi = ipsForIpapi.get(this.ip);
        else {
            ipInformationFromIpapi = new IpInformationFromIpapi(ip, propiedades.getProperty("IPAPI_ACCESS_KEY"));
            ipsForIpapi.put(this.ip, ipInformationFromIpapi);
        }
        return ipInformationFromIpapi;

    }

    private IpInformationFromAbstractApi getipinformationfromAbstractapi() {
        if(ipsForAbstractApi.get(this.ip) != null)
            ipInformationFromAbstractApi = ipsForAbstractApi.get(this.ip);
        else {
            ipInformationFromAbstractApi = new IpInformationFromAbstractApi(ip, propiedades.getProperty("ABSTRACTAPI_ACCESS_KEY"));
            ipsForAbstractApi.put(this.ip, ipInformationFromAbstractApi);
        }
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

        try {
            this.countryIsoCode = getProviderFor("ISO_CODE_PROVIDER").retrieveCountryIsoCode();
        } catch (NullPointerException e) {
            this.countryIsoCode = defaultInformation.getString("countryIsoCode", NODATA);
        }
    }
     public void setCountryCurrency() {

         try{
             this.currency = getProviderFor("CURRENCY_PROVIDER").retrieveCountryCurrency();
         }catch (NullPointerException e){
             this.currency = defaultInformation.getString("currency", NODATA);
         }
    }

    public void setDistanceToBuenosAires() {
        try{
            this.latitude = getProviderFor("LATITUDE_AND_LONGITUDE_PROVIDER").retrieveCountryLatitude();
        }catch (NullPointerException e){
            this.latitude = defaultInformation.getDouble("latitude", 0.00);
        }

        try{
            this.longitude = getProviderFor("LATITUDE_AND_LONGITUDE_PROVIDER").retrieveCountryLongitude();
        }catch (NullPointerException e){
            this.longitude = defaultInformation.getDouble("longitude", 0.00);
        }

    }

    public void setQuoteAgainstDollar() {
        try{
            this.quoteAgainstDollar = getProviderFor("QUOTE_PROVIDER").retrieveQuoteAgainstDollar();
        }catch (NullPointerException e){
            this.quoteAgainstDollar = defaultInformation.getDouble("quoteAgainstDollar", 0.00);
        }
    }

    public void setLanguages() {
        this.languages = getProviderFor("LANGUAGES_PROVIDER").retrieveCountryLanguages();
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

/*
    private List<InformationProvider> getIpInformationProviders() {
        List<InformationProvider> ipInformationProviders = new ArrayList<InformationProvider>();

        ipInformationProviders.add(IpapiInformationProvider);

        return ipInformationProviders;
    }

*/
}
