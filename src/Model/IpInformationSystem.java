package Model;

import Infrastructure.Repositories.IpInformationInMemory;
import Infrastructure.Repositories.IpInformationInMongoDB;
import Interfaces.IpInformationRespositoryInterface;
import com.eclipsesource.json.JsonObject;
import com.mongodb.MongoClientException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

public class IpInformationSystem {

    private IpInformationRespositoryInterface repository;
    private IpInformationQueueInterface messageBroker;
    private Map<String, String> result;
    private final IpInformationBuilder builder;

    public static String getConfigurationFileName(){
        return "config.properties";
    }

    public IpInformationSystem(){
        Properties configuration = getConfiguration();

        this.builder = IpInformationBuilder.basedOnConfiguration(configuration);

        messageBroker = initializeMessageBroker(configuration);

        repository = initializeRepository(configuration);

    }

    private IpInformationRespositoryInterface initializeRepository(Properties configuration) {
        final IpInformationRespositoryInterface repository = new IpInformationInMemory();

        String repositoryName = (String) configuration.getOrDefault("REPOSITORY", "");

        if(repositoryName.equals("MongoDB"))
            this.repository = new IpInformationInMongoDB(configuration);

        return repository;
    }

    private IpInformationQueueInterface initializeMessageBroker(Properties configuration) {
        this.messageBroker = new IpInformationNonQueue();
        String messageBrokerName = (String) configuration.getOrDefault("MESSAGE_BROKER", "");

        if(messageBrokerName.equals("RabbitMQ")){
            String messageBrokerQueueName = (String) configuration.getOrDefault("MESSAGE_BROKER", "ip_information");
            String messageBrokerHost = (String) configuration.getOrDefault("MESSAGE_BROKER", "localhost");
            int messageBrokerPort = (int) configuration.getOrDefault("MESSAGE_BROKER", 5672);

            this.messageBroker = new IpInformationQueue(messageBrokerQueueName, messageBrokerHost, messageBrokerPort);
        };

        return messageBroker;
    }

    private static Properties getConfiguration() {
        Properties configuration = new Properties();
        try{
            configuration.load(new FileReader(new File(IpInformationSystem.getConfigurationFileName()).getAbsolutePath()));
        }catch(IOException error){
            configuration =  new Properties();
        }
        return configuration;
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

        Map<String, String> mostFarCountry = new HashMap<>();
        try{
            mostFarCountry = repository.getMostFarCountry();
        } catch(MongoClientException e){
            System.out.println("Por favor consulte nuevamente");
        };

        return mostFarCountry;
    }

    public String showMostFarCountry() {
        Map<String, String> mostFarCountryInformation=  this.getMostFarCountry();

        String information = mostFarCountryInformation.get("Info");
        if ( information!= null)
                return information;
        else{
            return "Nombre: " + mostFarCountryInformation.get("country_name")  + "\n" +
        "Código ISO: " + mostFarCountryInformation.get("country_code")  + "\n" +
        "Distancia: " + mostFarCountryInformation.get("distance")  + "\n" +
        "Invocaciones: " + mostFarCountryInformation.get("invocations");
        }
    }
    public Map<String, String> getLeastFarCountry() {


        Map<String, String> leastFarCountry = new HashMap<>();
        try{
            leastFarCountry = repository.getLeastFarCountry();
        } catch(MongoClientException e){
            System.out.println("Por favor consulte nuevamente");
        };

        return leastFarCountry;
    }
    public String showLeastFarCountry() {
        Map<String, String> leastFarCountryInformation=  this.getLeastFarCountry();

        String information = leastFarCountryInformation.get("Info");
        if ( information!= null)
            return information;
        else{
            return "Nombre: " + leastFarCountryInformation.get("country_name")  + "\n" +
                    "Código ISO: " + leastFarCountryInformation.get("country_code")  + "\n" +
                    "Distancia: " + leastFarCountryInformation.get("distance")  + "\n" +
                    "Invocaciones: " + leastFarCountryInformation.get("invocations");
        }
        }



    public Map<String, String> getAverageDistance() {

        Map<String, String> averageDistance = new HashMap<>();
        try{
            averageDistance = repository.getAverageDistance();
        } catch(MongoClientException e){
            System.out.println("Por favor consulte nuevamente");
        };

        return averageDistance;
    }
    public String showAverageDistance() {
        Map<String, String> averageDistance=  this.getAverageDistance();

        String information = averageDistance.get("Info");
        if ( information!= null)
            return information;
        else{
            return "Distancia Promedio: " + averageDistance.get("averageDistance");
        }
    }

    public void addToQueue(IpInformation result) throws IOException, TimeoutException {
        (new IpInformationQueue("ip_information", "localhost", 5672)).addToQueue(result);
    }

    public String lastPersistedIpTimestamp() {
        String lastPersistedIpTimestamp = "";
        try{
            lastPersistedIpTimestamp = repository.lastPersistedIpTimestamp();
        } catch(MongoClientException e){
            System.out.println("Por favor consulte nuevamente");
        };
        return lastPersistedIpTimestamp;
    }

    public void save(JsonObject object) {
        repository.saveFromJson(object);
    }

    public Map<String, String> getResult() {
        return result;
    }
}
