package Model;

import java.util.ArrayList;
import java.util.List;

public class IpInformationNonQueue implements IpInformationQueueInterface{

    List<IpInformation> ipInformationCollection = new ArrayList<>();
    @Override
    public void addToQueue(IpInformation result) {

        ipInformationCollection.add(result);
    }
    @Override
    public void retrieveFromQueue(IpInformationSystem system) {
        for (IpInformation ipInformation : ipInformationCollection) {
            system.save(ipInformation.toJson());
        }

        ipInformationCollection = new ArrayList<>();
    }
}

