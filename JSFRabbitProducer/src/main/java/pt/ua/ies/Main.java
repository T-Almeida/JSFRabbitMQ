/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ua.ies;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ASUS
 */
public class Main {
    private final static String QUEUE_NAME = "Avisos";
    public static void main(String[] args) {
        try {
            /**
             * Connection to the server
             */
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            /**
             * declare a queue to send the message
             * Operation is idempotent (only create if dont exist)
             */
            
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            
            
            String message = "Random Message " + new Random().nextInt(1000);
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
            
            /**
             * Close the channel and the connection
             */
            channel.close();
            connection.close();
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}
