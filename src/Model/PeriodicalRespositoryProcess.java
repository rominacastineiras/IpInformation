package Model;

import Interfaces.IpInformationRespositoryInterface;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.rabbitmq.client.*;

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

             (new IpInformationQueue("ip_information", "localhost", 5672)).retrieveFromQueue(system);

         } catch (InterruptedException e) {
        } catch (IOException e) {
             throw new RuntimeException(e);
         } catch (TimeoutException e) {
             throw new RuntimeException(e);
         }
        }
    }
}
