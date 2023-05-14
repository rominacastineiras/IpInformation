package Model;

import Interfaces.IpInformationRespositoryInterface;
import com.eclipsesource.json.JsonObject;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;

public class IpInformationSystem {

    private final IpInformationRespositoryInterface repository;
    private Map<String, String> result;
    private List<IpInformation> resultsToPersist = new ArrayList<>();
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
        builder.setQuoteAgainstDollar();

        IpInformation ipInformation = builder.build();

        resultsToPersist.add(ipInformation);

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

    public void persistNewResults() {
        resultsToPersist.forEach(resultToPersist ->

                 repository.save(resultToPersist)
        );

        resultsToPersist = new ArrayList<>();

    }

    public String lastPersistedIpTimestamp() {
        return repository.lastPersistedIpTimestamp();
    }
}
