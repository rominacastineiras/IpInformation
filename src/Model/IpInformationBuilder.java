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
    private Properties propiedades = new Properties();
    private JsonObject defaultInformation;

    private ApiIntanceProvider apiInstanceProvider;
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
    private IpInformationInterface ipInformationInterface;


    public static IpInformationBuilder basedOnConfiguration(String configurationFileName){

        return basedOnConfigurationOrByDefault(configurationFileName, DEFAULT_INFORMATION);
    }

    public static IpInformationBuilder basedOnConfigurationOrByDefault(String configurationFileName, JsonObject defaultJson){

        return new IpInformationBuilder(configurationFileName, defaultJson);
    }
    private IpInformationBuilder(String configurationFileName, JsonObject defaultJson) {
        defaultInformation = defaultJson;
        apiInstanceProvider = new ApiIntanceProvider();

        try{
            propiedades.load(new FileReader(configurationFileName));
        }catch(IOException error){
        }
    }

    public void setCountryName(){
        //TODO: ErrorJAR    this.countryName = getProviderFor("NAME_PROVIDER").retrieveCountryName();
        this.countryName = getProviderFor("https://ipgeolocation.abstractapi.com/v1/").retrieveCountryName();
    }

    private IpInformationInterface getProviderFor(String providerNameField) {

        //TODO: ErrorJAR    String providerName = (String) propiedades.getOrDefault(providerNameField, "");
        String providerName = providerNameField;
        if(IpInformationFromAbstractApi.handle(providerName))
            return apiInstanceProvider.getipinformationfromAbstractapi(ip);
        else
            if(IpInformationFromIpapi.handle(providerName))
                return apiInstanceProvider.getIpInformationFromIpapi(ip);
            else
                if(IpInformationFromApilayer.handle(providerName))
                    return apiInstanceProvider.getIpInformationFromApilayer(currency);
                else
                    return apiInstanceProvider.getInformationProviderNotDefined();
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
            //TODO: ErrorJAR       this.countryIsoCode = getProviderFor("ISO_CODE_PROVIDER").retrieveCountryIsoCode();
            this.countryIsoCode = getProviderFor("https://ipgeolocation.abstractapi.com/v1/").retrieveCountryIsoCode();
        } catch (NullPointerException e) {
            this.countryIsoCode = defaultInformation.getString("countryIsoCode", NODATA);
        }
    }
     public void setCountryCurrency() {
         try{
             //TODO: ErrorJAR this.currency = getProviderFor("CURRENCY_PROVIDER").retrieveCountryCurrency();
             this.currency = getProviderFor("https://ipgeolocation.abstractapi.com/v1/").retrieveCountryCurrency();
         }catch (NullPointerException e){
             this.currency = defaultInformation.getString("currency", NODATA);
         }
    }

    public void setDistanceToBuenosAires() {
        try{
            //TODO: ErrorJAR       this.latitude = getProviderFor("LATITUDE_AND_LONGITUDE_PROVIDER").retrieveCountryLatitude();
            this.latitude = getProviderFor("https://ipgeolocation.abstractapi.com/v1/").retrieveCountryLatitude();
        }catch (NullPointerException e){
            this.latitude = defaultInformation.getDouble("latitude", 0.00);
        }

        try{
//TODO: ErrorJAR            this.longitude = getProviderFor("LATITUDE_AND_LONGITUDE_PROVIDER").retrieveCountryLongitude();
            this.longitude = getProviderFor("https://ipgeolocation.abstractapi.com/v1/").retrieveCountryLongitude();
        }catch (NullPointerException e){
            this.longitude = defaultInformation.getDouble("longitude", 0.00);
        }

    }

    public void setQuoteAgainstDollar() {
        try{
            //TODO: ErrorJAR         this.quoteAgainstDollar = getProviderFor("QUOTE_PROVIDER").retrieveQuoteAgainstDollar();
            this.quoteAgainstDollar = getProviderFor("https://api.apilayer.com/fixer/latest").retrieveQuoteAgainstDollar();
        }catch (NullPointerException e){
            this.quoteAgainstDollar = defaultInformation.getDouble("quoteAgainstDollar", 0.00);
        }
    }

    public void setLanguages() {
   //TODO: ErrorJAR     this.languages = getProviderFor("LANGUAGES_PROVIDER").retrieveCountryLanguages();
        this.languages = getProviderFor("http://api.ipapi.com/api").retrieveCountryLanguages();
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

}
