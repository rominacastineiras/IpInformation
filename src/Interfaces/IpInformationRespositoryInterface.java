package Interfaces;

import Model.IpInformation;
import com.eclipsesource.json.JsonObject;

import java.util.Map;

public interface IpInformationRespositoryInterface {
    void connectIfNecessary();

    void save(IpInformation ipInformation);

    void saveFromJson(JsonObject object);

    Map<String, String> getMostFarCountry();

    Map<String, String> getLeastFarCountry();

    Map<String, String> getAverageDistance();

    String lastPersistedIpTimestamp();

    boolean isInMemory();
}
