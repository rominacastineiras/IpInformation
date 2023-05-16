package Infrastructure.MessageBrokers;

import Interfaces.IpInformationQueueInterface;
import Model.IpInformation;
import Model.IpInformationSystem;
import com.eclipsesource.json.Json;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class RabbitMQMessageBroker implements IpInformationQueueInterface {

    private final String name;
    private final String host;
    private final int port;

    public RabbitMQMessageBroker(String name, String host, int port){
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
        DefaultConsumer consumer = getDefaultConsumer(system, channel);

        channel.basicConsume(name, false, consumer);
    }

    private static DefaultConsumer getDefaultConsumer(IpInformationSystem system, Channel channel) {
        return new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(
                    String consumerTag,
                    Envelope envelope,
                    AMQP.BasicProperties properties,
                    byte[] body) throws IOException {

                String message = new String(body, StandardCharsets.UTF_8);
                system.save(Json.parse(message).asObject());
                long deliveryTag = envelope.getDeliveryTag();
                channel.basicAck(deliveryTag, false);

            }
        };
    }
}
