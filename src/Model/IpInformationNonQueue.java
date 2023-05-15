package Model;

import com.eclipsesource.json.Json;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class IpInformationNonQueue implements IpInformationQueueInterface{

    List<IpInformation> ipInformationCollection = new ArrayList<>();
    public void addToQueue(IpInformation result) throws IOException, TimeoutException {

        ipInformationCollection.add(result);
    }

    public void retrieveFromQueue(IpInformationSystem system) throws IOException, TimeoutException {
        for(int i = 0; i < ipInformationCollection.size(); i++){
            system.save(ipInformationCollection.get(i).toJson());
        }
    }
}

