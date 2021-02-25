/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

import entities.User;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.StringTokenizer;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class AuthFilter implements ContainerRequestFilter{
    
    @PersistenceContext
    EntityManager em;

    @Override
    public void filter(ContainerRequestContext crc) throws IOException {
        List<String> authHeaders = crc.getHeaders().get("Authorization");
        
        if (authHeaders != null && authHeaders.size() > 0) {
            
            String authHeaderValue = authHeaders.get(0);
            String decodedAuthHeader = new String(Base64.getDecoder().decode(authHeaderValue.replaceFirst("Basic ", "")), StandardCharsets.UTF_8);
            StringTokenizer st = new StringTokenizer(decodedAuthHeader, ":");
            String username = st.nextToken();
            String password = st.nextToken();
            
            TypedQuery<User> tq = em.createQuery("SELECT u FROM User u WHERE u.name = :username", User.class);
            tq.setParameter("username", username);
            User user = tq.getResultList().get(0);
                        
            if (user == null) {
                Response response = Response.status(Response.Status.UNAUTHORIZED).entity("Wrong username!").build();
                crc.abortWith(response);
                return;
            }
            
            if (!user.getPassword().equals(password)) {
                Response response = Response.status(Response.Status.UNAUTHORIZED).entity("Wrong password!").build();
                crc.abortWith(response);
                return;
            }
            
            if (user.getPrivilege() == "ADMIN") {
                return;
            }
            

            //TODO user privileges
            return;
        }
        
        Response response = Response.status(Response.Status.UNAUTHORIZED).entity("Username & Password required!").build();
        crc.abortWith(response);
    }
    
}
