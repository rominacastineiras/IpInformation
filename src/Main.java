import Model.IpInformationSystem;
import Model.PeriodicalRespositoryProcess;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws IOException, TimeoutException {

        IpInformationSystem system = IpInformationSystem.ipInformationSystem();

        startQueryingAndShowMenu(args, system);

        new PeriodicalRespositoryProcess(system).start();

        waitSomeSeconds(5000);

        showResultsAndAskToContinue(system);

    }

    private static void startQueryingAndShowMenu(String[] args, IpInformationSystem system) throws IOException, TimeoutException {
        String ip;
        if(args.length != 0) {
            System.out.println("Bienvenido/a, por favor espere a que se termine de ejecutar la consulta.");
            system.newQueryFor(args[0]);

        }else {
            System.out.println("Por favor ingrese una ip válida:");

            Scanner entradaEscaner = new Scanner(System.in);

            ip = entradaEscaner.nextLine();
            if(isValidInet4Address(ip))
                system.newQueryFor(ip);
            else{
                System.out.println("La ip ingresada no es una ip válida");
                startQueryingAndShowMenu(args, system);
            }
        }
    }

    public static boolean isValidInet4Address(String ip)
    {
        String IPV4_REGEX =
                "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                        "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                        "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                        "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

        Pattern IPv4_PATTERN = Pattern.compile(IPV4_REGEX);

        if (ip == null) {
            return false;
        }

        Matcher matcher = IPv4_PATTERN.matcher(ip);

        return matcher.matches();
    }
    public static void waitSomeSeconds(int seconds) {
        try{
            Thread.sleep(seconds);
        } catch (InterruptedException e) {
            //Do nothing
        }
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

        String askToContinue;

        Scanner scan = new Scanner(System.in);

        askToContinue = scan.nextLine();

        if(askToContinue.equals("S") || askToContinue.equals("s")) {
            System.out.println("Por favor ingrese una ip válida:");

            scan = new Scanner(System.in);

            ip = scan.nextLine();
            system.newQueryFor(ip);
            showResultsAndAskToContinue(system);
        }
        else
            System. exit(0);
    }

}