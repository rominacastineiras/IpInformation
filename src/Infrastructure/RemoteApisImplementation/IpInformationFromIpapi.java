package Infrastructure.RemoteApisImplementation;

import Interfaces.IpInformationInterface;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class IpInformationFromIpapi implements IpInformationInterface {

    private final String ip;
    private final String accessKey;
    private int retries = 0;
    private JsonObject data = new JsonObject();


    public static boolean handle(String url) {
        return url.contains("api.ipapi.com");
    }

    public IpInformationFromIpapi(String ip, String accessKey) {
        this.ip = ip;
        this.accessKey = accessKey;
    }

    private JsonObject getData() {

        if(data.isEmpty()){
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://api.ipapi.com/api/" + ip + "?access_key=" + accessKey ))
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            try {
                HttpResponse response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
                data = Json.parse((String) response.body()).asObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                //Se reintenta porque la Api al ser gratis tiene límite de consultas por segundo
                retries++;
                try{
                    Thread.sleep(5000);
                }catch (InterruptedException ex){

                }
                if(retries < 5)
                    this.getData();
            }
        }
        return data;

    }

    public String retrieveCountryName() {
        return getData().getString("","").toString();
    }

    public String retrieveCountryIsoCode() {
        return getData().getString("","").toString();
    }

    @Override
    public List<String> retrieveCountryLanguages() {
        getData().get("location").asObject().get("languages");

        JsonArray languagesCollection = getData().get("location").asObject().get("languages").asArray();
        ArrayList<String> languages = new ArrayList<>();

        for (int i = 0; i <= languagesCollection.size() - 1; i++) {
            languages.add(languagesCollection.get(i).asObject().getString("name", ""));
        }

        return languages;
    }

    public String retrieveCountryCurrency() {
        return getData().get("currency").asObject().getString("", "");

    }

    @Override
    public String retrieveCountryTimeZone() {
        return null;
    }

    @Override
    public String retrieveCountryDistanceToBuenosAires() {
        return null;
    }

    @Override
    public double retrieveQuoteAgainstDollar() {
        return 0.00;
    }

    public double retrieveCountryLatitude(){
        return getData().getDouble("", 0.00);
    }
    public double retrieveCountryLongitude(){
        return getData().getDouble("", 0.00);
    }

}
