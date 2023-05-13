package Interfaces;

import Model.IpInformation;

import java.util.Map;

public interface IpInformationRespositoryInterface {
    void connectIfNecessary();

    Map<String, String> getMostFarCountry();

    void save(IpInformation ipInformation);

    Map<String, String> getLeastFarCountry();

    Map<String, String> getAverageDistance();
}
