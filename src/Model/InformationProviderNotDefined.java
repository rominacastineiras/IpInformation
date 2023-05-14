package Model;

import Interfaces.IpInformationInterface;
import com.eclipsesource.json.JsonObject;

import java.util.ArrayList;
import java.util.Collections;

public class InformationProviderNotDefined implements IpInformationInterface {
    private final JsonObject defaultInformation;

    public InformationProviderNotDefined(JsonObject defaultInformation) {
        this.defaultInformation = defaultInformation;
    }

    @Override
    public String retrieveCountryName() {
        return this.defaultInformation.getString("countryName", "Not defined");
    }

    @Override
    public String retrieveCountryIsoCode() {
        return this.defaultInformation.getString("countryIsoCode", "Not defined");
    }

    @Override
    public ArrayList<String> retrieveCountryLanguages() {
        return new ArrayList<>(Collections.singleton(this.defaultInformation.getString("language", "Not defined"))); //TODO: No se por que me obliga a poner esto del singleton
    }

    @Override
    public String retrieveCountryCurrency() {
        return this.defaultInformation.getString("currency", "Not defined");
    }

    @Override
    public String retrieveCountryTimeZone() {

        return this.defaultInformation.getString("timeZone", "Not defined");

    }

    @Override
    public double retrieveQuoteAgainstDollar() {
               return this.defaultInformation.getDouble("", 0.00);

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
