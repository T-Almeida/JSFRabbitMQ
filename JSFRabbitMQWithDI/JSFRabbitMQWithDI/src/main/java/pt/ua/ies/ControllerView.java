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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author ASUS
 */
@ManagedBean
@SessionScoped
public class ControllerView implements DisplayMessage{
    private String recive;
    private String reciveBeforeRefresh;
    
    private Connection connection;
    
    @PostConstruct
    public void init()
    {
        System.out.println("init");
        recive = "";
        reciveBeforeRefresh = recive;
        try {
            //iniciar o consumo de mensagens pelo broker
            /**
             * Connection to the server
             */
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            connection = factory.newConnection();
            Channel channel = connection.createChannel();
            
            /**
             * declare the queue which will receive the message
             * because the receiver can start before the sender
             */
            channel.queueDeclare(DisplayMessage.QUEUE_NAME, false, false, false, null);
            
            MessageConsumer m = new MessageConsumer(channel, this);
            
            channel.basicConsume(DisplayMessage.QUEUE_NAME, true, m);
        } catch (Exception ex) {
            Logger.getLogger(ControllerView.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    @PreDestroy
    public void close()
    {
        try {
            System.out.println("Close connection");
            connection.close();
        } catch (IOException ex) {
            Logger.getLogger(ControllerView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void display(String message) {
        recive=message;
       System.out.println("Consume " + message);
    }

    public void refresh()
    {
        System.out.println("- ----  DO REFRESH ----- ");
        // only refresh if value is old
        if (!reciveBeforeRefresh.equals(recive))
            try {
                // refresh page
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
                
                reciveBeforeRefresh = recive;
            } catch (IOException ex) {
                Logger.getLogger(ControllerView.class.getName()).log(Level.SEVERE, null, ex);
            }
        
    }
    
    public String getRecive() {
        return recive;
    }

    public void setRecive(String recive) {
        this.recive = recive;
    }
    
    
    
    
}
