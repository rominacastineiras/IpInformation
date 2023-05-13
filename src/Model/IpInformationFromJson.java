package Model;

import Interfaces.IpInformationInterface;
import com.eclipsesource.json.JsonObject;

import java.util.List;

public class IpInformationFromJson implements IpInformationInterface {

    private final JsonObject data;

    public IpInformationFromJson(JsonObject data) {
        this.data = data;
    }

    public String retrieveCountryName() {
        return data.getString("country_name","").toString();
    }
    public String retrieveCountryIsoCode() {
        return data.getString("alpha2Code","").toString();
    }

    @Override
    public List<String> retrieveCountryLanguages() {
        return null;
    }

    @Override
    public String retrieveCountryCurrency() {
        return null;
    }

    @Override
    public String retrieveCountryTimeZone() {
        return null;
    }

    @Override
    public String retrieveCountryDistanceToBuenosAires() {
        return null;
    }

    @Override
    public double retrieveQuoteAgainstDollar() {
        return 0.00;
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
