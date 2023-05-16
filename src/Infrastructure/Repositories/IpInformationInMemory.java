package Infrastructure.Repositories;

import Interfaces.IpInformationRespositoryInterface;
import Model.IpInformation;
import com.eclipsesource.json.JsonObject;

import java.time.LocalTime;
import java.util.*;

import static java.lang.Integer.parseInt;

public class IpInformationInMemory implements IpInformationRespositoryInterface {
    private final Map<String, Map<String, String>> countryIsoCodeAndIpInformationRelations = new HashMap<>();
    private final List<IpInformation> collection = new ArrayList<>();
    @Override
    public void connectIfNecessary() {
    }

    private void createStadisticsRelations(IpInformation result, Map<String, String> map) {
        if(result != null) {
            map.put("country_name", result.getCountryName());
            map.put("country_code", result.getCountryCode());
            map.put("distance", String.valueOf(result.distanceToBuenosAires()));
            map.put("invocations", (countryIsoCodeAndIpInformationRelations.get(result.getCountryCode()).get("invocations")));
        }else{
            map.put("Info", "Todavía no hay estadísticas, consulte más tarde");
        }
    }

    static class AscendentDistanceComparator implements java.util.Comparator<IpInformation>{
        @Override
        public int compare(IpInformation a, IpInformation b) {
            return a.distanceToBuenosAires() - b.distanceToBuenosAires();
        }
    }
    static class DescendentDistanceComparator implements java.util.Comparator<IpInformation>{
        @Override
        public int compare(IpInformation a, IpInformation b) {
            return b.distanceToBuenosAires() - a.distanceToBuenosAires();
        }
    }

    private Map<String, String> getStadisticsRelations() {
        Map<String, String> map = new HashMap<>();
        Optional<IpInformation> ipInformation = collection.stream().findFirst();

        ipInformation.ifPresent(information -> createStadisticsRelations(information, map));

        return map;
    }

    @Override
    public Map<String, String> getMostFarCountry() {

        collection.sort(new DescendentDistanceComparator());
        return getStadisticsRelations();
    }

    @Override
    public Map<String, String> getLeastFarCountry() {

        collection.sort(new AscendentDistanceComparator());
        return getStadisticsRelations();
    }

    @Override
    public Map<String, String> getAverageDistance() {
        double operando = 0.00;
        int divisor = 0;
        Map<String, String> map = new HashMap<>();

        for(int i = 0; i < countryIsoCodeAndIpInformationRelations.size(); i++){
            operando = operando + collection.get(i).distanceToBuenosAires() *  parseInt((countryIsoCodeAndIpInformationRelations.get(collection.get(i).getCountryCode()).get("invocations")));
            divisor = divisor +  parseInt((countryIsoCodeAndIpInformationRelations.get(collection.get(i).getCountryCode()).get("invocations")));
        }
        map.put("averageDistance",String.valueOf(operando / divisor));

        return map;
    }

    @Override
    public String lastPersistedIpTimestamp() {
        return LocalTime.now().toString();
    }

    @Override
    public boolean isInMemory() {
        return true;
    }

    public void saveFromJson(JsonObject object){
        save(new IpInformation(
                object.getString("countryName",""),
                object.getString("countryIsoCode",""),
                object.getString("currency",""),
                object.getDouble("longitude",0.00),
                object.getDouble("latitude",0.00),
                new ArrayList<>(Collections.singleton(object.getString("languages", ""))),
                object.getDouble("quoteAgainstDollar",0.00),
                object.getString("timezone", "")
        ));
    }
    @Override
    public void save(IpInformation ipInformation) {

        Map<String, String> result = ipInformation.result();

        Map<String, String> ipInformationFound = countryIsoCodeAndIpInformationRelations.get(ipInformation.getCountryCode());

        int invocations = 1;

        if(ipInformationFound != null){
            updateStadistic(result, ipInformationFound);
        }else{
            insertStadistic(result, invocations);
            collection.add(ipInformation);
        }
    }

    private void insertStadistic(Map<String, String> result, int invocations) {
        Map<String, String> someIpInformation = new HashMap<>();
        someIpInformation.put("country_name", result.get("countryName"));
        someIpInformation.put("country_code", result.get("countryIsoCode"));
        someIpInformation.put("distance", result.get("distanceToBuenosAires"));
        someIpInformation.put("invocations", String.valueOf(invocations));
        someIpInformation.put("timestamp", result.get("timestamp"));

        countryIsoCodeAndIpInformationRelations.put(result.get("countryIsoCode"), someIpInformation);

    }

    private void updateStadistic(Map<String, String> result, Map<String, String> ipInformationFound) {
        int invocations;
        invocations = parseInt(ipInformationFound.get("invocations"));
        invocations++;
        ipInformationFound.put("invocations", String.valueOf(invocations));
        countryIsoCodeAndIpInformationRelations.put(result.get("countryIsoCode"), ipInformationFound);
    }
}
