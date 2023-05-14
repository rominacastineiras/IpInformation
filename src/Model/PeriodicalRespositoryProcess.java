package Model;

import Interfaces.IpInformationRespositoryInterface;

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
             system.persistNewResults();
        } catch (InterruptedException e) {
        }
        }
    }
}
