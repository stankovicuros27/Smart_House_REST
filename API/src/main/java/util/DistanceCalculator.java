/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import json.simple.JSONObject;
import json.simple.parser.JSONParser;
import json.simple.parser.ParseException;


public class DistanceCalculator {
    
    public static String calculateTravelTime(String locationFrom, String locationTo) {
        
        String time = "";
        String path = System.getProperty("user.dir");
        
        try {
            String key = "6pYNw3dNiKcnh9DnGeoz11MZcKG2Gudh";
            String uriPath = "http://www.mapquestapi.com/directions/v2/route?key=" + key + "&from=" + locationFrom + "&to=" + locationTo;

            Runtime.
                    getRuntime().
                    exec("cmd /c start \"\" call httpreq.bat \"" + uriPath + "\" -method GET -reportfile result.json");    
            Thread.sleep(6000);
            Runtime.getRuntime().exec("taskkill /f /im cmd.exe");

            time = parseTimeFromJSON("result.json");
            System.out.println("Time required to travel from " + locationFrom + " to " + locationTo + " is " + time);
        } catch (IOException ex) {
            Logger.getLogger(DistanceCalculator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(DistanceCalculator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return time;
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
