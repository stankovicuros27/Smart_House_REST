package gui;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Uros
 */
public class HttpConnector {
    public String executeRequest(String userName, String password, int userID, String method, String methodPath, List<String> queryParams, List<String> queryValues) throws InterruptedException {
        
        userName = "admin";
        password = "admin";
        
        String response = "";
        
        try {
            
            String credential = Base64.getEncoder().encodeToString((userName+":"+password).getBytes("UTF-8"));
            
            String urlString = "http://localhost:8080/API/smarthouse/services/";
            urlString += methodPath;
            urlString += "?userid=";
            urlString += userID;
            urlString += "&";
          
            for (int i = 0; i < queryParams.size(); i++) {
                urlString += queryParams.get(i);
                urlString += "=";
                urlString += queryValues.get(i);
                if (i != queryParams.size() - 1) {
                    urlString += "&";
                }
            }
            
            urlString = urlString.replaceAll(" ", "+");
            
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(method);
            con.setRequestProperty("Authorization", "Basic " + credential.substring(0, credential.length()-1));
            int responceCode = con.getResponseCode();
            
            con.connect();
            
            Thread.sleep(200);
            Reader r;
            
            if (con.getInputStream() != null) {
                r = new InputStreamReader(con.getInputStream());
            }
            else {
                r = new InputStreamReader(con.getErrorStream()); 
            }
            
            BufferedReader in = new BufferedReader(r);
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            
            response = content.toString();
            System.out.println(content);
                
            con.disconnect();
            

        } catch (MalformedURLException ex) {
            Logger.getLogger(HttpConnector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HttpConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }
}
