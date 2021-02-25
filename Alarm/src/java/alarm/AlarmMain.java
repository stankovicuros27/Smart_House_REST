/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alarm;

import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.TextMessage;


public class AlarmMain implements Runnable {
    
    @Resource(lookup = "connectionFactory")
    private static ConnectionFactory connectionFactory;
    
    @Resource(lookup = "alarmQueue")
    private static Queue alarmQueue;
    
    @Resource(lookup = "musicPlayerQueue")
    private static Queue musicPlayerQueue;
     
    
    private LocalTime ringTime;
    private String songName;
    
    public AlarmMain(LocalTime ringTime, String songName) {
        this.ringTime = ringTime;
        this.songName = songName;
    }
    
    public static void main(String[] args) {
        
        JMSContext context = connectionFactory.createContext();
        context.setClientID("Alarm");
        JMSConsumer consumer = context.createConsumer(alarmQueue);

        while(true) {
            
            System.out.println("Waiting for alarm request...");
            Message message = consumer.receive();
            
            try {
                String songName = ((TextMessage) message).getText();
                String timeString = message.getStringProperty("alarmTime");
                LocalTime alarmRingTime = LocalTime.parse(timeString);
                System.out.println("Creating alarm for song " + songName + " and time " + timeString);
                new Thread(new AlarmMain(alarmRingTime, songName)).start();
                
            } catch (JMSException ex) {
                Logger.getLogger(AlarmMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    @Override
    public void run() {
        
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        
        while(true) {
           
            System.out.println("Current time: " + LocalTime.now().getHour() + ":" + LocalTime.now().getMinute());

            try {
                if (ringTime.getMinute() == LocalTime.now().getMinute() && ringTime.getHour() == LocalTime.now().getHour()) {

                    TextMessage msg = context.createTextMessage(songName);
                    producer.send(musicPlayerQueue, msg);
                    
                    System.out.println("Alarm ringed!");
                    break;
                }
                
                Thread.sleep(60000);
                
            } catch (InterruptedException ex) {
                Logger.getLogger(AlarmMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
