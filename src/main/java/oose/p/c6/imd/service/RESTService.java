package oose.p.c6.imd.service;


import oose.p.c6.imd.domain.Librarian;
import oose.p.c6.imd.domain.User;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class RESTService {

    @Inject
    Librarian l;

    @GET
    public String hello(){
        return l.tested();
    }


    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(JsonObject jo){
        User u = l.getUserByUsername(jo.getString("username"));
        System.out.println(jo.getString("password"));
        System.out.println("De waarde's van u en jo zijn " + u + " & " + jo);
        if(u.passwordCorrect(jo.getString("password"))){
            Token t = TokenManager.getInstance().createTokenForUser(u);
            return Response.status(200).entity(t.getTokenString()).build();
        }
        return Response.status(400).build();
    }
}
