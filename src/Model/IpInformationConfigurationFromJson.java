package Model;

import com.eclipsesource.json.JsonObject;

public class IpInformationConfigurationFromJson {

    private final JsonObject countryInformation;

    IpInformationConfigurationFromJson(JsonObject countryInformation){
        this.countryInformation = countryInformation;
    }
}
