/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package distancecalculator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.TextMessage;
import json.simple.JSONObject;
import json.simple.parser.JSONParser;
import json.simple.parser.ParseException;

/**
 *
 * @author Uros
 */
public class DistanceCalculatorMain {

    @Resource(lookup = "connectionFactory")
    private static ConnectionFactory connectionFactory;
    
    @Resource(lookup = "distanceQueueForward")
    private static Queue distanceQueueForward;
       
    
    public static void main(String[] args) {
        
        JMSContext context = connectionFactory.createContext();
        JMSConsumer consumer = context.createConsumer(distanceQueueForward);
        JMSProducer producer = context.createProducer();
        
        while(true) {
            
            System.out.println("Waiting for task distance request...");
            Message message = consumer.receive();

            try {
                TextMessage msg = (TextMessage) message;
                String locationFrom = msg.getText();
                String locationTo = msg.getStringProperty("locationTo");
                             
                String key = "6pYNw3dNiKcnh9DnGeoz11MZcKG2Gudh";
                String uriPath = "http://www.mapquestapi.com/directions/v2/route?key=" + key + "&from=" + locationFrom + "&to=" + locationTo;

                Runtime.
                    getRuntime().
                    exec("cmd /c start \"\" call httpreq.bat \"" + uriPath + "\" -method GET -reportfile result.json");    
                Thread.sleep(6000);
                Runtime.getRuntime().exec("taskkill /f /im cmd.exe");

                String time = parseTimeFromJSON("result.json");
                System.out.println("Time required to travel from " + locationFrom + " to " + locationTo + " is " + time);
                

                TextMessage msgSend = context.createTextMessage();
                msgSend.setStringProperty("time", time);
                producer.send(distanceQueueForward, msg);

            } catch (JMSException ex) {
                System.err.println("Wrong message format!");
            } catch (Exception ex) {
                System.err.println("Song not found!");
            }
        }
    }
    
    public static String parseTimeFromJSON(String fileName) throws IOException {
           
        String time = null;
        BufferedReader br = null;
   
        try {
            File inputFile = new File(fileName);
            FileInputStream fis = new FileInputStream(inputFile);
            br =  new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
            
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(br);
            JSONObject data = (JSONObject) json.get("route");
            
            time = (String)data.get("formattedTime");
            
        } catch (ParseException ex) {
            System.err.println("Path not found!");
            time = null;
        } finally {
            if (br != null) {
                br.close();
            }
            return time;
        }
    }
    
}
