package Model;

import Interfaces.IpInformationRespositoryInterface;
import com.eclipsesource.json.JsonObject;

import java.util.Map;

public class IpInformationSystem {

    private final IpInformationRespositoryInterface repository;
    private Map<String, String> result;
    private final IpInformationBuilder builder;

    public IpInformationSystem(IpInformationRespositoryInterface repository, IpInformationBuilder builder){
        this.repository = repository;
        this.builder = builder;
    }

    public void newQueryFor(String ip) {
        repository.connectIfNecessary();

        builder.setIp(ip);
        builder.setCountryName();
        builder.setCountryIsoCode();
        builder.setCountryCurrency();
        //        builder.setCountryTimezone();
        builder.setDistanceToBuenosAires();
        builder.setLanguages();
      //  builder.setQuoteAgainstDollar();

        IpInformation ipInformation = builder.build();

        repository.save(ipInformation);

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
}
