package Model;

import com.eclipsesource.json.JsonObject;

import java.time.LocalDateTime;
import java.util.*;

public class IpInformation{
    private final LocalDateTime timestamp;
    private final String countryName;
    private final String countryIsoCode;
    private final String currency;
    private final double longitude;
    private final double latitude;
    private final List<String> languages;
    private final String timezone;
    private final double quoteAgainstDollar;

    public IpInformation(String countryName, String countryIsoCode, String currency, double longitude, double latitude, List<String> languages, double quoteAgainstDollar, String timezone) {
        this.countryName = countryName;
        this.countryIsoCode = countryIsoCode;
        this.currency = currency;
        this.latitude = latitude;
        this.longitude = longitude;
        this.languages = languages;
        this.quoteAgainstDollar = quoteAgainstDollar;
        this.timestamp = LocalDateTime.now();
        this.timezone = timezone;
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

        String languagesCollection = languages.toString();

        result.put("countryName", countryName);
        result.put("countryIsoCode", countryIsoCode);
        result.put("currency", currency);
        result.put("distanceToBuenosAires", String.valueOf(distanceToBuenosAires()));
        result.put("timezone", timezone);
        result.put("languages", languagesCollection.substring(1, languagesCollection.length() - 1));
        result.put("quoteAgainstDollar", String.valueOf(quoteAgainstDollar));
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
                .add("quoteAgainstDollar", quoteAgainstDollar)
                .add("timezone", timezone);

        return  json.asObject();
    }
}
