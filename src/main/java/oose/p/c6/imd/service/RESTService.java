package oose.p.c6.imd.service;


import oose.p.c6.imd.domain.Librarian;
import oose.p.c6.imd.domain.User;

import javax.json.JsonObject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

public class RESTService {
    @POST
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(JsonObject jo){
        Librarian l = new Librarian();
        User u = l.getUser(jo.getString("username"));
        if(u.passwordCorrect(jo.getString("password"))){
            Token t = TokenManager.getInstance().createTokenForUser(u);
            return Response.status(200).entity(t.getTokenString()).build();
        }
        return Response.status(400).build();
    }
}
