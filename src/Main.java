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
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class Main {

    public static void main(String[] args) throws IOException, TimeoutException {
        IpInformationBuilder informationBuilder = IpInformationBuilder.basedOnConfiguration((new File("config.properties").getAbsolutePath()));
        IpInformationSystem system = new IpInformationSystem(new IpInformationInMongoDB(), informationBuilder);

        String ip = "";

        if(args.length != 0) {
            system.newQueryFor(args[0]);

        }else {
            System.out.println("Por favor ingrese una ip válida:");

            Scanner entradaEscaner = new Scanner(System.in);

            ip = entradaEscaner.nextLine();
            system.newQueryFor(ip);

        }

        new PeriodicalRespositoryProcess(system).start();
        try{
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }

        try{
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }

        showResultsAndAskToContinue(system);

    }

    private static void showResultsAndAskToContinue(IpInformationSystem system) throws IOException, TimeoutException {
        String ip;
        System.out.println("Resultado de consulta: " + system.showResult());

        System.out.println("Distancia más lejana: " + system.getMostFarCountry());

        System.out.println("Distancia más cercana: " + system.getLeastFarCountry());

        System.out.println("Distancia promedio: " + system.getAverageDistance());

        System.out.println("Última actualización de estadísticas: " + system.lastPersistedIpTimestamp());


        System.out.println("¿Desea realizar otra consulta? S/N");

        String consultaParaSeguir = "";

        Scanner entradaEscaner = new Scanner(System.in);

        consultaParaSeguir = entradaEscaner.nextLine();

        if(consultaParaSeguir.equals("S") || consultaParaSeguir.equals("s")) {
            System.out.println("Por favor ingrese una ip válida:");

            entradaEscaner = new Scanner(System.in);

            ip = entradaEscaner.nextLine();
            system.newQueryFor(ip);
            showResultsAndAskToContinue(system);
        }
        else
            System. exit(0);
    }

}