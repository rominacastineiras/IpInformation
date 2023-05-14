package Model;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.*;

public class IpInformation{
    private LocalDateTime timestamp = LocalDateTime.now();
    String ip;
    IpInformationBuilder informationBuilder;
    private String countryName = "";
    private String countryIsoCode;
    private String currency;
    private double longitude;
    private double latitude;
    private List<String> languages;
    private double quoteAgainstDollar;

    public IpInformation(String countryName, String countryIsoCode, String currency, double longitude, double latitude, List<String> languages, double quoteAgainstDollar) {
        this.countryName = countryName;
        this.countryIsoCode = countryIsoCode;
        this.currency = currency;
        this.latitude = latitude;
        this.longitude = longitude;
        this.languages = languages;
        this.quoteAgainstDollar = quoteAgainstDollar;
        this.timestamp = LocalDateTime.now();
    }

    public void retrieveInformation(){
        //            country layer
        informationBuilder.setCountryName();
 /*       informationBuilder.setCountryIsoCOode();
//                ipapi
        informationBuilder.setCountryLanguages();
//                abstractapi
        informationBuilder.setCountryCurrency();
        informationBuilder.setCountryTimeZone();
        informationBuilder.setCountryDistanceToBuenosAires();
//                apilayer
        informationBuilder.setCountryQuoteAgainstDollar();
*/
    }

    public String retrieveAlphaCode() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://api.ipapi.com/api/161.185.160.93?access_key=21d86faa5a30addd1f92a5447e46110c"))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        String alphaCode = "";

        try {
            HttpResponse response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
             alphaCode = Json.parse((String) response.body()).asObject().get("data").asObject().getString("country_code", "");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            alphaCode = "No info";
        }
        return alphaCode;
    }

    public double distanceInKm(double lat1, double lon1, double lat2, double lon2) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        }
        else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            dist = dist * 1.609344;
            return (dist);
        }
    }

    public boolean countryNameIs(String aCountryName) {
        return this.countryName.equals(aCountryName);
    }

    public boolean countryIsoCodeIs(String countryIsoCode) {
        return this.countryIsoCode.equals(countryIsoCode);
    }

    public boolean countryCurrencyIs(String aCurrency) {
        return this.currency.equals(aCurrency);
    }

    public boolean distanceToBuenosAiresIs(double distanceToBuenosAires) {
        return distanceToBuenosAires() == distanceToBuenosAires;
    }

    private int distanceToBuenosAires() {
        double buenosAiresLatitude = -34.61315;
        double buenosAiresLongitude = -58.37723;
        return (int)this.distanceInKm(this.latitude, this.longitude, buenosAiresLatitude, buenosAiresLongitude);
    }

    public boolean languagesAre(List<String> languages) {
        return this.languages.equals(languages);
    }

    public boolean quoteAgainstDollarIs(double quote) {
        return this.quoteAgainstDollar == quote;
    }

    public Map<String, String> result() {
        Map<String, String> result = new HashMap<>();

        result.put("countryName", countryName);
        result.put("countryIsoCode", countryIsoCode);
        result.put("currency", currency);
        result.put("distanceToBuenosAires", String.valueOf(distanceToBuenosAires()));
        result.put("timestamp", "timestamp"); //TODO: Completar
        result.put("languages", languages.toString());
        result.put("quoteAgainstDollar", String.valueOf(quoteAgainstDollar));
        result.put("countryName", countryName);
        result.put("timestamp", timestamp.toString());


        return result;

    }

    public JsonObject toJson(){
        JsonObject json = new JsonObject().add("countryName", countryName)
                        .add("countryIsoCode", countryIsoCode)
                .add("currency",currency)
                .add("latitude", latitude)
                .add("longitude", longitude)
                .add("languages", languages.toString())
                .add("quoteAgainstDollar", quoteAgainstDollar);


        return  json.asObject();
    }
}
