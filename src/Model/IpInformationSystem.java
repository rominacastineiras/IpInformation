package Model;

import Interfaces.IpInformationRespositoryInterface;
import com.eclipsesource.json.JsonObject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class IpInformationSystem {

    private final IpInformationRespositoryInterface repository;
    private Map<String, String> result;
    private List<IpInformation> resultsToPersist = new ArrayList<>();
    private final IpInformationBuilder builder;

    public IpInformationSystem(IpInformationRespositoryInterface repository, IpInformationBuilder builder){
        this.repository = repository;
        this.builder = builder;
    }

    public void newQueryFor(String ip) throws IOException, TimeoutException {
        repository.connectIfNecessary();

        builder.setIp(ip);
        builder.setCountryName();
        builder.setCountryIsoCode();
        builder.setCountryCurrency();
        //        builder.setCountryTimezone();
        builder.setDistanceToBuenosAires();
        builder.setLanguages();
        builder.setQuoteAgainstDollar();

        IpInformation ipInformation = builder.build();

        addToQueue(ipInformation);

        result = ipInformation.result();
    }

    public Map<String, String> showResult(){
        return result;
    }

    public Map<String, String> getMostFarCountry() {
        return repository.getMostFarCountry();
    }

    public Map<String, String> getLeastFarCountry() {
        return repository.getLeastFarCountry();
    }

    public Map<String, String> getAverageDistance() {
        return repository.getAverageDistance();
    }

    public void addToQueue(IpInformation result) throws IOException, TimeoutException {
        String QUEUE_NAME = "order_queue";
        String HOST = "localhost";
        int PORT = 5672;
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        factory.setHost(HOST);
        factory.setPort(PORT);
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            channel.basicPublish("", QUEUE_NAME, null, result.toJson().toString().getBytes());
            channel.close();
            connection.close();


    }

    public String lastPersistedIpTimestamp() {
        return repository.lastPersistedIpTimestamp();
    }

    public void save(JsonObject object) {
        repository.saveFromJson(object);
    }
}
