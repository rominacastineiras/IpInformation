import Infrastructure.Repositories.IpInformationInMongoDB;
import Model.IpInformationBuilder;
import Model.IpInformationSystem;
import Model.PeriodicalRespositoryProcess;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class Main {

    public static void main(String[] args) throws IOException, TimeoutException {
        IpInformationBuilder informationBuilder = IpInformationBuilder.basedOnConfiguration((new File("config.properties").getAbsolutePath()));
        IpInformationSystem system = new IpInformationSystem(new IpInformationInMongoDB(), informationBuilder);

        String ip;

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
            //Do nothing
        }

        try{
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            //Do nothing
        }

        showResultsAndAskToContinue(system);

    }

    private static void showResultsAndAskToContinue(IpInformationSystem system) throws IOException, TimeoutException {
        String ip;
        System.out.println("Resultado de consulta: " + "\n" + system.showResult());

        System.out.println("------------------------------------------------------------------------------" );
        System.out.println("Distancia más lejana: " + "\n" + system.showMostFarCountry());

        System.out.println("------------------------------------------------------------------------------" );
        System.out.println("Distancia más cercana: " + "\n" + system.showLeastFarCountry());

        System.out.println("------------------------------------------------------------------------------" );
        System.out.println("Distancia promedio: " + "\n" + system.showAverageDistance());

        System.out.println("------------------------------------------------------------------------------" );
        System.out.println("Última actualización de estadísticas: "  + "\n" + system.lastPersistedIpTimestamp());


        System.out.println("¿Desea realizar otra consulta? S/N");

        String consultaParaSeguir;

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