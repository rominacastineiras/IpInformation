package Tests;

import Model.IpInformation;
import Model.IpInformationBuilder;
import com.eclipsesource.json.JsonObject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMQTests {

    @Test
    public void shouldRetrieveCountryName() throws IOException, TimeoutException {
        String QUEUE_NAME = "order_queue";
        String HOST = "localhost";
        int PORT = 5672;

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        factory.setPort(PORT);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        for (int i = 0; i < 2; i++) {
            String message = "hola";

            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("Produced: " + message);
        }
        channel.close();
        connection.close();
    }

    /*@Test
    public void shouldConsumer() throws IOException, TimeoutException {
        String QUEUE_NAME = "order_queue";
        String HOST = "localhost";
        int PORT = 5672;

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        factory.setPort(PORT);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(
                    String consumerTag,
                    Envelope envelope,
                    AMQP.BasicProperties properties,
                    byte[] body) throws IOException {

                String message = new String(body, "UTF-8");
                System.out.println("Consumed: " + message);
            }
        };
        channel.cons.basicConsume(QUEUE_NAME, true, consumer);
    }*/



}
