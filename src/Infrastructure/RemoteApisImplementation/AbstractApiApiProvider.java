package Infrastructure.RemoteApisImplementation;

import Interfaces.IpInformationInterface;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AbstractApiApiProvider implements IpInformationInterface {

    private static final String NODATA = "No Data";
    private final String ip;
    private final String accessKey;
    private int retries = 0;
    private JsonObject data = new JsonObject();


    public static boolean handle(String url) {
        return url.contains("ipgeolocation.abstractapi.com");
    }

    public AbstractApiApiProvider(String ip, String accessKey) {
        this.ip = ip;
        this.accessKey = accessKey;
    }

    private JsonObject getData() {

        if(data.isEmpty()){
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://ipgeolocation.abstractapi.com/v1/?api_key=" + accessKey + "&ip_address=" + ip ))
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            try {
                HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
                data = Json.parse(response.body()).asObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                //Se reintenta porque la Api al ser gratis tiene límite de consultas por segundo
                System.out.println("Reintentando conexión a AbstractApi (reintento " + retries + "de " + 3 +")");
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
        return getData().getString("country",NODATA);
    }

    public String retrieveCountryIsoCode() {
        return getData().getString("country_code",NODATA);
    }

    @Override
    public List<String> retrieveCountryLanguages() {
        return new ArrayList<>(Collections.singleton(NODATA));
    }

    public String retrieveCountryCurrency() {
        return getData().get("currency").asObject().getString("currency_code", NODATA);
    }

    @Override
    public String retrieveCountryTimeZone() {
        return getData().get("timezone").asObject().getString("current_time", NODATA);
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