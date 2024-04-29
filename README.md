# RabbitMQ HelloWorld Example

This repository contains a simple Java application demonstrating the basic use of RabbitMQ for messaging. The application creates a connection to a RabbitMQ server, binds a queue to an exchange, consumes messages, and gracefully shuts down by unbinding the queue and closing connections.

## Prerequisites

To run this application, you'll need:

- Java 8 or later.
- Access to a RabbitMQ server.

## Configuration

The application uses environment variables for configuration. Ensure these variables are set in your environment or modify the source code to hardcode these values:

- `RABBITMQ_HOST`: The hostname or IP address of your RabbitMQ server.
- `RABBITMQ_PORT`: The port on which your RabbitMQ server is running (usually 5672).
- `RABBITMQ_USER`: Username for RabbitMQ authentication.
- `RABBITMQ_PASS`: Password for RabbitMQ authentication.

## Running the Application

To run the application, compile the Java source code and execute the main class.
 
## Overview of Operation
The HelloWorld class performs the following operations:

- Initialization: Prints environment variables to the console.
- Start: Connects to RabbitMQ, binds a queue to an exchange, and starts consuming messages.
- Message Consumption: A callback function prints each received message to the console.
- Stop: Unbinds the queue from the exchange and closes the connection and channel.

A shutdown hook is added to ensure that the stop method is called on application shutdown, allowing for a graceful termination.

## License
This project is licensed under the MIT License - see the LICENSE file for details.