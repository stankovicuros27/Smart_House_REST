/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musicplayer;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.TextMessage;
import json.simple.JSONArray;
import json.simple.JSONObject;
import json.simple.parser.JSONParser;
import json.simple.parser.ParseException;


public class MusicPlayerMain {

    @Resource(lookup = "connectionFactory")
    private static ConnectionFactory connectionFactory;
    
    @Resource(lookup = "musicPlayerQueue")
    private static Queue musicPlayerQueue;
    
    
    
    public static void main(String[] args) {

        JMSContext context = connectionFactory.createContext();
        context.setClientID("MusicPlayer");
        JMSConsumer consumer = context.createConsumer(musicPlayerQueue);
        
        while(true) {
            
            System.out.println("Waiting for song request...");
            Message message = consumer.receive();

            try {
                String song = ((TextMessage) message).getText();
                System.out.println("Playing song " + song);
               
                song = song.replace(" ", "+");
                String uriPath = "https://deezerdevs-deezer.p.rapidapi.com/search?q=" + song;

                Runtime.
                getRuntime().
                exec("cmd /c start \"\" call httpreq.bat \"" + uriPath + "\" -method GET -header headers.txt -reportfile result.json");    
                Thread.sleep(6000);
                Runtime.getRuntime().exec("taskkill /f /im cmd.exe");

                URL url = parseURLFromJSON("result.json");
                if (url == null) throw new Exception();
                openWebpage(url);

            } catch (JMSException ex) {
                System.err.println("Wrong message format!");
            } catch (MalformedURLException ex) {
                System.err.println("Wrong URL!");
            } catch (Exception ex) {
                System.err.println("Song not found!");
            }
        }
    }
    
  
    public static URL parseURLFromJSON(String fileName) throws IOException {
        URL url = null;
        BufferedReader br = null;
   
        try {
            File inputFile = new File(fileName);
            FileInputStream fis = new FileInputStream(inputFile);
            br =  new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
            
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(br);
            JSONArray array = (JSONArray)json.get("data");
            JSONObject data = (JSONObject)array.get(0);
            
            String link = (String)data.get("preview");
            url = new URL(link);
        } catch (ParseException ex) {
            System.err.println("Song not found!");
            url = null;
        } finally {
            if (br != null) {
                br.close();
            }
            return url;
        }
    }
    
    
    public static boolean openWebpage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    

    public static boolean openWebpage(URL url) {
        try {
            return openWebpage(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return false;
    }
}
