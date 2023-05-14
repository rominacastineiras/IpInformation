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

public class IpInformationFromApilayer implements IpInformationInterface {

    private final String currencyCode;
    private final String accessKey;
    private int retries = 0;
    private JsonObject data = new JsonObject();
    private static final String NODATA = "No Data";



    public static boolean handle(String url) {
        return url.contains("api.apilayer.com");
    }

    public IpInformationFromApilayer(String currencyCode, String accessKey) {
        this.currencyCode = currencyCode;
        this.accessKey = accessKey;
    }

    private JsonObject getData() {

        if(data.isEmpty() && currencyCode != NODATA){
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.apilayer.com/fixer/latest?base="+ currencyCode ))
                    .headers("apikey", accessKey)
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
        return NODATA;
    }

    public String retrieveCountryIsoCode() {
        return NODATA;
    }

    @Override
    public List<String> retrieveCountryLanguages() {
        return new ArrayList<>(Collections.singleton(NODATA));
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
        return getData().get("rates").asObject().getDouble("USD", 0.00);
    }

    public double retrieveCountryLatitude(){
        return getData().getDouble("latitude", 0.00);
    }
    public double retrieveCountryLongitude(){
        return getData().getDouble("longitude", 0.00);
    }

}
