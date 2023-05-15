package Model;

import Interfaces.IpInformationRespositoryInterface;
import com.eclipsesource.json.JsonObject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class IpInformationSystem {

    private final IpInformationRespositoryInterface repository;
    private Map<String, String> result;
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
        builder.setCountryTimezone();
        builder.setDistanceToBuenosAires();
        builder.setLanguages();
        builder.setQuoteAgainstDollar();
        builder.setCountryTimezone();

        IpInformation ipInformation = builder.build();

        addToQueue(ipInformation);

        result = ipInformation.result();
    }

    public String showResult(){

        return "Fecha y hora: " + LocalDateTime.now() + "\n" +
        "Nombre: " + result.get("countryName")  + "\n" +
        "Código ISO: " + result.get("countryIsoCode")  + "\n" +
        "Idiomas: " + result.get("languages")  + "\n" +
        "Moneda: " + result.get("currency")  + "\n" +
        "Hora: " + result.get("timezone")  + "\n" +
        "Distancia estimada: " + result.get("distanceToBuenosAires")  + "\n" +
        "Cotización USD: " + result.get("quoteAgainstDollar");
    }

    public Map<String, String> getMostFarCountry() {
        return repository.getMostFarCountry();
    }

    public String showMostFarCountry() {
        Map<String, String> mostFarCountryInformation=  repository.getMostFarCountry();

        return "Nombre: " + mostFarCountryInformation.get("country_name")  + "\n" +
        "Código ISO: " + mostFarCountryInformation.get("country_code")  + "\n" +
        "Distancia: " + mostFarCountryInformation.get("distance")  + "\n" +
        "Invocaciones: " + mostFarCountryInformation.get("invocations");
    }
    public Map<String, String> getLeastFarCountry() {
        return repository.getLeastFarCountry();
    }
    public String showLeastFarCountry() {
        Map<String, String> leastFarCountryInformation=  repository.getLeastFarCountry();

        return "Nombre: " + leastFarCountryInformation.get("country_name")  + "\n" +
        "Código ISO: " + leastFarCountryInformation.get("country_code")  + "\n" +
        "Distancia: " + leastFarCountryInformation.get("distance")  + "\n" +
        "Invocaciones: " + leastFarCountryInformation.get("invocations");
    }

    public Map<String, String> getAverageDistance() {
        return repository.getAverageDistance();
    }
    public String showAverageDistance() {
        Map<String, String> averageDistance=  repository.getAverageDistance();

        return "Distancia Promedio: " + averageDistance.get("averageDistance");
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

    public Map<String, String> getResult() {
        return result;
    }
}
