import Infrastructure.Repositories.IpInformationInMongoDB;
import Model.IpInformationBuilder;
import Model.IpInformationSystem;
import com.eclipsesource.json.Json;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {

    public static void main(String[] args) {
        IpInformationBuilder informationBuilder = IpInformationBuilder.basedOnConfiguration((new File("config.properties").getAbsolutePath()));
        IpInformationSystem system = new IpInformationSystem(new IpInformationInMongoDB(), informationBuilder);


        system.newQueryFor(args[0]);
        System.out.println(system.showResult());

        System.out.println(system.getMostFarCountry());

        System.out.println(system.getLeastFarCountry());

        System.out.println(system.getAverageDistance());
}
}