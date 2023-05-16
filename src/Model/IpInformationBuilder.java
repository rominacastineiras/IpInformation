package Model;

import Infrastructure.RemoteApisImplementation.IpInformationFromAbstractApi;
import Infrastructure.RemoteApisImplementation.IpInformationFromApilayer;
import Infrastructure.RemoteApisImplementation.IpInformationFromIpapi;
import Interfaces.IpInformationInterface;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.ParseException;

import java.util.*;

public class IpInformationBuilder {
    private String ip;
    private final Properties configuration;
    private final JsonObject defaultInformation;

    private final ApiIntanceProvider apiInstanceProvider;
    private static final String NODATA = "No Data";

    private static final  JsonObject DEFAULT_INFORMATION = new JsonObject()
            .add("countryName",NODATA)
            .add("countryIsoCode",NODATA)
            .add("currency",NODATA)
            .add("timezone",NODATA)
            .add("longitude",0.00)
            .add("latitude",0.00)
            .add("language", NODATA)
            .add("quoteAgainstDollar", 0.00);
    private String countryName;
    private String countryIsoCode;
    private String currency;
    private double latitude;
    private double longitude;
    private List<String> languages;
    private double quoteAgainstDollar;
    private String timezone;


    public static IpInformationBuilder basedOnConfiguration(Properties configuration){

        return basedOnConfigurationOrByDefault(configuration, DEFAULT_INFORMATION);
    }

    public static IpInformationBuilder basedOnConfigurationOrByDefault(Properties configuration, JsonObject defaultJson){

        return new IpInformationBuilder(configuration, defaultJson);
    }
    private IpInformationBuilder(Properties configuration, JsonObject defaultJson) {
        defaultInformation = defaultJson;

        apiInstanceProvider = new ApiIntanceProvider(configuration, defaultInformation);
        this.configuration = configuration;

    }

    private IpInformationInterface getProviderFor(String providerNameField) {

        String providerName = (String) configuration.getOrDefault(providerNameField, "");
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
                quoteAgainstDollar,
                timezone
        );
    }

    public void setCountryName(){
        try {
            this.countryName = getProviderFor("NAME_PROVIDER").retrieveCountryName();
        } catch (NullPointerException | ParseException e ) {
            this.countryName = defaultInformation.getString("countryName", NODATA);
        }

    }

    public void setCountryIsoCode() {
        try {
            this.countryIsoCode = getProviderFor("ISO_CODE_PROVIDER").retrieveCountryIsoCode();
        } catch (NullPointerException | ParseException e ) {
            this.countryIsoCode = defaultInformation.getString("countryIsoCode", NODATA);
        }
    }
    public void setCountryCurrency() {
         try{
             this.currency = getProviderFor("CURRENCY_PROVIDER").retrieveCountryCurrency();
         }catch (NullPointerException  | ParseException e){
             this.currency = defaultInformation.getString("currency", NODATA);
         }
    }

    public void setDistanceToBuenosAires() {
        try{
            this.latitude = getProviderFor("LATITUDE_AND_LONGITUDE_PROVIDER").retrieveCountryLatitude();
        }catch (NullPointerException  | ParseException e){
            this.latitude = defaultInformation.getDouble("latitude", 0.00);
        }

        try{
            this.longitude = getProviderFor("LATITUDE_AND_LONGITUDE_PROVIDER").retrieveCountryLongitude();
        }catch (NullPointerException  | ParseException e){
            this.longitude = defaultInformation.getDouble("longitude", 0.00);
        }

    }

    public void setQuoteAgainstDollar() {
        try{
            this.quoteAgainstDollar = getProviderFor("QUOTE_PROVIDER").retrieveQuoteAgainstDollar();
        }catch (NullPointerException  | ParseException e){
            this.quoteAgainstDollar = defaultInformation.getDouble("quoteAgainstDollar", 0.00);
        }
    }

    public void setCountryTimezone() {
        try{
            this.timezone = getProviderFor("TIME_ZONE_PROVIDER").retrieveCountryTimeZone();
        }catch (NullPointerException  | ParseException e){
            this.timezone = defaultInformation.getString("timezone", NODATA);
        }
    }

    public void setLanguages() {
        this.languages = getProviderFor("LANGUAGES_PROVIDER").retrieveCountryLanguages();
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
