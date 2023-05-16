package Model;

import Infrastructure.MessageBrokers.NoMessageBroker;
import Infrastructure.MessageBrokers.RabbitMQMessageBroker;
import Infrastructure.Repositories.IpInformationInMemory;
import Infrastructure.Repositories.IpInformationInMongoDB;
import Interfaces.IpInformationQueueInterface;
import Interfaces.IpInformationRespositoryInterface;
import com.eclipsesource.json.JsonObject;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import static java.lang.Integer.parseInt;

public class IpInformationSystem {

    private IpInformationRespositoryInterface repository;
    private IpInformationQueueInterface messageBroker;
    private Map<String, String> result;
    private final IpInformationBuilder builder;

    public static String getConfigurationFileName(){
        return "config.properties";
    }


    public static IpInformationSystem ipInformationSystemWithCustomConfiguration(Properties configuration){

        return new IpInformationSystem(configuration);

    }
        public static IpInformationSystem ipInformationSystem(){

        return new IpInformationSystem(getConfiguration());

    }

    private IpInformationSystem(Properties configuration){
        this.builder = IpInformationBuilder.basedOnConfiguration(configuration);

        initializeMessageBroker(configuration);

        initializeRepository(configuration);

    }

    private void initializeRepository(Properties configuration) {
        this.repository = new IpInformationInMemory();

        String repositoryName = (String) configuration.getOrDefault("REPOSITORY", "");

        if(repositoryName.equals("MongoDB"))
            this.repository = new IpInformationInMongoDB(configuration);
    }

    private void initializeMessageBroker(Properties configuration) {
        this.messageBroker = new NoMessageBroker();
        String messageBrokerName = (String) configuration.getOrDefault("MESSAGE_BROKER", "");

        if(messageBrokerName.equals("RabbitMQ")){
            String messageBrokerQueueName = (String) configuration.getOrDefault("MB_QUEUE_NAME", "ip_information");
            String messageBrokerHost = (String) configuration.getOrDefault("MB_HOST", "localhost");
            String messageBrokerPort = (String) configuration.getOrDefault("MB_PORT", "5672");

            this.messageBroker = new RabbitMQMessageBroker(messageBrokerQueueName, messageBrokerHost, parseInt(messageBrokerPort));
        }

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

        builder.setIp(ip.trim());
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
    public static void waitSomeSeconds(int seconds) {
        try{
            Thread.sleep(seconds);
        } catch (InterruptedException e) {
            //Do nothing
        }
    }

    public String showResult() {

        if(repository.isInMemory())
            IpInformationSystem.waitSomeSeconds(2000);


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
        if(repository.isInMemory())
            IpInformationSystem.waitSomeSeconds(2000);

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
        return repository.getLeastFarCountry();
    }
    public String showLeastFarCountry() {
        if(repository.isInMemory())
            IpInformationSystem.waitSomeSeconds(2000);
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
        return repository.getAverageDistance();
    }
    public String showAverageDistance() {
        if(repository.isInMemory())
            IpInformationSystem.waitSomeSeconds(2000);
        Map<String, String> averageDistance=  this.getAverageDistance();

        String information = averageDistance.get("Info");
        if ( information!= null)
            return information;
        else{
            return averageDistance.get("averageDistance");
        }
    }

    public void addToQueue(IpInformation result) throws IOException, TimeoutException {
        messageBroker.addToQueue(result);
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

    public IpInformationQueueInterface getMessageBroker() {
        return messageBroker;
    }
}
