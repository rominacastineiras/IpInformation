package Model;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface IpInformationQueueInterface {
    void addToQueue(IpInformation result) throws IOException, TimeoutException ;
    void retrieveFromQueue(IpInformationSystem system) throws IOException, TimeoutException ;
    }
