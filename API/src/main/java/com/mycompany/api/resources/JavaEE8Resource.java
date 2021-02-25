package com.mycompany.api.resources;

import entities.Song;
import entities.Task;
import entities.User;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import util.DistanceCalculator;


@Stateless
@Path("services")
public class JavaEE8Resource {
    
    @PersistenceContext
    EntityManager em;
    
    @Resource(lookup = "connectionFactory")
    ConnectionFactory connectionFactory;
    
    @Resource(lookup = "musicPlayerQueue")
    Queue musicPlayerQueue;
    
    @Resource(lookup = "alarmQueue")
    Queue alarmQueue;   
    
    @GET
    @Path("playsong")
    public Response playSong(@QueryParam("userid") int userId, @QueryParam("song") String songName) {
      
        User user = em.find(User.class, userId);
        if (user == null) {
            return Response.status(Response.Status.PROXY_AUTHENTICATION_REQUIRED).entity("Invalid user id").build();
        }
        
        Song song = new Song();
        song.setName(songName);
        song.setIdUserListener(user);
        em.persist(song);
        
        JMSContext context = connectionFactory.createContext();
        JMSProducer producer = context.createProducer();
        TextMessage msg = context.createTextMessage(songName);
        producer.send(musicPlayerQueue, msg);
                    
        return Response.status(Response.Status.OK).entity("Song " + songName + " request for playing sent").build();
    }
    
    
    @POST
    @Path("makealarm")
    public Response makeAlarm(@QueryParam("userid") int userId, @QueryParam("ringtime") String ringTime, @QueryParam("song") String songName) {
        
        try {
            
            JMSContext context = connectionFactory.createContext();
            JMSProducer producer = context.createProducer();
            TextMessage msg = context.createTextMessage(songName);
            msg.setStringProperty("alarmTime", ringTime);
            producer.send(alarmQueue, msg);
            
        } catch (JMSException ex) {
            Logger.getLogger(JavaEE8Resource.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Response.status(Response.Status.OK).entity("Alarm set!").build();
    }
    
    
    //DateTime format is: "yyyy-MM-dd HH:mm" - 2016-03-04 11:30
    @POST
    @Path("maketask")
    public Response makeTask(@QueryParam("userid") int userId, @QueryParam("location") String location, @QueryParam("starttime") String startTime, @QueryParam("endtime") String endTime) throws ParseException, InterruptedException {
        
        em.getEntityManagerFactory().getCache().evictAll();
        
        User user = em.find(User.class, userId);
        if (user == null) {
            return Response.status(Response.Status.PROXY_AUTHENTICATION_REQUIRED).entity("Invalid user id").build();
        }
        
        List<Task> tasks = user.getTaskList();
        
        if (tasks == null || tasks.isEmpty()) {
            
            Task task = new Task();
            task.setIdUser(user);
            task.setLocation(location);
            task.setStartTime(startTime);
            task.setEndTime(endTime);
            em.persist(task);
            
        } else {
            
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            
            LocalDateTime startDateTime = LocalDateTime.parse(startTime, formatter);
            LocalDateTime endDateTime = LocalDateTime.parse(endTime, formatter);
            
            for (Task t : tasks) {
                
                LocalDateTime startDateTimeT = LocalDateTime.parse(t.getStartTime(), formatter);
                LocalDateTime endDateTimeT = LocalDateTime.parse(t.getEndTime(), formatter);
                
                if ((startDateTimeT.isAfter(startDateTime) && startDateTimeT.isBefore(endDateTime)) ||
                    (endDateTimeT.isAfter(startDateTime) && endDateTimeT.isBefore(endDateTime)) ||
                    (startDateTimeT.isBefore(startDateTime) && endDateTimeT.isAfter(endDateTime)) || 
                    (startDateTimeT.equals(startDateTime) && endDateTimeT.equals(endDateTime))) {
                    
                    return Response.status(Response.Status.OK).entity("Task cannot be inserted, ovarlapping without travel!").build();
                }
                
                String time = DistanceCalculator.calculateTravelTime(location, t.getLocation());
                Thread.sleep(3000);
                
                LocalTime travelTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm:ss"));
                
                startDateTimeT = startDateTimeT.minus(travelTime.getHour(), ChronoUnit.HOURS);
                startDateTimeT = startDateTimeT.minus(travelTime.getMinute(), ChronoUnit.MINUTES);
                
                endDateTimeT = endDateTimeT.plus(travelTime.getHour(), ChronoUnit.HOURS);
                endDateTimeT = endDateTimeT.plus(travelTime.getMinute(), ChronoUnit.MINUTES);
                
                if ((startDateTimeT.isAfter(startDateTime) && startDateTimeT.isBefore(endDateTime)) ||
                        (endDateTimeT.isAfter(startDateTime) && endDateTimeT.isBefore(endDateTime)) ||
                        (startDateTimeT.isBefore(startDateTime) && endDateTimeT.isAfter(endDateTime)) || 
                        (startDateTimeT.equals(startDateTime) && endDateTimeT.equals(endDateTime))) {
                    
                    return Response.status(Response.Status.OK).entity("Task cannot be inserted, ovarlapping! Travel time: " + time).build();
                }
                
            }
            
            Task task = new Task();
            task.setIdUser(user);
            task.setLocation(location);
            task.setStartTime(startTime);
            task.setEndTime(endTime);
            em.persist(task);
            
        }
        
        return Response.status(Response.Status.OK).entity("Task insertion request sent, and task is inserted!").build();
    }
    
    
    @GET
    @Path("gettasks")
    public Response getTasks(@QueryParam("userid") int userId) {
        
        em.getEntityManagerFactory().getCache().evictAll();
        
        User user = em.find(User.class, userId);
        if (user == null) {
            return Response.status(Response.Status.PROXY_AUTHENTICATION_REQUIRED).entity("Invalid user id").build();
        }
        
        String ret = "";
        int cnt = 1;
        for (Task task : user.getTaskList()) {
            ret += cnt++ + "." + task.getIdTask() + "-" + task.getStartTime() + " // " + task.getEndTime() + ", " + task.getLocation() + ";";
        }
        return Response.status(Response.Status.OK).entity(ret).build();
    }
    
    
        
    @GET
    @Path("getplaylist")
    public Response getPlaylist(@QueryParam("userid") int userId) {
        
        em.getEntityManagerFactory().getCache().evictAll();
        
        User user = em.find(User.class, userId);
        if (user == null) {
            return Response.status(Response.Status.PROXY_AUTHENTICATION_REQUIRED).entity("Invalid user id").build();
        }
        
        String ret = "";
        int cnt = 1;
        for (Song song : user.getSongList()) {
            ret += cnt + "." + song.getName() + ";";
            cnt++;
        }
                
        return Response.status(Response.Status.OK).entity(ret).build();
    }
}
