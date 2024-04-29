package com.tai.rabbitmqhelloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class HelloWorld {
    private Connection connection;
    private Channel channel;

    private static final String EXECUTOR_MONITOR_EXCHANGE_NAME = "executor-service-monitor-exchange";

    private static final String HELLO_WORLD_EXCHANGE_NAME = "hello-world-exchange";
    private static final String HELLO_WORLD_QUEUE_NAME = "hello-world-queue";

    public HelloWorld() {
        System.out.println(" === RabbitMQ :: HelloWorld ===");
        Map<String, String> env = System.getenv();
        for (Map.Entry<String, String> entry : env.entrySet()) {
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }
    }

    public void start() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        System.out.println(" - Connecting to RABBITMQ_HOST [" + System.getenv("RABBITMQ_HOST") + "]");
        factory.setHost(System.getenv("RABBITMQ_HOST"));
        factory.setPort(Integer.parseInt(System.getenv("RABBITMQ_PORT")));
        factory.setUsername(System.getenv("RABBITMQ_USER"));
        factory.setPassword(System.getenv("RABBITMQ_PASS"));

        // Configure your connection factory details here
        connection = factory.newConnection();
        channel = connection.createChannel();

        // Bind the hello world app queue to its exchange
        System.out.println(" >>> Bind the queue to its exchange [" + HELLO_WORLD_QUEUE_NAME + "] -> [" + HELLO_WORLD_EXCHANGE_NAME + "]");
        channel.queueBind(HELLO_WORLD_QUEUE_NAME, HELLO_WORLD_EXCHANGE_NAME, "container");

        System.out.println(" >>> Bind the queue to its exchange [" + HELLO_WORLD_QUEUE_NAME + "] -> [" + HELLO_WORLD_EXCHANGE_NAME + "]");
        channel.exchangeBind(EXECUTOR_MONITOR_EXCHANGE_NAME, HELLO_WORLD_EXCHANGE_NAME,"container");

        System.out.println(" >>> Connecting to queue [" + HELLO_WORLD_QUEUE_NAME + "]");
        channel.basicConsume(HELLO_WORLD_QUEUE_NAME, true, deliverCallback, consumerTag -> {
        });
    }

    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), "UTF-8");
        System.out.println("[Hello-World] Received: " + message);

        // USER LOGIC HERE
    };

    public void stop() throws IOException, TimeoutException {
        System.out.println(" >>> Unbind the queue to its exchange [" + HELLO_WORLD_QUEUE_NAME + "] -> [" + HELLO_WORLD_EXCHANGE_NAME + "]");
        channel.queueUnbind(HELLO_WORLD_QUEUE_NAME, HELLO_WORLD_EXCHANGE_NAME, "container");
        System.out.println(" >>> Unbind the queue to its exchange [" + HELLO_WORLD_QUEUE_NAME + "] -> [" + HELLO_WORLD_EXCHANGE_NAME + "]");
        channel.exchangeUnbind(EXECUTOR_MONITOR_EXCHANGE_NAME, HELLO_WORLD_EXCHANGE_NAME, "container");
        channel.close();
        connection.close();
    }

    public static void main(String[] args) throws Exception {
        HelloWorld manager = new HelloWorld();
        manager.start();
        // Add shutdown hook to clean up resources
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Received SIGTERM signal. Performing graceful shutdown...");
            try {
                manager.stop();
            } catch (IOException | TimeoutException e) {
                e.printStackTrace();
            }
        }));
    }
}
