package Model;

import Interfaces.IpInformationInterface;
import com.eclipsesource.json.JsonObject;

import java.util.ArrayList;
import java.util.Collections;

public class NotDefinedApiProvider implements IpInformationInterface {
    public static final String NOT_DEFINED = "Not defined";
    private final JsonObject defaultInformation;

    public NotDefinedApiProvider(JsonObject defaultInformation) {
        this.defaultInformation = defaultInformation;
    }

    @Override
    public String retrieveCountryName() {
        return this.defaultInformation.getString("countryName", NOT_DEFINED);
    }

    @Override
    public String retrieveCountryIsoCode() {
        return this.defaultInformation.getString("countryIsoCode", NOT_DEFINED);
    }

    @Override
    public ArrayList<String> retrieveCountryLanguages() {
        return new ArrayList<>(Collections.singleton(this.defaultInformation.getString("language", NOT_DEFINED))); //TODO: No se por que me obliga a poner esto del singleton
    }

    @Override
    public String retrieveCountryCurrency() {
        return this.defaultInformation.getString("currency", NOT_DEFINED);
    }

    @Override
    public String retrieveCountryTimeZone() {
        return this.defaultInformation.getString("timezone", NOT_DEFINED);
    }

    @Override
    public double retrieveQuoteAgainstDollar() {
               return this.defaultInformation.getDouble("quoteAgainstDollar", 0.00);
    }

    @Override
    public double retrieveCountryLatitude() {
        return 0;
    }

    @Override
    public double retrieveCountryLongitude() {
        return 0;
    }
}
