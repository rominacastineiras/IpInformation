import Infrastructure.Repositories.IpInformationInMongoDB;
import Model.IpInformationBuilder;
import Model.IpInformationSystem;
import Model.PeriodicalRespositoryProcess;
import com.eclipsesource.json.Json;
import com.rabbitmq.client.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.TimeoutException;

public class Main {

    public static void main(String[] args) throws IOException, TimeoutException {
        IpInformationBuilder informationBuilder = IpInformationBuilder.basedOnConfiguration((new File("config.properties").getAbsolutePath()));
        IpInformationSystem system = new IpInformationSystem(new IpInformationInMongoDB(), informationBuilder);


        system.newQueryFor(args[0]);
        new PeriodicalRespositoryProcess(system).start();
        try{
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }

        try{
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }

        System.out.println("Resultado de consulta: " + system.showResult());

        System.out.println("Distancia más lejana: " + system.getMostFarCountry());

        System.out.println("Distancia más cercana: " + system.getLeastFarCountry());

        System.out.println("Distancia promedio: " + system.getAverageDistance());

        System.out.println("Última actualización de estadísticas: " + system.lastPersistedIpTimestamp());


    }

}