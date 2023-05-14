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
             String QUEUE_NAME = "order_queue";
             String HOST = "localhost";
             int PORT = 5672;

             ConnectionFactory factory = new ConnectionFactory();
             factory.setHost(HOST);
             factory.setPort(PORT);
             Connection connection = factory.newConnection();
             Channel channel = connection.createChannel();
             DefaultConsumer consumer = new DefaultConsumer(channel) {
                 @Override
                 public void handleDelivery(
                         String consumerTag,
                         Envelope envelope,
                         AMQP.BasicProperties properties,
                         byte[] body) throws IOException {

                     String message = new String(body, "UTF-8");
                     system.save(Json.parse(message).asObject());
                     long deliveryTag = envelope.getDeliveryTag();
                     channel.basicAck(deliveryTag, false);

                 }
             };

             channel.basicConsume(QUEUE_NAME, false, consumer);        } catch (InterruptedException e) {
        } catch (IOException e) {
             throw new RuntimeException(e);
         } catch (TimeoutException e) {
             throw new RuntimeException(e);
         }
        }
    }
}
