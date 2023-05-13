package Model;

import java.util.List;

public interface IpInformationInterface {
    String retrieveCountryName();

    String retrieveCountryIsoCode();

    List<String> retrieveCountryLanguages();

    String retrieveCountryCurrency();
    String retrieveCountryTimeZone();

    String retrieveCountryDistanceToBuenosAires();

    double retrieveQuoteAgainstDollar();

    double retrieveCountryLatitude();
    double retrieveCountryLongitude();

}