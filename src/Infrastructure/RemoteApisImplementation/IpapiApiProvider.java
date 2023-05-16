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

public class IpapiApiProvider implements IpInformationInterface {

    private final String ip;
    private final String accessKey;
    private int retries = 0;
    private JsonObject data = new JsonObject();

    private static final String NODATA = "No Data";

    public static boolean handle(String url) {
        return url.contains("api.ipapi.com");
    }

    public IpapiApiProvider(String ip, String accessKey) {
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
                HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
                data = Json.parse(response.body()).asObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                //Se reintenta porque la Api al ser gratis tiene límite de consultas por segundo
                System.out.println("Reintentando conexión a Ipapi (reintento " + retries + "de " + 3 +")");
                retries++;
                try{
                    Thread.sleep(3000);
                }catch (InterruptedException ex){
                    //Do nothing
                }
                if(retries < 3)
                    this.getData();
            }
        }
        return data;

    }

    public String retrieveCountryName() {
        return getData().getString("country_name",NODATA);
    }

    public String retrieveCountryIsoCode() {
        return getData().getString("country_code",NODATA);
    }

    @Override
    public List<String> retrieveCountryLanguages() {
        getData().get("location").asObject().get("languages");

        return getLanguagesAsStringList();
    }

    private ArrayList<String> getLanguagesAsStringList() {
        JsonArray languagesCollection = getData().get("location").asObject().get("languages").asArray();
        ArrayList<String> languages = new ArrayList<>();

        for (int i = 0; i <= languagesCollection.size() - 1; i++) {
            languages.add(languagesCollection.get(i).asObject().getString("name", NODATA));
        }
        return languages;
    }

    public String retrieveCountryCurrency() {
        return NODATA;

    }

    @Override
    public String retrieveCountryTimeZone() {
        return NODATA;
    }

    @Override
    public double retrieveQuoteAgainstDollar() {
        return 0.00;
    }
    @Override
    public double retrieveCountryLatitude(){
        return getData().getDouble("latitude", 0.00);
    }
    @Override
    public double retrieveCountryLongitude(){
        return getData().getDouble("longitude", 0.00);
    }

}
