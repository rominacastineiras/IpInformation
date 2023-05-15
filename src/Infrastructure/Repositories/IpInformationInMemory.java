package Infrastructure.Repositories;

import Interfaces.IpInformationRespositoryInterface;
import Model.IpInformation;
import com.eclipsesource.json.JsonObject;

import java.time.LocalTime;
import java.util.*;

import static java.lang.Integer.parseInt;

public class IpInformationInMemory implements IpInformationRespositoryInterface {
    private Map<String, Map<String, String>> collectionMap = new HashMap<>();
    private List<IpInformation> collection = new ArrayList<>();
    @Override
    public void connectIfNecessary() {
    }

    private void createEstimationRelations(IpInformation result, Map<String, String> map) {
        if(result != null) {
            map.put("country_name", result.getCountryName());
            map.put("country_code", result.getCountryCode());
            map.put("distance", String.valueOf(result.distanceToBuenosAires()));
            map.put("invocations", (collectionMap.get(result.getCountryCode()).get("invocations")));
        }else{
            map.put("Info", "Todavía no hay estadísticas, consulte más tarde");
        }
    }

    class DistanceComparator implements java.util.Comparator<IpInformation>{
        @Override
        public int compare(IpInformation a, IpInformation b) {
            return a.distanceToBuenosAires() - b.distanceToBuenosAires();
        }
    }
    class DistanceComparatorReverse implements java.util.Comparator<IpInformation>{
        @Override
        public int compare(IpInformation a, IpInformation b) {
            return b.distanceToBuenosAires() - a.distanceToBuenosAires();
        }
    }

    @Override
    public Map<String, String> getMostFarCountry() {

        Collections.sort(collection, new DistanceComparator());
        Map<String, String> map = new HashMap<>();
        Optional<IpInformation> ipInformation = collection.stream().findFirst();

        if(ipInformation.isPresent())
            createEstimationRelations(ipInformation.get(), map);

        return map;
    }

    @Override
    public Map<String, String> getLeastFarCountry() {

        Collections.sort(collection, new DistanceComparatorReverse());
        Map<String, String> map = new HashMap<>();
        Optional<IpInformation> ipInformation = collection.stream().findFirst();

        if(ipInformation.isPresent())
            createEstimationRelations(ipInformation.get(), map);

        return map;
    }

    @Override
    public Map<String, String> getAverageDistance() {
        double operando = 0.00;
        int divisor = 0;
        Map<String, String> map = new HashMap<>();

        for(int i = 0; i < collectionMap.size(); i++){
            operando = operando + collection.get(i).distanceToBuenosAires() *  parseInt((collectionMap.get(collection.get(i).getCountryCode()).get("invocations")));
            divisor = divisor +  parseInt((collectionMap.get(collection.get(i).getCountryCode()).get("invocations")));
        }
        map.put("averageDistance",String.valueOf(operando / divisor));

        return map;
    }

    @Override
    public String lastPersistedIpTimestamp() {
        return LocalTime.now().toString();
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

        Map<String, String> ipInformationFound = collectionMap.get("country_code");

        int invocations = 1;

        if(ipInformationFound != null){
            Map<String, String> newInformation = new HashMap<>();

           ipInformationFound.put("invocations", String.valueOf(invocations));
            updateEstimation(result, ipInformationFound);
        }else{
            insertEstimation(result, invocations);
            collection.add(ipInformation);
        }
    }

    private void insertEstimation(Map<String, String> result, int invocations) {
        Map<String, String> someIpInformation = new HashMap<>();
        someIpInformation.put("country_name", result.get("countryName"));
        someIpInformation.put("country_code", result.get("countryIsoCode"));
        someIpInformation.put("distance", result.get("distanceToBuenosAires"));
        someIpInformation.put("invocations", String.valueOf(invocations));
        someIpInformation.put("timestamp", result.get("timestamp"));

        collectionMap.put(result.get("countryIsoCode"), someIpInformation);

    }

    private void updateEstimation(Map<String, String> result, Map<String, String> ipInformationFound) {
        int invocations;
        invocations = parseInt(ipInformationFound.get("invocations"));
        invocations++;
        ipInformationFound.put("invocations", String.valueOf(invocations));
        collectionMap.put(result.get("countryIsoCode"), ipInformationFound);
    }
}
