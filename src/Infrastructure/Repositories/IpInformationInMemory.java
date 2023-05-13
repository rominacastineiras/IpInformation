package Infrastructure.Repositories;

import Interfaces.IpInformationRespositoryInterface;
import Model.IpInformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IpInformationInMemory implements IpInformationRespositoryInterface {
    private List<String> collection = new ArrayList<>();
    @Override
    public void connectIfNecessary() {
        collection = new ArrayList<>();
    }

    @Override
    public Map<String, String> getMostFarCountry() {

        String result = String.valueOf(collection.stream().sorted().findFirst());
        Map<String, String> map = new HashMap<>();
/*
        map.put("country_name", (String) result.get("country_name"));
        map.put("country_code", (String) result.get("country_code"));
        map.put("distance", (String) result.get("distance"));
        map.put("invocations", (String) result.get("invocations"));
*/
        return map;

    }

    @Override
    public void save(IpInformation ipInformation) {

    }

    @Override
    public Map<String, String> getLeastFarCountry() {
        return null;
    }

    @Override
    public Map<String, String> getAverageDistance() {
        return null;
    }
}
