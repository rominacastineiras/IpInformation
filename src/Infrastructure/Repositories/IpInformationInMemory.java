package Infrastructure.Repositories;

import Interfaces.IpInformationRespositoryInterface;
import Model.IpInformation;

import java.util.Map;

public class IpInformationInMemory implements IpInformationRespositoryInterface {
    @Override
    public void connectIfNecessary() {

    }

    @Override
    public Map<String, String> getMostFarCountry() {
        return null;
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
