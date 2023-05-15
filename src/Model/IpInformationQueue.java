package Model;

import Interfaces.IpInformationRespositoryInterface;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class IpInformationQueue implements IpInformationQueueInterface{

    private String name;
    private String host;
    private int port;

    public IpInformationQueue(String name, String host, int port){
        this.name = name;
        this.host = host;
        this.port =port;
    }
    public void addToQueue(IpInformation result) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        factory.setHost(host);
        factory.setPort(port);
        channel.queueDeclare(name, false, false, false, null);

            channel.basicPublish("", name, null, result.toJson().toString().getBytes());
            channel.close();
            connection.close();


    }

    public void retrieveFromQueue(IpInformationSystem system) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
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

        channel.basicConsume(name, false, consumer);
    }
}
