package Model;

import com.eclipsesource.json.Json;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class IpInformation{
    String ip;
    IpInformationBuilder informationBuilder;
    private final String countryName;
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
        double buenosAiresLatitude = -34.61315;
        double buenosAiresLongitude = -58.37723;

        return this.distanceInKm(this.latitude, this.longitude, buenosAiresLatitude, buenosAiresLongitude) == distanceToBuenosAires;
    }

    public boolean languagesAre(List<String> languages) {
        return this.languages.equals(languages);
    }

    public boolean quoteAgainstDollarIs(double quote) {
        return this.quoteAgainstDollar == quote;
    }
}
