/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ua.ies;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;


/**
 *
 * @author ASUS
 */

public class MessageConsumer extends DefaultConsumer{
    
    private DisplayMessage callback;

    
    public MessageConsumer(Channel channel,DisplayMessage display) {
        super(channel);
        callback = display;

    }
    
    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)throws IOException 
    {
        String message = new String(body, "UTF-8");
        callback.display(message);

    }
    
}
