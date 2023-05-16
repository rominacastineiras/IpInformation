package Model;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class PeriodicalRespositoryProcess extends Thread{
    private final IpInformationSystem system;
    public PeriodicalRespositoryProcess(IpInformationSystem system){
        this.system = system;
    }

    @Override
    public void run(){
        while(true){
         try{
             Thread.sleep(2000);

             system.getMessageBroker().retrieveFromQueue(system);

         } catch (InterruptedException e) {
             //Do nothing
         } catch (IOException e) {
             throw new RuntimeException(e);
         } catch (TimeoutException e) {
             throw new RuntimeException(e);
         }
        }
    }
}
